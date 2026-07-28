/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package thegate.gate;

import com.packageing.tools.packagetools.PackageManager;
import com.packageing.tools.packagetools.entitys.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import thegate.gate.GateObject;

public class GateChevron {
    public int Index;
    public ArmorStand LightBot;
    public ArmorStand LightTop;
    public ArmorStand FrameBot;
    public ArmorStand LFrameBotLeft;
    public ArmorStand LFrameBotRight;
    public ArmorStand LFrameTopLeft;
    public ArmorStand LFrameTopRight;
    private Vector v1;
    private Vector v2;
    public boolean On = false;
    private GateObject gate;

    public GateChevron(int index, ArmorStand LightBot, ArmorStand LightTop, ArmorStand FrameBot, ArmorStand LFrameBotLeft, ArmorStand LFrameBotRight, ArmorStand LFrameTopLeft, ArmorStand LFrameTopRight, GateObject gate) {
        this.Index = index;
        this.LightBot = LightBot;
        this.LightTop = LightTop;
        this.FrameBot = FrameBot;
        this.LFrameBotLeft = LFrameBotLeft;
        this.LFrameBotRight = LFrameBotRight;
        this.LFrameTopLeft = LFrameTopLeft;
        this.LFrameTopRight = LFrameTopRight;
        this.gate = gate;
    }

    public void UpdateLight() {
        if (this.On) {
            for (Player p : this.gate.getPlayerInRange()) {
                this.LightBot.setHeadMaterial(new ItemStack(this.gate.getChevron_lightMaterial_on()));
                this.LightTop.setHeadMaterial(new ItemStack(this.gate.getChevron_lightMaterial_on()));
                PackageManager.SendUpdate(this.LightBot, p);
                PackageManager.SendUpdate(this.LightTop, p);
            }
        } else {
            for (Player p : this.gate.getPlayerInRange()) {
                this.LightBot.setHeadMaterial(new ItemStack(this.gate.getChevron_lightMaterial()));
                this.LightTop.setHeadMaterial(new ItemStack(this.gate.getChevron_lightMaterial()));
                PackageManager.SendUpdate(this.LightBot, p);
                PackageManager.SendUpdate(this.LightTop, p);
            }
        }
    }

    public Vector getV1() {
        return this.v1;
    }

    public void setV1(Vector v1) {
        this.v1 = v1;
    }

    public Vector getV2() {
        return this.v2;
    }

    public void setV2(Vector v2) {
        this.v2 = v2;
    }
}

