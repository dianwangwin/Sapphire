package today.Sapphire.start.player;

import com.darkmagician6.eventapi.EventTarget;

import today.Sapphire.events.EventUpdate;
import today.Sapphire.start.Mod;

public class AutoRespawn extends Mod {

	public AutoRespawn() {
		super("AutoRespawn", Category.Player);
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
