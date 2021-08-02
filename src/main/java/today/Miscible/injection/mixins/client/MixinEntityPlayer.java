package today.Miscible.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer extends MixinEntityLivingBase {

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
	protected int flyToggleTimer;

	@Shadow
	public PlayerCapabilities capabilities;

	@Shadow
	public abstract int getItemInUseDuration();

	@Shadow
	public abstract ItemStack getItemInUse();

	@Shadow
	public abstract boolean isUsingItem();

}
