/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package thegate.gate;

import org.bukkit.Material;
import thegate.main.ConfigManager;

public enum BlockedState {
    unlocked{

        @Override
        public Material getMaterial() {
            return Material.DARK_OAK_DOOR;
        }

        @Override
        public String getText() {
            return ConfigManager.getString("GUIS.EditGateGUI.Items.Unlock", new String[0]);
        }
    }
    ,
    locked{

        @Override
        public Material getMaterial() {
            return Material.BARRIER;
        }

        @Override
        public String getText() {
            return ConfigManager.getString("GUIS.EditGateGUI.Items.Lock", new String[0]);
        }
    }
    ,
    block_incoming{

        @Override
        public Material getMaterial() {
            return Material.SHIELD;
        }

        @Override
        public String getText() {
            return ConfigManager.getString("GUIS.EditGateGUI.Items.BlockIncoming", new String[0]);
        }
    }
    ,
    block_outgoing{

        @Override
        public Material getMaterial() {
            return Material.COBWEB;
        }

        @Override
        public String getText() {
            return ConfigManager.getString("GUIS.EditGateGUI.Items.BlockOutgoing", new String[0]);
        }
    };


    private BlockedState() {
    }

    public abstract Material getMaterial();

    public abstract String getText();

    /* synthetic */ BlockedState(String string, int n, BlockedState blockedState) {
        this();
    }
}

