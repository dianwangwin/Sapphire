package today.Sapphire.utils.overwriteUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ImplClass {
	
	// Gui.drawRect;
	public static void drawRect(int x, int y, int xx, int yy, int color) {
        int implX;
        if (x < xx) {
        	implX = x;
            x = xx;
            xx = implX;
        }

        if (y < yy) {
        	implX = y;
            y = yy;
            yy = implX;
        }

        float lvt_5_3_ = (float)(color >> 24 & 255) / 255.0F;
        float lvt_6_1_ = (float)(color >> 16 & 255) / 255.0F;
        float lvt_7_1_ = (float)(color >> 8 & 255) / 255.0F;
        float lvt_8_1_ = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer world = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(lvt_6_1_, lvt_7_1_, lvt_8_1_, lvt_5_3_);
        world.begin(7, DefaultVertexFormats.POSITION);
        world.pos((double)x, (double)yy, 0.0D).endVertex();
        world.pos((double)xx, (double)yy, 0.0D).endVertex();
        world.pos((double)xx, (double)y, 0.0D).endVertex();
        world.pos((double)x, (double)y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
	
	
}
