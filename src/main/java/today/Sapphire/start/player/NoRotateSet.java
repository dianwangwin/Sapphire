package today.Sapphire.start.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import today.Sapphire.events.EventReceivePacket;
import today.Sapphire.start.Mod;
import today.Sapphire.start.combat.Killaura;

public class NoRotateSet extends Mod {

	public NoRotateSet() {
		super("NoRotateSet", Category.Player);
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
