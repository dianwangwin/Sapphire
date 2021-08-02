package today.Miscible.gui.clickgui.buttons;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import today.Miscible.utils.fontManager.CFontRenderer;
import today.Miscible.utils.fontManager.FontLoaders;
import today.Miscible.utils.handler.MouseInputHandler;
import today.Miscible.utils.render.ClientUtil;
import today.Miscible.utils.render.Colors;
import today.Miscible.utils.render.FlatColors;
import today.Miscible.utils.render.RenderUtil;


public class UIPopUPChooseButton {
    public float x;
    public float y;
    public float currentRadius;
    public float minRadius;
    public float maxRadius;
    private boolean open;
    private boolean animateUp;
    private boolean animateDown;
    private MouseInputHandler mouseClickedPopUpMenu = new MouseInputHandler(0);
    private double animationScale;
    public String textureLocation;
    public String name;

    public UIPopUPChooseButton(String name, int x2, int y2, String textureLocation) {
        this.name = name;
        this.maxRadius = 15.0f;
        this.textureLocation = textureLocation;
        this.x = x2;
        this.y = y2;
    }

    public void draw(int mouseX, int mouseY) {
        this.maxRadius = 12.0f;
        float add2 = RenderUtil.delta * 100.0f;
        float f2 = this.currentRadius + add2 < this.maxRadius ? (this.currentRadius = this.currentRadius + add2) : this.maxRadius;
        this.currentRadius = f2;
        this.animationScale = RenderUtil.getAnimationState(this.animationScale, this.isHovering(mouseX, mouseY) ? 1.05 : 1.0, 1.0);
        if (this.animationScale < 1.0) {
            this.animationScale = 1.0;
        }
        float xMid = this.x + this.maxRadius / 2.0f;
        float yMid = this.y + this.maxRadius / 2.0f;
        GL11.glPushMatrix();
        GL11.glTranslated(xMid, yMid, 0.0);
        if (this.isHovering(mouseX, mouseY)) {
            GL11.glScaled(this.animationScale, this.animationScale, 0.0);
        }
        GL11.glTranslated(- xMid, - yMid, 0.0);
        if (this.currentRadius > 1.0f) {
            RenderUtil.circle(this.x, this.y, this.currentRadius, FlatColors.ASPHALT.c);
            if (this.isHovering(mouseX, mouseY)) {
                RenderUtil.drawFilledCircle(this.x, this.y, this.currentRadius + 0.5f, ClientUtil.reAlpha(Colors.BLACK.c, 0.1f));
            }
            RenderUtil.drawImage(new ResourceLocation(this.textureLocation), (int)(this.x - this.currentRadius / 2.0f), (int)(this.y - this.currentRadius / 2.0f), (int)this.currentRadius, (int)this.currentRadius);
        }
        GL11.glPopMatrix();
        if (this.currentRadius == this.maxRadius && this.isHovering(mouseX, mouseY)) {
            CFontRenderer font = FontLoaders.kiona16;
            int xFont = (int)(this.x + this.maxRadius + 5.0f);
            int yFont = (int)(this.y - this.maxRadius / 4.0f - 1.0f);
            RenderUtil.drawRoundedRect(xFont - 2, yFont - 1, xFont + font.getStringWidth(this.name) + 2, yFont + font.getHeight() + 1, 1.0f, FlatColors.ASPHALT.c);
            font.drawString(this.name, xFont, yFont, FlatColors.GREY.c);
        }
    }

    public boolean clicked(int mouseX, int mouseY) {
        if (this.currentRadius == this.maxRadius && this.mouseClickedPopUpMenu.canExcecute() && this.isHovering(mouseX, mouseY)) {
            return true;
        }
        return false;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if ((float)mouseX >= this.x - this.currentRadius && (float)mouseX <= this.x + this.currentRadius && (float)mouseY >= this.y - this.currentRadius && (float)mouseY <= this.y + this.currentRadius) {
            return true;
        }
        return false;
    }

    public void setY(float y2) {
        this.y = y2;
    }
}

