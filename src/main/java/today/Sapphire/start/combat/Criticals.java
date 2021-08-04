package today.Sapphire.start.combat;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2APacketParticles;
import today.Sapphire.events.EventReceivePacket;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;
import today.Sapphire.utils.timeUtils.TimeHelper;

public class Criticals extends Mod {
	public Value mode = new Value("Criticals", "Mode", 0);
	public Value delay = new Value("Criticals","Delay", Double.valueOf(200.0D), Double.valueOf(0.0D),
			Double.valueOf(1000.0D), 10.0D);
	public TimeHelper timer = new TimeHelper();
	public int crittimes = 0;

	public Criticals() {
		super("Criticals", Category.Combat);
		this.mode.addValue("Vanilla");
		this.mode.addValue("Hypixel");
		this.mode.addValue("Mineplex");
	}

	@EventTarget
	public void onAttack(EventReceivePacket event) {
		if (event.getPacket() instanceof S2APacketParticles
				|| event.getPacket().toString().contains("S2APacketParticles")) {
			return;
		}
		if (event.getPacket() instanceof C02PacketUseEntity && !(event.getPacket() instanceof S2APacketParticles)
				&& !(event.getPacket() instanceof C0APacketAnimation)) {
			C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
			int ticks = (int)this.delay.getValueState();
			if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && Killaura.target != null
					&& timer.delay(ticks * 5) && mc.thePlayer.onGround) {
				//doCrits();
				timer.reset();
			}
		}
	}
}
