package today.Miscible.gui.clickgui.options;

import java.awt.Color;

import today.Miscible.start.Value;
import today.Miscible.utils.fontManager.CFontRenderer;
import today.Miscible.utils.fontManager.FontLoaders;
import today.Miscible.utils.handler.MouseInputHandler;
import today.Miscible.utils.render.RenderUtil;


public class UIToggleButton {
	private Value value;
    private MouseInputHandler handler;
    public int width;
    private int height;
    private int lastX;
    private float animationX = 2.14748365E9f;

    public UIToggleButton(Value value, MouseInputHandler handler, int width, int height) {
        this.value = value;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, int x, int y) {
        CFontRenderer font = FontLoaders.kiona16;
        int radius = 4;
        String strValue = this.value.getValueName().split("_")[1];
        boolean enabled = (Boolean)this.value.getValueState();
        int color = enabled ? new Color(0, 133, 242).getRGB() : new Color(100, 100, 100).getRGB();
        this.animate(x, mouseY, radius, enabled);
        this.toggle(mouseX, mouseY, x, y, radius);
        this.drawToggleButton(x, y, radius, color, enabled);
        font.drawString(strValue, (float)(x + 5) + 0.5f + 10.0F, (float)y + (float)(this.height - font.getHeight()) / 2.0f + 1.0f + 1.0F, -1);
        this.lastX = x;
    }

    private void drawToggleButton(int x, int y, int radius, int color, boolean enabled) {
        float xMid = x + this.width - radius * 2 - 3;
        float yMid = (float)y + (float)(this.height - radius) / 2.0f + 2.0f;
        RenderUtil.drawRect((float)(x + 5) + 0.5f - 2.0F, yMid + 0.5F - 2.0F, (float)(x + 5) + 0.5f + 6.0F, yMid + 0.5F + 6.0F, color);
    }

    private void animate(int x, int y, int radius, boolean enabled) {
        float xEnabled;
        float xMid = x + this.width - radius * 2 - 3;
        float yMid = (float)y + (float)(this.height - radius) / 2.0f - 3.0f;
        float f = xEnabled = !enabled ? xMid - (float)radius + 0.25f : xMid + (float)radius - 0.25f;
        if (this.lastX != x) {
            this.animationX = xEnabled;
        }
        this.animationX = this.animationX == 2.14748365E9f ? xEnabled : (float)RenderUtil.getAnimationState((double)this.animationX, (double)xEnabled, (double)50.0);
    }

    private void toggle(int mouseX, int mouseY, int x, int y, int radius) {
        if (this.isHovering(mouseX, mouseY, x, y, radius) && this.handler.canExcecute()) {
            this.value.setValueState((Object)((Boolean)this.value.getValueState() == false));
        }
    }

    public boolean isHovering(int mouseX, int mouseY, int x, int y, int radius) {
        float xMid = (float)x + (float)(this.width - radius) / 2.0f;
        float yMid = (float)y + (float)(this.height - radius) / 2.0f;
        /*if (mouseX >= x && mouseY >= y && mouseX <= x + this.width && mouseY < y + this.height) {
            return true;
        }*/
        if (mouseX >= (float)(x + 5) + 0.5f - 2.0F && mouseY >= yMid + 0.5F - 2.0F && mouseX <= (float)(x + 5) + 0.5f + 6.0F && mouseY < yMid + 0.5F + 6.0F) {
            return true;
        }
        return false;
    }
}

