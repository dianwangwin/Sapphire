package today.Miscible.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;

@Mixin(value={Render.class})
public abstract class MixinRender {
    @Shadow
    protected abstract <T extends Entity> boolean bindEntityTexture(T entity);

   
}
