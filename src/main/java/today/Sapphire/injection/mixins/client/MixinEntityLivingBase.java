package today.Sapphire.injection.mixins.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

@Mixin({EntityLivingBase.class})
public abstract class MixinEntityLivingBase extends MixinEntity {

	@Shadow
	public void onLivingUpdate() {
	}

	@Shadow
	public abstract boolean isOnLadder();

	@Shadow
	public abstract IAttributeInstance getEntityAttribute(IAttribute p_getEntityAttribute_1_);

	@Shadow
	protected abstract void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos);

	@Shadow
	public abstract boolean isPotionActive(Potion potionIn);

	@Shadow
	public abstract PotionEffect getActivePotionEffect(Potion potionIn);

	@Shadow
	public abstract void setSprinting(boolean p_setSprinting_1_);

	@Shadow
	public abstract void setLastAttacker(Entity p_setLastAttacker_1_);


}
