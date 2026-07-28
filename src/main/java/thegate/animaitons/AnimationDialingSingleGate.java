/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package thegate.animaitons;

import com.packageing.tools.packagetools.PackageManager;
import com.packageing.tools.packagetools.entitys.ArmorStand;
import java.util.ArrayList;
import org.bukkit.Bukkit;
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

public class AnimationDialingSingleGate
extends BukkitRunnable {
    char[] list = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    ArmorStand[] symbols;
    Vector[] PositionSymbols;
    Vector[] PositionSymbolsAfter;
    float speed = Globals.DialingSpeed;
    ArrayList<GateChevron> chevrons;
    Vector up = new Vector(0, 1, 0);
    Vector middle;
    boolean dialingDone = false;
    GateObject gate;
    Plugin plug;
    private String address = "";
    private int currentChar = 0;
    private int currentIndex = 0;
    private float[] currentHeadAngle = new float[16];
    private boolean stop = false;
    private int time = 0;
    private int till = 0;
    private float angle = 0.0f;
    private float angDone = 0.0f;
    private boolean LockingIn = false;
    private int dir = -1;
    private int waitforrailsound = 0;
    private float prefAng = 0.0f;
    private Player player;
    private boolean aniStop = false;
    boolean down = true;

    public AnimationDialingSingleGate(GateObject gate, Plugin p, String Address, Player player) {
        this.player = player;
        this.plug = p;
        this.symbols = gate.getPackages().getSymbol();
        this.PositionSymbols = gate.getPackages().getSymbolPositionAfterRotation() != null ? gate.getPackages().getSymbolPositionAfterRotation() : CreateGate.getSymbolVectors(gate);
        this.PositionSymbolsAfter = (Vector[])this.PositionSymbols.clone();
        this.chevrons = gate.getPackages().getChevrons();
        this.gate = gate;
        this.speed = Globals.DialingSpeed;
        this.address = Address;
        this.middle = new Vector(gate.getGate().getX() + 0.5, gate.getGate().getY() + 1.5, gate.getGate().getZ() + 0.5);
        int i = 0;
        while (i < 16) {
            this.currentHeadAngle[i] = 0.0f;
            ++i;
        }
        this.currentIndex = this.getIndex(this.address.charAt(this.currentChar));
        this.runTaskTimerAsynchronously(this.plug, 0L, Globals.DialingAnimationTicks);
    }

    public void run() {
        if (this.aniStop) {
            this.Stopping();
            return;
        }
        ++this.time;
        if (this.stop && this.time < this.till) {
            return;
        }
        this.time = 0;
        if (this.currentChar > 6) {
            this.LockAnimation();
            this.gate.getPackages().setSymbolPositionAfterRotation(this.PositionSymbolsAfter);
            this.gate.dialingSingle = null;
            this.gate.Activate(this.address, true, this.player);
            this.cancel();
            return;
        }
        if (this.angle > 360.0f) {
            this.angle = 0.0f;
        }
        if ((this.dir < 0 ? this.prefAng > -this.currentHeadAngle[this.currentIndex] : this.prefAng < -this.currentHeadAngle[this.currentIndex]) && this.angDone > 40.0f) {
            this.LockingIn = true;
        }
        if (this.LockingIn) {
            this.LockAnimation();
        }
        if (this.LockingIn) {
            return;
        }
        if (!this.dialingDone) {
            this.angle += this.speed * (float)this.dir;
            this.angDone += Math.abs(this.speed);
            this.RotateSymbols();
            if (this.waitforrailsound <= 0) {
                Bukkit.getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.gate.getGate().getWorld().playSound(this.gate.getGate(), Globals.DefaultSpinningSound, Globals.DefaultSpinningVolume, Globals.DefaultSpinningPitch));
                this.waitforrailsound = 40;
            }
            --this.waitforrailsound;
        }
    }

    public void RotateSymbols() {
        this.prefAng = -this.currentHeadAngle[this.currentIndex];
        int i = 0;
        while (i < 16) {
            Vector v;
            this.PositionSymbolsAfter[i] = v = GateMath.getRotZ(this.PositionSymbols[i], Math.toRadians(this.angle));
            this.currentHeadAngle[i] = (float)(-Math.toDegrees(this.getAngle(v)));
            this.symbols[i].setHeadRotation(0.0f, 0.0f, this.currentHeadAngle[i]);
            v = GateMath.getRotY(v, Math.toRadians(this.gate.getFacing() * 90.0f));
            this.symbols[i].setLocation(v.getX() + this.middle.getX(), v.getY() + this.middle.getY(), v.getZ() + this.middle.getZ());
            this.symbols[i].setBodyRotation(180.0f + this.gate.getFacing() * 90.0f);
            for (Player p : this.gate.getPlayerInRange()) {
                PackageManager.SendTeleport(this.symbols[i], p);
            }
            ++i;
        }
    }

    public void Stop() {
        this.aniStop = true;
    }

    private void Stopping() {
        GateChevron c22;
        for (GateChevron chev : this.gate.getPackages().getChevrons()) {
            if (!chev.On) continue;
            chev.On = false;
            chev.UpdateLight();
        }
        c22 = this.chevrons.get(0);
        c22.LFrameBotLeft.setLocation(c22.getV1().getX(), c22.getV1().getY(), c22.getV1().getZ());
        c22.LFrameBotLeft.setBodyRotation(90.0f * this.gate.getFacing());
        c22.LFrameBotRight.setLocation(c22.getV2().getX(), c22.getV2().getY(), c22.getV2().getZ());
        c22.LFrameBotRight.setBodyRotation(90.0f * this.gate.getFacing());
        for (Player p : this.gate.getPlayerInRange()) {
            PackageManager.SendTeleport(c22.LFrameBotLeft, p);
            PackageManager.SendTeleport(c22.LFrameBotRight, p);
        }
        this.gate.getPackages().setSymbolPositionAfterRotation(this.PositionSymbolsAfter);
        this.cancel();
    }

    public void LockAnimation() {
        int currentOn;
        GateChevron c2 = this.chevrons.get(0);
        c2.LFrameBotLeft.setLocation(c2.LFrameBotLeft.getLocationX(), c2.LFrameBotLeft.getLocationY() + (this.down ? -0.125 : 0.125), c2.LFrameBotLeft.getLocationZ());
        c2.LFrameBotLeft.setBodyRotation(90.0f * this.gate.getFacing());
        c2.LFrameBotRight.setLocation(c2.LFrameBotRight.getLocationX(), c2.LFrameBotRight.getLocationY() + (this.down ? -0.125 : 0.125), c2.LFrameBotRight.getLocationZ());
        c2.LFrameBotRight.setBodyRotation(90.0f * this.gate.getFacing());
        for (Player p : this.gate.getPlayerInRange()) {
            PackageManager.SendTeleport(c2.LFrameBotLeft, p);
            PackageManager.SendTeleport(c2.LFrameBotRight, p);
        }
        int n = currentOn = this.currentChar + 1 > 3 ? (this.currentChar + 3) % 9 : (this.currentChar + 1) % 9;
        if (this.down) {
            if (this.currentChar < 7) {
                ++this.currentChar;
            }
            this.stop = true;
            this.till = 20;
            this.down = false;
            c2.On = true;
            this.angDone = 0.0f;
            this.chevrons.get((int)currentOn).On = true;
            this.chevrons.get(currentOn).UpdateLight();
            c2.UpdateLight();
            Bukkit.getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.plug.getServer().getWorld(this.gate.getWorldName()).playSound(this.gate.getGate(), Globals.DefaultChevronOpenSound, Globals.DefaultChevronOpenVolume, Globals.DefaultChevronOpenPitch));
        } else {
            if (this.currentChar < 7) {
                this.currentIndex = this.getIndex(this.address.charAt(this.currentChar));
            }
            this.dir *= -1;
            c2.On = false;
            this.LockingIn = false;
            this.stop = false;
            this.till = 20;
            this.down = true;
            Bukkit.getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.plug.getServer().getWorld(this.gate.getWorldName()).playSound(this.gate.getGate(), Globals.DefaultChevronLockSound, Globals.DefaultChevronLockVolume, Globals.DefaultChevronLockPitch));
            if (this.currentChar != 7) {
                c2.UpdateLight();
            }
        }
    }

    public int getIndex(char c2) {
        int i = 0;
        while (i < 16) {
            if (this.list[i] == c2) {
                return i;
            }
            ++i;
        }
        return 0;
    }

    private double getAngle(Vector v1) {
        double ang = Math.acos((v1.getX() * this.up.getX() + v1.getY() * this.up.getY() + v1.getZ() * this.up.getZ()) / (v1.length() * this.up.length()));
        if (this.cross(v1) < 0.0) {
            ang = Math.PI * 2 - ang;
        }
        return ang;
    }

    private double cross(Vector v) {
        return v.getY() * this.up.getZ() - v.getZ() * this.up.getY() + (v.getZ() * this.up.getX() - v.getX() * this.up.getZ()) + (v.getX() * this.up.getY() - v.getY() * this.up.getX());
    }
}

