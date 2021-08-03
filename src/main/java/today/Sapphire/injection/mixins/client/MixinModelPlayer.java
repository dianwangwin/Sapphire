package today.Sapphire.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;

@Mixin(ModelPlayer.class)
public abstract class MixinModelPlayer extends ModelBiped  {

}
