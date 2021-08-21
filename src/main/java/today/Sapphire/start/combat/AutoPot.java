package today.Sapphire.start.combat;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import today.Sapphire.events.EventPreMotion;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;
import today.Sapphire.utils.timeUtils.TimeHelper;

public class AutoPot extends Mod {
	private Value<Double> delay = new Value<Double>("AutoPot","Delay", 50.0, 0.0, 1000.0, 10.0);
	private Value<Double> health = new Value<Double>("AutoPot","Health", 6.0, 0.5, 9.5, 0.5);
	private Value<Boolean> throwInInv = new Value<Boolean>("AutoPot","OnlyInv", true);
	private TimeHelper timer = new TimeHelper();
	private TimeHelper throwTimer = new TimeHelper();
	private boolean nextTick = false;

	public AutoPot() {
		super("AutoPot", Category.Combat);
	}

	@Override
	public void onDisable() {
		super.onDisable();

	}

	public void onEnable() {
		super.isEnabled();

	}

	@EventTarget
	public void onPre(EventPreMotion event) {
		if ((double) (mc.thePlayer.getHealth() / 2.0f) <= health.getValueState()) {
			event.pitch = 90.0f;
			getPotion();
			nextTick = true;
		}
		if (nextTick) {
			event.pitch = 90.0f;
			throwPotion();
			nextTick = false;
		}
	}

	private void getPotion() {
		int slotId = getFreeSlot();
		if (slotId != -1) {
			int id = 9;
			while (id <= 35) {
				ItemStack currentItem;
				Slot currentSlot = mc.thePlayer.inventoryContainer.getSlot(id);
				if (currentSlot.getHasStack() && (currentItem = currentSlot.getStack()).getItem() instanceof ItemPotion
						&& isSplashPotion(currentItem)
						&& timer.isDelayComplete(delay.getValueState().intValue())) {
					mc.playerController.windowClick(0, id, 0, 1, mc.thePlayer);
					slotId = getFreeSlot();
					timer.reset();
				}
				++id;
			}
		}
	}

	private void throwPotion() {
		int slotId = getFreeSlot();
		if (slotId != -1) {
			int id = 36;
			while (id <= 44) {
				ItemStack currentItem;
				Slot currentSlot = mc.thePlayer.inventoryContainer.getSlot(id);
				if (currentSlot.getHasStack() && (currentItem = currentSlot.getStack()).getItem() instanceof ItemPotion
						&& isSplashPotion(currentItem)
						&& throwTimer.isDelayComplete(delay.getValueState().intValue())) {
					int old = mc.thePlayer.inventory.currentItem;
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(id - 36));
					mc.thePlayer.sendQueue
							.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1,
									mc.thePlayer.inventoryContainer.getSlot(id).getStack(), 0.0f, 0.0f, 0.0f));
					mc.thePlayer.inventory.currentItem = id - 36;
					mc.thePlayer.sendQueue.addToSendQueue(
							new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(old));
					mc.thePlayer.inventory.currentItem = old;
					throwTimer.reset();
				}
				++id;
			}
		}
	}

	private boolean isSplashPotion(ItemStack itemStack) {
		return ItemPotion.isSplash(itemStack.getMetadata());
	}

	private int getFreeSlot() {
		int id = 36;
		while (id < 45) {
			Slot currentSlot = mc.thePlayer.inventoryContainer.getSlot(id);
			if (!currentSlot.getHasStack()) {
				return 1;
			}
			++id;
		}
		return -1;
	}
}

