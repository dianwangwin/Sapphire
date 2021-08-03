package today.Sapphire.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.gui.Gui;
import today.Sapphire.utils.overwriteUtil.ImplClass;

@Mixin({ Gui.class })
public class MixinGui {

	@Overwrite
	public static void drawRect(int x, int y, int xx, int yy,
			int color) {
		ImplClass.drawRect(x, y, xx, yy, color);
	}

}
