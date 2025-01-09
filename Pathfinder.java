public class Pathfinder extends Adventurer{
    // Pathfinders can heal allies
    private int heal = 5;
    private int healMax = 10;

    public Pathfinder (String name) {
        super("Pathfinder " + name, 15);
    }

    public Pathfinder (String name, int hp) {
        super("Pathfinder " + name, hp);
    }

    public Pathfinder (String name, int hp, int heal, int healMax) {
        super("Pathfinder " + name, hp);
        this.heal = heal;
        this.healMax = healMax;
    }

    @Override
    public String getSpecialName() {
        return "healing";
    }

    @Override
    public int getSpecial() {
        return this.heal;
    }

    @Override
    public void setSpecial(int n) {
        this.heal = n;
    }

    @Override
    public int getSpecialMax () {
        return this.healMax;
    }

    @Override
    public String attack(Adventurer other) {
        other.applyDamage(1);
        return this.getName() + " attacks " + other.getName() + " and inflicts 1hp damage.";
    }

    @Override
    public String support(Adventurer other) {
        if (this.getSpecial() > 0) {
            other.setHP(other.getHP() + 5);
            this.setSpecial(this.getSpecial() + 1);
            return this.getName() + " heals " + other.getName() + " by 5hp and restores 1 healing.";
        }

        return "No healing resources available.";
    }

    @Override
    public String support() {
        if (this.getSpecial() > 0) {
            this.setHP(this.getHP() + 5);
            this.setSpecial(this.getSpecial() + 1);
            return this.getName() + " heals themselves by 5hp and restores 1 healing.";
        }

        return "No healing resources available.";
    }

    public String specialAttack(Adventurer other) {
        if (this.getSpecial() >= 2) {
            other.applyDamage(4);
            this.setSpecial(this.getSpecial() - 2);
            return this.getName() + " attacks " + other.getName() + " and inflicts 4hp damage.";
        }

        return "Insufficient " +  this.getSpecialName() + " resources available. 2 needed, " + this.getSpecial() + " available.";
    }







}
