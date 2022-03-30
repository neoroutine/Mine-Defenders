package neoroutine.minetd.common.blocks.towers;

public class TowerProperties
{
    private int DELAY       = TowerDelay.PAWN;
    private int REACH       = TowerReach.PAWN;
    private int DAMAGE      = TowerDamage.PAWN;
    private int CONSUMPTION = TowerConsumption.PAWN;

    public TowerProperties(int delay, int reach, int damage, int consumption)
    {
        DELAY       = delay;
        REACH       = reach;
        DAMAGE      = damage;
        CONSUMPTION = consumption;
    }


    public int getDelay() {return DELAY;}

    public void setDelay(int delay) {this.DELAY = delay;}


    public int getReach() {return REACH;}

    public void setReach(int reach) {this.REACH = reach;}


    public int getDamage() {return DAMAGE;}

    public void setDamage(int damage) {this.DAMAGE = DAMAGE;}


    public int getConsumption() { return CONSUMPTION;}

    public void setConsumption(int consumption) { this.CONSUMPTION = consumption; }
}
