/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package thegate.main.save;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;
import thegate.main.TheGateMain;
import thegate.main.save.DatabaseFormat;
import thegate.main.save.DatabaseInfo;

public class DatabaseManager {
    private Thread SqlStatementThread;
    private boolean StatementThread = true;
    private Semaphore lock = new Semaphore(1);
    private ConcurrentLinkedQueue<String> SQLStatements = new ConcurrentLinkedQueue<String>();
    private DatabaseInfo dbInfo;
    private Connection connection;

    public DatabaseManager(DatabaseInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public boolean createDatabaseConnection(Plugin plugin) {
        switch (this.dbInfo.getDbFormat()) {
            case SQLITE: {
                File sqlitefile = new File(plugin.getDataFolder(), String.valueOf(File.separator) + this.dbInfo.getFileName() + ".sqlite");
                if (!sqlitefile.exists()) {
                    try {
                        sqlitefile.createNewFile();
                    }
                    catch (IOException e) {
                        plugin.getLogger().log(Level.WARNING, "Could not create new save file!");
                        return false;
                    }
                }
                try {
                    this.connection = this.openNewConnection();
                    plugin.getLogger().log(Level.INFO, "Database status: " + (this.testConnection() ? "Connected" : "Offline"));
                    plugin.getLogger().log(Level.INFO, "Using SQLite as save format.");
                    break;
                }
                catch (SQLException e) {
                    plugin.getLogger().log(Level.WARNING, "Connection to SQLite database failed!");
                    plugin.getLogger().log(Level.WARNING, "Check if your connection information is correct ->");
                    plugin.getLogger().log(Level.WARNING, "Path: " + this.dbInfo.getUrl());
                    plugin.getLogger().log(Level.WARNING, e.toString());
                    return false;
                }
            }
            case MYSQL: {
                try {
                    this.connection = this.openNewConnection();
                    plugin.getLogger().log(Level.INFO, "Database status: " + (this.testConnection() ? "Connected" : "Offline"));
                    plugin.getLogger().log(Level.INFO, "Using MySQL as save format.");
                    break;
                }
                catch (SQLException e) {
                    plugin.getLogger().log(Level.WARNING, "Connection to MySQL database failed!");
                    plugin.getLogger().log(Level.WARNING, "Check if your connection information is correct ->");
                    plugin.getLogger().log(Level.WARNING, "Path: " + this.dbInfo.getUrl());
                    plugin.getLogger().log(Level.WARNING, "UserName: " + this.dbInfo.getUser());
                    plugin.getLogger().log(Level.WARNING, "UserPassword: ***");
                    plugin.getLogger().log(Level.WARNING, e.toString());
                    return false;
                }
            }
            default: {
                plugin.getLogger().log(Level.WARNING, "No correct information given for the save format!");
                plugin.getLogger().log(Level.WARNING, "Check config!");
                return false;
            }
        }
        return true;
    }

    public void closeConnection() {
        try {
            this.connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startAsyncWorker() {
        this.SqlStatementThread = new Thread(() -> {
            while (this.StatementThread) {
                try {
                    this.lock.acquire();
                }
                catch (InterruptedException e) {
                    return;
                }
                if (!this.StatementThread) {
                    return;
                }
                try {
                    Connection conn = this.openNewConnection();
                    String s;
                    while ((s = this.SQLStatements.poll()) != null) {
                        try {
                            PreparedStatement statement = conn.prepareStatement(s);
                            statement.execute();
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.SqlStatementThread.start();
    }

    public void stopAsyncWorker() {
        this.StatementThread = false;
        this.lock.release();
        try {
            this.SqlStatementThread.join();
        }
        catch (InterruptedException e) {
            TheGateMain.theGateMain.getLogger().log(Level.WARNING, e.toString());
        }
    }

    public synchronized void asyncSqlStatements(List<String> list) {
        this.SQLStatements.addAll(list);
        this.lock.release();
    }

    public synchronized void asyncSqlStatement(String statement) {
        this.SQLStatements.add(statement);
        this.lock.release();
    }

    public boolean executeQuerry(String querry) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(querry);
        boolean out = statement.execute();
        return out;
    }

    public int executeUpdate(String querry) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(querry);
        int out = statement.executeUpdate();
        return out;
    }

    public boolean testConnection() {
        if (this.connection == null) {
            return false;
        }
        try {
            return this.executeQuerry("SELECT 1;");
        }
        catch (SQLException e) {
            return false;
        }
    }

    public boolean testConnection(Connection con) {
        if (con == null) {
            return false;
        }
        try {
            return con.prepareStatement("SELECT 1;").execute();
        }
        catch (SQLException e) {
            return false;
        }
    }

    public boolean hasTable(String TableName) {
        String querry = "SELECT * FROM " + TableName;
        try {
            return this.executeQuerry(querry);
        }
        catch (SQLException e) {
            return false;
        }
    }

    public ResultSet getData(String querry) throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet out = stmt.executeQuery(querry);
        return out;
    }

    public static DatabaseInfo getDatabaseInfo(String url, String name, String pass, DatabaseFormat format) {
        return new DatabaseInfo(url, name, pass, format);
    }

    public static DatabaseInfo getDatabaseInfo(String url, String name, String pass, String format) throws Exception {
        DatabaseFormat form = null;
        DatabaseFormat[] databaseFormatArray = DatabaseFormat.values();
        int n = databaseFormatArray.length;
        int n2 = 0;
        while (n2 < n) {
            DatabaseFormat f = databaseFormatArray[n2];
            if (f.toString().toUpperCase().equals(format.toUpperCase())) {
                form = f;
            }
            ++n2;
        }
        if (form == null) {
            throw new Exception("Format not valid: " + format);
        }
        return DatabaseManager.getDatabaseInfo(url, name, pass, form);
    }

    public static DatabaseInfo getDatabaseInfo(Plugin plugin, String fileName) {
        return new DatabaseInfo(plugin, fileName);
    }

    public Connection openNewConnection() throws SQLException {
        return this.dbInfo.getUser() != null && this.dbInfo.getPass() != null ? DriverManager.getConnection(this.dbInfo.getUrl(), this.dbInfo.getUser(), this.dbInfo.getPass()) : DriverManager.getConnection(this.dbInfo.getUrl());
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

