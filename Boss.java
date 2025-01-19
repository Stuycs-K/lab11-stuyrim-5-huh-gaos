public class Boss extends Adventurer{
    // Bosss can revive allies
    private int revive = 2;
    private int reviveMax = 2;

    public Boss (String name) {
        super(name, 30);
    }

    public Boss (String name, int hp) {
        super(name, hp);
    }

    public Boss (String name, int hp, int revive, int reviveMax) {
        super(name, hp);
        this.revive = revive;
        this.reviveMax = reviveMax;
    }

    @Override
    public String getSpecialName() {
        return "reviving";
    }

    @Override
    public int getSpecial() {
        return this.revive;
    }

    @Override
    public void setSpecial(int n) {
        this.revive = n;
    }

    @Override
    public int getSpecialMax () {
        return this.reviveMax;
    }

    @Override
    public String attack(Adventurer other) {
        // heal themselves , if possible
        if (this.getHP() < this.getmaxHP() / 3 && this.getSpecial() > 0) {
            return this.support(other);
        } else {
            other.applyDamage(5);
            return this.getName() + " attacks " + other.getName() + " and inflicts 5hp damage.";
        }
    }

    @Override
    public String support(Adventurer other) {
        if (this.getSpecial() > 0) {
            this.setHP(this.getmaxHP());
            this.setSpecial(this.getSpecial() -1);
            return this.getName() + " revives themselves to 30 HP and uses 1 reviving.";
        }

        return this.attack(other);
    }

    //never used
    @Override
    public String support() {
        if (this.getSpecial() > 0) {
            this.setHP(this.getHP() + 5);
            this.setSpecial(this.getSpecial() + 1);
            return this.getName() + " revives themselves by 5hp and restores 1 reviveing.";
        }

        return "No reviving resources available.";
    }

    public String specialAttack(Adventurer other) {
        // if (this.getSpecial() > 0) {
        //     other.applyDamage(other.getmaxHP() / 2);
        //     this.setSpecial(this.getSpecial() - 1);
        //     return this.getName() + " special attacks " + other.getName() + " and inflicts " + other.getmaxHP() / 2 + "hp damage.";
        // }

        // return this.attack(other);

        return this.attack(other);
    }

}
