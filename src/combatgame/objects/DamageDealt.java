package combatgame.objects;

public class DamageDealt {

	public int healthDamage;
	public int armorDamage;
	public boolean isAttack; //as opposed to be healed
	
	public DamageDealt(int healthDamage, int armorDamage, boolean isAttack) {
		this.healthDamage = healthDamage;
		this.armorDamage = armorDamage;
		this.isAttack = isAttack;
	}
	
}
