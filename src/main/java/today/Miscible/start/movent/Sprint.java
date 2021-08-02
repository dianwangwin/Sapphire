package today.Miscible.start.movent;

import com.darkmagician6.eventapi.EventTarget;

import today.Miscible.events.EventUpdate;
import today.Miscible.start.Mod;

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
