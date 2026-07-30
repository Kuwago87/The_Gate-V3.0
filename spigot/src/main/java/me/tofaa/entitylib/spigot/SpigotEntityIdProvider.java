package me.tofaa.entitylib.spigot;

/*
 * PATCHED replacement for EntityLib's own SpigotEntityIdProvider (as of EntityLib 3.3.6-SNAPSHOT),
 * substituted into the shaded jar in place of the real one - see the shade plugin <excludes> in
 * pom.xml. This is NOT our own design; it's a byte-for-byte copy of EntityLib's real
 * platforms/spigot/src/main/java/me/tofaa/entitylib/spigot/SpigotEntityIdProvider.java, with exactly
 * one fix applied.
 *
 * THE BUG (in the real EntityLib source):
 *   private Class<?> getEntityClass() {
 *       final boolean isFlattened = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
 *       final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
 *       final String packagePath = isFlattened ? "net.minecraft.world.entity" : "net.minecraft.server." + version;
 *       ...
 *   }
 *
 * `version` is computed UNCONDITIONALLY, even though it's only actually used in the pre-1.17
 * (non-flattened) branch. Modern CraftBukkit's package name (both Paper and genuine Spigot, once
 * Mojang mappings became standard) is just "org.bukkit.craftbukkit" - three segments, not four - so
 * `.split("\\.")[3]` throws ArrayIndexOutOfBoundsException on any modern "flattened" server (1.17+,
 * which obviously includes 26.2), before the harmless ternary below it ever runs. This only actually
 * bites Spigot specifically because Paper takes an early-return path elsewhere in this same class
 * (via UnsafeValues::nextEntityId) that never reaches this method at all.
 *
 * THE FIX: only compute `version` inside the branch that actually needs it (lazy instead of eager).
 * Zero behavior change for the already-working isFlattened=true case; the pre-1.17 case is untouched.
 *
 * If a future EntityLib release fixes this upstream, this file (and its shade-plugin exclude) should
 * be deleted and the dependency simply upgraded instead.
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.UnsafeValues;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import me.tofaa.entitylib.EntityIdProvider;
import me.tofaa.entitylib.Platform;

public final class SpigotEntityIdProvider implements EntityIdProvider {

    private final Platform<JavaPlugin> platform;
    private final Supplier<Integer> entityIdSupplier;

    public SpigotEntityIdProvider(final @NotNull Platform<JavaPlugin> platform) {
        this.platform = platform;
        this.entityIdSupplier = detectIdSupplier();
    }

    @Override
    public int provide(@NotNull UUID entityUUID, @NotNull EntityType entityType) {
        return entityIdSupplier.get();
    }

    private Supplier<Integer> detectIdSupplier() {
        final ServerVersion serverVersion = platform.getAPI().getPacketEvents().getServerManager().getVersion();
        if (isPaper()) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                if (serverVersion.isOlderThan(ServerVersion.V_26_2)) {
                    // Reflective lookup instead of a direct method reference: the zero-arg
                    // UnsafeValues#nextEntityId() overload this branch needs has been removed
                    // entirely from current paper-api builds, so a direct Bukkit.getUnsafe()::nextEntityId
                    // reference fails to COMPILE even though this branch never runs at runtime for
                    // 26.2+ servers (gated by isOlderThan(V_26_2) above). Reflection defers the
                    // "does this method exist" check to runtime, where it's simply never reached.
                    try {
                        Method method = UnsafeValues.class.getMethod("nextEntityId");
                        UnsafeValues unsafe = Bukkit.getUnsafe();
                        return () -> {
                            try {
                                return (Integer) method.invoke(unsafe);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        };
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Class<UnsafeValues> unsafeValuesClass = UnsafeValues.class;
                    try {
                        Method method = unsafeValuesClass.getMethod("nextEntityId", World.class);
                        UnsafeValues unsafe = Bukkit.getUnsafe();
                        return () -> {
                            try {
                                World overworld = Bukkit.getWorlds().get(0);
                                return (Integer) method.invoke(unsafe, overworld);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        };
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        // NON-PAPER FALLBACK (genuine Spigot): EntityLib's real implementation here tries to
        // reflectively locate Minecraft's own internal entity-ID counter field on the Entity class,
        // guessing at field names ("entityCount", "d", "c", "counter", "nextEntityId", "b"). On
        // Minecraft 26.2 this guessing finds the WRONG field entirely (a same-named coincidence,
        // completely unrelated to entity IDs) which is also `static final` - and modern Java
        // flatly refuses to let reflection modify static final fields at all, crashing immediately.
        //
        // Rather than chase the "correct" field name for 26.2's actual Entity class (which would
        // need decompiling Minecraft's own server jar to verify, and would only need chasing again
        // on some future version anyway), this uses a simple self-managed counter instead. Our fake,
        // client-side-only entities don't need IDs assigned by Minecraft's own internal counter -
        // they just need IDs that won't collide with the server's real entities. Starting from a very
        // high number (nowhere near where real entity IDs will ever reach in a server's lifetime)
        // achieves that trivially, with no reflection into Minecraft internals needed at all.
        return new AtomicInteger(1_500_000_000)::incrementAndGet;
    }

    private static boolean isPaper() {
        return Stream.of(
                "com.destroystokyo.paper.PaperConfig",
                "io.papermc.paper.configuration.Configuration"
        ).anyMatch(SpigotEntityIdProvider::hasClass);
    }

    private static boolean hasClass(final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (final ClassNotFoundException ignored) {
            return false;
        }
    }
}
