/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.PluginMessageListener
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package thegate.main;

import bstats.Metrics;
import com.gui.tools.guitools.InventoryManager;
import com.packageing.tools.packagetools.PackageManager;
import com.packageing.tools.packagetools.entitys.ArmorStand;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import thegate.bungee.PlayerServerData;
import thegate.bungee.Reconnect;
import thegate.commands.TheGateCommand;
import thegate.gate.CommandUseType;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.Config;
import thegate.main.ConfigManager;
import thegate.main.GateTools;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.save.AutoSyncDatabase;
import thegate.main.save.DBGateAccess;
import thegate.main.save.DatabaseManager;
import thegate.math.GateMath;

public class TheGateMain
extends JavaPlugin
implements Listener,
PluginMessageListener {
    public ConfigManager configManager;
    public static DBGateAccess SaveLoadInterface;
    public DatabaseManager dbManager;
    public static TheGateMain theGateMain;
    public Reconnect reconnect = new Reconnect();
    private int CooldownTimer = 100;
    public LocalDateTime AutoSyncTime = LocalDateTime.now();
    private static String[] supportedVersions;
    boolean hasWorldRefreshed = false;
    public Set<String> OnCooldown = new HashSet<String>();
    public Set<String> InTransit = new HashSet<String>();

    static {
        supportedVersions = new String[]{"26.2"};
    }

    /*
     * ADDED for the 1.21.1 -> 26.2 update: bootstrap PacketEvents and EntityLib.
     * PacketEvents MUST be bootstrapped in onLoad(), before onEnable() runs, so it can inject
     * into the network pipeline before any players connect. This is the standard pattern from
     * PacketEvents' own "Shading PacketEvents" guide - if the exact builder class name below
     * doesn't match what you get from `mvn package`, check
     * https://github.com/retrooper/packetevents/wiki for the current bootstrap snippet.
     */
    @Override
    public void onLoad() {
        com.github.retrooper.packetevents.PacketEvents.setAPI(
            io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder.build(this)
        );
        com.github.retrooper.packetevents.PacketEvents.getAPI().load();
    }

    public void onEnable() {
        com.github.retrooper.packetevents.PacketEvents.getAPI().init();

        me.tofaa.entitylib.spigot.SpigotEntityLibPlatform entityLibPlatform =
            new me.tofaa.entitylib.spigot.SpigotEntityLibPlatform(this);
        me.tofaa.entitylib.APIConfig entityLibSettings =
            new me.tofaa.entitylib.APIConfig(com.github.retrooper.packetevents.PacketEvents.getAPI())
                .usePlatformLogger();
        me.tofaa.entitylib.EntityLib.init(entityLibPlatform, entityLibSettings);

        theGateMain = this;
        if (!TheGateMain.isServerCompatible()) {
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "This version of the plugin is not supported on your server version!");
            this.getLogger().log(Level.WARNING, "You are using: " + theGateMain.getServer().getVersion());
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "Supported Versions by the Plugin:");
            String[] stringArray = supportedVersions;
            int n = supportedVersions.length;
            int n2 = 0;
            while (n2 < n) {
                String s = stringArray[n2];
                this.getLogger().log(Level.WARNING, "\t-" + s);
                ++n2;
            }
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "For a compatible version got to the version history on the spigot page.");
            this.getLogger().log(Level.WARNING, "For more information, requests or other support you could join our discord:");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "https://discord.gg/RW4C7bn");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
        }
        Config.LoadConfig((Plugin)this);
        this.configManager = new ConfigManager((Plugin)this);
        try {
            this.configManager.CreateConfigFiles();
        }
        catch (Exception e1) {
            TheGateMain.disable();
            return;
        }
        boolean missingConfig = false;
        if (this.configManager.hasUpdateConfig()) {
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "Your are missing some config options!");
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "If you want to do the change manualy heare is a list of all the missing options:");
            this.getLogger().log(Level.WARNING, "");
            ConfigManager.updatedValuesConfig.keySet().stream().forEach(x -> this.getLogger().log(Level.WARNING, String.valueOf(x) + " : " + ConfigManager.updatedValuesConfig.get(x)));
            this.getLogger().log(Level.WARNING, "");
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "You can see the newest version of the config file on our github page.");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "https://github.com/Badading/The_Gate/wiki/Config");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "For more information, requests or other support you could join our discord:");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "https://discord.gg/RW4C7bn");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------");
            missingConfig = true;
        }
        if (this.configManager.hasUpdateLang()) {
            this.getLogger().log(Level.WARNING, "-------------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "Your are missing some lines in the language file!");
            this.getLogger().log(Level.WARNING, "-------------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "If you want to do the change manualy heare is a list of all the missing options:");
            this.getLogger().log(Level.WARNING, "");
            ConfigManager.updatedValuesLang.keySet().stream().forEach(x -> this.getLogger().log(Level.WARNING, String.valueOf(x) + " : " + ConfigManager.updatedValuesLang.get(x)));
            this.getLogger().log(Level.WARNING, "");
            this.getLogger().log(Level.WARNING, "-------------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "You can see the newest version of the language file on our github page.");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "https://github.com/Badading/The_Gate/wiki/TextConfig");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "For more information, requests or other support you could join our discord:");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "https://discord.gg/RW4C7bn");
            this.getLogger().log(Level.WARNING, " ");
            this.getLogger().log(Level.WARNING, "-------------------------------------------------------------------------------");
            missingConfig = true;
        }
        if (missingConfig) {
            TheGateMain.disable();
            return;
        }
        if (Globals.UseBungee && !Globals.SaveFromat.equalsIgnoreCase("mysql")) {
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------------------");
            this.getLogger().log(Level.WARNING, "You are using bungee but no mysql database please connect the plguin to a mysql database");
            this.getLogger().log(Level.WARNING, "----------------------------------------------------------------------------------------");
        }
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.registerCommands();
        if (Globals.SaveFromat.equalsIgnoreCase("mysql")) {
            try {
                this.dbManager = new DatabaseManager(DatabaseManager.getDatabaseInfo(Globals.MySQLPath, Globals.MySQLUserName, Globals.MySQLUserPassword, Globals.SaveFromat.toUpperCase()));
            }
            catch (Exception e) {
                e.printStackTrace();
                TheGateMain.disable();
                return;
            }
        } else if (Globals.SaveFromat.equalsIgnoreCase("sqlite")) {
            this.dbManager = new DatabaseManager(DatabaseManager.getDatabaseInfo((Plugin)this, "GateSaveFile"));
        } else {
            this.getLogger().log(Level.WARNING, "Could not identify save format: " + Globals.SaveFromat);
            this.getLogger().log(Level.WARNING, "Supported save formats: SQLITE | MYSQL");
            TheGateMain.disable();
            return;
        }
        if (!this.dbManager.createDatabaseConnection((Plugin)this)) {
            TheGateMain.disable();
            return;
        }
        SaveLoadInterface = new DBGateAccess(this.dbManager);
        this.getServer().getPluginManager().registerEvents((Listener)new GateTools(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InventoryManager(), (Plugin)this);
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        if (SaveLoadInterface != null) {
            SaveLoadInterface.Load(this, null);
            SaveLoadInterface.LoadCoowners(this);
        }
        if (Globals.SaveFromat.equalsIgnoreCase("mysql") && Globals.UseBungee) {
            SaveLoadInterface.ClearPlayerTable();
        }
        if (Globals.SaveFromat.equalsIgnoreCase("mysql") && Globals.AutoSyncDatabase >= 0) {
            new AutoSyncDatabase(this).runTaskTimer((Plugin)this, 0L, Globals.AutoSyncDatabase);
        } else if (Globals.SaveFromat.equalsIgnoreCase("mysql")) {
        }
        if (!Globals.SaveFromat.equalsIgnoreCase("mysql") && Globals.UseBungee) {
            this.getLogger().log(Level.WARNING, "If you want to use Bungee you have to have a MySql database connected!");
            TheGateMain.disable();
        } else {
            this.getLogger().log(Level.INFO, "Have a save journey");
        }
        this.bStats();
    }

    public static void disable() {
        theGateMain.getServer().getPluginManager().disablePlugin((Plugin)theGateMain);
        if (SaveLoadInterface != null) {
            SaveLoadInterface.stopAsyncWorker();
            SaveLoadInterface.closeConnection();
        }
    }

    public void bStats() {
        int PluginIDBStats = 7042;
        Metrics metrics = new Metrics(this, PluginIDBStats);
        metrics.addCustomChart(new Metrics.SimplePie("use_bungee", new Callable<String>(){

            @Override
            public String call() throws Exception {
                if (Globals.UseBungee) {
                    return "True";
                }
                return "False";
            }
        }));
        metrics.addCustomChart(new Metrics.SimplePie("save_format", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Globals.SaveFromat.toLowerCase();
            }
        }));
    }

    public void onDisable() {
        if (SaveLoadInterface != null) {
            SaveLoadInterface.SaveDataFromSet(GateManager.getGatesAsSet());
        }
        for (GateObject gate : GateManager.getGatesAsSet()) {
            gate.Vanish(new Player[0]);
        }
        if (SaveLoadInterface != null) {
            SaveLoadInterface.stopAsyncWorker();
            SaveLoadInterface.closeConnection();
        }
        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }

    @EventHandler
    public void OnPlayerQuitEvent(PlayerQuitEvent e) {
        String worldName = e.getPlayer().getLocation().getWorld().getName();
        if (worldName == null || worldName == "") {
            return;
        }
        Set<GateObject> gates = GateManager.getGatesInWorld(worldName);
        if (gates == null) {
            return;
        }
        gates.stream().forEach(x -> x.removePlayerInRangeDisconnect(e.getPlayer()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!this.hasWorldRefreshed) {
            this.hasWorldRefreshed = true;
            GateManager.getGatesAsSet().stream().forEach(x -> {
                Location l = new Location(this.getServer().getWorld(x.getWorldName()), x.getGate().getX(), x.getGate().getY(), x.getGate().getZ());
                x.setGate(l);
                if (x.hasDHD()) {
                    Location l2 = new Location(this.getServer().getWorld(x.getWorldName()), x.getDHD().getX(), x.getDHD().getY(), x.getDHD().getZ());
                    x.setDHD(l2);
                }
            });
        }
        final Player player = e.getPlayer();
        ArmorStand stand = ArmorStand.CreateArmorStand(player.getLocation(), 0.0f, 0.0f, 0.0f, 0.0f);
        stand.setHeadMaterial(new ItemStack(Material.STONE));
        PackageManager.SendSpawnPackage(stand, player);
        PackageManager.SendDespawnPackage(stand.getEntityID(), player);
        this.OnCooldown.add(player.getName());
        GateManager.GateInRadius(player, Globals.VisibilityRadius);
        new BukkitRunnable(){

            public void run() {
                TheGateMain.this.OnCooldown.remove(player.getName());
            }
        }.runTaskLater((Plugin)this, (long)this.CooldownTimer);
        if (Globals.GateBuilderUUID.contains(e.getPlayer().getUniqueId().hashCode())) {
            e.getPlayer().sendMessage(ChatColor.GOLD + "[The Gate] Welcome traveler " + ChatColor.RED + "[" + e.getPlayer().getName() + "]" + ChatColor.GOLD + " may your journey be a save one.");
        }
        if (Globals.UseBungee) {
            PlayerServerData psd = SaveLoadInterface.getPlayerDataFromPlayerTable(player);
            if (psd != null) {
                GateObject destinationGate = GateManager.getGateWithAddress(psd.getAddressTo());
                GateObject GateOnOtherServer = GateManager.getGateOnOtherServerWithAddress(psd.getAddressFrom());
                if (destinationGate != null && GateOnOtherServer != null) {
                    destinationGate.addJoinedPlayer(player);
                    double playerx = psd.getPlayerX();
                    double playery = psd.getPlayerY();
                    double playerz = psd.getPlayerZ();
                    double x2 = GateOnOtherServer.getGate().getX() + 0.5;
                    double y = GateOnOtherServer.getGate().getY() + 1.5;
                    double z = GateOnOtherServer.getGate().getZ() + 0.5;
                    double otherGatex = destinationGate.getGate().getX() + 0.5;
                    double otherGatey = destinationGate.getGate().getY() + 1.5;
                    double otherGatez = destinationGate.getGate().getZ() + 0.5;
                    double facing = GateOnOtherServer.getFacing();
                    double facingothergate = destinationGate.getFacing();
                    Vector vp = GateMath.RotateVectorY(new Vector(playerx -= x2, playery -= y, playerz -= z), Math.toRadians((-facing + 2.0) * 90.0));
                    Vector vp2 = GateMath.RotateVectorY(vp, Math.toRadians(facingothergate * 90.0));
                    playerx = vp2.getX();
                    playery = vp2.getY();
                    playerz = vp2.getZ();
                    Location loc = new Location(this.getServer().getWorld(destinationGate.getWorldName()), playerx += otherGatex, (playery += otherGatey) + 0.25, playerz += otherGatez);
                    double playeryaw = (psd.getPlayerYaw() + 360.0) % 360.0;
                    loc.setYaw((float)(playeryaw + 180.0 + (facingothergate - facing) * 90.0));
                    loc.setPitch((float)psd.getPlayerPitch());
                    player.teleport(loc);
                    destinationGate.dispach(CommandUseType.OnExit, (CommandSender)player);
                    player.setVelocity(GateMath.RotateVectorY(new Vector(0.0, 0.0, 0.3), Math.toRadians(facingothergate * 90.0)));
                    if (destinationGate.woosh != null) {
                        destinationGate.woosh.quickShutdown = 100;
                    }
                    if (!destinationGate.isActive()) {
                        destinationGate.Activate(GateOnOtherServer.getAddress(), false, player);
                    }
                    this.getServer().getWorld(destinationGate.getWorldName()).playSound(destinationGate.getGate(), Globals.DefaultGateExitSound, Globals.DefaultGateExitVolume, Globals.DefaultGateExitPitch);
                    SaveLoadInterface.DeleatePlayerFromTablePlayers(player);
                    return;
                }
            } else {
                this.getLogger().log(Level.INFO, "[The Gate] No data for this player! (Main/onPlayerJoin)");
            }
        }
    }

    void registerCommands() {
        this.getCommand("Gatetools").setExecutor((CommandExecutor)new GateTools());
        this.getCommand("TheGate").setExecutor((CommandExecutor)new TheGateCommand());
    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        GateManager.GateInRadius(player, Globals.VisibilityRadius);
        if (!player.hasPermission(Perms.thegate_user_allowteleport.value())) {
            return;
        }
        if (this.InTransit.contains(player.getName())) {
            e.setCancelled(true);
        }
        if (this.OnCooldown.contains(player.getName())) {
            return;
        }
        double playerx = player.getLocation().getX();
        double playery = player.getLocation().getY();
        double playerz = player.getLocation().getZ();
        GateObject myGate = GateManager.getClosestGateTo(player.getLocation());
        if (myGate == null) {
            return;
        }
        if (myGate.isActive()) {
            GateObject destinationGate = GateManager.getGateWithAddress(myGate.getDiled());
            if (destinationGate != null) {
                if (destinationGate.isActive() && myGate.getGate().getWorld().equals(player.getLocation().getWorld())) {
                    double x = myGate.getGate().getX() + 0.5;
                    double y = myGate.getGate().getY() + 1.5;
                    double z = myGate.getGate().getZ() + 0.5;
                    double otherGatex = destinationGate.getGate().getX() + 0.5;
                    double otherGatey = destinationGate.getGate().getY() + 1.5;
                    double otherGatez = destinationGate.getGate().getZ() + 0.5;
                    if (GateMath.getDistance(playerx, playery, playerz, x, y, z) < 3.0 && myGate.isAllowTeleport()) {
                        Vector vn = myGate.getFacingVector();
                        double facing = myGate.getFacing();
                        double facingothergate = destinationGate.getFacing();
                        Vector vector = new Vector(playerx, playery, playerz);
                        Vector vector2 = new Vector(x, y, z);
                        if (GateMath.DistancePointPlane(vector, vector2, vn) < 0.2) {
                            Vector vector3 = new Vector(playerx, playery, playerz);
                            Vector vector4 = new Vector(x, y, z);
                            if (GateMath.DistancePointPlane(vector3, vector4, vn) > -0.3) {
                                if (myGate.isUseGatePerms() && !player.hasPermission(String.valueOf(Perms.thegate_user_teleport_.value()) + myGate.getAddress()) && !player.hasPermission(Perms.thegate_admin_dialgate.value())) {
                                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                                    return;
                                }
                                if (destinationGate.isUseGatePerms() && !player.hasPermission(String.valueOf(Perms.thegate_user_teleport_.value()) + destinationGate.getAddress()) && !player.hasPermission(Perms.thegate_admin_dialgate.value())) {
                                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                                    return;
                                }
                                if (!player.hasPermission(Perms.thegate_admin_ignoreiris.value())) {
                                    if (destinationGate.isIrisClosed() && Globals.DeadlyIris) {
                                        myGate.KillPlayer(player);
                                    }
                                    if (destinationGate.isIrisClosed() || myGate.isIrisClosed()) {
                                        return;
                                    }
                                }
                                Vector vp = GateMath.RotateVectorY(new Vector(playerx -= x, playery -= y, playerz -= z), Math.toRadians((-facing + 2.0) * 90.0));
                                Vector vp2 = GateMath.RotateVectorY(vp, Math.toRadians(facingothergate * 90.0));
                                playerx = vp2.getX();
                                playery = vp2.getY();
                                playerz = vp2.getZ();
                                Location loc = new Location(this.getServer().getWorld(destinationGate.getWorldName()), playerx += otherGatex, (playery += otherGatey) + 0.25, playerz += otherGatez);
                                float playeryaw = (player.getLocation().getYaw() + 360.0f) % 360.0f;
                                loc.setYaw((float)((double)(playeryaw + 180.0f) + (facingothergate - facing) * 90.0));
                                loc.setPitch(player.getLocation().getPitch());
                                this.getServer().getWorld(myGate.getWorldName()).playSound(myGate.getGate(), Globals.DefaultGateEnterSound, Globals.DefaultGateEnterVolume, Globals.DefaultGateEnterPitch);
                                player.teleport(loc);
                                myGate.dispach(CommandUseType.OnEnter, (CommandSender)player);
                                destinationGate.dispach(CommandUseType.OnExit, (CommandSender)player);
                                player.setVelocity(GateMath.RotateVectorY(new Vector(0.0, 0.0, 0.3), Math.toRadians(facingothergate * 90.0)));
                                this.getServer().getWorld(destinationGate.getWorldName()).playSound(destinationGate.getGate(), Globals.DefaultGateExitSound, Globals.DefaultGateExitVolume, Globals.DefaultGateExitPitch);
                                this.OnCooldown.add(player.getName());
                                new BukkitRunnable(){

                                    public void run() {
                                        TheGateMain.this.OnCooldown.remove(player.getName());
                                    }
                                }.runTaskLater((Plugin)this, (long)this.CooldownTimer);
                                return;
                            }
                        }
                    }
                }
            } else if (GateManager.hasGateOnOtherServerWithAddress(myGate.getDiled())) {
                double z;
                double y;
                double x;
                destinationGate = GateManager.getGateOnOtherServerWithAddress(myGate.getDiled());
                if (myGate.getGate().getWorld().equals(player.getLocation().getWorld()) && GateMath.getDistance(playerx, playery, playerz, x = myGate.getGate().getX() + 0.5, y = myGate.getGate().getY() + 1.5, z = myGate.getGate().getZ() + 0.5) < 3.0 && myGate.isAllowTeleport()) {
                    if (myGate.isUseGatePerms() && !player.hasPermission(String.valueOf(Perms.thegate_user_teleport_.value()) + myGate.getAddress()) && !player.hasPermission(Perms.thegate_admin_dialgate.value())) {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                        return;
                    }
                    if (destinationGate.isUseGatePerms() && !player.hasPermission(String.valueOf(Perms.thegate_user_teleport_.value()) + destinationGate.getAddress()) && !player.hasPermission(Perms.thegate_admin_dialgate.value())) {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                        return;
                    }
                    Vector vn = new Vector(0, 0, 1);
                    double facing = myGate.getFacing();
                    vn = GateMath.RotateVectorY(vn, Math.toRadians(facing * 90.0));
                    Vector vector = new Vector(playerx, playery, playerz);
                    Vector vector5 = new Vector(x, y, z);
                    if (GateMath.DistancePointPlane(vector, vector5, vn) < 0.2) {
                        Vector vector6 = new Vector(playerx, playery, playerz);
                        Vector vector7 = new Vector(x, y, z);
                        if (GateMath.DistancePointPlane(vector6, vector7, vn) > -0.3) {
                            if (SaveLoadInterface.AddPlayerToTablePlayers(player, destinationGate.getAddress(), myGate.getAddress())) {
                                myGate.dispach(CommandUseType.OnEnter, (CommandSender)player);
                                this.reconnect.ReconnectPlayer(destinationGate.getServer(), e.getPlayer(), (Plugin)this);
                            }
                            this.OnCooldown.add(player.getName());
                            this.InTransit.add(player.getName());
                            new BukkitRunnable(){

                                public void run() {
                                    TheGateMain.this.OnCooldown.remove(player.getName());
                                    TheGateMain.this.InTransit.remove(player.getName());
                                    SaveLoadInterface.DeleatePlayerFromTablePlayers(player);
                                }
                            }.runTaskLater((Plugin)this, (long)this.CooldownTimer);
                            this.getServer().getWorld(myGate.getWorldName()).playSound(myGate.getGate(), Globals.DefaultGateEnterSound, Globals.DefaultGateEnterVolume, Globals.DefaultGateEnterPitch);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTeleportEvent(PlayerTeleportEvent e) {
        Set<GateObject> gate = GateManager.getGatesInWorld(e.getPlayer().getLocation().getWorld().getName());
        if (gate != null && gate.size() > 0) {
            gate.stream().forEach(x -> x.removePlayerInRange(e.getPlayer()));
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        if (!e.getBlock().getType().equals((Object)Globals.DefaultDHDMaterial)) {
            return;
        }
        if (GateManager.hasDHDatLocation(e.getBlock().getLocation()) && !e.getPlayer().hasPermission(Perms.thegate_admin_breakdhd.value())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.DHDBreak", new String[0]));
        } else {
            GateObject obj = GateManager.getGateByDHD(e.getBlock().getLocation());
            if (obj == null) {
                return;
            }
            obj.setDHD(null);
            e.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message10", "{ADDRESS}", obj.getAddress()));
        }
    }

    public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
    }

    public static String getVersion() {
        return theGateMain.getDescription().getVersion();
    }

    public static boolean isServerCompatible() {
        String serverVersion = theGateMain.getServer().getVersion();
        boolean IsSupported = false;
        String[] stringArray = supportedVersions;
        int n = supportedVersions.length;
        int n2 = 0;
        while (n2 < n) {
            String s = stringArray[n2];
            if (serverVersion.contains(s)) {
                IsSupported = true;
            }
            ++n2;
        }
        return IsSupported;
    }
}

