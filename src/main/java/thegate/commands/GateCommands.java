/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.entity.Player
 */
package thegate.commands;

import java.util.List;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import thegate.gate.CommandUseType;
import thegate.gate.CommandUser;
import thegate.gate.GateCommandInfo;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.TheGateMain;

public class GateCommands {
    public static boolean handle(String[] args, Player player) {
        boolean hasPlayer;
        boolean bl = hasPlayer = player != null;
        if (args.length < 2) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Missing arguments!");
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "/thegate commands <add|list|remove> ...");
            return true;
        }
        switch (args[1].toLowerCase()) {
            case "add": {
                GateCommands.add(args, player, hasPlayer);
                return true;
            }
            case "list": {
                GateCommands.list(args, player, hasPlayer);
                return true;
            }
            case "remove": {
                GateCommands.remove(args, player, hasPlayer);
                return true;
            }
        }
        return true;
    }

    private static void add(String[] args, Player player, boolean hasPlayer) {
        String command = "/thegate commands add <address> <player|console> <OnEnter|OnExit|OnActivate|OnDeactivate> <Command>";
        if (args.length < 6) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Missing arguments!");
            GateCommands.sendMessage(hasPlayer, player, null, command);
            return;
        }
        CommandUser user = null;
        CommandUseType useType = null;
        String gateCommand = "";
        String address = args[2];
        GateObject gate = GateManager.getGateWithAddress(address);
        if (gate == null) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Gate with address <" + address + "> not found!");
            return;
        }
        int i = 5;
        while (i < args.length) {
            gateCommand = String.valueOf(gateCommand) + args[i] + " ";
            ++i;
        }
        if ((gateCommand = gateCommand.substring(0, gateCommand.length() - 1)).length() >= 256) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Command must not exceed 128");
            return;
        }
        try {
            user = CommandUser.valueOf(args[3]);
        }
        catch (Exception e) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Unknown user type: " + args[3]);
        }
        try {
            useType = CommandUseType.valueOf(args[4]);
        }
        catch (Exception e) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Unknown usage type: " + args[4]);
        }
        if (gate.addCommand(useType, gateCommand, user)) {
            TheGateMain.SaveLoadInterface.saveGateCommand(gate.getAddress(), gateCommand, useType, user);
            GateCommands.sendMessage(hasPlayer, player, ChatColor.GREEN, "Command was added to gate: " + gate.getAddress());
        } else {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Could not add command!");
        }
    }

    private static void remove(String[] args, Player player, boolean hasPlayer) {
        GateCommandInfo info;
        String command = "/thegate commands remove <address> <usetype> <index>";
        if (args.length < 5) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Missing arguments!");
            GateCommands.sendMessage(hasPlayer, player, null, command);
            return;
        }
        CommandUseType type = null;
        int index = 0;
        try {
            type = CommandUseType.valueOf(args[3]);
        }
        catch (Exception e) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Could not find use type: " + args[3]);
            return;
        }
        try {
            index = Integer.valueOf(args[4]);
        }
        catch (Exception e) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Could not cast index argument to integer: " + args[4]);
            return;
        }
        GateObject gate = GateManager.getGateWithAddress(args[2]);
        if (gate == null) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Could not find gate with address: " + args[2]);
        }
        if ((info = gate.removeCommand(type, index)) != null) {
            TheGateMain.SaveLoadInterface.removeGateCommand(args[2], info.getCommand(), type);
            GateCommands.sendMessage(hasPlayer, player, ChatColor.GREEN, "Command was removed from gate: " + args[2]);
        } else {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.GOLD, "Could not find command to remove!");
        }
    }

    private static void list(String[] args, Player player, boolean hasPlayer) {
        String command = "/thegate commands list <address>";
        if (args.length < 3) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Missing arguments!");
            GateCommands.sendMessage(hasPlayer, player, null, command);
            return;
        }
        String address = args[2];
        GateObject gate = GateManager.getGateWithAddress(address);
        if (gate == null) {
            GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "No gate found with address: " + address);
            return;
        }
        GateCommands.sendMessage(hasPlayer, player, ChatColor.GOLD, "Commands can be used by clicking on them");
        GateCommands.sendMessage(hasPlayer, player, ChatColor.RED, "Note: When clicking the command the player will be the executer of the command even if console was specified as executer!");
        GateCommands.sendMessage(hasPlayer, player, ChatColor.GOLD, "<==========[Commands for " + address + "]==========>");
        gate.getGateCommands().keySet().forEach(x -> {
            List<GateCommandInfo> gateCommand = gate.getGateCommands().get(x);
            if (gateCommand != null) {
                int i = 0;
                while (i < gateCommand.size()) {
                    TextComponent msg0 = new TextComponent("Type: " + x.toString() + " ");
                    TextComponent msg1 = new TextComponent("[" + gateCommand.get(i).getCommand() + "]");
                    TextComponent msg2 = new TextComponent(" [X] ");
                    msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + gateCommand.get(i).getCommand().replace("{PLAYER}", player.getName())));
                    msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thegate commands remove " + gate.getAddress() + " " + x.toString() + " " + i));
                    msg0.setColor(ChatColor.AQUA);
                    msg1.setColor(ChatColor.GOLD);
                    msg2.setColor(ChatColor.RED);
                    player.spigot().sendMessage(new BaseComponent[]{msg0, msg1, msg2});
                    ++i;
                }
            }
        });
        GateCommands.sendMessage(hasPlayer, player, ChatColor.GOLD, "<=========================================>");
    }

    private static void sendMessage(boolean hasPlayer, Player player, ChatColor c2, String message) {
        if (hasPlayer) {
            player.sendMessage((c2 == null ? "" : c2) + message);
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, message);
        }
    }

    private static void textCommand(Player p, String msg, String command, ChatColor c2) {
        TextComponent message = new TextComponent(msg);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        message.setColor(c2);
        p.spigot().sendMessage((BaseComponent)message);
    }
}

