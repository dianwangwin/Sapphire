package today.Sapphire.start.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import today.Sapphire.events.EventReceivePacket;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;

public class Velocity extends Mod {

	private final Value<String> mode;
	private final Value<Boolean> S27;

	private final Value<Double> Horizontal , Vertical;

	public Velocity() {
		super("Velocity", Category.Combat);
		mode = new Value<>("Velocity", "Mode", new String[]{"Simple"} , 0);
		Horizontal =  new Value<>("Velocity", "Horizontal", 1.0 , 0.0 ,  1.0 , 0.05);
		Vertical =  new Value<>("Velocity", "Vertical", 1.0 , 0.0 ,  1.0 , 0.05);
		S27 = new Value<>("Velocity", "CancelPacketExplosion", true);
	}

	@EventTarget
	public void onEvent(EventReceivePacket event) {
		if (mc.theWorld.getEntityByID(((S12PacketEntityVelocity) event.packet).getEntityID()) == mc.thePlayer) {

			if (mode.getModeName().equals("Simple")) {
				if (event.getPacket() instanceof S12PacketEntityVelocity) {
					S12PacketEntityVelocity entityVelocity = (S12PacketEntityVelocity)event.getPacket();

					if(Horizontal.getValueState() == 0.0 && Vertical.getValueState() == 0.0) {
						event.setCancelled(true);
					} else {
						entityVelocity.motionX *= Horizontal.getValueState();
						entityVelocity.motionZ *= Horizontal.getValueState();
						entityVelocity.motionY *= Vertical.getValueState();
					}
				}
			}

			if (event.getPacket() instanceof S27PacketExplosion && S27.getValueState()) {
				event.setCancelled(true);
			}
		}
	}
}
