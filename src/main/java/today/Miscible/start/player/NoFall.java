package today.Miscible.start.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import today.Miscible.events.EventPreMotion;
import today.Miscible.start.Mod;
import today.Miscible.start.Value;

public class NoFall extends Mod {

	public Value<String> mode = new Value("NoFall", "Mode", 0);

	public NoFall() {
		super("NoFall", Category.Movement);
		this.mode.mode.add("Hypixel");
	}

	private float lastFall;

	@EventTarget
	private void onPre(EventPreMotion event) {
		this.setDisplayName(this.mode.getModeName(this.mode));
		
		switch (this.mode.getModeName(this.mode)) {
		case "Hypixel": {
			float falldis = 0.825f + (float) getJumpEffect();
			if (mc.thePlayer.fallDistance - this.lastFall >= falldis) {
				this.lastFall = mc.thePlayer.fallDistance;
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer(true));
			} else if (mc.thePlayer.isCollidedVertically) {
				this.lastFall = 0f;
			}
			break;
		}
		}
	}

	public static int getJumpEffect() {
		return mc.thePlayer.isPotionActive(Potion.jump)
				? mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1
				: 0;
	}

}