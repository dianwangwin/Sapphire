package today.Sapphire.start.movent;

import com.darkmagician6.eventapi.EventTarget;

import today.Sapphire.events.EventUpdate;
import today.Sapphire.start.Mod;

public class Sprint extends Mod {

	public Sprint() {
		super("Sprint", Category.Movement);
	}
	
	@EventTarget
	private void onSprintln(EventUpdate event) {

		if(this.isEnabled())
		mc.thePlayer.setSprinting(mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f);
	}
}
