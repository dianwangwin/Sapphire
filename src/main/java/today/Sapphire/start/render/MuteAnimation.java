package today.Sapphire.start.render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import today.Sapphire.events.EventSendPacket;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;

public class MuteAnimation extends Mod{
	
    public Value<Boolean> swing;

	public MuteAnimation() {
		super("MuteAnimation", Category.Render);
		swing = new Value("MuteAnimation_swing", false);
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
