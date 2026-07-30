# The_Gate-V3.0 | A simple Rewrite of the Gate plugin for MC 1.26.2+

 original Plugin:

The_Gate (minecraft 1.15 to 1.21.1 ) | Author: EndMy5uffering

Public repository for the Minecraft Spigot plugin The_Gate :https://github.com/Badading/The_Gate/

You can contact us here:

Discord: https://discord.gg/RW4C7bn | Spigot: https://www.spigotmc.org/resources/the-gate.70247/

------------------------------
The_Gate-V3.0 (minecraft 1.26.2+)

Modrinth: https://modrinth.com/plugin/the-gate

You can contact me here: https://github.com/Kuwago87/The_Gate-V3.0/issues

-------------------------
### Upgrading from the original plugin

If you're currently running the original [The Gate](https://github.com/Badading/The_Gate) plugin
on 1.21.1 and want to switch to this 26.2-compatible fork, your existing gate data carries over
without any extra steps:

1. **Back up your `plugins/The_Gate/` folder first.** This should go smoothly, but backing up
   before any plugin swap costs nothing and saves everything if something unrelated goes wrong.
2. Stop your server.
3. Remove the old `The_Gate.jar`, drop this fork's jar in `plugins/` instead.
4. Leave your existing `plugins/The_Gate/` folder (config, lang file, and database) exactly where
   it is — don't delete or regenerate it.
5. Update your server itself to Minecraft 26.2 (Paper or a Paper-based fork) if you haven't
   already.
6. Start your server.

Your gates, config, and permissions all carry over as-is — nothing about the config format,
database schema, or plugin folder name changed in this fork. See
[CHANGES.md](CHANGES.md) for the full technical breakdown of what *did* change internally.
