package today.Sapphire.start.movent;

import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;

public class KeepSprint extends Mod {

	public final Value<Double> speed;

	public KeepSprint() {
		super("KeepSprint", Category.Movement);
		speed = new Value("KeepSprint" , "Speed", 1.0, 0.6, 1.0, 0.01);
	}
}
