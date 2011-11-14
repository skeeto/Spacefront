package spacefront.weapons;

import spacefront.Shot;
import spacefront.Weapon;

public class GrenadeWeapon implements Weapon {

    private static final long FIRE_DELAY = 500;

    public Shot fire(double destx, double desty) {
        return new Grenade(0, 0, destx, desty);
    }

    public long getSpeed() {
        return FIRE_DELAY;
    }
}
