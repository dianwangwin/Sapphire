package today.Miscible.gui.clickgui;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import today.Miscible.start.Mod.Category;
import today.Miscible.utils.fontManager.CFontRenderer;
import today.Miscible.utils.fontManager.FontLoaders;
import today.Miscible.utils.handler.MouseInputHandler;
import today.Miscible.utils.render.Colors;
import today.Miscible.utils.render.RenderUtil;

public class ClickMenuCategory {
	public Category c;
    ClickMenuMods uiMenuMods;
    private MouseInputHandler handler;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    public int x2;
    public int y2;
    public boolean drag = true;
    private double arrowAngle = 0.0;
    public double opacity = 0.0;

    public ClickMenuCategory(Category c, int x, int y, int width, int tab_height, MouseInputHandler handler) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.tab_height = tab_height;
        this.uiMenuMods = new ClickMenuMods(c, handler);
        this.handler = handler;
    }

    public void draw(int mouseX, int mouseY) {
        boolean hoverArrow;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        CFontRenderer font = FontLoaders.kiona16;
        this.opacity = this.opacity + 10.0 < 200.0 ? (this.opacity += 10.0) : 200.0;
        Color color = new Color(-2146365167);
        String name = "";
        name = this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length());
        RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.tab_height - 1,   new Color(color.getRed(), color.getGreen(), color.getBlue(), 220).getRGB());
        font.drawStringWithShadow(name, this.x + 5, this.y + (this.tab_height - font.getHeight()) / 2, Colors.WHITE.c);
        double xMid = this.x + this.width - 6;
        double yMid = this.y + 10;
        this.arrowAngle = RenderUtil.getAnimationState(this.arrowAngle, this.uiMenuMods.open ? 0 : 0, 1000.0);
        GL11.glPushMatrix();
        GL11.glTranslated((double)xMid, (double)yMid, (double)0.0);
        GL11.glRotated((double)this.arrowAngle, (double)0.0, (double)0.0, (double)1.0);
        GL11.glTranslated((double)(-xMid), (double)(-yMid), (double)0.0);
        boolean bl = hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        CFontRenderer openll = FontLoaders.kiona16;
        GL11.glPopMatrix();
        this.upateUIMenuMods();
        this.width = 100;
        this.uiMenuMods.draw(mouseX, mouseY);
        this.move(mouseX, mouseY);
    }

    private void move(int mouseX, int mouseY) {
        boolean hoverArrow;
        boolean bl = hoverArrow = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        if (!hoverArrow && this.isHovering(mouseX, mouseY) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }
        
        if (hoverArrow && this.handler.canExcecute2()) {
            boolean bl2 = this.uiMenuMods.open = !this.uiMenuMods.open;
        }
        /*if (hoverArrow && this.handler.canExcecute2() || hoverArrow && this.handler.canExcecute()) {
            boolean bl2 = this.uiMenuMods.open = !this.uiMenuMods.open;
        }*/
        if (!Mouse.isButtonDown((int)0)) {
            this.drag = false;
        }
        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.tab_height - 1) {
            return true;
        }
        return false;
    }

    private void upateUIMenuMods() {
        this.uiMenuMods.x = this.x;
        this.uiMenuMods.y = this.y;
        this.uiMenuMods.tab_height = this.tab_height;
        this.uiMenuMods.width = this.width;
    }

    public void mouseClick(int mouseX, int mouseY) {
        this.uiMenuMods.mouseClick(mouseX, mouseY);
    }

    public void mouseRelease(int mouseX, int mouseY) {
        this.uiMenuMods.mouseRelease(mouseX, mouseY);
    }
}

