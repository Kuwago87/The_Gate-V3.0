/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package thegate.main.save;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import thegate.bungee.PlayerServerData;
import thegate.gate.BlockedState;
import thegate.gate.CommandUseType;
import thegate.gate.CommandUser;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.TheGateMain;
import thegate.main.save.DatabaseManager;

public class DBGateAccess {
    private DatabaseManager dbAccess;

    public DBGateAccess(DatabaseManager dbAccess) {
        this.dbAccess = dbAccess;
        dbAccess.startAsyncWorker();
        this.createNewDatabase();
        if (Globals.UseBungee && Globals.SaveFromat.equalsIgnoreCase("mysql")) {
            this.createNewPlayerTable();
        }
        this.checkDatabase();
    }

    public void stopAsyncWorker() {
        this.dbAccess.stopAsyncWorker();
    }

    public void closeConnection() {
        this.dbAccess.closeConnection();
    }

    public void Load(TheGateMain mainGate, Player player) {
        new Thread(() -> {
            boolean noWorld = false;
            ResultSet rs = null;
            try {
                rs = this.dbAccess.getData("SELECT * FROM " + Globals.gatesTable + ";");
            }
            catch (SQLException e) {
                mainGate.getLogger().log(Level.WARNING, "Unable to load data!");
                return;
            }
            if (rs == null) {
                mainGate.getLogger().log(Level.WARNING, "Data was null!");
                return;
            }
            try {
                ArrayList<GateObject> gates = new ArrayList<GateObject>();
                while (rs.next()) {
                    GateObject obj = this.getGateObjectFromResultSet(rs);
                    if (obj == null) continue;
                    ResultSet commands = this.loadCommandsForGate(obj.getAddress());
                    while (commands.next()) {
                        obj.addCommand(CommandUseType.values()[commands.getInt(4)], commands.getString(3), CommandUser.values()[commands.getInt(5)]);
                    }
                    if (obj.getGate().getWorld() == null) {
                        noWorld = true;
                    }
                    gates.add(obj);
                }
                GateManager.addGate(gates);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            if (noWorld) {
                TheGateMain.theGateMain.getLogger().log(Level.WARNING, "[The_Gate] Warning: World not found, if you are using any world managment Plugins please contect the auther of the plugin (The Gate) so the managment plugin can be added to the list of dependencies");
            }
        }).start();
    }

    private GateObject getGateObjectFromResultSet(ResultSet rs) {
        try {
            GateObject obj = new GateObject();
            obj.setAddress(rs.getString("address"));
            double x = rs.getDouble("locx");
            double y = rs.getDouble("locy");
            double z = rs.getDouble("locz");
            World w = TheGateMain.theGateMain.getServer().getWorld(rs.getString("world"));
            obj.setWorldName(rs.getString("world"));
            obj.setGate(new Location(w, x, y, z));
            obj.setChunkX(rs.getInt("chunkx"));
            obj.setChunkZ(rs.getInt("chunkz"));
            if (rs.getBoolean("has_dhd")) {
                double dhdx = rs.getDouble("dhdx");
                double dhdy = rs.getDouble("dhdy");
                double dhdz = rs.getDouble("dhdz");
                obj.setDHD(new Location(w, dhdx, dhdy, dhdz));
            }
            obj.setFacing(rs.getFloat("facing"));
            obj.setBlockedState(BlockedState.values()[rs.getInt("dialingdisabled")]);
            obj.setOpen(rs.getBoolean("open"));
            obj.setRingMaterial(Material.valueOf((String)rs.getString("ring")));
            obj.setChevron_botMaterial(Material.valueOf((String)rs.getString("chevron_bot_material")));
            obj.setChevron_lightMaterial(Material.valueOf((String)rs.getString("chevron_light_material_off")));
            obj.setChevron_lightMaterial_on(Material.valueOf((String)rs.getString("chevron_light_material_on")));
            obj.setChevrons_frameMaterial(Material.valueOf((String)rs.getString("chevron_frame_material")));
            obj.setHorizonMaterial(Material.valueOf((String)rs.getString("horizon_material")));
            obj.setOwnerUUID(rs.getString("uuid"));
            obj.setOwnerName(rs.getString("player_name"));
            obj.setNetwork(rs.getString("primary_network"));
            obj.setSecundaryNetwork(rs.getString("secundary_network"));
            obj.setGateName(rs.getString("gate_name"));
            obj.setDescription(rs.getString("description"));
            String serverName = rs.getString("server_name");
            if (!Globals.UseBungee) {
                serverName = Globals.ServerName;
            }
            obj.setServer(serverName);
            obj.setIrisCode(rs.getString("iriscode"));
            obj.setIrisMaterial(Material.valueOf((String)rs.getString("irismaterial")));
            obj.setUseGatePerms(rs.getBoolean("usegateperm"));
            obj.setUpdated(false);
            return obj;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<GateObject> GateListOtherServers() {
        String SqlStatement = "SELECT * FROM " + Globals.gatesTable + " WHERE NOT server_name='" + Globals.ServerName + "'";
        try {
            HashSet<GateObject> gates = new HashSet<GateObject>();
            ResultSet rs = this.dbAccess.getData(SqlStatement);
            while (rs.next()) {
                gates.add(this.getGateObjectFromResultSet(rs));
            }
            return gates;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SaveSingleGate(GateObject obj) {
        this.dbAccess.asyncSqlStatement(obj.getInsertString());
    }

    public void updateGate(GateObject obj) {
        this.dbAccess.asyncSqlStatement(obj.getUpdateString());
    }

    public void saveGateCommand(String address, String command, CommandUseType type, CommandUser user) {
        String querry = "INSERT INTO " + Globals.gatesCommandsTable + "(address, command, usetype, user) VALUES('" + address + "','" + command + "','" + type.ordinal() + "','" + user.ordinal() + "');";
        this.dbAccess.asyncSqlStatement(querry);
    }

    public void removeGateCommand(String address, String command, CommandUseType type) {
        String querry = "DELETE FROM " + Globals.gatesCommandsTable + " WHERE address='" + address + "' AND command='" + command + "' AND usetype='" + type.ordinal() + "';";
        this.dbAccess.asyncSqlStatement(querry);
    }

    public ResultSet loadCommandsForGate(String address) {
        try {
            return this.dbAccess.getData("SELECT * FROM " + Globals.gatesCommandsTable + " WHERE address='" + address + "';");
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SaveDataFromSet(Set<GateObject> gates) {
        ArrayList<String> querrys = new ArrayList<String>();
        for (GateObject o : gates) {
            if (!o.isUpdated()) continue;
            querrys.add(o.getUpdateString());
        }
        if (!querrys.isEmpty()) {
            this.dbAccess.asyncSqlStatements(querrys);
        }
    }

    public void DeleateElementFromDatabase(GateObject obj) {
        String querry = "DELETE FROM " + Globals.gatesTable + " WHERE address = '" + obj.getAddress() + "'";
        this.dbAccess.asyncSqlStatement(querry);
    }

    public void clearDatabase() {
        String querry = "DELETE FROM " + Globals.gatesTable + ";";
        String querry2 = "DELETE FROM " + Globals.coownerTable + ";";
        this.dbAccess.asyncSqlStatements(Lists.newArrayList(new String[]{querry, querry2}));
    }

    public void DeleatePlayerFromCoowner(String uuid, String address) {
        String querry = "DELETE FROM " + Globals.coownerTable + " WHERE (uuid = '" + uuid + "' AND address = '" + address + "');";
        this.dbAccess.asyncSqlStatement(querry);
    }

    public void AddPlayerToCoowner(String uuid, String name, String address) {
        String querry = "INSERT INTO " + Globals.coownerTable + "(address, player_name, uuid)" + " VALUES('" + address + "'" + ",'" + name + "'" + ",'" + uuid + "'" + "); ";
        this.dbAccess.asyncSqlStatement(querry);
    }

    public void LoadCoowners(TheGateMain mg) {
        try {
            try (ResultSet rs = this.dbAccess.getData("SELECT * FROM " + Globals.coownerTable + ";")) {
                while (rs.next()) {
                    GateObject obj = GateManager.getGateWithAddress(rs.getString("address"));
                    if (obj == null) continue;
                    String p = rs.getString("uuid");
                    String name = rs.getString("player_name");
                    if (p == null) continue;
                    obj.addCoOwner(p, name);
                }
            }
        }
        catch (SQLException e) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.getMessage());
        }
    }

    public void DeleatePlayerFromTablePlayers(Player player) {
        String querry = "DELETE FROM " + Globals.playerTable + " WHERE uuid = '" + player.getUniqueId() + "'";
        this.dbAccess.asyncSqlStatement(querry);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean hasPlayerInPlayersTable(Player player) {
        try {
            try (ResultSet rs = this.dbAccess.getData("SELECT player_name FROM " + Globals.playerTable + " WHERE uuid = '" + player.getUniqueId() + "'")) {
                if (!rs.next()) return false;
                return true;
            }
        }
        catch (SQLException e) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean hasGateWithAddressInTableGates(String Address) {
        try {
            try (ResultSet rs = this.dbAccess.getData("SELECT address FROM " + Globals.gatesTable + " WHERE address= '" + Address + "';")) {
                if (!rs.next()) return false;
                return true;
            }
        }
        catch (SQLException e) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public void ClearPlayerTable() {
        String querry = "DELETE FROM " + Globals.playerTable + " WHERE server_name='" + Globals.ServerName + "';";
        this.dbAccess.asyncSqlStatement(querry);
    }

    public boolean AddPlayerToTablePlayers(Player player, String address, String address_from) {
        try {
            double y = player.getLocation().getY() + 1.0;
            int i = this.dbAccess.executeUpdate("INSERT INTO " + Globals.playerTable + "(address, address_from, player_name, uuid, player_x, player_y, player_z, player_yaw, player_pitch, server_name) VALUES ('" + address + "', '" + address_from + "' , '" + player.getName() + "', '" + player.getUniqueId() + "', '" + player.getLocation().getX() + "', '" + y + "', '" + player.getLocation().getZ() + "', '" + player.getLocation().getYaw() + "', '" + player.getLocation().getPitch() + "', '" + Globals.ServerName + "');");
            return i > 0;
        }
        catch (SQLException e) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public PlayerServerData getPlayerDataFromPlayerTable(Player p) {
        try {
            try (ResultSet rs = this.dbAccess.getData("SELECT * FROM " + Globals.playerTable + " WHERE player_name='" + p.getName() + "';")) {
                PlayerServerData psd = new PlayerServerData();
                while (rs.next()) {
                    psd.setPlayerX(rs.getDouble("player_x"));
                    psd.setPlayerY(rs.getDouble("player_y"));
                    psd.setPlayerZ(rs.getDouble("player_z"));
                    psd.setPlayerYaw(rs.getDouble("player_yaw"));
                    psd.setPlayerPitch(rs.getDouble("player_pitch"));
                    psd.setAddressTo(rs.getString("address"));
                    psd.setAddressFrom(rs.getString("address_from"));
                }
                return psd;
            }
        }
        catch (SQLException e) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.getMessage());
            return null;
        }
    }

    protected void createNewDatabase() {
        String idMYSQL = "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY";
        String idSQLite = "id INTEGER PRIMARY KEY AUTOINCREMENT";
        String sql = "CREATE TABLE IF NOT EXISTS " + Globals.gatesTable + " (" + " address VARCHAR(7) PRIMARY KEY," + " world VARCHAR(30)," + " server_name VARCHAR(30)," + " locx DOUBLE," + " locy DOUBLE," + " locz DOUBLE," + " chunkx INT," + " chunkz INT," + " has_dhd BOOLEAN," + " dhdx DOUBLE," + " dhdy DOUBLE," + " dhdz DOUBLE," + " facing FLOAT," + " dialingdisabled TINYINT," + " open BOOLEAN," + " ring VARCHAR(35)," + " chevron_bot_material VARCHAR(35)," + " chevron_light_material_off VARCHAR(35)," + " chevron_light_material_on VARCHAR(35)," + " chevron_frame_material VARCHAR(35)," + " horizon_material VARCHAR(35)," + " uuid VARCHAR(36)," + " player_name VARCHAR(20)," + " gate_name VARCHAR(20)," + " description VARCHAR(64)," + " primary_network VARCHAR(30)," + " secundary_network VARCHAR(30)," + " iriscode VARCHAR(7)," + " irismaterial VARCHAR(35)," + " irisautoclose BOOLEAN," + " usegateperm BOOLEAN); ";
        String querry2 = "CREATE TABLE IF NOT EXISTS " + Globals.coownerTable + " (" + " {ID}," + " address VARCHAR(7)," + " player_name VARCHAR(20)," + " uuid VARCHAR(36)); ";
        String querry3 = "CREATE TABLE IF NOT EXISTS " + Globals.gatesCommandsTable + " (" + " {ID}," + " address VARCHAR(7)," + " command VARCHAR(256)," + " usetype INTEGER," + " user INTEGER); ";
        querry2 = Globals.SaveFromat.equalsIgnoreCase("sqlite") ? querry2.replace("{ID}", idSQLite) : querry2.replace("{ID}", idMYSQL);
        querry3 = Globals.SaveFromat.equalsIgnoreCase("sqlite") ? querry3.replace("{ID}", idSQLite) : querry3.replace("{ID}", idMYSQL);
        this.dbAccess.asyncSqlStatements(Lists.newArrayList(new String[]{sql, querry2, querry3}));
    }

    protected void createNewPlayerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + Globals.playerTable + " (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, address VARCHAR(7), address_from VARCHAR(7), player_name VARCHAR(20), uuid VARCHAR(36), player_x DOUBLE, player_y DOUBLE, player_z DOUBLE, player_yaw DOUBLE, player_pitch DOUBLE, server_name VARCHAR(30));";
        this.dbAccess.asyncSqlStatement(sql);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void checkDatabase() {
        checkAndAddColumn(
            "SELECT iriscode FROM " + Globals.gatesTable + ";",
            "ALTER TABLE " + Globals.gatesTable + " ADD iriscode VARCHAR(7) DEFAULT '0000000';",
            "Column IrisCode Added"
        );
        checkAndAddColumn(
            "SELECT irismaterial FROM " + Globals.gatesTable + ";",
            "ALTER TABLE " + Globals.gatesTable + " ADD irismaterial VARCHAR(35) DEFAULT '" + ConfigManager.pluginConfig.getString("GateMaterial.Default-Iris-Material") + "';",
            "Column Irismaterial Added"
        );
        checkAndAddColumn(
            "SELECT usegateperm FROM " + Globals.gatesTable + ";",
            "ALTER TABLE " + Globals.gatesTable + " ADD usegateperm BOOLEAN DEFAULT 0;",
            "Column usegateperm Added"
        );
    }

    /*
     * REWRITTEN during the 26.2 migration. CFR decompiled the original try-with-resources logic (probe a
     * SELECT, ALTER TABLE to add the column if it fails) into an unreadable ~260-line reconstruction that
     * reused the same generated variable names (conn32, stmt22, etc.) across multiple unrelated try blocks
     * in the same method scope, which doesn't compile. This is the same logic, restated as plain
     * try-with-resources - confirmed equivalent to the original control flow: probe query, catch
     * SQLException, run ALTER TABLE, swallow any SQLException from that (matches the original's empty
     * catch blocks).
     */
    private void checkAndAddColumn(String checkSql, String alterSql, String successLogMessage) {
        try (Connection conn = this.dbAccess.openNewConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(checkSql);
        } catch (SQLException e) {
            try (Connection conn = this.dbAccess.openNewConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute(alterSql);
                TheGateMain.theGateMain.getLogger().log(Level.INFO, successLogMessage);
            } catch (SQLException ex) {
                // empty catch block - matches original behavior
            }
        }
    }
}

