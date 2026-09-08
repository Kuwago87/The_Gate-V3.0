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

    /*
     * Comment-preserving auto-fix for missing config.yml keys, added for the Bedrock-textures update.
     * A plain pluginConfig.save() would work but strips every comment and reflows the whole file via
     * SnakeYAML's writer - not acceptable for a heavily-annotated config.yml like this one. Instead this
     * only splices in the specific missing keys as new lines at the right place, leaving every existing
     * line (including comments and blank lines) completely untouched.
     *
     * Must be called AFTER hasUpdateConfig() - it reads updatedValuesConfig, which hasUpdateConfig()
     * populates as a side effect. If anything about the file's structure isn't what this expects, it logs
     * a warning and leaves the file alone rather than risking corrupting it - the existing warning log
     * (which lists every missing key/value manually) still covers that fallback case.
     */
    /**
     * Returns true only if every missing key was located and inserted successfully - the caller can then
     * safely skip the "disable until manually fixed" fallback for this run. Returns false (and leaves
     * whatever DID succeed written to disk anyway - partial progress still helps) if anything couldn't be
     * confidently placed, so the existing safety behavior (require a manual fix, matching how this worked
     * before auto-insert existed) still applies for a run where something looked unexpected.
     */
    public boolean autoInsertMissingKeys() {
        if (updatedValuesConfig.isEmpty()) {
            return true;
        }
        try {
            List<String> lines = new ArrayList<String>(java.nio.file.Files.readAllLines(this.pluginConfigFile.toPath(), java.nio.charset.StandardCharsets.UTF_8));

            // Group every missing leaf path by the deepest parent path that already exists in the deployed
            // config - e.g. "GateMaterial.BedrockHeadTextures.HONEY_BLOCK" groups under "GateMaterial" if
            // BedrockHeadTextures itself doesn't exist yet at all, or under "GateMaterial.BedrockHeadTextures"
            // if a future update just adds one more key to an already-existing BedrockHeadTextures section.
            Map<String, List<String>> byParent = new java.util.LinkedHashMap<String, List<String>>();
            for (String fullPath : updatedValuesConfig.keySet()) {
                String parent = this.deepestExistingParent(fullPath);
                byParent.computeIfAbsent(parent, k -> new ArrayList<String>()).add(fullPath);
            }

            // Process bottom-of-file anchors first so earlier insertions don't shift the line numbers
            // out from under anchors we haven't processed yet.
            List<Map.Entry<String, List<String>>> anchors = new ArrayList<Map.Entry<String, List<String>>>(byParent.entrySet());
            anchors.sort((a, b) -> Integer.compare(this.findAnchorLine(lines, b.getKey()), this.findAnchorLine(lines, a.getKey())));

            boolean allResolved = true;
            for (Map.Entry<String, List<String>> entry : anchors) {
                String parentPath = entry.getKey();
                int anchorLine = this.findAnchorLine(lines, parentPath);
                if (anchorLine < 0) {
                    allResolved = false;
                    continue; // couldn't safely locate this anchor - it stays out of the file, but is still listed in the warning log above
                }
                int anchorIndent = parentPath.isEmpty() ? -2 : this.indentOf(lines.get(anchorLine));
                int insertAt = this.findSectionEnd(lines, anchorLine, anchorIndent);
                List<String> block = this.buildYamlBlock(entry.getValue(), parentPath, anchorIndent + 2);
                lines.addAll(insertAt, block);
            }

            java.nio.file.Files.write(this.pluginConfigFile.toPath(), lines, java.nio.charset.StandardCharsets.UTF_8);
            pluginConfig = YamlConfiguration.loadConfiguration(this.pluginConfigFile);
            return allResolved;
        } catch (Exception e) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The Gate] Could not auto-update config.yml - please add the missing options listed above manually. (" + e + ")");
            return false;
        }
    }

    /** Longest dotted-path prefix of fullPath that already exists as a section in the deployed config. Returns "" if not even the first segment exists (meaning: insert as a new top-level section at the end of the file). */
    private String deepestExistingParent(String fullPath) {
        String[] segments = fullPath.split("\\.");
        for (int i = segments.length - 1; i >= 1; i--) {
            String candidate = String.join(".", java.util.Arrays.copyOfRange(segments, 0, i));
            if (pluginConfig.isConfigurationSection(candidate)) {
                return candidate;
            }
        }
        return "";
    }

    private int indentOf(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

    /**
     * Walks the raw file tracking YAML nesting depth by indentation, to find the line that declares the
     * given dotted key path. Returns lines.size() for the root ("") case as a sentinel meaning "end of
     * file", or -1 if a non-root path genuinely can't be found (safety fallback - caller skips it).
     */
    private int findAnchorLine(List<String> lines, String path) {
        if (path.isEmpty()) {
            return lines.size();
        }
        String[] target = path.split("\\.");
        List<String> stack = new ArrayList<String>();
        List<Integer> stackIndent = new ArrayList<Integer>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            int indent = this.indentOf(line);
            while (!stackIndent.isEmpty() && stackIndent.get(stackIndent.size() - 1) >= indent) {
                stack.remove(stack.size() - 1);
                stackIndent.remove(stackIndent.size() - 1);
            }
            int colon = trimmed.indexOf(':');
            if (colon < 0) {
                continue;
            }
            String key = trimmed.substring(0, colon).trim();
            stack.add(key);
            stackIndent.add(indent);
            if (stack.size() == target.length && stack.equals(java.util.Arrays.asList(target))) {
                return i;
            }
        }
        return -1;
    }

    /** First line after anchorLine whose indentation is <= anchorIndent (a sibling key, or the parent's own end) - i.e. the correct insertion point for a block nested one level deeper than the anchor. */
    private int findSectionEnd(List<String> lines, int anchorLine, int anchorIndent) {
        if (anchorLine >= lines.size()) {
            return lines.size();
        }
        for (int i = anchorLine + 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) {
                continue;
            }
            if (this.indentOf(line) <= anchorIndent) {
                return i;
            }
        }
        return lines.size();
    }

    /** Builds the actual new YAML lines for the missing keys under one parent, correctly nested/indented, using Bukkit's own YamlConfiguration writer so quoting matches the rest of the file. */
    private List<String> buildYamlBlock(List<String> fullPaths, String parentPath, int childIndentSpaces) {
        YamlConfiguration temp = new YamlConfiguration();
        for (String fullPath : fullPaths) {
            String relative = parentPath.isEmpty() ? fullPath : fullPath.substring(parentPath.length() + 1);
            temp.set(relative, this.sorcepluginConfig.get(fullPath));
        }
        String raw = temp.saveToString();
        List<String> result = new ArrayList<String>();
        result.add(" ".repeat(childIndentSpaces) + "# --- auto-added by The_Gate - see the wiki for details on these new options ---");
        for (String line : raw.split("\n")) {
            if (line.isBlank()) {
                continue;
            }
            result.add(" ".repeat(childIndentSpaces) + line);
        }
        return result;
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

