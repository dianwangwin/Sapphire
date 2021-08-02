package today.Miscible.start.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import today.Miscible.events.EventReceivePacket;
import today.Miscible.start.Mod;
import today.Miscible.start.combat.Killaura;

public class NoRotateSet extends Mod {

	public NoRotateSet() {
		super("NoRotateSet", Category.Playe);
	}

	@EventTarget
	public void onEvent(EventReceivePacket event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
			if (this.mc.thePlayer.rotationYaw > -90.0f && this.mc.thePlayer.rotationPitch < 90.0f
					|| Killaura.blockingStatus) {
				packet.yaw = this.mc.thePlayer.rotationYaw;
				packet.pitch = this.mc.thePlayer.rotationPitch;
			}
		}
	}
}
