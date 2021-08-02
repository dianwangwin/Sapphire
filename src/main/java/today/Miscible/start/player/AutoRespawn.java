package today.Miscible.start.player;

import com.darkmagician6.eventapi.EventTarget;

import today.Miscible.events.EventUpdate;
import today.Miscible.start.Mod;

public class AutoRespawn extends Mod {

	public AutoRespawn() {
		super("AutoRespawn", Category.Playe);
	}

	@EventTarget
    public void onUpdate(EventUpdate event) {
        if (!this.mc.thePlayer.isEntityAlive()) {
            this.mc.thePlayer.respawnPlayer();
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        
    }
    public void onEnable() {
    	super.isEnabled();
       
    }
}
