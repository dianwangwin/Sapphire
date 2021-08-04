package today.Sapphire.start.combat;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import today.Sapphire.events.EventReceivePacket;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;

public class Velocity extends Mod {
	private Value<String> mode;

	public Velocity() {
		super("Velocity", Category.Combat);
		this.mode = new Value<String>("Velocity", "Mode", 0);
		this.mode.addValue("Simple");
	}

	@EventTarget
	public void onEvent(EventReceivePacket event) {
		this.setDisplayName(this.mode.getModeName(this.mode));
		S12PacketEntityVelocity packet;
		if (this.mc.theWorld
				.getEntityByID((packet = (S12PacketEntityVelocity) event.packet).getEntityID()) == this.mc.thePlayer) {
			switch (this.mode.getModeName(this.mode)) {
			case "Simple": {
				if (event.getPacket() instanceof S12PacketEntityVelocity) {
					event.setCancelled(true);
				}

				if (event.getPacket() instanceof S27PacketExplosion) {
					event.setCancelled(true);
				}
				break;
			}
			}
		}
	}
}
