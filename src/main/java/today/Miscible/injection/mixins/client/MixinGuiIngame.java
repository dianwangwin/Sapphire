package today.Miscible.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import today.Miscible.events.EventRender2D;

@Mixin({GuiIngame.class})
public class MixinGuiIngame {

	@Inject(method = "renderTooltip", at = @At("RETURN"))
	private void render2D(ScaledResolution p_175264_1_, float p_175264_2_, CallbackInfo callbackInfo) {
		EventRender2D event = new EventRender2D(p_175264_2_);
		EventManager.call(event);
	}

}
