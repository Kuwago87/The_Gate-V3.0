/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package thegate.gate;

import com.packageing.tools.packagetools.PackageManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import thegate.animaitons.AnimationDialing;
import thegate.animaitons.AnimationDialingSingleGate;
import thegate.animaitons.IrisAnimation;
import thegate.animaitons.WooshAnimation;
import thegate.gate.BlockedState;
import thegate.gate.CommandUseType;
import thegate.gate.CommandUser;
import thegate.gate.CreateGate;
import thegate.gate.GateChevron;
import thegate.gate.GateCommandInfo;
import thegate.gate.GateManager;
import thegate.gate.IrisPart;
import thegate.gate.packages.GatePackages;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;
import thegate.math.GateMath;

public class GateObject {
    private boolean updated = false;
    private boolean shutdown = false;
    private boolean dialingout = false;
    private boolean Active = false;
    private boolean AllowTeleport = false;
    private BlockedState Locked = BlockedState.unlocked;
    private boolean Open = true;
    private boolean WormholeOutgoing = false;
    private boolean IrisClosed = false;
    private boolean commandOnDeactivate = false;
    private boolean useGatePerms = false;
    private int acitveTime = Globals.GateTime;
    private String IrisCode = "0000000";
    private boolean dialinginProssed = false;
    private String OwnerUUID = null;
    private String OwnerName = "";
    private Location Gate;
    private Location DHD;
    private int ChunkX = 0;
    private int ChunkZ = 0;
    private String address = "";
    private String Server = "none";
    private String Network = Globals.Networks.get(0);
    private String SecundaryNetwork = "null";
    private String WorldName = "";
    private String Diled = "";
    private Player lastActivatedBy = null;
    private String description = "";
    private String gateName = "";
    private ArrayList<String> DisplayText = new ArrayList();
    private Material ringMaterial = Globals.DefaultringMaterial;
    private Material chevrons_frameMaterial = Globals.Defaultchevrons_frameMaterial;
    private Material chevron_botMaterial = Globals.Defaultchevron_botMaterial;
    private Material chevron_lightMaterial = Globals.Defaultchevron_lightMaterial;
    private Material chevron_lightMaterial_on = Globals.Defaultchevron_lightMaterial_ON;
    private Material horizonMaterial = Globals.DefaulthorizonMaterial;
    private Material irisMaterial = Globals.DefaultIrisMaterial;
    private float facing = 0.0f;
    private Vector facingVector;
    private Map<String, String> coOwner = new HashMap<String, String>();
    private Set<Player> PlayerInRange = new HashSet<Player>();
    private Set<Player> JoinedPlayers = new HashSet<Player>();
    private Map<Location, Block> barrierBlocks = new HashMap<Location, Block>();
    private Map<CommandUseType, List<GateCommandInfo>> gateCommands = new HashMap<CommandUseType, List<GateCommandInfo>>();
    public AnimationDialing dialing = null;
    public AnimationDialingSingleGate dialingSingle = null;
    public WooshAnimation woosh = null;
    public IrisAnimation irisAnimaiton = null;
    private GatePackages packages;

    public GateObject() {
        this.Server = Globals.ServerName;
        this.packages = new GatePackages(this);
    }

    public GateObject(Location Gate, String WorldName, Location DHD, float facing, String address, String OwnerID, String OwnerName) {
        this.Gate = Gate;
        this.ChunkX = Gate.getChunk().getX();
        this.ChunkZ = Gate.getChunk().getZ();
        this.WorldName = WorldName;
        this.DHD = DHD;
        this.address = address;
        this.facing = facing;
        this.OwnerUUID = OwnerID;
        this.OwnerName = OwnerName;
        this.Network = Globals.WorldDefaultSpawn.containsKey(Gate.getWorld().getName()) ? (!Globals.WorldDefaultSpawn.get(Gate.getWorld().getName()).booleanValue() ? Globals.Networks.get(0) : Globals.WorldNames.get(Gate.getWorld().getName())) : Globals.Networks.get(0);
        this.Server = Globals.ServerName;
        this.packages = new GatePackages(this);
    }

    public GateObject(Location Gate, String WorldName, float facing, String Address, String OwnerID, String OwnerName) {
        this.Gate = Gate;
        this.ChunkX = Gate.getChunk().getX();
        this.ChunkZ = Gate.getChunk().getZ();
        this.WorldName = WorldName;
        this.facing = facing;
        this.address = Address;
        this.OwnerUUID = OwnerID;
        this.OwnerName = OwnerName;
        this.Network = Globals.WorldDefaultSpawn.containsKey(Gate.getWorld().getName()) ? (!Globals.WorldDefaultSpawn.get(Gate.getWorld().getName()).booleanValue() ? Globals.Networks.get(0) : Globals.WorldNames.get(Gate.getWorld().getName())) : Globals.Networks.get(0);
        this.Server = Globals.ServerName;
        this.packages = new GatePackages(this);
    }

    public void Repair() {
        this.Deactivate();
        this.Vanish(new Player[0]);
        CreateGate.CreateGateRing(this);
        CreateGate.CreateSymbols(this);
        CreateGate.CreateEventHorizon(this);
        if (Globals.DisplayName) {
            CreateGate.CreateDisplayTextStands(this);
        }
        for (Player p : this.PlayerInRange) {
            this.Display(p);
        }
    }

    public void Refresh() {
        this.Repair();
        this.packages.Refresh();
    }

    private void Display(Player p) {
        if (this.packages.Symbol == null || this.packages.Chevrons == null || this.packages.Ring == null) {
            this.Repair();
        }
        this.packages.Display(p);
    }

    public void StartDialingOutSequenceSingleGate(Plugin p, TheGateMain mainGate, String Address, Player player) {
        this.setDialingout(true);
        this.setDiled(Address);
        GateObject otherGate = GateManager.getGateOnOtherServerWithAddress(Address);
        if (otherGate != null) {
            this.dialingSingle = new AnimationDialingSingleGate(this, p, Address, player);
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The Gate] nullpointer avoided (gateobject == null in dialing sequence)");
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The Gate] player diald from: " + this.address);
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The Gate] address of null gate: " + Address);
        }
    }

    public void StartDialingOutSequence(String Address, Player player) {
        this.setDialingout(true);
        this.setDiled(Address);
        GateObject otherGate = GateManager.getGateWithAddress(Address);
        if (otherGate != null) {
            if (this.packages.Symbol == null || this.packages.Chevrons == null || this.packages.Ring == null) {
                this.Repair();
                this.setDialingout(true);
                this.setDiled(Address);
                this.setDialinginProssed(true);
            }
            if (otherGate.packages.Symbol == null || otherGate.packages.Chevrons == null || otherGate.packages.Ring == null) {
                otherGate.Repair();
                otherGate.setDialingout(false);
                otherGate.setDiled(this.address);
                otherGate.setDialinginProssed(true);
            }
            this.dialing = new AnimationDialing(this, otherGate, (Plugin)TheGateMain.theGateMain, Address, player);
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The Gate] nullpointer avoided (gateobject == null in dialing sequence)");
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The Gate] player diald from: " + this.address);
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The Gate] address of null gate: " + Address);
        }
    }

    public void Vanish(Player ... p) {
        if (p.length == 0 || p == null) {
            this.packages.getEntityIDs().stream().forEach(x -> {
                for (Player player : this.PlayerInRange) {
                    PackageManager.SendDespawnPackage(x, player);
                }
            });
            return;
        }
        this.packages.getEntityIDs().stream().forEach(x -> {
            Player[] playerArray2 = p;
            int n = p.length;
            int n2 = 0;
            while (n2 < n) {
                Player player = playerArray2[n2];
                PackageManager.SendDespawnPackage(x, player);
                ++n2;
            }
        });
    }

    public void KillPlayer(Player p) {
        this.removePlayerInRange(p);
        p.setHealth(0.0);
    }

    public boolean isActive() {
        return this.Active;
    }

    public void Activate(String Address, boolean AllowTeleport, Player player) {
        this.lastActivatedBy = player;
        if (this.packages.Symbol == null || this.packages.Chevrons == null || this.packages.Ring == null) {
            this.Repair();
        }
        if (!this.Active && this.woosh == null) {
            this.setActive(false);
            this.setAllowTeleport(AllowTeleport);
            this.setDiled(Address);
            this.setDialinginProssed(true);
            this.setDialingout(AllowTeleport);
            this.woosh = new WooshAnimation(this, (Plugin)TheGateMain.theGateMain);
            if (!this.packages.Chevrons.get((int)0).On) {
                for (GateChevron c2 : this.packages.Chevrons) {
                    c2.On = true;
                    c2.UpdateLight();
                }
            }
            if (this.dialingout) {
                TheGateMain.theGateMain.getServer().getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.dispach(CommandUseType.OnActivate, (CommandSender)this.lastActivatedBy));
            }
            this.commandOnDeactivate = true;
        }
    }

    public void Deactivate() {
        if (this.commandOnDeactivate && this.dialingout) {
            TheGateMain.theGateMain.getServer().getScheduler().runTask((Plugin)TheGateMain.theGateMain, () -> this.dispach(CommandUseType.OnDeactivate, (CommandSender)this.lastActivatedBy));
        }
        this.commandOnDeactivate = false;
        this.lastActivatedBy = null;
        this.setActive(false);
        this.setDiled("");
        this.setDialingout(false);
        this.setDialinginProssed(false);
        this.setDialingout(false);
        this.JoinedPlayers.clear();
        if (this.woosh != null) {
            this.woosh.cancel();
            this.woosh.Remove();
            this.woosh = null;
        }
        if (this.dialing != null) {
            this.dialing.Stop();
            this.dialing = null;
        }
        if (this.dialingSingle != null) {
            this.dialingSingle.Stop();
            this.dialingSingle = null;
        }
    }

    public void dialGate(GateObject other, Player player) {
        this.StartDialingOutSequence(other.getAddress(), player);
        this.setDiled(other.getAddress());
        other.setDiled(this.getAddress());
        this.setDialinginProssed(true);
        other.setDialinginProssed(true);
    }

    public void addPlayerInRange(Player p) {
        if (!this.PlayerInRange.contains(p)) {
            this.PlayerInRange.add(p);
            this.Display(p);
        }
    }

    public void removePlayerInRange(Player p) {
        if (this.PlayerInRange.contains(p)) {
            this.PlayerInRange.remove(p);
            this.Vanish(p);
        }
    }

    public void removePlayerInRangeDisconnect(Player p) {
        if (this.PlayerInRange.contains(p)) {
            this.PlayerInRange.remove(p);
            this.Vanish(p);
        }
    }

    public String getInsertString() {
        String querry = "INSERT INTO " + Globals.gatesTable + "(address, world, server_name, locx, locy, locz, chunkx, chunkz, has_dhd, dhdx, dhdy, dhdz, facing, dialingdisabled, open, ring, chevron_bot_material, chevron_light_material_off, chevron_light_material_on, chevron_frame_material, horizon_material, uuid, player_name, gate_name, description, primary_network, secundary_network, iriscode, irismaterial)" + " VALUES('" + this.getAddress() + "'" + ",'" + this.getWorldName() + "'" + ",'" + (this.getServer() == "" ? "none" : this.getServer()) + "'" + "," + this.getGate().getX() + "," + this.getGate().getY() + "," + this.getGate().getZ() + "," + this.getChunkX() + "," + this.getChunkZ() + "," + this.hasDHD() + "," + (this.hasDHD() ? this.getDHD().getX() : 0.0) + "," + (this.hasDHD() ? this.getDHD().getY() : 0.0) + "," + (this.hasDHD() ? this.getDHD().getZ() : 0.0) + "," + this.getFacing() + "," + this.getBlockedState().ordinal() + "," + this.isOpen() + ",'" + this.getRingMaterial() + "'" + ",'" + this.getChevron_botMaterial() + "'" + ",'" + this.getChevron_lightMaterial() + "'" + ",'" + this.getChevron_lightMaterial_on() + "'" + ",'" + this.getChevrons_frameMaterial() + "'" + ",'" + this.getHorizonMaterial() + "'" + ",'" + this.getOwnerUUID() + "'" + ",'" + this.getOwnerName().toString() + "'" + ",'" + this.getGateName() + "'" + ",'" + this.getDescription() + "'" + ",'" + this.getNetwork() + "'" + ",'" + this.getSecondaryNetwork() + "'" + ",'" + this.getIrisCode() + "'" + ",'" + this.getIrisMaterial() + "'" + ");";
        return querry;
    }

    public String getUpdateString() {
        return "UPDATE " + Globals.gatesTable + " SET world='" + this.getWorldName() + "'" + ", server_name='" + (this.getServer() == "" ? "'none'" : this.getServer()) + "'" + ", locx=" + this.getGate().getX() + ", locy=" + this.getGate().getY() + ", locz=" + this.getGate().getZ() + ", chunkx=" + this.getChunkX() + ", chunkz=" + this.getChunkZ() + ", has_dhd=" + this.hasDHD() + ", dhdx=" + (this.hasDHD() ? this.getDHD().getX() : 0.0) + ", dhdy=" + (this.hasDHD() ? this.getDHD().getY() : 0.0) + ", dhdz=" + (this.hasDHD() ? this.getDHD().getZ() : 0.0) + ", facing=" + this.getFacing() + ", dialingdisabled=" + this.getBlockedState().ordinal() + ", open=" + this.isOpen() + ", ring='" + this.getRingMaterial() + "'" + ", chevron_bot_material='" + this.getChevron_botMaterial() + "'" + ", chevron_light_material_off='" + this.getChevron_lightMaterial() + "'" + ", chevron_light_material_on='" + this.getChevron_lightMaterial_on() + "'" + ", chevron_frame_material='" + this.getChevrons_frameMaterial() + "'" + ", horizon_material='" + this.getHorizonMaterial() + "'" + ", uuid='" + this.getOwnerUUID() + "'" + ", player_name='" + this.getOwnerName().toString() + "'" + ", gate_name='" + this.getGateName() + "'" + ", description='" + this.getDescription() + "'" + ", primary_network='" + this.getNetwork() + "'" + ", secundary_network='" + this.getSecondaryNetwork() + "'" + ", iriscode='" + this.getIrisCode() + "'" + ", irismaterial='" + this.getIrisMaterial() + "'" + ", usegateperm=" + this.isUseGatePerms() + " WHERE address='" + this.getAddress() + "';";
    }

    public void addCoOwner(String uuid, String name) {
        this.coOwner.put(uuid, name);
    }

    public boolean addCommand(CommandUseType type, String command, CommandUser user) {
        List<GateCommandInfo> list = this.gateCommands.get((Object)type);
        if (list == null) {
            list = new ArrayList<GateCommandInfo>();
        }
        list.add(new GateCommandInfo(command, user, this));
        this.gateCommands.put(type, list);
        return true;
    }

    public GateCommandInfo removeCommand(CommandUseType type, int index) {
        List<GateCommandInfo> list = this.gateCommands.get((Object)type);
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (index > list.size() - 1 || index < 0) {
            return null;
        }
        GateCommandInfo obj = list.get(index);
        if (obj == null) {
            return null;
        }
        list.remove(obj);
        this.gateCommands.put(type, list);
        return obj;
    }

    public void dispach(CommandUseType type, CommandSender sender) {
        List<GateCommandInfo> list = this.gateCommands.get((Object)type);
        if (list == null) {
            return;
        }
        list.forEach(x -> x.dispach(sender));
    }

    public boolean canDial(GateObject other, Player whoDialed) {
        if (other.isUseGatePerms() && !whoDialed.hasPermission(String.valueOf(Perms.thegate_user_dial_.value()) + other.getAddress()) && !whoDialed.hasPermission(Perms.thegate_admin_dialgate.value())) {
            whoDialed.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
            return false;
        }
        if (this.isUseGatePerms() && !whoDialed.hasPermission(String.valueOf(Perms.thegate_user_dial_.value()) + this.getAddress()) && !whoDialed.hasPermission(Perms.thegate_admin_dialgate.value())) {
            whoDialed.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
            return false;
        }
        if (!(other.isOpen() || other.hasOwningRelation(whoDialed) || whoDialed.hasPermission(Perms.thegate_admin_dialgate.value()))) {
            return false;
        }
        return this.hasSameNetwork(other) && this.checkActivity(other) && this.checkBlockedState(other);
    }

    public boolean hasSameNetwork(GateObject other) {
        return this.getNetwork().equals(other.getNetwork()) || this.getNetwork().equals(other.getSecondaryNetwork()) || this.getSecondaryNetwork().equals(other.getNetwork());
    }

    public boolean checkBlockedState(GateObject other) {
        return !other.getBlockedState().equals((Object)BlockedState.block_incoming) && !other.getBlockedState().equals((Object)BlockedState.locked) && !this.getBlockedState().equals((Object)BlockedState.block_outgoing) && !this.getBlockedState().equals((Object)BlockedState.locked);
    }

    public boolean checkActivity(GateObject other) {
        return !other.isActive() && !this.isActive() && !other.isDialinginProssed() && !this.isDialinginProssed();
    }

    public boolean isOwner(Player p) {
        return p.getUniqueId().toString().equals(this.OwnerUUID);
    }

    public boolean isCoowner(Player p) {
        return this.getCoOwner().get(p.getUniqueId().toString()) != null;
    }

    public boolean hasOwningRelation(Player p) {
        return this.isOwner(p) || this.isCoowner(p);
    }

    public void setActive(boolean acitve) {
        this.Active = acitve;
    }

    public Location getGate() {
        return this.Gate;
    }

    public void setGate(Location gate) {
        this.Gate = gate;
    }

    public Location getDHD() {
        return this.DHD;
    }

    public void setDHD(Location dHD) {
        this.DHD = dHD;
    }

    public boolean hasDHD() {
        return this.DHD != null;
    }

    public String toString() {
        return "{Gate: " + this.address + "; Name: [" + this.gateName + "]; OwnerID: " + this.OwnerUUID + " OwnerName: " + this.OwnerName + "; Network: " + this.getNetwork() + "; SecondaryNetwork: " + this.getSecondaryNetwork() + "}";
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDiled() {
        return this.Diled;
    }

    public void setDiled(String diled) {
        this.Diled = diled;
    }

    public float getFacing() {
        return this.facing;
    }

    public void setFacing(float facing) {
        this.facing = facing;
    }

    public boolean isAllowTeleport() {
        return this.AllowTeleport;
    }

    public void setAllowTeleport(boolean allowTeleport) {
        this.AllowTeleport = allowTeleport;
    }

    public BlockedState getBlockedState() {
        return this.Locked;
    }

    public void setBlockedState(BlockedState state) {
        this.Locked = state;
    }

    public Material getRingMaterial() {
        return this.ringMaterial;
    }

    public void setRingMaterial(Material ringMaterial) {
        this.ringMaterial = ringMaterial;
    }

    public Material getChevrons_frameMaterial() {
        return this.chevrons_frameMaterial;
    }

    public void setChevrons_frameMaterial(Material chevrons_frameMaterial) {
        this.chevrons_frameMaterial = chevrons_frameMaterial;
    }

    public Material getChevron_botMaterial() {
        return this.chevron_botMaterial;
    }

    public void setChevron_botMaterial(Material chevron_botMaterial) {
        this.chevron_botMaterial = chevron_botMaterial;
    }

    public Material getChevron_lightMaterial() {
        return this.chevron_lightMaterial;
    }

    public void setChevron_lightMaterial(Material chevron_lightMaterial) {
        this.chevron_lightMaterial = chevron_lightMaterial;
    }

    public Material getHorizonMaterial() {
        return this.horizonMaterial;
    }

    public void setHorizonMaterial(Material horizonMaterial) {
        this.horizonMaterial = horizonMaterial;
    }

    public Material getChevron_lightMaterial_on() {
        return this.chevron_lightMaterial_on;
    }

    public void setChevron_lightMaterial_on(Material chevron_lightMaterial_on) {
        this.chevron_lightMaterial_on = chevron_lightMaterial_on;
    }

    public boolean isShutdown() {
        return this.shutdown;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public int getAcitveTime() {
        return this.acitveTime;
    }

    public void setAcitveTime(int acitveTime) {
        this.acitveTime = acitveTime;
    }

    public boolean isWormholeOutgoing() {
        return this.WormholeOutgoing;
    }

    public void setWormholeOutgoing(boolean wormholeOutgoing) {
        this.WormholeOutgoing = wormholeOutgoing;
    }

    public String getOwnerUUID() {
        return this.OwnerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.OwnerUUID = ownerUUID;
    }

    public String getOwnerName() {
        return this.OwnerName;
    }

    public void setOwnerName(String ownerName) {
        this.OwnerName = ownerName;
    }

    public String getNetwork() {
        return this.Network;
    }

    public void setNetwork(String network) {
        this.Network = network;
    }

    public boolean isOpen() {
        return this.Open;
    }

    public void setOpen(boolean open) {
        this.Open = open;
    }

    public String getSecondaryNetwork() {
        return this.SecundaryNetwork;
    }

    public void setSecundaryNetwork(String secundaryNetwork) {
        this.SecundaryNetwork = secundaryNetwork;
    }

    public int getChunkX() {
        return this.ChunkX;
    }

    public void setChunkX(int chunkX) {
        this.ChunkX = chunkX;
    }

    public int getChunkZ() {
        return this.ChunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.ChunkZ = chunkZ;
    }

    public boolean isDialinginProssed() {
        return this.dialinginProssed;
    }

    public void setDialinginProssed(boolean dialinginProssed) {
        this.dialinginProssed = dialinginProssed;
    }

    public boolean isDialingout() {
        return this.dialingout;
    }

    public void setDialingout(boolean dialingout) {
        this.dialingout = dialingout;
    }

    public String getServer() {
        return this.Server;
    }

    public void setServer(String server) {
        this.Server = server;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGateName() {
        return this.gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public Map<String, String> getCoOwner() {
        return this.coOwner;
    }

    public void setCoOwner(Map<String, String> coOwner) {
        this.coOwner = coOwner;
    }

    public void removeCoOwner(String uuid) {
        this.coOwner.remove(uuid);
    }

    public Set<Player> getPlayerInRange() {
        return this.PlayerInRange;
    }

    public Vector getFacingVector() {
        if (this.facingVector == null) {
            this.facingVector = GateMath.getFacingVector(new Vector(0, 0, 1), Math.toRadians(this.facing * 90.0f)).normalize();
        }
        return this.facingVector;
    }

    public void setFacingVector(Vector facingVector) {
        this.facingVector = facingVector;
    }

    public String getWorldName() {
        return this.WorldName;
    }

    public void setWorldName(String worldName) {
        this.WorldName = worldName;
    }

    public ArrayList<String> getDisplayText() {
        return this.DisplayText;
    }

    public void setDisplayText(ArrayList<String> displayText) {
        this.DisplayText = displayText;
    }

    public Set<Player> getJoinedPlayers() {
        return this.JoinedPlayers;
    }

    public void addJoinedPlayer(Player p) {
        this.JoinedPlayers.add(p);
    }

    public GatePackages getPackages() {
        return this.packages;
    }

    public void setPackages(GatePackages packages) {
        this.packages = packages;
    }

    public Material getIrisMaterial() {
        Iterator<IrisPart> iterator = this.packages.getIris().iterator();
        if (iterator.hasNext()) {
            IrisPart p = iterator.next();
            return p.getIrisMaterial();
        }
        return this.irisMaterial;
    }

    public void setIrisMaterial(Material irisMaterial) {
        this.irisMaterial = irisMaterial;
        for (IrisPart p : this.packages.getIris()) {
            p.setIrisMaterial(irisMaterial);
        }
    }

    public boolean isIrisClosed() {
        return this.IrisClosed;
    }

    public void setIrisClosed(boolean irisClosed) {
        this.IrisClosed = irisClosed;
    }

    public IrisAnimation getIrisAnimaiton() {
        return this.irisAnimaiton;
    }

    public void setIrisAnimaiton(IrisAnimation irisAnimaiton) {
        this.irisAnimaiton = irisAnimaiton;
    }

    public String getIrisCode() {
        return this.IrisCode;
    }

    public void setIrisCode(String irisCode) {
        this.IrisCode = irisCode;
    }

    public boolean isUpdated() {
        return this.updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public Map<Location, Block> getBarrierBlocks() {
        return this.barrierBlocks;
    }

    public void setBarrierBlocks(Map<Location, Block> barrierBlocks) {
        this.barrierBlocks = barrierBlocks;
    }

    public Block getBarrierBlock(Location l) {
        return this.barrierBlocks.get(l);
    }

    public Map<CommandUseType, List<GateCommandInfo>> getGateCommands() {
        return this.gateCommands;
    }

    public void setGateCommands(Map<CommandUseType, List<GateCommandInfo>> gateCommands) {
        this.gateCommands = gateCommands;
    }

    public Player getLastActivatedBy() {
        return this.lastActivatedBy;
    }

    public void setLastActivatedBy(Player lastActivatedBy) {
        this.lastActivatedBy = lastActivatedBy;
    }

    public boolean isUseGatePerms() {
        return this.useGatePerms;
    }

    public void setUseGatePerms(boolean useGatePerms) {
        this.useGatePerms = useGatePerms;
    }
}

