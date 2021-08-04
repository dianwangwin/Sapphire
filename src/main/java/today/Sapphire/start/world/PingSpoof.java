package today.Sapphire.start.world;

import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import today.Sapphire.events.EventSendPacket;
import today.Sapphire.start.Mod;

public class PingSpoof extends Mod {

	public PingSpoof() {
		super("PingSpoof", Category.World);
	}

	@EventTarget
	public void onPacket(EventSendPacket event) {

		if (event.packet instanceof C0FPacketConfirmTransaction) {
			C0FPacketConfirmTransaction c0f = (C0FPacketConfirmTransaction) event.packet;
			
			mc.getNetHandler().getNetworkManager()
					.sendPacket(new C0FPacketConfirmTransaction(Integer.MAX_VALUE, c0f.getUid(), false));
			if (c0f.getUid() < 0 && c0f.getWindowId() == 0)
				event.setCancelled(true);
		}
		
		if (event.getPacket() instanceof C00PacketKeepAlive) {
			mc.getNetHandler().getNetworkManager()
			.sendPacket(new C00PacketKeepAlive(Integer.MIN_VALUE + (new Random()).nextInt(100)));
            
			event.setCancelled(true);
         }
	}
}
