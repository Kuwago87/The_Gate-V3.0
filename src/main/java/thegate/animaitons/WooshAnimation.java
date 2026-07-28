/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package thegate.animaitons;

import com.packageing.tools.packagetools.PackageManager;
import com.packageing.tools.packagetools.entitys.ArmorStand;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import thegate.gate.CreateGate;
import thegate.gate.GateChevron;
import thegate.gate.GateObject;
import thegate.main.Globals;
import thegate.main.TheGateMain;
import thegate.math.GateMath;

public class WooshAnimation
extends BukkitRunnable {
    GateObject gate;
    public int layerDone = 0;
    boolean horizonCreated = false;
    boolean wooshDone = false;
    private Map<Integer, Set<ArmorStand>> horizon = null;
    private Map<ArmorStand, Vector> horizonLocations = null;
    private Map<ArmorStand, Vector> horizonHeadPosition = null;
    float time = 0.0f;
    private Set<Block> blocks = new HashSet<Block>();
    TheGateMain mg;
    int waitforsound = 0;
    int shutdown = Globals.GateTime;
    public int quickShutdown = Globals.GateTime;
    int ShutdownBecauseOfPhysics = 45600;
    int StopTime = 0;
    boolean stop = false;
    int till = 0;
    double woooshTime = 0.0;
    float distance = 0.0f;

    public WooshAnimation(GateObject obj, Plugin plugin) {
        this.gate = obj;
        this.mg = (TheGateMain)plugin;
        if (obj.getPackages().getHorizon() == null || obj.getPackages().getHorizonLocations() == null || obj.getPackages().getHorizonHeadPosition() == null) {
            CreateGate.CreateEventHorizon(obj);
        }
        this.horizon = obj.getPackages().getHorizon();
        this.horizonLocations = obj.getPackages().getHorizonLocations();
        this.horizonHeadPosition = obj.getPackages().getHorizonHeadPosition();
        Bukkit.getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> plugin.getServer().getWorld(this.gate.getWorldName()).playSound(this.gate.getGate(), Globals.DefaultGateActivateSound, Globals.DefaultGateActivateVolume, Globals.DefaultGateActivatePitch));
        this.getBlocks();
        this.runTaskTimerAsynchronously(plugin, 0L, Globals.DialingAnimationTicks);
    }

    private void activate() {
        for (Player p : this.gate.getPlayerInRange()) {
            int i = 1;
            while (i < 6) {
                for (ArmorStand s : this.horizon.get(i)) {
                    PackageManager.SendSpawnPackage(s, p);
                }
                ++i;
            }
        }
        this.horizonCreated = true;
        this.wooshDone = true;
        this.layerDone = 5;
        this.gate.setActive(true);
    }

    public void run() {
        --this.waitforsound;
        --this.shutdown;
        --this.quickShutdown;
        --this.ShutdownBecauseOfPhysics;
        ++this.StopTime;
        if (this.stop && this.StopTime < this.till) {
            return;
        }
        this.StopTime = 0;
        this.stop = false;
        if (this.shutdown <= 0 && this.quickShutdown <= 0) {
            this.gate.Deactivate();
            this.cancel();
        } else if (this.quickShutdown <= 0) {
            this.gate.Deactivate();
            this.cancel();
        } else if (this.ShutdownBecauseOfPhysics <= 0) {
            this.gate.Deactivate();
            this.cancel();
        }
        if (this.horizonCreated && this.wooshDone) {
            if (Globals.DoHorizonEffect) {
                this.WableHorizon();
            }
            return;
        }
        if (!Globals.DoVortex) {
            this.activate();
            return;
        }
        if (this.layerDone < 5 && Globals.DoVortex) {
            ++this.layerDone;
            for (Player p : this.gate.getPlayerInRange()) {
                for (ArmorStand s : this.horizon.get(this.layerDone)) {
                    PackageManager.SendSpawnPackage(s, p);
                }
            }
            this.stop = true;
            this.till = 4;
            this.horizonCreated = this.layerDone <= 5;
            return;
        }
        if (!this.wooshDone && this.horizonCreated && Globals.DoVortex) {
            this.Wooooosh();
        }
        if (this.waitforsound < 0) {
            Bukkit.getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.gate.getGate().getWorld().playSound(this.gate.getGate(), Globals.DefaultGateAmbientSound, Globals.DefaultGateAmbientVolume, Globals.DefaultGateAmbientePitch));
            this.waitforsound = 50;
        }
    }

    public void Wooooosh() {
        if (this.woooshTime >= 180.0) {
            this.wooshDone = true;
            this.gate.setActive(true);
            return;
        }
        this.woooshTime += Globals.VortexSpeed;
        if (this.gate.isIrisClosed()) {
            return;
        }
        if (Globals.DeadlyVortex) {
            this.deadlyVortex();
        }
        if (Globals.GateCanBreakBlocks) {
            TheGateMain.theGateMain.getServer().getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.destroyBlocks());
        }
        int i = 1;
        while (i < 6) {
            this.distance = (float)(4.0 * Math.sin(Math.toRadians(this.woooshTime)));
            this.distance *= (float)i / (12.0f - (float)i);
            for (ArmorStand s : this.horizon.get(i)) {
                Vector v = this.horizonLocations.get(s);
                Vector v3f = this.horizonHeadPosition.get(s);
                s.setHeadRotation((float)v3f.getX(), (float)v3f.getY() - 30.0f * this.distance, (float)v3f.getZ());
                s.setLocation(v.getX() + (double)this.distance * this.gate.getFacingVector().getX(), v.getY() + (double)this.distance * this.gate.getFacingVector().getY(), v.getZ() + (double)this.distance * this.gate.getFacingVector().getZ());
                s.setBodyRotation(180.0f + this.gate.getFacing() * 90.0f);
                for (Player p : this.gate.getPlayerInRange()) {
                    PackageManager.SendTeleport(s, p);
                }
            }
            ++i;
        }
    }

    public void deadlyVortex() {
        for (Player p : this.gate.getPlayerInRange()) {
            if (p.getGameMode().equals((Object)GameMode.SPECTATOR)) continue;
            Location l = p.getLocation();
            double dist = GateMath.DistancePointLine(new Vector(l.getX(), l.getY(), l.getZ()), new Vector(this.gate.getGate().getX() + 0.5, this.gate.getGate().getY() + 1.5, this.gate.getGate().getZ() + 0.5), this.gate.getFacingVector());
            double distPlaneGate = GateMath.DistancePointPlane(new Vector(l.getX(), l.getY(), l.getZ()), new Vector(this.gate.getGate().getX() + 0.5, this.gate.getGate().getY() + 1.5, this.gate.getGate().getZ() + 0.5), this.gate.getFacingVector());
            double distPlaneEnd = GateMath.DistancePointPlane(new Vector(l.getX(), l.getY(), l.getZ()), new Vector(this.gate.getGate().getX() + 0.5 + this.gate.getFacingVector().getX() * (double)this.distance, this.gate.getGate().getY() + 1.5 + this.gate.getFacingVector().getY() * (double)this.distance, this.gate.getGate().getZ() + 0.5 + this.gate.getFacingVector().getZ() * (double)this.distance), this.gate.getFacingVector());
            if (!(dist < 3.0) || !(distPlaneGate > 0.0) || !(distPlaneEnd < 0.0) || this.gate.getJoinedPlayers().contains(p) && this.mg.OnCooldown.contains(p.getName())) continue;
            Bukkit.getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.gate.KillPlayer(p));
        }
    }

    public void destroyBlocks() {
        double x = this.gate.getGate().getX() + 0.5;
        double y = this.gate.getGate().getY() + 3.0;
        double z = this.gate.getGate().getZ() + 0.5;
        double fx = this.gate.getFacingVector().getX();
        double fz = this.gate.getFacingVector().getZ();
        double d = (double)this.distance * Globals.DestructionDistanceMult;
        x += fx * d;
        z += fz * d;
        for (Block b : this.blocks) {
            double bx = b.getLocation().getX() + 0.5;
            double by = b.getLocation().getY() + 0.5;
            double bz = b.getLocation().getZ() + 0.5;
            if (Globals.excludeList.contains(b.getType()) || !(GateMath.getDistance(bx, by, bz, x, y, z) < Globals.Radius)) continue;
            b.setType(Material.AIR);
        }
    }

    public void getBlocks() {
        double x = this.gate.getGate().getX() + 0.5;
        double y = this.gate.getGate().getY() + 2.5;
        double z = this.gate.getGate().getZ() + 0.5;
        World w = this.gate.getGate().getWorld();
        Vector or = GateMath.CrossProduct(new Vector(0, 1, 0), this.gate.getFacingVector());
        int bx = Globals.Selection_X;
        while (bx < Globals.SelectionX) {
            int by = Globals.Selection_Y;
            while (by < Globals.SelectionY) {
                double d = 0.0;
                while (d < Globals.DestructionDistance) {
                    this.blocks.add(this.getBlockAllongVector(w, new Vector(x + or.getX() * (double)bx * 0.25, y + (double)by, z + or.getZ() * (double)bx * 0.25), this.gate.getFacingVector(), d));
                    d += 0.5;
                }
                ++by;
            }
            ++bx;
        }
    }

    public Block getBlockAllongVector(World w, Vector point, Vector dir, double k) {
        double x = point.getX() + dir.getX() * k;
        double y = point.getY() + dir.getY() * k;
        double z = point.getZ() + dir.getZ() * k;
        Location l = new Location(w, x, y, z);
        return w.getBlockAt(l);
    }

    public void WableHorizon() {
        if (this.horizon.keySet() == null) {
            return;
        }
        this.time = (this.time + 5.0f) % 360.0f;
        for (Set<ArmorStand> set : this.horizon.values()) {
            for (ArmorStand s : set) {
                s.setHeadRotation((float)(5.0 * Math.sin(Math.toRadians(this.time))), (float)(4.0 * Math.cos(Math.toRadians(this.time))), s.getHeadZ());
                for (Player p : this.gate.getPlayerInRange()) {
                    PackageManager.SendUpdate(s, p);
                }
            }
        }
    }

    public void Remove() {
        for (Set<ArmorStand> set : this.horizon.values()) {
            for (ArmorStand s : set) {
                for (Player p : this.gate.getPlayerInRange()) {
                    PackageManager.SendDespawnPackage(s.getEntityID(), p);
                }
            }
        }
        for (GateChevron c2 : this.gate.getPackages().getChevrons()) {
            if (!c2.On) continue;
            c2.On = false;
            c2.UpdateLight();
        }
    }

    public boolean isWooshDone() {
        return this.wooshDone;
    }
}

