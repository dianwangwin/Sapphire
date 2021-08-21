package today.Sapphire.newclickui.impl;

import today.Sapphire.newclickui.ClickUI;
import today.Sapphire.start.Value;
import today.Sapphire.utils.TranslateUtil;
import today.Sapphire.utils.fontManager.CFontRenderer;
import today.Sapphire.utils.fontManager.FontLoaders;
import today.Sapphire.utils.render.RenderUtil;

import java.awt.Color;


public class BoolValue {
	public Value values;
	
	public float length, x, y;
	
	public String name;
	
	public TranslateUtil anima = new TranslateUtil(0,0);
	
	public BoolValue(Value values) {
		this.values = values;
		this.name = values.getValueName().split("_")[1];
		anima.setXY((boolean) values.getValueState() ? 10 : 0, (boolean) values.getValueState() ? 0 : 255);
	}

	public void draw(float x, float y, float mouseX, float mouseY) {
		this.x = x;
		this.y = y;
			anima.interpolate((boolean) values.getValueState() ? 10 : 0, (boolean) values.getValueState() ? 255 : 0, 0.25f);
		length = 30;


		CFontRenderer font = FontLoaders.kiona18;
		CFontRenderer booleaninfo = FontLoaders.kiona11;


		font.drawString(name, x + 20, y + 5, new Color(255,255,255,255).getRGB());

		RenderUtil.circle(x + 220, y + 12, 4, new Color(70,70,70,255).getRGB());
		RenderUtil.circle(x + 210, y + 12, 4, new Color(70,70,70,255).getRGB());
		RenderUtil.drawRect(x + 210, y + 7.5f, x + 220, y + 16.5f, new Color(70,70,70,255).getRGB());

		RenderUtil.circle(x + 210 + anima.getX(), y + 12, 4, new Color(180,180,180,255).getRGB());
		RenderUtil.circle(x + 210, y + 12, 4, new Color(180,180,180,255).getRGB());
		RenderUtil.drawRect(x + 210, y + 7.5f, x + 210 + anima.getX(), y + 16.5f, new Color(180,180,180,255).getRGB());

		RenderUtil.circle(x + 210 + anima.getX(), y + 12, 5, new Color(255,255,255,255).getRGB());

		if(ClickUI.isHover(mouseX, mouseY, x + 205, y + 7, x + 225, y + 17)) {
			booleaninfo.drawString((boolean) (values.getValueState()) ? "On" : "Off", x + 207 + anima.getX(), y + 9, new Color(35, 35, 35).getRGB());
		}
	}
	
	public void handleMouse(float mouseX, float mouseY, int key) {
		if(ClickUI.isHover(mouseX, mouseY, x + 205, y + 7, x + 225, y + 17) && key == 0) {
			values.setValueState(!(boolean)(values.getValueState()));
		}
	}
	
	public float getLength() {
		return this.length;
	}
}
