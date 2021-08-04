package today.Miscible.start.render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import today.Miscible.events.EventReceivePacket;
import today.Miscible.events.EventSendPacket;
import today.Miscible.start.Mod;
import today.Miscible.start.Value;

public class MuteAnimation extends Mod{
	
    public Value<Boolean> swing;

	public MuteAnimation() {
		super("MuteAnimation", Category.Render);
		swing = new Value("MuteAnimation","swing", false);
	}

	
	@EventTarget
	public void onPacket(EventSendPacket event) {
		if(event.packet instanceof C07PacketPlayerDigging) {
			if(C07PacketPlayerDigging.Action.DROP_ITEM != null) {
				mc.thePlayer.swingItem();
			}
		}
		
		if(event.packet instanceof C08PacketPlayerBlockPlacement) {
			if(mc.thePlayer.itemInUseCount != 0) {
				mc.thePlayer.swingItem();
			}
		}
	}
}
