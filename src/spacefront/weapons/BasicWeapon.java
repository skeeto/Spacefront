package spacefront.weapons;

import spacefront.Shot;
import spacefront.Weapon;

public class BasicWeapon implements Weapon {

    private static final long FIRE_DELAY = 200;

    public Shot fire(double destx, double desty) {
        return new BasicShot(0, 0, destx, desty);
    }

    public long getSpeed() {
        return FIRE_DELAY;
    }
}
