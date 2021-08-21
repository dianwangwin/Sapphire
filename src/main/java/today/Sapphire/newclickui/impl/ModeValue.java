package today.Sapphire.newclickui.impl;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import today.Sapphire.newclickui.ClickUI;
import today.Sapphire.start.Value;
import today.Sapphire.utils.TranslateUtil;
import today.Sapphire.utils.fontManager.CFontRenderer;
import today.Sapphire.utils.fontManager.FontLoaders;
import today.Sapphire.utils.render.RenderUtil;

public class ModeValue {
	public Value values;
	
	public float length, x, y;
	
	public String name;
	
	public TranslateUtil anima = new TranslateUtil(0,0);
	
	public boolean isOpen = false;
	
	public ModeValue(Value values) {
		this.values = values;
		this.name = values.getModeTitle();
		this.anima.setXY(18, 0);
		this.isOpen = false;
	}
	
	public void draw(float x, float y, float mouseX, float mouseY) {
		this.x = x;
		this.y = y;
		anima.interpolate(isOpen ? values.mode.size() * 18 : 18, 0, 0.2f);
		length = 30 + anima.getX() - 18;
		CFontRenderer font = FontLoaders.kiona18;

		font.drawString(name, x + 20, y + 6, new Color(255,255,255,255).getRGB());


		RenderUtil.drawRect(x + 130, y + 2, x + 220, y + anima.getX() + 3, new Color(35,35,35,255).getRGB());
		//RenderUtil.drawRoundRect(x + 131, y + 3, x + 219, y + anima.getX() + 2, 2, new Color(50,50,50,255).getRGB());
		if(this.isOpen || anima.getX() != 18) {
			RenderUtil.drawRect(x + 130, y + 20, x + 220, y + 21,  new Color(17, 17, 17, 255).getRGB());

			//RenderUtil.drawRect(x + 131, y + anima.getX() + 3, x + 219, y + values.mode.size() * 18 + 3, new Color(35,35,35,255).getRGB());

		}
		int i = 0;
		float modeY = 18;
		while (i < values.mode.size()) {
			if((String)values.getModeAt(values.getCurrentMode()) != (String)values.mode.get(i)) {
				font.drawCenteredString((String)values.mode.get(i), x + 130 + (220 - 130) / 2, y + modeY + 6, new Color(255,255,255,255).getRGB());
				if(ClickUI.isHover(mouseX, mouseY, x + 131, y + modeY + 4, x + 219, y + modeY + 22)) {
					RenderUtil.drawCircle(x + 140, y + modeY + 12 , 3.5f , new Color(111, 255, 111, 150));
				} else {
					RenderUtil.drawCircle(x + 140, y + modeY + 12 , 3 , new Color(255, 255, 255, 255));
				}

				modeY += 18.0f;
			}
			++i;
		}

		RenderUtil.drawRect(x + 131, y + anima.getX() + 3, x + 219, y + values.mode.size() * 18 + 3, new Color(50,50,50,255).getRGB());
		font.drawCenteredString((String)values.getModeAt(values.getCurrentMode()), x + 130 + (220 - 130) / 2, y + 6, new Color(255,255,255,255).getRGB());

	}
	
	public void handleMouse(float mouseX, float mouseY, int key) {
		if(ClickUI.isHover(mouseX, mouseY, x + 131, y + 3, x + 219, y + anima.getX() + 2) && ClickUI.isHover(mouseX, mouseY, x, y, x + 240, y + 235) && key == 0) {
			int i = 0;
			float modeY = 18;
			while (i < values.mode.size()) {
				if((String)values.getModeAt(values.getCurrentMode()) != (String)values.mode.get(i)) {
					if(ClickUI.isHover(mouseX, mouseY, x + 131, y + modeY + 4, x + 219, y + modeY + 22)) {
						values.setCurrentMode(i);
					}
					modeY += 18.0f;
				}
				++i;
			}
			isOpen = !isOpen;
		}
	}
	
	public float getLength() {
		return this.length;
	}
}
