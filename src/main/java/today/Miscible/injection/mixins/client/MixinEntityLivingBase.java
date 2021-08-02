package today.Miscible.injection.mixins.client;

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
	protected abstract void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos);

	@Shadow
	public abstract boolean isPotionActive(Potion potionIn);

	@Shadow
	public abstract PotionEffect getActivePotionEffect(Potion potionIn);
	
}
