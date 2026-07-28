/*
 * Decompiled with CFR 0.152.
 */
package thegate.main;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import thegate.main.Globals;
import thegate.main.TheGateMain;

public class Config {
    public static void LoadConfig(Plugin mainGate) {
        mainGate.getConfig().options().copyDefaults(true);
        mainGate.saveDefaultConfig();
        FileConfiguration config = mainGate.getConfig();
        Material DRM = Material.getMaterial(config.getString("GateMaterial.Default-Ring"));
        Material DCFM = Material.getMaterial(config.getString("GateMaterial.Default-Chevrons-Frame-Material"));
        Material DCBM = Material.getMaterial(config.getString("GateMaterial.Default-Chevron-Bottom-Material"));
        Material DCLM = Material.getMaterial(config.getString("GateMaterial.Default-Chevron-Light-Material-Off"));
        Material DCLMO = Material.getMaterial(config.getString("GateMaterial.Default-Chevron-Light-Material-On"));
        Material DHM = Material.getMaterial(config.getString("GateMaterial.Default-Horizon-Material"));
        Material DDHDM = Material.getMaterial(config.getString("GateMaterial.Default-DHD-Material"));
        Globals.DefaultIrisMaterial = Material.getMaterial(config.getString("GateMaterial.Default-Iris-Material"));
        if (DRM != null) {
            Globals.DefaultringMaterial = DRM;
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Default ring material created an error!");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Check the spelling in the config");
        }
        if (DCFM != null) {
            Globals.Defaultchevrons_frameMaterial = DCFM;
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Default chevron frame material created an error!");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Check the spelling in the config");
        }
        if (DCBM != null) {
            Globals.Defaultchevron_botMaterial = DCBM;
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Default chevron bottom material created an error!");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Check the spelling in the config");
        }
        if (DCLM != null) {
            Globals.Defaultchevron_lightMaterial = DCLM;
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Default chevron light material created an error!");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Check the spelling in the config");
        }
        if (DCLMO != null) {
            Globals.Defaultchevron_lightMaterial_ON = DCLMO;
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Default chevron light on material created an error!");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Check the spelling in the config");
        }
        if (DHM != null) {
            Globals.DefaulthorizonMaterial = DHM;
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Default horizon material created an error!");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Check the spelling in the config");
        }
        if (DDHDM != null) {
            Globals.DefaultDHDMaterial = DDHDM;
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Default DHD material created an error!");
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "<The_Gate> Check the spelling in the config");
        }
        int t = config.getInt("GateSettings.AnimationSettings.ActiveTime");
        Globals.GateTime = t > 45600 ? 45600 : t;
        Globals.CreateBarrier = config.getBoolean("GateSettings.PlaceBarrier");
        Globals.DefaultGateCreationTool = Material.getMaterial(config.getString("ToolCustomization.Default-GateCreationTool"));
        Globals.DefaultGateEditTool = Material.getMaterial(config.getString("ToolCustomization.Default-GateEditTool"));
        Globals.DefaultAbydosCartouche = Material.getMaterial(config.getString("ToolCustomization.Default-AbydosCartouche"));
        Globals.DefaultGateCrystal = Material.getMaterial(config.getString("ToolCustomization.Default-GateCrystal"));
        Globals.DefaultIDCTransmitter = Material.getMaterial(config.getString("ToolCustomization.Default-IDCTrancmitter"));
        Globals.AllowQuickDial = config.getBoolean("GateSettings.AllowQuickDial");
        Globals.PlayerGateAmmount = config.getInt("GateSettings.UserOwnedGates");
        Globals.GateExclusionRadius = config.getInt("GateSettings.GateExclusionRadius");
        Globals.DefaultGateActivateSound = Sound.valueOf(config.getString("GateSoundSettings.Gate-Activate-Sound"));
        Globals.DefaultGateActivateVolume = (float)config.getDouble("GateSoundSettings.Gate-Activate-Volume");
        Globals.DefaultGateActivatePitch = (float)config.getDouble("GateSoundSettings.Gate-Activate-Pitch");
        Globals.DefaultGateAmbientSound = Sound.valueOf(config.getString("GateSoundSettings.Gate-Ambient-Sound"));
        Globals.DefaultGateAmbientVolume = (float)config.getDouble("GateSoundSettings.Gate-Ambient-Volume");
        Globals.DefaultGateAmbientePitch = (float)config.getDouble("GateSoundSettings.Gate-Ambient-Pitch");
        Globals.DefaultChevronLockSound = Sound.valueOf(config.getString("GateSoundSettings.Gate-ChevronLock-Sound"));
        Globals.DefaultChevronLockVolume = (float)config.getDouble("GateSoundSettings.Gate-ChevronLock-Volume");
        Globals.DefaultChevronLockPitch = (float)config.getDouble("GateSoundSettings.Gate-ChevronLock-Pitch");
        Globals.DefaultChevronOpenSound = Sound.valueOf(config.getString("GateSoundSettings.Gate-ChevronOpen-Sound"));
        Globals.DefaultChevronOpenVolume = (float)config.getDouble("GateSoundSettings.Gate-ChevronOpen-Volume");
        Globals.DefaultChevronOpenPitch = (float)config.getDouble("GateSoundSettings.Gate-ChevronOpen-Pitch");
        Globals.DefaultSpinningSound = Sound.valueOf(config.getString("GateSoundSettings.Gate-DialingAmbient-Sound"));
        Globals.DefaultSpinningVolume = (float)config.getDouble("GateSoundSettings.Gate-DialingAmbient-Volume");
        Globals.DefaultSpinningPitch = (float)config.getDouble("GateSoundSettings.Gate-DialingAmbient-Pitch");
        Globals.DefaultGateEnterSound = Sound.valueOf(config.getString("GateSoundSettings.Gate-Enter-Sound"));
        Globals.DefaultGateEnterVolume = (float)config.getDouble("GateSoundSettings.Gate-Enter-Volume");
        Globals.DefaultGateEnterPitch = (float)config.getDouble("GateSoundSettings.Gate-Enter-Pitch");
        Globals.DefaultGateExitSound = Sound.valueOf(config.getString("GateSoundSettings.Gate-Exit-Sound"));
        Globals.DefaultGateExitVolume = (float)config.getDouble("GateSoundSettings.Gate-Exit-Volume");
        Globals.DefaultGateExitPitch = (float)config.getDouble("GateSoundSettings.Gate-Exit-Pitch");
        Globals.DialingSpeed = (float)config.getDouble("GateSettings.AnimationSettings.DialingSpeed");
        List<String> SymbolMaterial = config.getStringList("GateMaterial.GateSymbolMaterial");
        int i = 0;
        while (i < 16) {
            Globals.SymbolMaterial[i] = Material.valueOf(SymbolMaterial.get(i));
            ++i;
        }
        Globals.DisplayName = config.getBoolean("GateSettings.DisplayName");
        Globals.Networks.clear();
        Globals.Networks.add("Global");
        List<String> Networks = ((TheGateMain)mainGate).getConfig().getStringList("GateSettings.Networks");
        List<String> CustomWorldNames = ((TheGateMain)mainGate).getConfig().getStringList("GateSettings.WorldsAsNetworks");
        for (String s : CustomWorldNames) {
            if (mainGate.getServer().getWorld(s.split(":")[1]) == null) continue;
            Globals.WorldNames.put(s.split(":")[1], s.split(":")[0]);
            Globals.WorldDefaultSpawn.put(s.split(":")[1], Boolean.valueOf(s.split(":")[2]));
            Globals.Networks.add(s.split(":")[0]);
        }
        int i2 = 0;
        while (i2 < Networks.size()) {
            Globals.Networks.add(Networks.get(i2));
            ++i2;
        }
        Globals.SaveFromat = config.getString("PluginSettings.Savefile");
        if (Globals.SaveFromat.equals("MYSQL")) {
            Globals.MySQLPath = config.getString("PluginSettings.Path");
            Globals.MySQLUserName = config.getString("PluginSettings.Username");
            Globals.MySQLUserPassword = config.getString("PluginSettings.Password");
            Globals.AutoSyncDatabase = config.getInt("PluginSettings.AutoSyncDatabase");
            Globals.UseBungee = config.getBoolean("BungeeSettings.UseBungee");
            Globals.ServerName = config.getString("BungeeSettings.ServerName");
        }
        Globals.VisibilityRadius = Integer.valueOf(config.getString("GateSettings.GateVisibilityRadius"));
        Globals.DeadlyVortex = config.getBoolean("GateSettings.DeadlyVortex");
        Globals.DeadlyIris = config.getBoolean("GateSettings.DeadlyIris");
        Globals.GateCanBreakBlocks = config.getBoolean("GateSettings.GateCanBreakBlocks");
        List<String> mat = config.getStringList("GateSettings.DistrucitonSettings.ExcludedBlocks");
        HashSet<Material> excludeMat = new HashSet<Material>();
        for (String s : mat) {
            excludeMat.add(Material.valueOf(s));
        }
        Globals.excludeList = excludeMat;
        Globals.SelectionX = config.getInt("GateSettings.DestrucitonSettings.SelectionX");
        Globals.Selection_X = config.getInt("GateSettings.DestrucitonSettings.Selection-X");
        Globals.SelectionY = config.getInt("GateSettings.DestrucitonSettings.SelectionY");
        Globals.Selection_Y = config.getInt("GateSettings.DestrucitonSettings.Selection-Y");
        Globals.Radius = config.getDouble("GateSettings.DestrucitonSettings.Radius");
        Globals.DestructionDistance = config.getDouble("GateSettings.DestrucitonSettings.Distance");
        Globals.DestructionDistanceMult = config.getDouble("GateSettings.DestrucitonSettings.DistanceMult");
        Globals.MoreInfo = config.getBoolean("PluginSettings.ExtendedPlguinInfo");
        Globals.DialingAnimationTicks = config.getInt("GateSettings.AnimationSettings.DialingAnimationTicks");
        Globals.IrisAnimaitonTicks = config.getInt("GateSettings.AnimationSettings.IrisAnimaitonTicks");
        Globals.IrisSpeed = config.getInt("GateSettings.AnimationSettings.IrisSpeed");
        Globals.VortexSpeed = config.getInt("GateSettings.AnimationSettings.VortexSpeed");
        Globals.DoDialing = config.getBoolean("GateSettings.AnimationSettings.DoDialing");
        Globals.DoVortex = config.getBoolean("GateSettings.AnimationSettings.DoVortex");
        Globals.DoHorizonEffect = config.getBoolean("GateSettings.AnimationSettings.DoHorizonEffect");
        Globals.DoIrisAnimaiton = config.getBoolean("GateSettings.AnimationSettings.DoIrisAnimation");
        Globals.UseDummyOwner = config.getBoolean("GateSettings.UseDummyOwner");
        Globals.dummyOwnerName = config.getString("GateSettings.DummyOwnerName");
        Globals.playerTable = config.getString("PluginSettings.TablePlayer");
        Globals.coownerTable = config.getString("PluginSettings.TableCoOwner");
        Globals.gatesTable = config.getString("PluginSettings.TableGates");
        Globals.gatesCommandsTable = config.getString("PluginSettings.GateCommands");
        Globals.AllowDialSuggestions = config.getBoolean("GateSettings.AllowDialSuggestions");
    }
}

