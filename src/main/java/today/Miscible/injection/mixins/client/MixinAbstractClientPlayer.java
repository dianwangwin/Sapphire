package today.Miscible.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin({AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer {

	
}
