package today.Miscible.start.player;

import java.util.Iterator;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import today.Miscible.events.EventUpdate;
import today.Miscible.start.Mod;
import today.Miscible.start.Mod.Category;
import today.Miscible.start.Value;
import today.Miscible.utils.timeUtils.TimeHelper;

public class ChestStealer extends Mod {

	public Value<Double> delay = new Value("ChestStealer_Delay", 65d, 0d, 200d, 1d);
	public Value<Boolean> cchest = new Value("ChestStealer_CloseChest", true);
	public Value<Boolean> IGNORE = new Value("ChestStealer_Ignore", true);
	public Value<Boolean> DROP = new Value("ChestStealer_Drop", true);
	public Value<String> mode = new Value("ChestStealer", "Mode", 0);
	TimeHelper time = new TimeHelper();
	private boolean isStealing;

	public ChestStealer() {
		super("ChestStealer", Category.World);
		this.mode.mode.add("Hypixel");
	}
	
    private static Minecraft mc = Minecraft.getMinecraft();

	@EventTarget
	public void onUpdate(EventUpdate event) {
		Container container = mc.thePlayer.openContainer;
		if (mode.isCurrentMode("Hypixel")) {

			if (mc.currentScreen instanceof GuiChest) {
				GuiChest guiChest = (GuiChest) mc.currentScreen;
				String name = guiChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase();
				if (IGNORE.getValueState().booleanValue() && !name.contains("chest")) {
					return;
				}

				this.isStealing = true;
				boolean full = true;
				ItemStack[] arrayOfItemStack = mc.thePlayer.inventory.mainInventory;
				int j = mc.thePlayer.inventory.mainInventory.length;

				for (int i = 0; i < j; ++i) {
					ItemStack item = arrayOfItemStack[i];
					if (item == null) {
						full = false;
						break;
					}
				}

				boolean containsItems = false;
				if (!full) {
					ItemStack stack;
					int index;
					for (index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); ++index) {
						stack = guiChest.lowerChestInventory.getStackInSlot(index);
						if (stack != null && !this.isBad(stack)) {
							containsItems = true;
							break;
						}
					}

					if (containsItems) {
						for (index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); ++index) {
							stack = guiChest.lowerChestInventory.getStackInSlot(index);
							if (stack != null
									&& this.time.delay((float) (5 * (this.delay.getValueState().floatValue())))
									&& !this.isBad(stack)) {
								mc.playerController.windowClick(guiChest.inventorySlots.windowId, index, 0,
										(DROP.getValueState()).booleanValue() ? 0 : 1, mc.thePlayer);
								if (DROP.getValueState().booleanValue()) {
									mc.playerController.windowClick(guiChest.inventorySlots.windowId, -999, 0, 0,
											mc.thePlayer);
								}

								this.time.reset();
							}
						}
					} else if (cchest.getValueState().booleanValue()) {
						mc.thePlayer.closeScreen();
						this.isStealing = false;
					}
				} else if (cchest.getValueState().booleanValue()) {
					mc.thePlayer.closeScreen();
					this.isStealing = false;
				}
			} else {
				this.isStealing = false;
			}

		}
	}

	private boolean isContainerEmpty(Container container) {
		boolean flag = true;
		int i = 0;
		for (int j = container.inventorySlots.size() == 90 ? 54 : 27; i < j; ++i) {
			if (container.getSlot(i).getHasStack()) {
				flag = false;
			}
		}
		return flag;
	}

	private boolean isBad(ItemStack item) {
		if (item != null && item.getItem() instanceof ItemSword) {
			return this.getDamage(item) <= this.bestDamage();
		} else if (item != null && item.getItem() instanceof ItemArmor && !this.canEquip(item)
				&& (!this.betterCheck(item) || this.canEquip(item))) {
			return true;
		} else {
			return item != null && !(item.getItem() instanceof ItemArmor)
					&& (item.getItem().getUnlocalizedName().contains("tnt")
							|| item.getItem().getUnlocalizedName().contains("stick")
							|| item.getItem().getUnlocalizedName().contains("egg")
							|| item.getItem().getUnlocalizedName().contains("string")
							|| item.getItem().getUnlocalizedName().contains("flint")
							|| item.getItem().getUnlocalizedName().contains("compass")
							|| item.getItem().getUnlocalizedName().contains("feather")
							|| item.getItem().getUnlocalizedName().contains("bucket")
							|| item.getItem().getUnlocalizedName().contains("chest")
									&& !item.getDisplayName().toLowerCase().contains("collect")
							|| item.getItem().getUnlocalizedName().contains("snow")
							|| item.getItem().getUnlocalizedName().contains("fish")
							|| item.getItem().getUnlocalizedName().contains("enchant")
							|| item.getItem().getUnlocalizedName().contains("exp")
							|| item.getItem().getUnlocalizedName().contains("shears")
							|| item.getItem().getUnlocalizedName().contains("anvil")
							|| item.getItem().getUnlocalizedName().contains("torch")
							|| item.getItem().getUnlocalizedName().contains("seeds")
							|| item.getItem().getUnlocalizedName().contains("leather")
							|| item.getItem() instanceof ItemPickaxe || item.getItem() instanceof ItemGlassBottle
							|| item.getItem() instanceof ItemTool
							|| item.getItem().getUnlocalizedName().contains("piston")
							|| item.getItem().getUnlocalizedName().contains("potion") && this.isBadPotion(item));
		}
	}

	private double getProtectionValue(ItemStack stack) {
		return stack.getItem() instanceof ItemArmor ? (double) ((ItemArmor) stack.getItem()).damageReduceAmount
				+ (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount)
						* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D
				: 0.0D;
	}

	private boolean betterCheck(ItemStack stack) {
		try {
			if (stack.getItem() instanceof ItemArmor) {
				if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots")) {
					assert mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(1))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}

				if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings")) {
					assert mc.thePlayer.getEquipmentInSlot(2).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(2))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}

				if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate")) {
					assert mc.thePlayer.getEquipmentInSlot(3).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(3))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}

				if (mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet")) {
					assert mc.thePlayer.getEquipmentInSlot(4).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(4))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}
			}

			return false;
		} catch (Exception var3) {
			return false;
		}
	}

	private float bestDamage() {
		float bestDamage = 0.0F;

		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (is.getItem() instanceof ItemSword && this.getDamage(is) > bestDamage) {
					bestDamage = this.getDamage(is);
				}
			}
		}

		return bestDamage;
	}

	private boolean canEquip(ItemStack stack) {
		assert stack.getItem() instanceof ItemArmor;

		return mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots")
				|| mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings")
				|| mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate")
				|| mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
	}

	private boolean isBadPotion(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			ItemPotion potion = (ItemPotion) stack.getItem();
			if (ItemPotion.isSplash(stack.getItemDamage())) {
				Iterator var3 = potion.getEffects(stack).iterator();

				while (var3.hasNext()) {
					Object o = var3.next();
					PotionEffect effect = (PotionEffect) o;
					if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId()
							|| effect.getPotionID() == Potion.moveSlowdown.getId()
							|| effect.getPotionID() == Potion.weakness.getId()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private float getDamage(ItemStack stack) {
		return !(stack.getItem() instanceof ItemSword) ? 0.0F
				: (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F
						+ ((ItemSword) stack.getItem()).getMaxDamage();
	}

}
