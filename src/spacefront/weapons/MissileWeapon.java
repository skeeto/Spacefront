package spacefront.weapons;

import spacefront.Shot;
import spacefront.Weapon;

public class MissileWeapon implements Weapon {

    private static final long FIRE_DELAY = 600;

    public Shot fire(double destx, double desty) {
        return new Missile(0, 0, destx, desty);
    }

    public long getSpeed() {
        return FIRE_DELAY;
    }
}
