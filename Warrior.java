import java.util.ArrayList;

public class Warrior extends Adventurer {
	// field
	private int attack;
	private int support;
	private int shield;
	private int shieldMax;
	
	// constructors 
	public Warrior() {
		super("Basil");
		shieldMax = 9;
		shield = shieldMax;
		attack = 2;
		support = 3;
	}
	public Warrior(String name) {
		super(name);
		shieldMax = 9;
		shield = shieldMax;
		attack = 2;
		support = 3;
	}
	public Warrior(String name, int hp) {
		super(name, hp);
		shieldMax = 9;
		shield = shieldMax;
		attack = 2;
		support = 3;
	}
	
	// methods
	public String getSpecialName() {
		return "shield";
	}
	public int getSpecial() {
		return shield;
	}
	public void setSpecial(int n) {
		shield += n;
		if (shield > shieldMax) shield = shieldMax;
	}
	public int getSpecialMax() {
		return shieldMax;
	}
	
	public String attack(Adventurer other) {
		other.applyDamage(attack);
		restoreSpecial(4);
		return this + " dealt " + attack + " points of damage to " + other;
	}
	public String support(Adventurer other) {
		other.setHP(other.getHP() + support);
		other.restoreSpecial(2);
		return this.getName() + " has increased " + other.getName() + "'s health by " + support;
	}
	public String support() {
		setHP(getHP() + support);
		restoreSpecial(2);
		return this.getName() + " has increased their health by " + support + " and replenished their shield.";
	}
	public String specialAttack(Adventurer other) {
		if (shield > 0) {
			other.applyDamage((int)(other.getHP() * ((shield) / 10.0)));
			shield--;
			return this.getName() + " has decreased " + other.getName() + "'s health by " + (10.0 * (shield + 1)) + "%";
		} else {
			other.applyDamage(attack);
			return this + " does not have enough special points. Instead " + attack(other);
		}
	}
	public String specialAttack(ArrayList<Adventurer> party){
		return specialAttack(party.get((int)(Math.random() * party.size())));
	}
}
