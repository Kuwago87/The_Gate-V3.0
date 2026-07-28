/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package thegate.commands;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import thegate.commands.CreateGateCommand;
import thegate.commands.GateCommands;
import thegate.commands.RotateGateCommand;
import thegate.commands.SyncGatesWithDatabase;
import thegate.gate.CommandUseType;
import thegate.gate.CommandUser;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.Config;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;

public class TheGateCommand
implements TabExecutor {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (args.length > 0) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player)sender;
            }
            switch (args[0].toLowerCase()) {
                case "reload": {
                    boolean bl;
                    if (sender instanceof Player) {
                        bl = this.reload(player);
                        return bl;
                    }
                    bl = this.reload(null);
                    return bl;
                }
                case "permission": {
                    if (args.length <= 1) return false;
                    if (args[1] == null) return false;
                    switch (args[1].toLowerCase()) {
                        case "info": {
                            if (args.length > 2 && args[2] != null) {
                                switch (args[2].toLowerCase()) {
                                    case "admin": {
                                        if (!(sender instanceof Player)) return false;
                                        boolean bl = this.permissionListAdmin(player);
                                        return bl;
                                    }
                                    case "owner": {
                                        if (!(sender instanceof Player)) return false;
                                        boolean bl = this.permissionListOwner(player);
                                        return bl;
                                    }
                                    case "user": {
                                        if (!(sender instanceof Player)) return false;
                                        boolean bl = this.permissionListUser(player);
                                        return bl;
                                    }
                                }
                                if (!(sender instanceof Player)) return false;
                                this.permissionSelection(player);
                                return true;
                            }
                            if (!(sender instanceof Player)) return false;
                            this.permissionSelection(player);
                            return true;
                        }
                    }
                    return false;
                }
                case "syncdb": {
                    boolean bl;
                    if (sender instanceof Player) {
                        bl = SyncGatesWithDatabase.syncDB(player);
                        return bl;
                    }
                    bl = SyncGatesWithDatabase.syncDB(null);
                    return bl;
                }
                case "help": {
                    boolean bl;
                    if (sender instanceof Player) {
                        bl = this.theGateCommands(player);
                        return bl;
                    }
                    bl = this.theGateCommands(null);
                    return bl;
                }
                case "gatedescription": {
                    boolean bl;
                    if (args.length < 3) {
                        if (sender instanceof Player) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.MissingArguments", new String[0]));
                            player.sendMessage("/thegate gatedescription <address> <description>");
                            return true;
                        }
                        TheGateMain.theGateMain.getLogger().log(Level.WARNING, "Missing arguments!");
                        TheGateMain.theGateMain.getLogger().log(Level.WARNING, "/thegate gatedescription <address> <description>");
                        return true;
                    }
                    if (sender instanceof Player && !player.hasPermission(Perms.thegate_owner_gatedescriptioncommand.value()) && !player.hasPermission(Perms.thegate_admin_gatedescriptioncommand.value())) {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                        return true;
                    }
                    String address = args[1];
                    String desc = "";
                    int i = 2;
                    while (true) {
                        if (i >= args.length) {
                            if (!(sender instanceof Player)) break;
                            bl = this.gateDescriptionCommand(player, address, desc);
                            return bl;
                        }
                        desc = String.valueOf(desc) + args[i] + (i < args.length - 1 ? " " : "");
                        ++i;
                    }
                    bl = this.gateDescriptionCommand(null, address, desc);
                    return bl;
                }
                case "gatename": {
                    boolean bl;
                    if (args.length < 3) {
                        if (sender instanceof Player) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.MissingArguments", new String[0]));
                            player.sendMessage("/gatename <address> <name>");
                            return true;
                        }
                        TheGateMain.theGateMain.getLogger().log(Level.WARNING, "Missing arguments!");
                        TheGateMain.theGateMain.getLogger().log(Level.WARNING, "/thegate gatename <address> <name>");
                        return true;
                    }
                    if (sender instanceof Player && !player.hasPermission(Perms.thegate_owner_gatenamecommand.value()) && !player.hasPermission(Perms.thegate_admin_gatenamecommand.value())) {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                        return true;
                    }
                    String address2 = args[1];
                    String name = "";
                    int i = 2;
                    while (true) {
                        if (i >= args.length) {
                            name = name.substring(0, name.length() - 1);
                            if (!(sender instanceof Player)) break;
                            bl = this.gatenamingcommand(player, address2, name);
                            return bl;
                        }
                        name = String.valueOf(name) + args[i] + " ";
                        ++i;
                    }
                    bl = this.gatenamingcommand(null, address2, name);
                    return bl;
                }
                case "create": {
                    if (sender instanceof Player) {
                        if (!player.hasPermission(Perms.thegate_owner_gatecreatecommand.value()) && !player.hasPermission(Perms.thegate_admin_gatecreatecommand.value())) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                            return true;
                        }
                        if (!player.hasPermission(Perms.thegate_owner_creategate.value()) && !player.hasPermission(Perms.thegate_admin_creategate.value())) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                            return true;
                        }
                        if (GateManager.GateInRadius(player.getLocation())) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.GateNear", new String[0]));
                            return true;
                        }
                        if (!player.hasPermission(Perms.thegate_admin_creategate.value()) && GateManager.getPlayerGateAmmount(player) >= Globals.PlayerGateAmmount) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.GateAmmountCap", new String[0]));
                            return true;
                        }
                        if (args.length < 2) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.MissingArguments", new String[0]));
                            player.sendMessage("/thegate create <address> [angle]");
                            return true;
                        }
                        float rot = -1.0f;
                        if (args.length <= 2) return CreateGateCommand.create(player, args[1], "", 0, 0, 0, rot);
                        try {
                            rot = Float.valueOf(args[2]).floatValue();
                            return CreateGateCommand.create(player, args[1], "", 0, 0, 0, rot);
                        }
                        catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "Casting error!");
                            player.sendMessage("/thegate create <address> [angle]");
                            player.sendMessage("angle := [0, 360]");
                            return true;
                        }
                    }
                    if (args.length < 7) {
                        Bukkit.getLogger().log(Level.WARNING, "Missing agruments!");
                        Bukkit.getLogger().log(Level.WARNING, "/thegate create <address> <worldname> <x> <y> <z> <angle>");
                        return true;
                    }
                    try {
                        int x = Integer.valueOf(args[3]);
                        int y = Integer.valueOf(args[4]);
                        int z = Integer.valueOf(args[5]);
                        float rot = Float.valueOf(args[6]).floatValue();
                        return CreateGateCommand.create(null, args[1], args[2], x, y, z, rot);
                    }
                    catch (Exception e) {
                        Bukkit.getLogger().log(Level.WARNING, "Argument casting faild!");
                        Bukkit.getLogger().log(Level.WARNING, "One of the coordinates could not be casted to a number!");
                        Bukkit.getLogger().log(Level.WARNING, "/thegate create <address> <worldname> <x> <y> <z> <angle>");
                        Bukkit.getLogger().log(Level.WARNING, "angle := [0, 360]");
                        return true;
                    }
                }
                case "rotate": {
                    if (!(sender instanceof Player)) {
                        if (args.length >= 3) return RotateGateCommand.RotateGate(null, args[1], args[2]);
                        Bukkit.getLogger().log(Level.WARNING, "Missing agruments!");
                        Bukkit.getLogger().log(Level.WARNING, "/thegate rotate <address> <angle>");
                        Bukkit.getLogger().log(Level.WARNING, "angle := [0, 360]");
                        return true;
                    }
                    if (!player.hasPermission(Perms.thegate_owner_gaterotatecommand.value()) && !player.hasPermission(Perms.thegate_admin_gaterotatecommand.value())) {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                        return true;
                    }
                    if (args.length >= 3) return RotateGateCommand.RotateGate(player, args[1], args[2]);
                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.MissingArguments", new String[0]));
                    player.sendMessage("/thegate rotate <address> <angle>");
                    player.sendMessage("angle := [0, 360]");
                    return true;
                }
                case "rotatenear": {
                    if (!(sender instanceof Player)) return true;
                    if (!player.hasPermission(Perms.thegate_owner_gaterotatenearcommand.value()) && !player.hasPermission(Perms.thegate_admin_gaterotatenearcommand.value())) {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                        return true;
                    }
                    if (args.length >= 2) return RotateGateCommand.RotateGate(player, null, args[1]);
                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.MissingArguments", new String[0]));
                    player.sendMessage("/thegate rotatenear <angle>");
                    player.sendMessage("angle := [0, 360]");
                    return true;
                }
                case "commands": {
                    if (player.hasPermission(Perms.thegate_admin_editgatecommands.value())) return GateCommands.handle(args, player);
                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                    return true;
                }
            }
            return false;
        }
        if (sender instanceof Player) {
            return this.thegateInfo((Player)sender);
        }
        TheGateMain pl = TheGateMain.theGateMain;
        pl.getLogger().log(Level.INFO, "----------------------------------------------------------------------------");
        pl.getLogger().log(Level.INFO, "|                           The_Gate_debug_info                            |");
        pl.getLogger().log(Level.INFO, "----------------------------------------------------------------------------");
        pl.getLogger().log(Level.INFO, String.valueOf(TheGateMain.theGateMain.getDescription().getName()) + "_v" + TheGateMain.theGateMain.getDescription().getVersion());
        pl.getLogger().log(Level.INFO, "Minecraft Server Version: " + TheGateMain.theGateMain.getServer().getVersion());
        pl.getLogger().log(Level.INFO, "Minecraft Bukkit Version: " + TheGateMain.theGateMain.getServer().getBukkitVersion());
        pl.getLogger().log(Level.INFO, "Java version: " + System.getProperty("java.version"));
        pl.getLogger().log(Level.INFO, "Player count: " + TheGateMain.theGateMain.getServer().getOnlinePlayers().size() + "/" + TheGateMain.theGateMain.getServer().getMaxPlayers());
        pl.getLogger().log(Level.INFO, "----------------------------------------------------------------------------");
        pl.getLogger().log(Level.INFO, "Using bungee: " + Globals.UseBungee);
        pl.getLogger().log(Level.INFO, "Server name: " + Globals.ServerName);
        pl.getLogger().log(Level.INFO, "Used save format: " + Globals.SaveFromat);
        long min = ChronoUnit.MINUTES.between(TheGateMain.theGateMain.AutoSyncTime, LocalDateTime.now());
        long sec = ChronoUnit.SECONDS.between(TheGateMain.theGateMain.AutoSyncTime, LocalDateTime.now());
        if (TheGateMain.theGateMain.AutoSyncTime != null) {
            pl.getLogger().log(Level.INFO, "LastAutoSync: " + (min <= 0L ? String.valueOf(sec) + "sec" : String.valueOf(min) + "min"));
        }
        int total = GateManager.getGatesAsSet().size() + GateManager.getGatesOnOtherServer().size();
        pl.getLogger().log(Level.INFO, "Gate count: " + GateManager.getGatesAsSet().size() + "/" + total);
        pl.getLogger().log(Level.INFO, "----------------------------------------------------------------------------");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String str, String[] args) {
        block49: {
            block47: {
                block45: {
                    block43: {
                        if (args.length == 1) {
                            return Lists.newArrayList(new String[]{"help", "reload", "permission", "syncdb", "gatedescription", "gatename", "create", "rotate", "rotatenear", "commands"});
                        }
                        if (args.length != 2) break block43;
                        switch (args[0].toLowerCase()) {
                            case "permission": {
                                return Lists.newArrayList(new String[]{"info"});
                            }
                            case "commands": {
                                return Lists.newArrayList(new String[]{"add", "list", "remove"});
                            }
                            case "gatename": 
                            case "rotate": 
                            case "gatedescription": {
                                return GateManager.getAvailableGatesForEdit((Player)sender).stream().map(x -> x.getAddress()).collect(Collectors.toList());
                            }
                        }
                        return Lists.newArrayList(new String[]{""});
                    }
                    if (args.length != 3) break block45;
                    switch (args[0].toLowerCase()) {
                        case "permission": {
                            return Lists.newArrayList(new String[]{"admin", "owner", "user"});
                        }
                        case "commands": {
                            return GateManager.getAvailableGatesForEdit((Player)sender).stream().map(x -> x.getAddress()).collect(Collectors.toList());
                        }
                    }
                    return Lists.newArrayList(new String[]{""});
                }
                if (args.length != 4) break block47;
                switch (args[0].toLowerCase()) {
                    case "commands": {
                        if (args[1].equals("add")) {
                            return Arrays.stream(CommandUser.values()).map(x -> x.toString()).collect(Collectors.toList());
                        }
                        if (!args[1].equals("remove")) break;
                        return Arrays.stream(CommandUseType.values()).map(x -> x.toString()).collect(Collectors.toList());
                    }
                }
                return Lists.newArrayList(new String[]{""});
            }
            if (args.length != 5) break block49;
            switch (args[0].toLowerCase()) {
                case "commands": {
                    if (!args[1].equals("add")) break;
                    return Arrays.stream(CommandUseType.values()).map(x -> x.toString()).collect(Collectors.toList());
                }
            }
            return Lists.newArrayList(new String[]{""});
        }
        return Lists.newArrayList(new String[]{""});
    }

    private boolean theGateCommands(Player p) {
        if (p != null) {
            p.sendMessage(ChatColor.GOLD + "(===============[" + ChatColor.YELLOW + "The_Gate Commands" + ChatColor.GOLD + "]===============)");
            p.sendMessage(ChatColor.GOLD + "{List contains only commands usable by the player}");
            if (p.hasPermission(Perms.thegate_user_gatetools.value())) {
                this.textCommand(p, "[/gatetools]", "/gatetools", ChatColor.YELLOW);
            }
            this.textCommand(p, "[/thegate]", "/thegate", ChatColor.YELLOW);
            this.textCommand(p, "[/thegate help]", "/thegate help", ChatColor.YELLOW);
            if (p.hasPermission(Perms.thegate_admin_reloadconfig.value())) {
                this.textCommand(p, "[/thegate reload]", "/thegate reload", ChatColor.YELLOW);
            }
            if (Globals.MoreInfo) {
                this.textCommand(p, "[/thegate permission]", "/thegate permission info", ChatColor.YELLOW);
            }
            if (p.hasPermission(Perms.thegate_admin_syncdatabase.value())) {
                this.textCommand(p, "[/thegate syncdb]", "/thegate syncdb", ChatColor.YELLOW);
            }
            if (p.hasPermission(Perms.thegate_owner_gatedescriptioncommand.value()) || p.hasPermission(Perms.thegate_admin_gatedescriptioncommand.value())) {
                this.textCommandSuggest(p, "[/thegate gatedescription <address> <description>]", "/thegate gatedescription", ChatColor.YELLOW);
            }
            if (p.hasPermission(Perms.thegate_owner_gatenamecommand.value()) || p.hasPermission(Perms.thegate_admin_gatenamecommand.value())) {
                this.textCommandSuggest(p, "[/thegate gatename <address> <name>]", "/thegate gatename", ChatColor.YELLOW);
            }
            if (p.hasPermission(Perms.thegate_owner_gatecreatecommand.value()) || p.hasPermission(Perms.thegate_admin_gatecreatecommand.value())) {
                this.textCommandSuggest(p, "[/thegate create <address> [angle]]", "/thegate create", ChatColor.YELLOW);
            }
            if (p.hasPermission(Perms.thegate_owner_gaterotatecommand.value()) || p.hasPermission(Perms.thegate_admin_gaterotatecommand.value())) {
                this.textCommandSuggest(p, "[/thegate rotate <address> <angle>]", "/thegate rotate", ChatColor.YELLOW);
            }
            if (p.hasPermission(Perms.thegate_owner_gaterotatenearcommand.value()) || p.hasPermission(Perms.thegate_admin_gaterotatenearcommand.value())) {
                this.textCommandSuggest(p, "[/thegate rotatenear <angle>]", "/thegate rotatenear", ChatColor.YELLOW);
            }
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "This command will give an interactive list ingame.");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "Commands:");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "/thegate [reload|help|syncdb|gatedescription|gatename|create|rotate]");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "alias-> /tg");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "/gatetools");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "alias-> /gt");
        }
        return true;
    }

    private boolean thegateInfo(Player p) {
        p.sendMessage("\u00a76[The_Gate]: " + TheGateMain.theGateMain.getDescription().getName() + "_v" + TheGateMain.theGateMain.getDescription().getVersion());
        if (Globals.MoreInfo) {
            p.sendMessage("\u00a76GateVisibilityRadius: \u00a73" + Globals.VisibilityRadius);
            p.sendMessage("\u00a76ActiveTime: \u00a73" + Globals.GateTime);
            p.sendMessage("\u00a76UserOwnedGates: \u00a73" + Globals.PlayerGateAmmount);
            p.sendMessage("\u00a76DeadlyVortex: " + (Globals.DeadlyVortex ? "\u00a72" : "\u00a74") + Globals.DeadlyVortex);
            p.sendMessage("\u00a76GateCanBreakBlocks: " + (Globals.GateCanBreakBlocks ? "\u00a72" : "\u00a74") + Globals.GateCanBreakBlocks);
            p.sendMessage("\u00a76DestrucitonSettings: ");
            p.sendMessage("\u00a76- -SelectionX: \u00a73" + Globals.SelectionX);
            p.sendMessage("\u00a76- -Selection-X: \u00a73" + Globals.Selection_X);
            p.sendMessage("\u00a76- -SelectionY: \u00a73" + Globals.SelectionY);
            p.sendMessage("\u00a76- -Selection-Y: \u00a73" + Globals.Selection_Y);
            p.sendMessage("\u00a76- -Distance: \u00a73" + Globals.DestructionDistance);
            p.sendMessage("\u00a76- -DistanceMult: \u00a73" + Globals.DestructionDistanceMult);
            p.sendMessage("\u00a76- -Radius: \u00a73" + Globals.Radius);
            p.sendMessage("\u00a76GateExclusionRadius: \u00a73" + Globals.GateExclusionRadius);
            long min = ChronoUnit.MINUTES.between(TheGateMain.theGateMain.AutoSyncTime, LocalDateTime.now());
            long sec = ChronoUnit.SECONDS.between(TheGateMain.theGateMain.AutoSyncTime, LocalDateTime.now());
            if (TheGateMain.theGateMain.AutoSyncTime != null) {
                p.sendMessage("\u00a76LastAutoSync: \u00a73" + (min <= 0L ? String.valueOf(sec) + "sec" : String.valueOf(min) + "min"));
            }
        }
        return true;
    }

    private void permissionSelection(Player p) {
        TextComponent PreText = new TextComponent("Show permissions: ");
        PreText.setColor(ChatColor.GOLD);
        TextComponent TextComAdmin = new TextComponent("[Admin]");
        TextComAdmin.setColor(ChatColor.DARK_RED);
        TextComAdmin.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thegate permission info admin"));
        TextComponent TextComOwner = new TextComponent(" [Owner] ");
        TextComOwner.setColor(ChatColor.YELLOW);
        TextComOwner.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thegate permission info owner"));
        TextComponent TextComUser = new TextComponent("[User]");
        TextComUser.setColor(ChatColor.GRAY);
        TextComUser.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thegate permission info user"));
        p.spigot().sendMessage(new BaseComponent[]{PreText, TextComAdmin, TextComOwner, TextComUser});
    }

    private boolean permissionListAdmin(Player p) {
        if (!Globals.MoreInfo) {
            return true;
        }
        p.sendMessage(ChatColor.DARK_AQUA + "(===============[" + ChatColor.DARK_RED + "Admin" + ChatColor.GOLD + " Permissions" + ChatColor.DARK_AQUA + "]===============)");
        Perms[] permsArray = Perms.values();
        int n = permsArray.length;
        int n2 = 0;
        while (n2 < n) {
            Perms perm = permsArray[n2];
            String permOut = perm.value();
            if (permOut.contains(".admin.")) {
                p.sendMessage(this.hasPerm(p, permOut));
            }
            ++n2;
        }
        p.sendMessage(ChatColor.DARK_AQUA + "(============================================)");
        this.permissionSelection(p);
        return true;
    }

    private boolean permissionListOwner(Player p) {
        if (!Globals.MoreInfo) {
            return true;
        }
        p.sendMessage(ChatColor.DARK_AQUA + "(===============[" + ChatColor.YELLOW + "Owner" + ChatColor.GOLD + " Permissions" + ChatColor.DARK_AQUA + "]===============)");
        Perms[] permsArray = Perms.values();
        int n = permsArray.length;
        int n2 = 0;
        while (n2 < n) {
            Perms perm = permsArray[n2];
            String permOut = perm.value();
            if (permOut.contains(".owner.")) {
                p.sendMessage(this.hasPerm(p, permOut));
            }
            ++n2;
        }
        p.sendMessage(ChatColor.DARK_AQUA + "(============================================)");
        this.permissionSelection(p);
        return true;
    }

    private boolean permissionListUser(Player p) {
        if (!Globals.MoreInfo) {
            return true;
        }
        p.sendMessage(ChatColor.DARK_AQUA + "(===============[" + ChatColor.GRAY + "User" + ChatColor.GOLD + " Permissions" + ChatColor.DARK_AQUA + "]===============)");
        Perms[] permsArray = Perms.values();
        int n = permsArray.length;
        int n2 = 0;
        while (n2 < n) {
            Perms perm = permsArray[n2];
            String permOut = perm.value();
            if (permOut.contains(".user.")) {
                p.sendMessage(this.hasPerm(p, permOut));
            }
            ++n2;
        }
        p.sendMessage(ChatColor.DARK_AQUA + "(============================================)");
        this.permissionSelection(p);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean gateDescriptionCommand(Player p, String address, String description) {
        if (p != null) {
            GateObject obj = GateManager.getGateWithAddress(address);
            if (obj == null) {
                p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoGateWithAddress", "{ADDRESS}", address));
                return false;
            }
            if (p.hasPermission(Perms.thegate_admin_editgate.value()) || p.hasPermission(Perms.thegate_admin_editallgates.value()) || p.hasPermission(Perms.thegate_owner_editgate.value()) && obj.hasOwningRelation(p)) {
                if (description.length() > 64) {
                    p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.ToManyChars", "{CHAR_AMMOUNT}", "64"));
                    return true;
                }
                obj.setDescription(description);
                obj.Repair();
                TheGateMain.SaveLoadInterface.updateGate(obj);
                p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.DescriptionSet", new String[0]));
                return true;
            }
            p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
            return true;
        }
        if (p != null) return true;
        GateObject obj = GateManager.getGateWithAddress(address);
        if (obj == null) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "Gate with address: " + address + " could not be found!");
            return false;
        }
        if (description.length() > 64) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "The given text is to long! Only 64 characters are allowed!");
            return false;
        }
        obj.setDescription(description);
        obj.Repair();
        TheGateMain.SaveLoadInterface.updateGate(obj);
        TheGateMain.theGateMain.getLogger().log(Level.INFO, "Gate Discription set!");
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean gatenamingcommand(Player p, String address, String name) {
        if (p != null) {
            GateObject obj = GateManager.getGateWithAddress(address);
            if (obj == null) {
                p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoGateWithAddress", "{ADDRESS}", address));
                return false;
            }
            if (p.hasPermission(Perms.thegate_admin_editgate.value()) || p.hasPermission(Perms.thegate_admin_editallgates.value()) || p.hasPermission(Perms.thegate_owner_editgate.value()) && obj.hasOwningRelation(p)) {
                if (name.length() > 20) {
                    p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.ToManyChars", "{CHAR_AMMOUNT}", "20"));
                    return false;
                }
                obj.setGateName(name);
                obj.Repair();
                TheGateMain.SaveLoadInterface.updateGate(obj);
                p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.GateNameSet", new String[0]));
                return true;
            }
            p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
            return true;
        }
        if (p != null) return true;
        GateObject obj = GateManager.getGateWithAddress(address);
        if (obj == null) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "Gate with address: " + name + " could not be found!");
            return false;
        }
        if (name.length() > 20) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "The given text is to long! Only 20 characters are allowed!");
            return false;
        }
        obj.setGateName(name);
        obj.Repair();
        TheGateMain.SaveLoadInterface.updateGate(obj);
        TheGateMain.theGateMain.getLogger().log(Level.INFO, "Gate Discription set!");
        return true;
    }

    private String hasPerm(Player p, String permission) {
        boolean hasPerm = p.hasPermission(permission);
        ChatColor c2 = hasPerm ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
        return ChatColor.GRAY + "->" + permission + (permission.charAt(permission.length() - 1) == '.' ? "<args>" : "") + ": " + c2 + "[" + String.valueOf(hasPerm).toUpperCase() + "]";
    }

    private boolean reload(Player p) {
        if (p != null) {
            if (!p.hasPermission(Perms.thegate_admin_reloadconfig.value())) {
                p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                return true;
            }
            if (p.hasPermission(Perms.thegate_admin_reloadconfig.value())) {
                TheGateMain.theGateMain.reloadConfig();
                Config.LoadConfig((Plugin)TheGateMain.theGateMain);
                TheGateMain.theGateMain.configManager.CreateConfigFiles();
                GateManager.repairAll();
                p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.ConfigReload", new String[0]));
                return true;
            }
        }
        TheGateMain.theGateMain.reloadConfig();
        Config.LoadConfig((Plugin)TheGateMain.theGateMain);
        TheGateMain.theGateMain.configManager.CreateConfigFiles();
        GateManager.setGatesOnOtherServer(TheGateMain.SaveLoadInterface.GateListOtherServers());
        GateManager.repairAll();
        TheGateMain.theGateMain.getLogger().log(Level.INFO, "Config reloaded!");
        return true;
    }

    private void textCommand(Player p, String msg, String command, ChatColor c2) {
        TextComponent message = new TextComponent(msg);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        message.setColor(c2);
        p.spigot().sendMessage((BaseComponent)message);
    }

    private void textCommandSuggest(Player p, String msg, String command, ChatColor c2) {
        TextComponent message = new TextComponent(msg);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        message.setColor(c2);
        p.spigot().sendMessage((BaseComponent)message);
    }
}

