package today.Sapphire.start.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import today.Sapphire.events.EventPreMotion;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;

public class NoFall extends Mod {

	public Value<String> mode = new Value("NoFall", "Mode", new String[]{"Hypixel"},  0);

	public NoFall() {
		super("NoFall", Category.Movement);
		setDisplayName(mode.getModeName());
	}

	private float lastFall;

	@EventTarget
	private void onPre(EventPreMotion event) {

		switch (this.mode.getModeName()) {
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
