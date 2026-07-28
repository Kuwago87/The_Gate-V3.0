/*
 * Decompiled with CFR 0.152.
 */
package thegate.gate;

import com.packageing.tools.packagetools.entitys.ArmorStand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import thegate.gate.GateChevron;
import thegate.gate.GateObject;
import thegate.gate.IrisPart;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.math.GateMath;

public class CreateGate {
    public static void CreateGateRing(GateObject go) {
        float facing = go.getFacing();
        float baseRotVec = 90.0f * facing;
        float baseRot = 90.0f + 90.0f * facing;
        Location ringloc = go.getGate().clone();
        ringloc.setX(ringloc.getX() + 0.5);
        ringloc.setY(ringloc.getY() + 1.5);
        ringloc.setZ(ringloc.getZ() + 0.5);
        ArmorStand[] ArmorStandsRing = new ArmorStand[36];
        int i = 0;
        while (i < 360) {
            float angleoffset = -45.0f;
            double angle = Math.toRadians((float)i + angleoffset);
            double r = 3.1;
            Vector rotvec = CreateGate.getRotBaseVector(new Vector(1, 0, 0), angle, Math.toRadians(baseRotVec));
            Location l2 = new Location(ringloc.getWorld(), ringloc.getX() + r * rotvec.getX(), ringloc.getY() + r * rotvec.getY(), ringloc.getZ() + r * rotvec.getZ());
            ArmorStand stand = ArmorStand.CreateArmorStand(l2, (float)((double)i + 4.5 + (double)angleoffset), 0.0f, 0.1f, baseRot);
            stand.setSmall(false);
            stand.setHeadMaterial(new ItemStack(go.getRingMaterial()));
            ArmorStandsRing[i / 10] = stand;
            go.getPackages().addEntityID(stand);
            i += 10;
        }
        ArrayList<GateChevron> Chevrons = new ArrayList<GateChevron>();
        int ChevronCount = 0;
        baseRot -= 90.0f;
        int i2 = 0;
        while (i2 < 360) {
            double angle = Math.toRadians(-i2 + 90);
            double r = 2.75;
            double yoff = 0.71;
            Vector rotvec = CreateGate.getRotBaseVector(new Vector(1, 0, 0), angle, Math.toRadians(baseRotVec));
            Vector offset = CreateGate.getRotBaseVector(new Vector(0.0, 0.0, 0.1), angle, Math.toRadians(baseRotVec));
            Location lbase = new Location(ringloc.getWorld(), ringloc.getX() + offset.getX() + r * rotvec.getX(), ringloc.getY() + offset.getY() + yoff + r * rotvec.getY(), ringloc.getZ() + offset.getZ() + r * rotvec.getZ());
            ArmorStand standLbase = ArmorStand.CreateArmorStand(lbase, 0.0f, 0.0f, i2, baseRot);
            standLbase.setSmall(true);
            standLbase.setHeadMaterial(new ItemStack(go.getChevron_botMaterial()));
            r = 2.85;
            offset = CreateGate.getRotBaseVector(new Vector(0.0, 0.0, 0.15), angle, Math.toRadians(baseRotVec));
            Location llite1 = new Location(ringloc.getWorld(), ringloc.getX() + offset.getX() + r * rotvec.getX(), ringloc.getY() + offset.getY() + yoff + r * rotvec.getY(), ringloc.getZ() + offset.getZ() + r * rotvec.getZ());
            ArmorStand standLLite1 = ArmorStand.CreateArmorStand(llite1, 0.01f, 0.0f, i2, baseRot);
            standLLite1.setSmall(true);
            standLLite1.setHeadMaterial(new ItemStack(go.getChevron_lightMaterial()));
            r = 3.0;
            Location llite2 = new Location(ringloc.getWorld(), ringloc.getX() + offset.getX() + r * rotvec.getX(), ringloc.getY() + offset.getY() + yoff + r * rotvec.getY(), ringloc.getZ() + offset.getZ() + r * rotvec.getZ());
            ArmorStand standLLite2 = ArmorStand.CreateArmorStand(llite2, 1.0f, 0.0f, i2, baseRot);
            standLLite2.setSmall(true);
            standLLite2.setHeadMaterial(new ItemStack(go.getChevron_lightMaterial()));
            r = 2.8;
            offset = CreateGate.getRotBaseVector(new Vector(0.0, 0.25, 0.15), angle, Math.toRadians(baseRotVec));
            Location framebl = new Location(ringloc.getWorld(), ringloc.getX() + offset.getX() + r * rotvec.getX(), ringloc.getY() + offset.getY() + yoff + r * rotvec.getY(), ringloc.getZ() + offset.getZ() + r * rotvec.getZ());
            Vector v1 = new Vector(framebl.getX(), framebl.getY(), framebl.getZ());
            ArmorStand standframebl = ArmorStand.CreateArmorStand(framebl, 5.0f, 25.0f, i2 - 10, baseRot);
            standframebl.setSmall(true);
            standframebl.setHeadMaterial(new ItemStack(go.getChevrons_frameMaterial()));
            r = 2.8;
            offset = CreateGate.getRotBaseVector(new Vector(0.0, -0.25, 0.15), angle, Math.toRadians(baseRotVec));
            Location framebr = new Location(ringloc.getWorld(), ringloc.getX() + offset.getX() + r * rotvec.getX(), ringloc.getY() + offset.getY() + yoff + r * rotvec.getY(), ringloc.getZ() + offset.getZ() + r * rotvec.getZ());
            Vector v2 = new Vector(framebr.getX(), framebr.getY(), framebr.getZ());
            ArmorStand standframebr = ArmorStand.CreateArmorStand(framebr, 5.0f, -25.0f, i2 + 10, baseRot);
            standframebr.setSmall(true);
            standframebr.setHeadMaterial(new ItemStack(go.getChevrons_frameMaterial()));
            r = 3.05;
            offset = CreateGate.getRotBaseVector(new Vector(0.0, 0.25, 0.15), angle, Math.toRadians(baseRotVec));
            Location frametl = new Location(ringloc.getWorld(), ringloc.getX() + offset.getX() + r * rotvec.getX(), ringloc.getY() + offset.getY() + yoff + r * rotvec.getY(), ringloc.getZ() + offset.getZ() + r * rotvec.getZ());
            ArmorStand standframetl = ArmorStand.CreateArmorStand(frametl, 5.0f, 25.0f, i2 - 10, baseRot);
            standframetl.setSmall(true);
            standframetl.setHeadMaterial(new ItemStack(go.getChevrons_frameMaterial()));
            r = 3.05;
            offset = CreateGate.getRotBaseVector(new Vector(0.0, -0.25, 0.15), angle, Math.toRadians(baseRotVec));
            Location frametr = new Location(ringloc.getWorld(), ringloc.getX() + offset.getX() + r * rotvec.getX(), ringloc.getY() + offset.getY() + yoff + r * rotvec.getY(), ringloc.getZ() + offset.getZ() + r * rotvec.getZ());
            ArmorStand standframetr = ArmorStand.CreateArmorStand(frametr, 5.0f, -25.0f, i2 + 10, baseRot);
            standframetr.setSmall(true);
            standframetr.setHeadMaterial(new ItemStack(go.getChevrons_frameMaterial()));
            GateChevron chevron = new GateChevron(ChevronCount, standLLite1, standLLite2, standLbase, standframebl, standframebr, standframetl, standframetr, go);
            go.getPackages().addEntityID(standLLite1);
            go.getPackages().addEntityID(standLLite2);
            go.getPackages().addEntityID(standLbase);
            go.getPackages().addEntityID(standframebl);
            go.getPackages().addEntityID(standframebr);
            go.getPackages().addEntityID(standframetl);
            go.getPackages().addEntityID(standframetr);
            chevron.setV1(v1);
            chevron.setV2(v2);
            Chevrons.add(chevron);
            ++ChevronCount;
            i2 += 40;
        }
        go.getPackages().setRing(ArmorStandsRing);
        go.getPackages().setChevrons(Chevrons);
        if (Globals.CreateBarrier) {
            CreateGate.CreateBarrier(go.getGate().clone(), go, facing);
        }
        CreateGate.createIris(go);
    }

    public static void CreateBarrier(Location loc, GateObject obj, double facing) {
        Vector v = new Vector(loc.getX(), loc.getY() + 0.5, loc.getZ());
        World w = loc.getWorld();
        HashSet<Block> blocks = new HashSet<Block>();
        if (facing == 0.0 || facing == 2.0) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() - 1, (int)v.getZ()).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() + 3, (int)v.getY() + i, (int)v.getZ()).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() + 3, (int)v.getY() + i, (int)v.getZ()).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() - 3, (int)v.getY() + i, (int)v.getZ()).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() - 3, (int)v.getY() + i, (int)v.getZ()).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() + 5, (int)v.getZ()).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() - 1, (int)v.getZ()));
                }
                if (w.getBlockAt((int)v.getX() + 3, (int)v.getY() + i, (int)v.getZ()).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() + 3, (int)v.getY() + i, (int)v.getZ()));
                }
                if (w.getBlockAt((int)v.getX() - 3, (int)v.getY() + i, (int)v.getZ()).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() - 3, (int)v.getY() + i, (int)v.getZ()));
                }
                if (w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() + 5, (int)v.getZ()));
                }
                ++i;
            }
        } else if (facing == 1.0 || facing == 3.0) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ() + i - 2).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ() + i - 2).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() + 3).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() + 3).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() - 3).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() - 3).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ() + i - 2).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ() + i - 2).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ() + i - 2).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ() + i - 2));
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() + 3).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() + 3));
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() - 3).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() - 3));
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ() + i - 2).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ() + i - 2));
                }
                ++i;
            }
        } else if (facing == 0.5 || facing == 2.5) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() + 2).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() + 2).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() - 2).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() - 2).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() + 2).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() + 2));
                }
                if (w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() - 2).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() - 2));
                }
                ++i;
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() - 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() + 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() + 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() - 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() + 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() + 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()));
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() - 1));
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() + 1).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() + 1));
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()));
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() - 1));
            }
        } else if (facing == 1.5 || facing == 3.5) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() - 2).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() - 2).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() + 2).getType().equals(Material.AIR)) {
                    w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() + 2).setType(Material.BARRIER);
                }
                if (w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() - 2).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() - 2));
                }
                if (w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() + 2).getType().equals(Material.AIR)) {
                    blocks.add(w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() + 2));
                }
                ++i;
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() + 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() + 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() - 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() + 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() + 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() - 1).setType(Material.BARRIER);
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()));
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() + 1).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() + 1));
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() - 1));
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()));
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() + 1).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() + 1));
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() - 1).getType().equals(Material.AIR)) {
                blocks.add(w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() - 1));
            }
        }
    }

    public static void RemoveBarrier(Location loc, double facing) {
        Vector v = new Vector(loc.getX(), loc.getY() + 0.5, loc.getZ());
        World w = loc.getWorld();
        if (facing == 0.0 || facing == 2.0) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() - 1, (int)v.getZ()).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX() + 3, (int)v.getY() + i, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() + 3, (int)v.getY() + i, (int)v.getZ()).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX() - 3, (int)v.getY() + i, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() - 3, (int)v.getY() + i, (int)v.getZ()).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() + i - 2, (int)v.getY() + 5, (int)v.getZ()).setType(Material.AIR);
                }
                ++i;
            }
        } else if (facing == 1.0 || facing == 3.0) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ() + i - 2).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ() + i - 2).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() + 3).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() + 3).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() - 3).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() + i, (int)v.getZ() - 3).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ() + i - 2).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ() + i - 2).setType(Material.AIR);
                }
                ++i;
            }
        } else if (facing == 0.5 || facing == 2.5) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() + 2).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() + 2).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() - 2).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() - 2).setType(Material.AIR);
                }
                ++i;
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() - 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() - 1).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() + 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() + 1).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() - 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() - 1).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() + 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() + 1).setType(Material.AIR);
            }
        } else if (facing == 1.5 || facing == 3.5) {
            int i = 0;
            while (i < 5) {
                if (w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() - 2).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() + 2, (int)v.getY() + i, (int)v.getZ() - 2).setType(Material.AIR);
                }
                if (w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() + 2).getType().equals(Material.BARRIER)) {
                    w.getBlockAt((int)v.getX() - 2, (int)v.getY() + i, (int)v.getZ() + 2).setType(Material.AIR);
                }
                ++i;
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() - 1, (int)v.getZ()).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() + 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() - 1, (int)v.getZ() + 1).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() - 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() - 1, (int)v.getZ() - 1).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX(), (int)v.getY() + 5, (int)v.getZ()).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() + 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() - 1, (int)v.getY() + 5, (int)v.getZ() + 1).setType(Material.AIR);
            }
            if (w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() - 1).getType().equals(Material.BARRIER)) {
                w.getBlockAt((int)v.getX() + 1, (int)v.getY() + 5, (int)v.getZ() - 1).setType(Material.AIR);
            }
        }
    }

    public static void CreateSymbols(GateObject go) {
        float facing = go.getFacing();
        Location ringloc = go.getGate().clone();
        ringloc.setX(ringloc.getX() + 0.5);
        ringloc.setY(ringloc.getY() + 1.5);
        ringloc.setZ(ringloc.getZ() + 0.5);
        ArmorStand[] symbols = new ArmorStand[16];
        int Symbolindex = 0;
        float baseRot = 180.0f + 90.0f * facing;
        float baseRotVec = 90.0f * facing;
        float i = 0.0f;
        while (i < 360.0f) {
            double angle = Math.toRadians(i);
            float r = 2.36f;
            float d = 0.02f;
            Vector rotvec = CreateGate.getRotBaseVector(new Vector(0.0f, 1.0f, d), angle, Math.toRadians(baseRotVec));
            Location l2 = new Location(ringloc.getWorld(), ringloc.getX() + (double)r * rotvec.getX(), ringloc.getY() + (double)r * rotvec.getY(), ringloc.getZ() + (double)r * rotvec.getZ());
            ArmorStand stand = ArmorStand.CreateArmorStand(l2, 0.0f, 0.0f, i, baseRot);
            stand.setSmall(false);
            symbols[Symbolindex] = stand;
            stand.setHeadMaterial(new ItemStack(Globals.SymbolMaterial[Symbolindex]));
            go.getPackages().addEntityID(stand);
            ++Symbolindex;
            i = (float)((double)i + 22.5);
        }
        go.getPackages().setSymbol(symbols);
    }

    public static void CreateDisplayTextStands(GateObject gate) {
        List<String> text = ConfigManager.getStringList("GateDisplayText", new String[0]);
        HashSet<ArmorStand> stands = new HashSet<ArmorStand>();
        Location loc = gate.getGate().clone();
        loc.add(new Vector(0.5, 4.5, 0.5));
        int i = text.size() - 1;
        while (i >= 0) {
            if (!(text.get(i).contains("{GATENAME}") && gate.getGateName().equals("") || text.get(i).contains("{DESCRIPTION}") && gate.getDescription().equals(""))) {
                ArmorStand s = ArmorStand.CreateArmorStand(loc, 0.0f, 0.0f, 0.0f, 0.0f);
                s.setSmall(false);
                s.setCustomNameVisible(true);
                s.setCustomName(text.get(i).replace("{ADDRESS}", gate.getAddress()).replace("{GATENAME}", gate.getGateName()).replace("{DESCRIPTION}", gate.getDescription()).replace("{WORLD}", gate.getWorldName()).replace("{OWNER}", gate.getOwnerName()).replace("{NETWORK1}", gate.getNetwork()).replace("{NETWORK2}", gate.getSecondaryNetwork().equals("null") ? "" : gate.getSecondaryNetwork()).replace("&", "\u00a7"));
                gate.getPackages().addEntityID(s);
                stands.add(s);
                loc.add(new Vector(0.0, 0.3, 0.0));
            }
            --i;
        }
        gate.getPackages().setDisplayTextStands(stands);
    }

    public static Vector[] getSymbolVectors(GateObject go) {
        Location ringloc = go.getGate().clone();
        ringloc.setX(ringloc.getX() + 0.5);
        ringloc.setY(ringloc.getY() + 1.5);
        ringloc.setZ(ringloc.getZ() + 0.5);
        int Symbolindex = 0;
        float baseRotVec = 0.0f;
        Vector[] v = new Vector[16];
        float i = 0.0f;
        while (i < 360.0f) {
            double angle = Math.toRadians(i);
            float r = 2.36f;
            float d = 0.02f;
            Vector rotvec = CreateGate.getRotBaseVector(new Vector(0.0f, 1.0f, d), angle, Math.toRadians(baseRotVec));
            v[Symbolindex] = new Vector((double)r * rotvec.getX(), (double)r * rotvec.getY(), (double)r * rotvec.getZ());
            ++Symbolindex;
            i = (float)((double)i + 22.5);
        }
        return v;
    }

    public static Vector getRotBaseVector(Vector StartVector, double ax0, double ax1) {
        double x = StartVector.getX();
        double y = StartVector.getY();
        double z = StartVector.getZ();
        double dx = x * Math.cos(ax0) - y * Math.sin(ax0);
        double dy = x * Math.sin(ax0) + y * Math.cos(ax0);
        double dz = z;
        double dx2 = dx * Math.cos(ax1) - dz * Math.sin(ax1);
        double dy2 = dy;
        double dz2 = dx * Math.sin(ax1) + dz * Math.cos(ax1);
        return new Vector(dx2, dy2, dz2);
    }

    public static boolean CheckforBlocksGate(Location loc, float facing) {
        block5: {
            block4: {
                if (facing != 0.0f && facing != 2.0f) break block4;
                int i = 0;
                while (i < 5) {
                    Block b = loc.getWorld().getBlockAt(loc.getBlockX() - 2 + i, loc.getBlockY(), loc.getBlockZ());
                    if (b.getType().equals(Material.AIR) || b.getType().toString().toLowerCase().contains("glass") || b.getType().toString().toLowerCase().contains("water")) {
                        return false;
                    }
                    ++i;
                }
                break block5;
            }
            if (facing != 1.0f && facing != 3.0f) break block5;
            int i = 0;
            while (i < 5) {
                Block b = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 2 + i);
                if (b.getType().equals(Material.AIR) || b.getType().toString().toLowerCase().contains("glass") || b.getType().toString().toLowerCase().contains("water")) {
                    return false;
                }
                ++i;
            }
        }
        return true;
    }

    public static void CreateEventHorizon(GateObject gate) {
        ArmorStand stand;
        Location l2;
        double z;
        double y;
        double x;
        Vector rotvec;
        double r;
        double angle;
        Location ringloc = new Location(gate.getGate().getWorld(), gate.getGate().getX(), gate.getGate().getY(), gate.getGate().getZ());
        HashMap<Integer, Set<ArmorStand>> horizon = new HashMap<Integer, Set<ArmorStand>>();
        HashSet<ArmorStand> stands = new HashSet<ArmorStand>();
        HashMap<ArmorStand, Vector> hl = new HashMap<ArmorStand, Vector>();
        HashMap<ArmorStand, Vector> hhp = new HashMap<ArmorStand, Vector>();
        gate.getPackages().setHorizonLocations(hl);
        gate.getPackages().setHorizonHeadPosition(hhp);
        ringloc.setX(ringloc.getX() + 0.5);
        ringloc.setY(ringloc.getY() + 1.5);
        ringloc.setZ(ringloc.getZ() + 0.5);
        float facing = 180.0f + gate.getFacing() * 90.0f;
        float angle2 = gate.getFacing() * 90.0f;
        Vector backOffset = CreateGate.getRotBaseVector(new Vector(0.0, 0.0, -0.25), 0.0, Math.toRadians(angle2));
        Vector StandPosition = null;
        int i = 0;
        while (i < 360) {
            angle = Math.toRadians(i - 18);
            r = 2.6;
            rotvec = CreateGate.getRotBaseVector(new Vector(1, 0, 0), angle, Math.toRadians(angle2));
            x = backOffset.getX() + ringloc.getX() + r * rotvec.getX();
            y = backOffset.getY() + ringloc.getY() + r * rotvec.getY();
            z = backOffset.getZ() + ringloc.getZ() + r * rotvec.getZ();
            StandPosition = new Vector(x, y, z);
            l2 = new Location(ringloc.getWorld(), x, y, z);
            stand = ArmorStand.CreateArmorStand(l2, 0.0f, 0.0f, i, facing);
            stand.setSmall(false);
            gate.getPackages().getHorizonLocations().put(stand, StandPosition);
            gate.getPackages().getHorizonHeadPosition().put(stand, stand.getHeadVector());
            stands.add(stand);
            horizon.put(1, stands);
            gate.getPackages().addEntityID(stand);
            i += 14;
        }
        stands = new HashSet();
        i = 0;
        while (i < 360) {
            angle = Math.toRadians(i - 18);
            r = 2.0;
            rotvec = CreateGate.getRotBaseVector(new Vector(1, 0, 0), angle, Math.toRadians(angle2));
            x = backOffset.getX() + ringloc.getX() + r * rotvec.getX();
            y = backOffset.getY() + ringloc.getY() + r * rotvec.getY();
            z = backOffset.getZ() + ringloc.getZ() + r * rotvec.getZ();
            StandPosition = new Vector(x, y, z);
            l2 = new Location(ringloc.getWorld(), x, y, z);
            stand = ArmorStand.CreateArmorStand(l2, 0.0f, 0.0f, i + 2, facing);
            gate.getPackages().getHorizonLocations().put(stand, StandPosition);
            gate.getPackages().getHorizonHeadPosition().put(stand, stand.getHeadVector());
            stands.add(stand);
            horizon.put(2, stands);
            gate.getPackages().addEntityID(stand);
            i += 18;
        }
        stands = new HashSet();
        i = 0;
        while (i < 360) {
            angle = Math.toRadians(i - 18);
            r = 1.4;
            rotvec = CreateGate.getRotBaseVector(new Vector(1, 0, 0), angle, Math.toRadians(angle2));
            x = backOffset.getX() + ringloc.getX() + r * rotvec.getX();
            y = backOffset.getY() + ringloc.getY() + r * rotvec.getY();
            z = backOffset.getZ() + ringloc.getZ() + r * rotvec.getZ();
            StandPosition = new Vector(x, y, z);
            l2 = new Location(ringloc.getWorld(), x, y, z);
            stand = ArmorStand.CreateArmorStand(l2, 0.0f, 0.0f, i + 8, facing);
            gate.getPackages().getHorizonLocations().put(stand, StandPosition);
            gate.getPackages().getHorizonHeadPosition().put(stand, stand.getHeadVector());
            stands.add(stand);
            horizon.put(3, stands);
            gate.getPackages().addEntityID(stand);
            i += 24;
        }
        stands = new HashSet();
        i = 0;
        while (i < 360) {
            angle = Math.toRadians(i - 18);
            r = 1.0;
            rotvec = CreateGate.getRotBaseVector(new Vector(1, 0, 0), angle, Math.toRadians(angle2));
            x = backOffset.getX() + ringloc.getX() + r * rotvec.getX();
            y = backOffset.getY() + ringloc.getY() + r * rotvec.getY();
            z = backOffset.getZ() + ringloc.getZ() + r * rotvec.getZ();
            StandPosition = new Vector(x, y, z);
            l2 = new Location(ringloc.getWorld(), x, y, z);
            stand = ArmorStand.CreateArmorStand(l2, 0.0f, 0.0f, i + 32, facing);
            gate.getPackages().getHorizonLocations().put(stand, StandPosition);
            gate.getPackages().getHorizonHeadPosition().put(stand, stand.getHeadVector());
            stands.add(stand);
            horizon.put(4, stands);
            gate.getPackages().addEntityID(stand);
            i += 40;
        }
        stands = new HashSet();
        i = 0;
        while (i < 360) {
            angle = Math.toRadians(i + 45);
            r = -0.5;
            rotvec = CreateGate.getRotBaseVector(new Vector(1, 0, 0), angle, Math.toRadians(angle2));
            x = backOffset.getX() + ringloc.getX() + r * rotvec.getX();
            y = backOffset.getY() + ringloc.getY() + r * rotvec.getY();
            z = backOffset.getZ() + ringloc.getZ() + r * rotvec.getZ();
            StandPosition = new Vector(x, y, z);
            l2 = new Location(ringloc.getWorld(), x, y, z);
            stand = ArmorStand.CreateArmorStand(l2, 0.0f, 0.0f, i - 45, facing);
            gate.getPackages().getHorizonLocations().put(stand, StandPosition);
            gate.getPackages().getHorizonHeadPosition().put(stand, stand.getHeadVector());
            stands.add(stand);
            horizon.put(5, stands);
            gate.getPackages().addEntityID(stand);
            i += 90;
        }
        for (Set<ArmorStand> st : horizon.values()) {
            for (ArmorStand a : st) {
                a.setHeadMaterial(new ItemStack(gate.getHorizonMaterial()));
            }
        }
        gate.getPackages().setHorizon(horizon);
    }

    public static void createIris(GateObject gate) {
        Location ringloc = new Location(gate.getGate().getWorld(), gate.getGate().getX(), gate.getGate().getY(), gate.getGate().getZ());
        ringloc.setX(ringloc.getX() + 0.5);
        ringloc.setY(ringloc.getY() + 1.5);
        ringloc.setZ(ringloc.getZ() + 0.5);
        HashSet<IrisPart> iris = new HashSet<IrisPart>();
        float facing = gate.getFacing() * 90.0f;
        Vector RingCenter = new Vector(ringloc.getX(), ringloc.getY(), ringloc.getZ());
        double stepOff = 45.0;
        double r = 3.1;
        double p1Off = 15.0;
        double p2Off = 30.0;
        int k = 0;
        while (k < 8) {
            ArmorStand s;
            Vector rotated;
            double angOff;
            IrisPart part = new IrisPart();
            part.setR(r);
            part.setRingCenter(RingCenter);
            part.setRotationY(facing);
            Vector RP1 = new Vector(1, 0, 0);
            RP1 = GateMath.getRotZ(RP1, Math.toRadians((double)k * stepOff)).normalize();
            RP1.multiply(r);
            part.setRotationPoint1(RP1);
            Vector RP2 = new Vector(1, 0, 0);
            RP2 = GateMath.getRotZ(RP2, Math.toRadians((double)k * stepOff + p1Off)).normalize();
            RP2.multiply(r);
            part.setRotationPoint2(RP2);
            Vector RP3 = new Vector(1, 0, 0);
            RP3 = GateMath.getRotZ(RP3, Math.toRadians((double)k * stepOff + p2Off)).normalize();
            RP3.multiply(r);
            part.setRotationPoint3(RP3);
            Vector vr = new Vector(1, 0, 0);
            int standCounter = 0;
            double baseoff = 10.0;
            double headoff = 0.0;
            int a = 0;
            while (a < 60) {
                angOff = baseoff + (double)a + (double)k * stepOff;
                rotated = GateMath.getRotZ(vr, Math.toRadians(angOff)).normalize();
                rotated.multiply(r);
                s = ArmorStand.CreateArmorStand(new Location(ringloc.getWorld(), rotated.getX(), rotated.getY(), rotated.getZ()), 0.0f, 0.0f, (float)angOff, facing);
                part.setHeadAngle(standCounter, Math.toRadians(angOff + headoff));
                part.setStandPos(standCounter, rotated);
                part.setStand(standCounter++, s);
                gate.getPackages().addEntityID(s);
                a += 10;
            }
            a = 0;
            while (a < 30) {
                angOff = baseoff + (double)a + (double)k * stepOff + p1Off;
                rotated = GateMath.getRotZ(vr, Math.toRadians(angOff)).normalize();
                rotated.multiply(r);
                s = ArmorStand.CreateArmorStand(new Location(ringloc.getWorld(), rotated.getX(), rotated.getY(), rotated.getZ()), 0.0f, 0.0f, (float)angOff, facing);
                part.setHeadAngle(standCounter, Math.toRadians(angOff + headoff));
                part.setStandPos(standCounter, rotated);
                part.setStand(standCounter++, s);
                gate.getPackages().addEntityID(s);
                a += 10;
            }
            a = 0;
            while (a < 40) {
                angOff = baseoff + (double)a + (double)k * stepOff + p2Off;
                rotated = GateMath.getRotZ(vr, Math.toRadians(angOff)).normalize();
                rotated.multiply(r);
                s = ArmorStand.CreateArmorStand(new Location(ringloc.getWorld(), rotated.getX(), rotated.getY(), rotated.getZ()), 0.0f, 0.0f, (float)angOff, facing);
                part.setHeadAngle(standCounter, Math.toRadians(angOff + headoff));
                part.setStandPos(standCounter, rotated);
                part.setStand(standCounter++, s);
                gate.getPackages().addEntityID(s);
                a += 10;
            }
            part.calcRelativePosition();
            if (gate.isIrisClosed()) {
                part.setRotation(Math.toRadians(60.0));
            }
            if (!gate.isIrisClosed()) {
                part.setRotation(Math.toRadians(0.0));
            }
            part.setIrisMaterial(gate.getIrisMaterial());
            iris.add(part);
            ++k;
        }
        gate.getPackages().setIris(iris);
    }
}

