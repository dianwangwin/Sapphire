package today.Sapphire.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import today.Sapphire.start.ModManager;
import today.Sapphire.start.render.NameTag2D;

@Mixin(value={RendererLivingEntity.class})
public abstract class MixinRendererLivingEntity  extends MixinRender{

	@Inject(method = "canRenderName", at = @At("HEAD"), cancellable = true)
	private <T extends EntityLivingBase> void canRenderName(T entity,
			CallbackInfoReturnable callbackInfoReturnable) {
		NameTag2D nameTag2D = (NameTag2D) ModManager.getModuleForClass(NameTag2D.class);
		if (nameTag2D.isEnabled())
			callbackInfoReturnable.cancel();
			
	}
}
