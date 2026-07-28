/*
 * Decompiled with CFR 0.152.
 */
package thegate.bungee;

public class PlayerServerData {
    public double PlayerX = 0.0;
    public double PlayerY = 0.0;
    public double PlayerZ = 0.0;
    public double PlayerYaw = 0.0;
    public double PlayerPitch = 0.0;
    public String AddressFrom = "";
    public String AddressTo = "";

    public double getPlayerX() {
        return this.PlayerX;
    }

    public void setPlayerX(double playerX) {
        this.PlayerX = playerX;
    }

    public double getPlayerY() {
        return this.PlayerY;
    }

    public void setPlayerY(double playerY) {
        this.PlayerY = playerY;
    }

    public double getPlayerZ() {
        return this.PlayerZ;
    }

    public void setPlayerZ(double playerZ) {
        this.PlayerZ = playerZ;
    }

    public double getPlayerYaw() {
        return this.PlayerYaw;
    }

    public void setPlayerYaw(double playerYaw) {
        this.PlayerYaw = playerYaw;
    }

    public double getPlayerPitch() {
        return this.PlayerPitch;
    }

    public void setPlayerPitch(double playerPitch) {
        this.PlayerPitch = playerPitch;
    }

    public String getAddressFrom() {
        return this.AddressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.AddressFrom = addressFrom;
    }

    public String getAddressTo() {
        return this.AddressTo;
    }

    public void setAddressTo(String addressTo) {
        this.AddressTo = addressTo;
    }
}

