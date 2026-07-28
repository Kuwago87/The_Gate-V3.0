/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package thegate.gate;

import com.packageing.tools.packagetools.PackageManager;
import com.packageing.tools.packagetools.entitys.ArmorStand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import thegate.math.GateMath;

public class IrisPart {
    private double[] headangels = new double[13];
    private ArmorStand[] stands = new ArmorStand[13];
    private Vector[] standsPos = new Vector[13];
    private Vector rotationPoint1;
    private Vector rotationPoint2;
    private Vector rotationPoint3;
    private Vector rotationVector = new Vector(1, 0, 0);
    private Vector ringCenter = new Vector(0, 0, 0);
    double rotation = 0.0;
    private double rotationYDEG;
    private Material irisMaterial = Material.GRAY_STAINED_GLASS_PANE;
    double r = 3.0;

    public void setRotation(double angle) {
        Vector v;
        Vector backOffset = new Vector(0.0, 0.0, 0.1);
        int i = 0;
        while (i < 6) {
            if (this.standsPos[i] != null) {
                v = GateMath.getRotZ(this.standsPos[i], angle);
                v.add(this.rotationPoint1);
                v.add(backOffset);
                v = GateMath.getRotY(v, Math.toRadians(this.rotationYDEG));
                v.add(this.ringCenter);
                this.stands[i].setLocation(v.getX(), v.getY(), v.getZ());
                this.stands[i].setBodyRotation((float)this.rotationYDEG + 180.0f);
                this.stands[i].setHeadRotation(90.0f, 4.0f, (float)Math.toDegrees(this.headangels[i] + angle));
            }
            ++i;
        }
        i = 6;
        while (i < 9) {
            if (this.standsPos[i] != null) {
                v = GateMath.getRotZ(this.standsPos[i], angle * 1.05);
                v.add(this.rotationPoint2);
                v.add(backOffset);
                v = GateMath.getRotY(v, Math.toRadians(this.rotationYDEG));
                v.add(this.ringCenter);
                this.stands[i].setLocation(v.getX(), v.getY(), v.getZ());
                this.stands[i].setBodyRotation((float)this.rotationYDEG + 180.0f);
                this.stands[i].setHeadRotation(90.0f, 0.1f, (float)Math.toDegrees(this.headangels[i] + angle * 1.05));
            }
            ++i;
        }
        i = 9;
        while (i < 13) {
            if (this.standsPos[i] != null) {
                v = GateMath.getRotZ(this.standsPos[i], angle * 1.05);
                v.add(this.rotationPoint3);
                v.add(backOffset);
                v = GateMath.getRotY(v, Math.toRadians(this.rotationYDEG));
                v.add(this.ringCenter);
                this.stands[i].setLocation(v.getX(), v.getY(), v.getZ());
                this.stands[i].setBodyRotation((float)this.rotationYDEG + 180.0f);
                this.stands[i].setHeadRotation(90.0f, 0.1f, (float)Math.toDegrees(this.headangels[i] + angle * 1.05));
            }
            ++i;
        }
    }

    public void calcRelativePosition() {
        Vector v2;
        int i = 0;
        while (i < 6) {
            if (this.standsPos[i] != null) {
                this.standsPos[i] = v2 = new Vector(this.standsPos[i].getX() - this.rotationPoint1.getX(), this.standsPos[i].getY() - this.rotationPoint1.getY(), this.standsPos[i].getZ() - this.rotationPoint1.getZ());
            }
            ++i;
        }
        i = 6;
        while (i < 9) {
            if (this.standsPos[i] != null) {
                this.standsPos[i] = v2 = new Vector(this.standsPos[i].getX() - this.rotationPoint2.getX(), this.standsPos[i].getY() - this.rotationPoint2.getY(), this.standsPos[i].getZ() - this.rotationPoint2.getZ());
            }
            ++i;
        }
        i = 9;
        while (i < 13) {
            if (this.standsPos[i] != null) {
                this.standsPos[i] = v2 = new Vector(this.standsPos[i].getX() - this.rotationPoint3.getX(), this.standsPos[i].getY() - this.rotationPoint3.getY(), this.standsPos[i].getZ() - this.rotationPoint3.getZ());
            }
            ++i;
        }
    }

    public void display(Player p) {
        ArmorStand[] armorStandArray = this.stands;
        int n = this.stands.length;
        int n2 = 0;
        while (n2 < n) {
            ArmorStand s = armorStandArray[n2];
            if (s != null) {
                PackageManager.SendSpawnPackage(s, p);
            }
            ++n2;
        }
    }

    public void update(Player p) {
        ArmorStand[] armorStandArray = this.stands;
        int n = this.stands.length;
        int n2 = 0;
        while (n2 < n) {
            ArmorStand s = armorStandArray[n2];
            PackageManager.SendUpdate(s, p);
            ++n2;
        }
    }

    public void move(Player p) {
        ArmorStand[] armorStandArray = this.stands;
        int n = this.stands.length;
        int n2 = 0;
        while (n2 < n) {
            ArmorStand s = armorStandArray[n2];
            PackageManager.SendTeleport(s, p);
            ++n2;
        }
    }

    public void vanish(Player p) {
        ArmorStand[] armorStandArray = this.stands;
        int n = this.stands.length;
        int n2 = 0;
        while (n2 < n) {
            ArmorStand s = armorStandArray[n2];
            PackageManager.SendDespawnPackage(s, p);
            ++n2;
        }
    }

    public void setHeadAngle(int i, double a) {
        this.headangels[i] = a;
    }

    public void setStand(int i, ArmorStand s) {
        this.stands[i] = s;
    }

    public void setStandPos(int i, Vector v) {
        this.standsPos[i] = v;
    }

    public ArmorStand[] getStands() {
        return this.stands;
    }

    public Vector getRotationVector() {
        return this.rotationVector;
    }

    public void setRotationVector(Vector rotationVector) {
        this.rotationVector = rotationVector;
    }

    public Vector getRotationPoint1() {
        return this.rotationPoint1;
    }

    public void setRotationPoint1(Vector rotationPoint1) {
        this.rotationPoint1 = rotationPoint1;
    }

    public Vector getRotationPoint2() {
        return this.rotationPoint2;
    }

    public void setRotationPoint2(Vector rotationPoint2) {
        this.rotationPoint2 = rotationPoint2;
    }

    public Vector getRotationPoint3() {
        return this.rotationPoint3;
    }

    public void setRotationPoint3(Vector rotationPoint3) {
        this.rotationPoint3 = rotationPoint3;
    }

    public double getRotationY() {
        return this.rotationYDEG;
    }

    public void setRotationY(double rotationY) {
        this.rotationYDEG = rotationY;
    }

    public Vector getRingCenter() {
        return this.ringCenter;
    }

    public void setRingCenter(Vector ringCenter) {
        this.ringCenter = ringCenter;
    }

    public double getR() {
        return this.r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public Material getIrisMaterial() {
        return this.irisMaterial;
    }

    public void setIrisMaterial(Material irisMaterial) {
        ArmorStand[] armorStandArray = this.stands;
        int n = this.stands.length;
        int n2 = 0;
        while (n2 < n) {
            ArmorStand s = armorStandArray[n2];
            s.setHeadMaterial(new ItemStack(irisMaterial));
            ++n2;
        }
        this.irisMaterial = irisMaterial;
    }
}

