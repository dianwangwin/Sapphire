package today.Sapphire.utils.fontManager;


import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class FontLoaders {

    public static CFontRenderer micon15 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/micon.ttf"), 15), true, true);

    public static CFontRenderer robote18 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/Roboto-Medium.ttf"), 18), true, true);



    public static CFontRenderer kiona11 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/Roboto-Regular.ttf"), 11), true, true);
    public static CFontRenderer kiona16 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/Roboto-Regular.ttf"), 16), true, true);
    public static CFontRenderer kiona17 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/Roboto-Regular.ttf"), 17), true, true);
    public static CFontRenderer kiona18 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/Roboto-Regular.ttf"), 18), true, true);
    public static CFontRenderer kiona20 = new CFontRenderer(fontFromTTF(new ResourceLocation("Font/Roboto-Regular.ttf"), 20), true, true);

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
