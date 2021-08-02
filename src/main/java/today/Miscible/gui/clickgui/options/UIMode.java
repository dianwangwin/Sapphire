package today.Miscible.gui.clickgui.options;

import net.minecraft.client.gui.Gui;
import today.Miscible.start.Value;
import today.Miscible.utils.fontManager.CFontRenderer;
import today.Miscible.utils.fontManager.FontLoaders;
import today.Miscible.utils.handler.MouseInputHandler;
import today.Miscible.utils.render.ClientUtil;
import today.Miscible.utils.render.Colors;

public class UIMode {
	private int height;
    public int width;
    private Value value;
    private MouseInputHandler handler;

    public UIMode(Value value, MouseInputHandler handler, int width, int height) {
        this.value = value;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, int x, int y) {
        this.setNextMode(mouseX, mouseY, x, y);
        CFontRenderer font = FontLoaders.kiona16;
        String displayText = String.valueOf(this.value.getModeAt(this.value.getCurrentMode()));
        String modeCountText = "<" + String.valueOf((int)(this.value.getCurrentMode() + 1)) + "/" + this.value.mode.size() + ">";
        /*if (this.isHovering(mouseX, mouseY, x, y)) {
            Gui.drawRect((int)x, (int)y, (int)(x + this.width), (int)(y + this.height), (int)ClientUtil.reAlpha((int)Colors.BLACK.c, (float)0.35f));
        }*/
        Gui.drawRect((int)x + 4, (int)y + 5, (int)(x + this.width - 4), (int)(y + this.height), (int)ClientUtil.reAlpha((int)Colors.BLACK.c, (float)0.7f));
        font.drawStringWithShadow(value.getModeTitle(), x + 4, y - 0.5F, -1);
        font.drawString(displayText, (float)x + (float)(this.width - font.getStringWidth(displayText)) / 2.0f - font.getStringWidth(modeCountText) / 4 - 3, (float)y + (float)(this.height - font.getHeight()) / 2.0f + 1.5F, -1);
        font.drawString(modeCountText, (float)(x + this.width - font.getStringWidth(modeCountText) - 4 - 3), (float)y + (float)(this.height - font.getHeight()) / 2.0f + 1.5F, -1);
    }

    private void setNextMode(int mouseX, int mouseY, int x, int y) {
        if (this.isHovering(mouseX, mouseY, x, y) && this.handler.canExcecute()) {
            if (this.value.getCurrentMode() < this.value.mode.size() - 1) {
                this.value.setCurrentMode(this.value.getCurrentMode() + 1);
            } else {
                this.value.setCurrentMode(0);
            }
        }
    }

    public boolean isHovering(int mouseX, int mouseY, int x, int y) {
        if (mouseX >= x + 4 && mouseY >= y + 5 && mouseX <= x + this.width - 4 && mouseY < y + this.height + 1) {
            return true;
        }
        return false;
    }
}

