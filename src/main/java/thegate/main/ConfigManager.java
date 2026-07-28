/*
 * Decompiled with CFR 0.152.
 */
package thegate.main;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import thegate.main.TheGateMain;

public class ConfigManager {
    private Plugin mainGate;
    private static FileConfiguration TextConfig;
    public static FileConfiguration pluginConfig;
    private FileConfiguration sorceTextConfig;
    private FileConfiguration sorcepluginConfig;
    public File textconfig;
    public File pluginConfigFile;
    public static Map<String, String> updatedValuesConfig;
    public static Map<String, String> updatedValuesLang;
    private boolean newConfig = false;
    private boolean newLang = false;

    static {
        updatedValuesConfig = new HashMap<String, String>();
        updatedValuesLang = new HashMap<String, String>();
    }

    public ConfigManager(Plugin mainGate) {
        this.mainGate = mainGate;
    }

    public void CreateConfigFiles() {
        if (!this.mainGate.getDataFolder().exists()) {
            this.mainGate.getDataFolder().mkdir();
        }
        InputStreamReader r = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("config.yml"));
        this.sorcepluginConfig = YamlConfiguration.loadConfiguration(r);
        InputStreamReader r2 = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("lang.yml"));
        this.sorceTextConfig = YamlConfiguration.loadConfiguration(r2);
        this.textconfig = new File(this.mainGate.getDataFolder(), "lang.yml");
        this.pluginConfigFile = new File(this.mainGate.getDataFolder(), "config.yml");
        if (!this.textconfig.exists()) {
            try {
                this.mainGate.saveResource("lang.yml", false);
                this.textconfig = new File(this.mainGate.getDataFolder(), "lang.yml");
                this.newLang = true;
            }
            catch (Exception e) {
                TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.toString());
            }
        }
        if (!this.pluginConfigFile.exists()) {
            try {
                this.mainGate.saveResource("config.yml", false);
                this.pluginConfigFile = new File(this.mainGate.getDataFolder(), "config.yml");
                this.newConfig = true;
            }
            catch (Exception e) {
                TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.toString());
            }
        }
        TextConfig = YamlConfiguration.loadConfiguration(this.textconfig);
        pluginConfig = YamlConfiguration.loadConfiguration(this.pluginConfigFile);
    }

    public boolean hasUpdateConfig() {
        if (this.newConfig) {
            return false;
        }
        Set<String> s0 = this.sorcepluginConfig.getKeys(true);
        Set<String> s1 = pluginConfig.getKeys(true);
        s1.forEach(x -> {
            boolean bl = s0.remove(x);
        });
        s0.stream().filter(x -> !this.sorcepluginConfig.getString((String)x).contains("MemorySection")).forEach(x -> {
            String string = updatedValuesConfig.put((String)x, this.sorcepluginConfig.getString((String)x));
        });
        return !s0.isEmpty();
    }

    public boolean hasUpdateLang() {
        if (this.newLang) {
            return false;
        }
        Set<String> s0 = this.sorceTextConfig.getKeys(true);
        Set<String> s1 = TextConfig.getKeys(true);
        s1.forEach(x -> {
            boolean bl = s0.remove(x);
        });
        s0.stream().filter(x -> !this.sorceTextConfig.getString((String)x).contains("MemorySection")).forEach(x -> {
            String string = updatedValuesLang.put((String)x, this.sorceTextConfig.getString((String)x));
        });
        return !s0.isEmpty();
    }

    public static String getString(String Path2, String ... replace) {
        if (replace.length % 2 != 0) {
            throw new IllegalArgumentException("Not enough arguments!");
        }
        String out = TextConfig.getString(Path2);
        if (out == null || out == "") {
            return "[null]";
        }
        out = out.replace("&", "\u00a7");
        int i = 0;
        while (i < replace.length) {
            out = out.replace(replace[i], replace[i + 1]);
            i += 2;
        }
        return out;
    }

    public static List<String> getStringList(String Path2, String ... replace) {
        ArrayList<String> l = new ArrayList<String>();
        l.add("[Null]");
        return ConfigManager.getStringList(Path2, l, replace);
    }

    public static List<String> getStringList(String Path2, List<String> returnValue, String ... replace) {
        if (replace.length % 2 != 0) {
            throw new IllegalArgumentException("Not enough arguments!");
        }
        List<String> out = TextConfig.getStringList(Path2);
        if (out == null || out.isEmpty()) {
            return returnValue;
        }
        return out.stream().map(x -> {
            String b = x.replace("&", "\u00a7");
            int i = 0;
            while (i < replace.length) {
                b = b.replace(replace[i], replace[i + 1]);
                i += 2;
            }
            return b;
        }).collect(Collectors.toList());
    }

    public static FileConfiguration getConfigFile() {
        return pluginConfig;
    }
}

