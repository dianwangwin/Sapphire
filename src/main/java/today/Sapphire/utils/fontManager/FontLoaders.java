package today.Sapphire.utils.fontManager;


import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class FontLoaders {
	public static final CFontRenderer kiona16 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/arial.ttf"), 16), true, true);
    public static CFontRenderer kiona = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/arial.ttf"), 19), true, true);
    public static final CFontRenderer GUI44 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/GUI.ttf"), 44), true, true);


    public static Font fontFromTTF(ResourceLocation fontLocation, float fontSize) {
        Font output = null;
        try {
            output = Font.createFont(Font.PLAIN,
                    Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
