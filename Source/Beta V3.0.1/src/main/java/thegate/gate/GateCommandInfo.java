/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package thegate.gate;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import thegate.gate.CommandUser;
import thegate.gate.GateObject;

public class GateCommandInfo {
    private final String command;
    private final CommandUser user;
    private final GateObject gate;

    public GateCommandInfo(String command, CommandUser user, GateObject gate) {
        this.command = command;
        this.user = user;
        this.gate = gate;
    }

    public void dispach(CommandSender sender) {
        String[] args = this.command.split(" ");
        String out = "";
        int count = 0;
        String[] stringArray = args;
        int n = args.length;
        int n2 = 0;
        while (n2 < n) {
            String s = stringArray[n2];
            if (s.charAt(0) == '~') {
                ++count;
                if (s.length() == 1) {
                    switch (count) {
                        case 1: {
                            s = String.valueOf(this.gate.getGate().getX());
                            break;
                        }
                        case 2: {
                            s = String.valueOf(this.gate.getGate().getY());
                            break;
                        }
                        case 3: {
                            s = String.valueOf(this.gate.getGate().getZ());
                            break;
                        }
                    }
                } else if (s.length() > 1) {
                    double x = 0.0;
                    try {
                        x = Double.valueOf(s.replace("~", ""));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    switch (count) {
                        case 1: {
                            s = String.valueOf(this.gate.getGate().getX() + x);
                            break;
                        }
                        case 2: {
                            s = String.valueOf(this.gate.getGate().getY() + x);
                            break;
                        }
                        case 3: {
                            s = String.valueOf(this.gate.getGate().getZ() + x);
                            break;
                        }
                    }
                }
            }
            out = String.valueOf(out) + s + " ";
            ++n2;
        }
        out = out.substring(0, out.length() - 1);
        if (sender instanceof Player) {
            Player player = (Player)sender;
            out = out.replace("{PLAYER}", player.getName());
        }
        this.user.dispach(out, sender);
    }

    public String getCommand() {
        return this.command;
    }

    public CommandUser getUser() {
        return this.user;
    }

    public String toString() {
        return "Command: " + this.command + " -> for user: " + this.user.name();
    }
}

