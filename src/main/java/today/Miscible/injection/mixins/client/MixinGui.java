package today.Miscible.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import today.Miscible.utils.overwriteUtil.ImplClass;

@Mixin({ Gui.class })
public class MixinGui {

	@Overwrite
	public static void drawRect(int x, int y, int xx, int yy,
			int color) {
		ImplClass.drawRect(x, y, xx, yy, color);
	}

}
