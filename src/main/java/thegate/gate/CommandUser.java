/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package thegate.gate;

import java.util.logging.Level;
import org.bukkit.command.CommandSender;
import thegate.main.TheGateMain;

public enum CommandUser {
    Player{

        @Override
        public void dispach(String command, CommandSender sender) {
            if (sender == null) {
                TheGateMain.theGateMain.getLogger().log(Level.WARNING, "Command could not be executed!");
                TheGateMain.theGateMain.getLogger().log(Level.WARNING, "-> " + command.toString());
                return;
            }
            TheGateMain.theGateMain.getServer().dispatchCommand(sender, command);
        }
    }
    ,
    Console{

        @Override
        public void dispach(String command, CommandSender sender) {
            TheGateMain.theGateMain.getServer().dispatchCommand((CommandSender)TheGateMain.theGateMain.getServer().getConsoleSender(), command);
        }
    };


    private CommandUser() {
    }

    public abstract void dispach(String var1, CommandSender var2);

    /* synthetic */ CommandUser(String string, int n, CommandUser commandUser) {
        this();
    }
}

