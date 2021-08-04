package today.Sapphire.gui.clickgui.options;

import java.awt.Color;

import net.minecraft.client.gui.Gui;
import today.Sapphire.utils.fontManager.CFontRenderer;
import today.Sapphire.utils.fontManager.FontLoaders;
import today.Sapphire.utils.render.RenderUtil;

public class UISlider {
	private int height;
    public int width;
    public boolean drag;
    private Color color;
    private float lastMouseX;
    private int x2;
    private int y2;
    private String name;
    private double min;
    private double max;
    private double step;
    
    public UISlider(final String valueName, final double min, final double max, final double step) {
        this.height = 1;
        this.width = 100;
        this.lastMouseX = -1.0f;
        this.name = valueName;
        this.min = min;
        this.max = max;
        this.step = step;
        this.lastMouseX = -1.0f;
    }
    
    public UISlider(final String valueName, final double min, final double max, final double step, final int width) {
        this.height = 1;
        this.width = 100;
        this.lastMouseX = -1.0f;
        this.name = valueName;
        this.width = width;
        this.min = min;
        this.max = max;
        this.step = step;
        this.lastMouseX = -1.0f;
    }
    
    public double draw(final float value, final int mouseX, final int mouseY, final int x, final int y) {
        this.height = 2;
        final CFontRenderer font = FontLoaders.kiona16;
        final String strValue = String.valueOf(String.valueOf(this.name));
        final float strWidth = (float)font.getStringWidth(this.name);
        final float strHeight = (float)font.getStringHeight(this.name);
        final float vWidth = (float)font.getStringWidth(strValue);
        final float vheight = (float)font.getStringHeight(strValue);
        //Gui.drawRect(x, y, x + this.width, y + height, new Color(100, 100, 100).getRGB());
        //font.drawString(strValue, x + this.width / 2 - vWidth / 2.0f, (float)(y + this.height + 2), -1);
        Gui.drawRect(x, y + font.getHeight(), x + this.width, y + height + font.getHeight(), new Color(100, 100, 100).getRGB());
        font.drawString(strValue, x, (float)(y + this.height - 4), -1);
        font.drawStringWithShadow("" + value, x + this.width - font.getStringWidth("" + value), (float)(y + this.height - 4), -1);
        //font.drawString(string, x, mouseY, color)
        this.x2 = x;
        this.y2 = y;
        return this.changeValue(value, mouseX, mouseY, x, y);
    }
    
    public boolean mouseClick(final int mouseX, final int mouseY) {
        if (this.isHovering(mouseX, mouseY)) {
            this.drag = true;
        }
        return this.drag;
    }
    
    public void mouseRelease() {
        this.drag = false;
    }
    
    private double changeValue(final float value, final int mouseX, final int mouseY, final int x, final int y) {
    	final CFontRenderer font = FontLoaders.kiona16;
        final double valAbs = mouseX - x;
        double perc = valAbs / this.width;
        perc = Math.min(Math.max(0.0, perc), 1.0);
        final double valRel = (this.max - this.min) * perc;
        double valuu = this.min + valRel;
        final double percSlider = (value - this.min) / (this.max - this.min);
        final double val = x + this.width * percSlider;
        RenderUtil.drawRect((float)x, (float)y + font.getHeight(), (this.lastMouseX == -1.0f) ? ((float)(int)val) : (x + this.width * this.lastMouseX), (float)(y + this.height) + font.getHeight(), new Color(19, 120, 190).getRGB());
        //Gui.circle((float)val, y + 1, 2f, Colors.BLACK.c);
        RenderUtil.drawRect((float)val - 2, y - 2 + font.getHeight(),(float) val + 3, y + 3 + font.getHeight(), new Color(0, 133, 242).getRGB());
        if (this.drag) {
            this.lastMouseX = (Math.min(Math.max(x, mouseX), x + this.width) - (float)x) / this.width;
            valuu = Math.round(valuu * (1.0 / this.step)) / (1.0 / this.step);
            return valuu;
        }
        return Math.round(value * (1.0 / this.step)) / (1.0 / this.step);
    }
    
    public boolean isHovering(final int mouseX, final int mouseY) {
    	final CFontRenderer font = FontLoaders.kiona16;
        return mouseX >= this.x2 && mouseY >= this.y2 + font.getHeight() && mouseX <= this.x2 + this.width && mouseY < this.y2 + this.height + font.getHeight();
    }
}

