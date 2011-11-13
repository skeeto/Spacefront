package spacefront;

public class BasicWeapon implements Weapon {

    public Shot fire(double destx, double desty) {
        return new BasicShot(0, 0, destx, desty);
    }
}
