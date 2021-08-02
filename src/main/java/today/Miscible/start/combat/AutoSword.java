package today.Miscible.start.combat;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import today.Miscible.events.EventUpdate;
import today.Miscible.start.Mod;
import today.Miscible.start.Mod.Category;

public class AutoSword extends Mod {
	public AutoSword() {
		super("AutoSword", Category.Combat);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.thePlayer.getCurrentEquippedItem() != null
				&& mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
			ItemSword currentSword = (ItemSword) mc.thePlayer.getCurrentEquippedItem().getItem();
			for (int i = 0; i < 45; ++i) {
				float currentDamage;
				float itemDamage;
				Item item;
				if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
						|| !((item = mc.thePlayer.inventoryContainer.getSlot(i).getStack()
								.getItem()) instanceof ItemSword)
						|| (itemDamage = this.getSharpnessLevel(mc.thePlayer.inventoryContainer.getSlot(i).getStack())
								+ ((ItemSword) item).getDamageVsEntity()) <= (currentDamage = this.getSharpnessLevel(
										mc.thePlayer.getCurrentEquippedItem()) + currentSword.getDamageVsEntity()))
					continue;
				this.swap(i, mc.thePlayer.inventory.currentItem);
				break;
			}
		}
	}

	protected void swap(int slot, int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	private float getSharpnessLevel(ItemStack stack) {
		if (!(stack.getItem() instanceof ItemSword)) {
			return 0.0f;
		}
		return (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
	}

	public void onDisable() {
		super.onDisable();
	}

}