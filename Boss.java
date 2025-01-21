import java.util.ArrayList;

public class Boss extends Adventurer{
    // Bosses can revive allies
	private int minions = 2;
	private int minionsMax = 2;

    public Boss () {
        super("Boss", 30);
    }

    public Boss (String name) {
        super(name, 30);
    }

    public Boss (String name, int hp) {
        super(name, hp);
    }

    public Boss (String name, int hp, int minions, int minionsMax) {
        super(name, hp);
        this.minions = minions;
        this.minionsMax = minionsMax;
    }

    @Override
    public String getSpecialName() {
        return "minions";
    }

    @Override
    public int getSpecial() {
        return this.minions;
    }

    @Override
    public void setSpecial(int n) {
        if (n < 3) this.minions = n;
    }

    @Override
    public int getSpecialMax () {
        return this.minionsMax;
    }

    @Override
    public String attack(Adventurer other) {
        // heal themselves , if possible
        if (this.getHP() < this.getmaxHP() / 5 && other.getHP() > getHP()) {
            return this.support();
        } else {
            other.applyDamage(5);
            return this.getName() + " attacks " + other.getName() + " and inflicts 5HP damage.";
        }
    }

    @Override
    public String support(Adventurer other) {
        return support();
    }

    //never used
    @Override
    public String support() {
        this.setHP(this.getHP() + 10);
        return this.getName() + " revives themselves by 10HP.";
    }
	
	public String specialAttack(Adventurer other) {
		return support();
	}
    public String specialAttack(ArrayList<Adventurer> enemies) {
		if (getSpecial() > 0) {
			int rN = (int)(Math.random() * 3);
			Adventurer c;
			if (rN == 0) {
				c = new CodeWarrior("Minion " + (getSpecialMax() - getSpecial() + 1));
			} else if (rN == 1) {
				c = new Warrior("Minion " + (getSpecialMax() - getSpecial() + 1));
			} else {
				c = new Pathfinder("Minion " + (getSpecialMax() - getSpecial() + 1));
			}
			
			if (enemies.size() == 1) {
				enemies.add(0, c);
			} else {
				enemies.add(c);
			}
			
			setSpecial(getSpecial() - 1);
			return this + " has generated a minion.";
		} else {
			return support();
		}
    }
}
