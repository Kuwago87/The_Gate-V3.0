/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataOutput
 *  com.google.common.io.ByteStreams
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package thegate.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Reconnect {
    public void ReconnectPlayer(String Server2, Player player, Plugin p) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(Server2);
        player.sendPluginMessage(p, "BungeeCord", out.toByteArray());
    }
}

