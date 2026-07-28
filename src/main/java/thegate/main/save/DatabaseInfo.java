/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package thegate.main.save;

import java.io.File;
import org.bukkit.plugin.Plugin;
import thegate.main.save.DatabaseFormat;

class DatabaseInfo {
    private final String url;
    private final String user;
    private final String pass;
    private final DatabaseFormat dbFormat;
    private final String fileName;

    public DatabaseInfo(String url, String name, String pass, DatabaseFormat dbFormat) {
        switch (dbFormat) {
            case MYSQL: {
                this.url = "jdbc:mysql:" + url;
                this.user = name;
                this.pass = pass;
                this.dbFormat = dbFormat;
                break;
            }
            case SQLITE: {
                this.url = "jdbc:sqlite:" + url + ".sqlite";
                this.user = null;
                this.pass = null;
                this.dbFormat = dbFormat;
                break;
            }
            default: {
                this.url = url;
                this.user = name;
                this.pass = pass;
                this.dbFormat = dbFormat;
            }
        }
        this.fileName = "";
    }

    public DatabaseInfo(Plugin plugin, String fileName) {
        this.dbFormat = DatabaseFormat.SQLITE;
        this.url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + fileName + ".sqlite";
        this.user = null;
        this.pass = null;
        this.fileName = fileName;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUser() {
        return this.user;
    }

    public String getPass() {
        return this.pass;
    }

    public DatabaseFormat getDbFormat() {
        return this.dbFormat;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String toString() {
        return "URL:\t" + (this.url != null ? this.url : "null") + "\nName:\t" + (this.user != null ? this.user : "null") + "\nDBFormat:\t" + (this.dbFormat.toString() != null ? this.dbFormat.toString() : "null") + "\nFileName:\t" + (this.fileName != null ? this.fileName : "null");
    }
}

