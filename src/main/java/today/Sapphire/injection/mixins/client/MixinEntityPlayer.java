package today.Sapphire.injection.mixins.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import today.Sapphire.start.ModManager;
import today.Sapphire.start.movent.KeepSprint;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer extends MixinEntityLivingBase {

	@Shadow
	public InventoryPlayer inventory;
	@Shadow
	public PlayerCapabilities capabilities;
	@Shadow
	protected int flyToggleTimer;

	@Shadow
	public abstract ItemStack getHeldItem();

	@Shadow
	public abstract GameProfile getGameProfile();

	@Shadow
	protected abstract boolean canTriggerWalking();

	@Shadow
	protected abstract String getSwimSound();

	@Shadow
	public abstract FoodStats getFoodStats();

	@Shadow
	public abstract int getItemInUseDuration();

	@Shadow
	public abstract ItemStack getItemInUse();

	@Shadow
	public abstract boolean isUsingItem();

	@Shadow
	public abstract ItemStack getCurrentEquippedItem();

	@Shadow
	public abstract void onCriticalHit(Entity entityHit);

	@Shadow
	public abstract void onEnchantmentCritical(Entity entityHit);

	@Shadow
	public abstract void triggerAchievement(StatBase achievementIn);


	/**
	 * @reason  LiquidSlowly
	 * @author LiquidSlowly
	 */
	@Overwrite
	public void attackTargetEntityWithCurrentItem(Entity p_attackTargetEntityWithCurrentItem_1_) {
		if (ForgeHooks.onPlayerAttackTarget((EntityPlayer) (Object) this, p_attackTargetEntityWithCurrentItem_1_)) {
			if (p_attackTargetEntityWithCurrentItem_1_.canAttackWithItem() && !p_attackTargetEntityWithCurrentItem_1_.hitByEntity((EntityPlayer) (Object) this)) {
				float f = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
				int i = 0;
				float f1 = 0.0F;
				if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase) {
					f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItem(), ((EntityLivingBase) p_attackTargetEntityWithCurrentItem_1_).getCreatureAttribute());
				} else {
					f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
				}

				i = i + EnchantmentHelper.getKnockbackModifier((EntityPlayer) (Object) this);
				if (this.isSprinting()) {
					++i;
				}

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.blindness) && this.ridingEntity == null && p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase;
					if (flag && f > 0.0F) {
						f *= 1.5F;
					}

					f += f1;
					boolean flag1 = false;
					int j = EnchantmentHelper.getFireAspectModifier((EntityPlayer) (Object) this);
					if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase && j > 0 && !p_attackTargetEntityWithCurrentItem_1_.isBurning()) {
						flag1 = true;
						p_attackTargetEntityWithCurrentItem_1_.setFire(1);
					}

					double d0 = p_attackTargetEntityWithCurrentItem_1_.motionX;
					double d1 = p_attackTargetEntityWithCurrentItem_1_.motionY;
					double d2 = p_attackTargetEntityWithCurrentItem_1_.motionZ;
					boolean flag2 = p_attackTargetEntityWithCurrentItem_1_.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) (Object) this), f);
					if (flag2) {
						if (i > 0) {
							final KeepSprint keepSprint = (KeepSprint) ModManager.getModByName("KeepSprint");
							p_attackTargetEntityWithCurrentItem_1_.addVelocity((double) (-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * (float) i * 0.5F));

							assert keepSprint != null;
							float speed = (float) ((keepSprint.isEnabled) ? keepSprint.speed.getValueState() : 0.6D);

							this.motionX *= speed;
							this.motionZ *= speed;

							if(!keepSprint.isEnabled)
							this.setSprinting(false);
						}

						if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityPlayerMP && p_attackTargetEntityWithCurrentItem_1_.velocityChanged) {
							((EntityPlayerMP) p_attackTargetEntityWithCurrentItem_1_).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(p_attackTargetEntityWithCurrentItem_1_));
							p_attackTargetEntityWithCurrentItem_1_.velocityChanged = false;
							p_attackTargetEntityWithCurrentItem_1_.motionX = d0;
							p_attackTargetEntityWithCurrentItem_1_.motionY = d1;
							p_attackTargetEntityWithCurrentItem_1_.motionZ = d2;
						}

						if (flag) {
							this.onCriticalHit(p_attackTargetEntityWithCurrentItem_1_);
						}

						if (f1 > 0.0F) {
							this.onEnchantmentCritical(p_attackTargetEntityWithCurrentItem_1_);
						}

						if (f >= 18.0F) {
							this.triggerAchievement(AchievementList.overkill);
						}

						this.setLastAttacker(p_attackTargetEntityWithCurrentItem_1_);
						if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase) {
							EnchantmentHelper.applyThornEnchantments((EntityLivingBase) p_attackTargetEntityWithCurrentItem_1_, (EntityPlayer) (Object) this);
						}

						EnchantmentHelper.applyArthropodEnchantments((EntityPlayer) (Object) this, p_attackTargetEntityWithCurrentItem_1_);
						ItemStack itemstack = this.getCurrentEquippedItem();
						Entity entity = p_attackTargetEntityWithCurrentItem_1_;
						if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityDragonPart) {
							IEntityMultiPart ientitymultipart = ((EntityDragonPart) p_attackTargetEntityWithCurrentItem_1_).entityDragonObj;
							if (ientitymultipart instanceof EntityLivingBase) {
								entity = (EntityLivingBase) ientitymultipart;
							}
						}

						if (itemstack != null && entity instanceof EntityLivingBase) {
							itemstack.hitEntity((EntityLivingBase) entity, (EntityPlayer) (Object) this);
							if (itemstack.stackSize <= 0) {
								this.destroyCurrentEquippedItem();
							}
						}

						if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase) {
							this.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
							if (j > 0) {
								p_attackTargetEntityWithCurrentItem_1_.setFire(j * 4);
							}
						}

						this.addExhaustion(0.3F);
					} else if (flag1) {
						p_attackTargetEntityWithCurrentItem_1_.extinguish();
					}
				}
			}
		}
	}


	@Shadow
	public abstract void destroyCurrentEquippedItem();

	@Shadow
	public abstract void addStat(StatBase stat, int amount);

	@Shadow
	public abstract void addExhaustion(float p_71020_1_);


}
