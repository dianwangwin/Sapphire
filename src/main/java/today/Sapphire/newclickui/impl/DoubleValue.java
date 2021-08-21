package today.Sapphire.newclickui.impl;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import today.Sapphire.newclickui.ClickUI;
import today.Sapphire.start.Value;
import today.Sapphire.utils.TranslateUtil;
import today.Sapphire.utils.fontManager.CFontRenderer;
import today.Sapphire.utils.fontManager.FontLoaders;
import today.Sapphire.utils.render.RenderUtil;

public class DoubleValue {
	public Value values;
	
	public float length, x, y;
	
	public String name;
	
	public boolean premouse;
	
	public TranslateUtil anima = new TranslateUtil(0,0);
	
	public DoubleValue(Value values) {
		this.values = values;
		this.name = values.getValueName().split("_")[1];
	}
	
	public void draw(float x, float y, float mouseX, float mouseY) {
		this.x = x;
		this.y = y;
		length = 30;
		CFontRenderer font = FontLoaders.kiona18;
		font.drawString(name, x + 20, y + 5, new Color(255,255,255,255).getRGB());
		double inc = values.getSteps();
		double max = (double) values.getValueMax();
		double min = (double) values.getValueMin();
		double valn = (double) values.getValueState();
		int longValue = 220 - 130;
		anima.interpolate((float) (longValue * (valn - min) / (max - min)), 0, 0.4f);
		RenderUtil.drawRect(x + 130, y + 11, x + 220, y + 13 ,  new Color(70,70,70,255).getRGB());
		RenderUtil.drawRect(x + 130, y + 11, x + 130 + anima.getX(), y + 13 ,  new Color(255,255,255,255).getRGB());
//		RenderUtil.drawRect(x + 130, y + 11, x + 220, y + 13, Colors.GREY.c);
//		RenderUtil.drawRect(x + 130, y + 11, x + 130 + anima.getX(), y + 13, ClickUI.color);
		RenderUtil.circle((float) (x + 130 + anima.getX()), y + 12, 2, new Color(255,255,255,255).getRGB());

		if(ClickUI.isHover(mouseX, mouseY, x + 120, y + 2, x + 230, y + length - 2))
			font.drawCenteredString((double)values.getValueState() + "", (float) (x + 125 + longValue * (valn - min) / (max - min)), y - 2, new Color(255,255,255,255).getRGB());

	}
	
	public void handleMouseinRender(float mouseX, float mouseY, int key) {
		if(ClickUI.isHover(mouseX, mouseY, x + 120, y + 2, x + 230, y + length - 2)) {
			if (Mouse.isButtonDown(0) && premouse) {
				double inc = values.getSteps();
				double max = (double) values.getValueMax();
				double min = (double) values.getValueMin();
				double valn = (double) values.getValueState();
				int longValue = 220 - 130;
				double valAbs = mouseX - (x + 130);
				double perc = valAbs / ((longValue) * Math.max(Math.min(valn / max, 0), 1));
				perc = Math.min(Math.max(0, perc), 1);
				double valRel = (max - min) * perc;
				double val = min + valRel;
				val = Math.round(val * (1 / inc)) / (1 / inc);
				values.setValueState(val);
			}
		}
	}
	
	public void handleMouse(float mouseX, float mouseY, int key) {
		if(ClickUI.isHover(mouseX, mouseY, x + 120, y + 2, x + 230, y + length - 2)) {
			if (key == 0) {
				premouse = true;
			}
		}else {
			premouse = false;
		}
	}
	
	public float getLength() {
		return this.length;
	}
}
