package ao.domain.spell.effect;

import ao.domain.character.Character;
import ao.domain.character.UserCharacter;
import ao.domain.worldobject.WorldObject;

public class PoisonEffect implements Effect {

	@Override
	public void apply(Character caster, Character target) {
		target.setPoisoned(true);
	}

	@Override
	public boolean appliesTo(Character caster, Character target) {
		if (target instanceof UserCharacter && !target.isDead()) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean appliesTo(Character caster, WorldObject worldobject) {
		return false;
	}

	@Override
	public void apply(Character caster, WorldObject target) {
		
	}

}
