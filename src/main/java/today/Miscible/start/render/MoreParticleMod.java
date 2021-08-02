package today.Miscible.start.render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import today.Miscible.events.EventSendPacket;
import today.Miscible.start.Mod;

public class MoreParticleMod extends Mod{

	private EntityLivingBase target = null;
	
	public MoreParticleMod() {
		super("MoreParticleMod", Category
				.Render);
	}

	
}
