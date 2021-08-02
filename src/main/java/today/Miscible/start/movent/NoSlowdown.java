package today.Miscible.start.movent;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import today.Miscible.events.EventPostMotion;
import today.Miscible.events.EventPreMotion;
import today.Miscible.start.Mod;
import today.Miscible.start.Value;
import today.Miscible.start.combat.Killaura;

public class NoSlowdown extends Mod {

	public Value<String> mode;

	public NoSlowdown() {
		super("NoSlow", Category.Movement);
		this.mode = new Value("NoSlow", "Mode", 0);
		this.mode.addValue("Hypixel");
		this.mode.addValue("Vanilla");
	}

	@EventTarget
	public void onPre(EventPreMotion pre) {

		if (Killaura.blockTarget != null && (mc.thePlayer.isBlocking()
				|| Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword)) {

			switch (this.mode.getModeName(this.mode)) {
			case "Hypixel": {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
				break;
			}
			case "Vanilla": {

				break;
			}
			}
		}
	}

	@EventTarget
	public void onPost(EventPostMotion post) {

		if (Killaura.blockTarget != null && (mc.thePlayer.isBlocking()
				|| Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
			switch (this.mode.getModeName(this.mode)) {
			case "Hypixel": {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1),
						255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
				break;
			}
			case "Vanilla": {

				break;
			}
			}
		}
	}
}
