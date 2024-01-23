package glorydark.nukkit;

import cn.nukkit.level.Location;

/**
 * @author glorydark
 */
public class PotionSplashData {

    private final int potionId;

    private final Location location;

    public PotionSplashData(int potionId, Location location) {
        this.potionId = potionId;
        this.location = location;
    }

    public int getPotionId() {
        return potionId;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "PotionSplashData{" +
                "potionId=" + potionId +
                ", location=" + location +
                '}';
    }
}
