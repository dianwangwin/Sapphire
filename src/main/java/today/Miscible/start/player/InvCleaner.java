package today.Miscible.start.player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import today.Miscible.events.EventUpdate;
import today.Miscible.start.Mod;
import today.Miscible.start.Value;
import today.Miscible.utils.timeUtils.TimeHelper;

public class InvCleaner extends Mod {
	public final Set<Integer> blacklistedItemIDs = new HashSet<>();
	private  final Minecraft MC = Minecraft.getMinecraft();
	private  final Random RANDOM = new Random();
	
	private int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
	private int slots;
	private double numberIdkWillfigureout;
	private boolean bol;

	public ItemStack[] bestArmorSet;
	public ItemStack bestSword;
	public ItemStack bestPickAxe;
	public ItemStack bestBow;

	public Value<Double> clspeed = new Value("InvCleaner","CleanSpeed", 30d, 1d, 20d, 1d);
	public Value<Boolean> onInv = new Value("InvCleaner","OpenInv", true);
	public Value<Boolean> autodisable = new Value("InvCleaner","AutoDisable", true);
	TimeHelper timer = new TimeHelper();

	public InvCleaner() {
		super("InvCleaner", Category.World);
		Arrays.stream(new int[] {
				// Egg
				344,
				// Stick
				280,
				// String
				287,
				// Flint
				318,
				// Compass
				345,
				// Feather
				288,
				// Experience Bottle
				384,
				// Enchanting Table
				116,
				// Chest
				// 54,
				// Snowball
				332,
				// Anvil
				145 }).forEach(this.blacklistedItemIDs::add);
	}

	@Override
	public void onEnable() {
		this.slots = 9;

		super.onEnable();
	}

	public boolean getOpenInv() {
		return this.onInv.getValueState().booleanValue();
	}

	public int getCleanSpeed() {
		return this.clspeed.getValueState().intValue();
	}

	public boolean getAutoDisable() {
		return this.autodisable.getValueState().booleanValue();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (this.getOpenInv() && mc.currentScreen == null) {
			return;
		}
		if (MC.thePlayer.ticksExisted % getCleanSpeed() == 0 && RANDOM.nextInt(2) == 0) {
			this.bestArmorSet = getBestArmorSet();
			this.bestSword = getBestItem(ItemSword.class, Comparator.comparingDouble(this::getSwordDamage));
			this.bestPickAxe = getBestItem(ItemPickaxe.class, Comparator.comparingDouble(this::getMiningSpeed));
			this.bestBow = getBestItem(ItemBow.class, Comparator.comparingDouble(this::getBowPower));

			java.util.Optional<Slot> blacklistedItem = ((List<Slot>) MC.thePlayer.inventoryContainer.inventorySlots)
					.stream().filter(Slot::getHasStack)
					.filter(slot -> Arrays.stream(MC.thePlayer.inventory.armorInventory)
							.noneMatch(slot.getStack()::equals))
					.filter(slot -> !slot.getStack().equals(MC.thePlayer.getHeldItem()))
					.filter(slot -> isItemBlackListed(slot.getStack())).findFirst();
			if (blacklistedItem.isPresent()) {
				this.dropItem(blacklistedItem.get().slotNumber);
			} else {

				if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory
						|| mc.currentScreen instanceof GuiChat) {
					if (timer.delay(100) && weaponSlot >= 36) {

						if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
							getBestWeapon(weaponSlot);

						} else {
							if (!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
								getBestWeapon(weaponSlot);
							}
						}
					}
				}
				if (this.getAutoDisable()) {
					this.set(false);
				}
			}
		}
	}

	public void dropItem(int slotID) {
		MC.playerController.windowClick(0, slotID, 1, 4, MC.thePlayer);
	}

	// Objects.requireNonNull is just for debugging. It can't be null
	// Things to throw out
	public boolean isItemBlackListed(ItemStack itemStack) {
		Item item = itemStack.getItem();

		return blacklistedItemIDs.contains(Item.getIdFromItem(item))
				|| item instanceof ItemBow && !this.bestBow.equals(itemStack)
				|| item instanceof ItemTool && !this.bestPickAxe.equals(itemStack) || item instanceof ItemFishingRod
				|| item instanceof ItemGlassBottle || item instanceof ItemBucket
				|| item instanceof ItemFood && !(item instanceof ItemAppleGold)
				|| item instanceof ItemSword && !this.bestSword.equals(itemStack)
				|| item instanceof ItemArmor && !this.bestArmorSet[((ItemArmor) item).armorType].equals(itemStack)
				|| item instanceof ItemPotion && isPotionNegative(itemStack);
	}

	// Improved check to reduce copy pasty code
	public ItemStack getBestItem(Class<? extends Item> itemType, Comparator comparator) {
		java.util.Optional bestItem = ((List<Slot>) MC.thePlayer.inventoryContainer.inventorySlots).stream()
				.map(Slot::getStack).filter(Objects::nonNull)
				.filter(itemStack -> itemStack.getItem().getClass().equals(itemType)).max(comparator);

		return (ItemStack) bestItem.orElse(null);
	}

	// Armor check
	public ItemStack[] getBestArmorSet() {
		ItemStack[] bestArmorSet = new ItemStack[4];

		List<ItemStack> armor = ((List<Slot>) MC.thePlayer.inventoryContainer.inventorySlots).stream()
				.filter(Slot::getHasStack).map(Slot::getStack)
				.filter(itemStack -> itemStack.getItem() instanceof ItemArmor).collect(Collectors.toList());

		for (ItemStack itemStack : armor) {
			ItemArmor itemArmor = (ItemArmor) itemStack.getItem();

			ItemStack bestArmor = bestArmorSet[itemArmor.armorType];

			if (bestArmor == null || getArmorDamageReduction(itemStack) > getArmorDamageReduction(bestArmor)) {
				bestArmorSet[itemArmor.armorType] = itemStack;
			}
		}

		return bestArmorSet;
	}

	public double getSwordDamage(ItemStack itemStack) {
		double damage = 0;

		java.util.Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream()
				.findFirst();

		if (attributeModifier.isPresent()) {
			damage = attributeModifier.get().getAmount();
		}

		damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

		return damage;
	}

	public double getBowPower(ItemStack itemStack) {
		double power = 0;

		java.util.Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream()
				.findFirst();

		if (attributeModifier.isPresent()) {
			power = attributeModifier.get().getAmount();
		}

		power += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

		return power;
	}

	public double getMiningSpeed(ItemStack itemStack) {
		double speed = 0;

		java.util.Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream()
				.findFirst();

		if (attributeModifier.isPresent()) {
			speed = attributeModifier.get().getAmount();
		}

		speed += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

		return speed;
	}

	public double getArmorDamageReduction(ItemStack itemStack) {
		int damageReductionAmount = ((ItemArmor) itemStack.getItem()).damageReduceAmount;

		damageReductionAmount += EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { itemStack },
				DamageSource.causePlayerDamage(MC.thePlayer));

		return damageReductionAmount;
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

	public boolean isBestWeapon(ItemStack stack) {
		float damage = getDamage(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getDamage(is) > damage && (is.getItem() instanceof ItemSword))
					return false;
			}
		}
		if ((stack.getItem() instanceof ItemSword)) {
			return true;
		} else {
			return false;
		}

	}

	public void getBestWeapon(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword)) {
					swap(i, slot - 36);
					timer.reset();
					break;
				}
			}
		}
	}

	private float getDamage2(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			ItemTool tool = (ItemTool) item;
			damage += getAttackDamage(stack);
		}
		if (item instanceof ItemSword) {
			ItemSword sword = (ItemSword) item;
			damage += getAttackDamage(stack);
		}
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f
				+ EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
		return damage;
	}

	private float getAttackDamage(ItemStack stack) {
		return !(stack.getItem() instanceof ItemSword) ? 0.0F
				: (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F
						+ ((ItemSword) stack.getItem()).getDamageVsEntity()
						+ (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
	}

	public void swap(int slot1, int hotbarSlot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
	}

	private float getDamage(ItemStack stack) {
		return !(stack.getItem() instanceof ItemSword) ? -1.0F
				: (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F
						+ ((ItemSword) stack.getItem()).getDamageVsEntity()
						+ (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
	}

	public boolean isPotionNegative(ItemStack itemStack) {
		ItemPotion potion = (ItemPotion) itemStack.getItem();

		List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

		return potionEffectList.stream().map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
				.anyMatch(Potion::isBadEffect);
	}

}
