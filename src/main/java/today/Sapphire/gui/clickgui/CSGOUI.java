package today.Sapphire.gui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import today.Sapphire.Sapphire;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Mod.Category;
import today.Sapphire.start.Value;
import today.Sapphire.utils.fontManager.FontLoaders;
import today.Sapphire.utils.render.Colors;
import today.Sapphire.utils.render.RenderUtil;

public class CSGOUI extends GuiScreen {
	public Category category = Category.Combat;
	public Mod currectMod = null;
	ArrayList<Mod> modsInType = Lists.newArrayList();
	ArrayList<Value> valueInModule = Lists.newArrayList();
	int startX = 100, startY = 100;
	int valueWheel = 0, moduleWheel = 0;
	boolean mouse;
	boolean previousmouse = true;
	int moveX, moveY;
    boolean Click;
    
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		if (!Mouse.isButtonDown(0)) {
			this.Click = false;
		}
		//this.drawGradientRect(0, 0, this.width, this.height, -1, -804253680);
		if (isHoveringCoords(startX + 85, startY + 2, 300, 30, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			if (moveX == 0 && moveY == 0) {
				moveX = mouseX - startX;
				moveY = mouseY - startY;
			} else {
				startX = mouseX - moveX;
				startY = mouseY - moveY;
			}
			this.previousmouse = true;
		} else if (moveX != 0 || moveY != 0) {
			moveX = 0;
			moveY = 0;
		}
		// RenderingUtil.drawRect(startX, startY, startX + 300, startY + 200, new
		// Color(29, 29, 29).getRGB());
		RenderUtil.drawBorderedRect(startX + 40, startY + 30, startX + 70, startY + 160, 0.3f, Colors.BLACK.c,
				new Color(182, 182, 182).getRGB());
		if (this.category == Category.Combat) {
			RenderUtil.drawBorderedRect(startX + 37, startY + 35, startX + 39, startY + 55, 0.3f, Colors.BLACK.c,
					new Color(182, 182, 182).getRGB());
		} else if (this.category == Category.Movement) {
			RenderUtil.drawBorderedRect(startX + 37, startY + 60, startX + 39, startY + 80, 0.3f, Colors.BLACK.c,
					new Color(182, 182, 182).getRGB());
		} else if (this.category == Category.Render) {
			RenderUtil.drawBorderedRect(startX + 37, startY + 85, startX + 39, startY + 105, 0.3f, Colors.BLACK.c,
					new Color(182, 182, 182).getRGB());
		} else if (this.category == Category.Player) {
			RenderUtil.drawBorderedRect(startX + 37, startY + 110, startX + 39, startY + 130, 0.3f, Colors.BLACK.c,
					new Color(182, 182, 182).getRGB());
		} else if (this.category == Category.World) {
			RenderUtil.drawBorderedRect(startX + 37, startY + 135, startX + 39, startY + 155, 0.3f, Colors.BLACK.c,
					new Color(182, 182, 182).getRGB());
		}

		int wheel = Mouse.getDWheel();
		if (currectMod == null) {
			modsInType.clear();
			for (Mod m : Sapphire.instance.modUtil.modList) {
				if (!m.getCategory().name().equalsIgnoreCase(category.name())) {
					continue;
				}
				modsInType.add(m);
			}
			//System.out.println(modsInType.toString());
			if (wheel < 0 && moduleWheel < modsInType.size() - 4) {
				moduleWheel++;
			}
			if (wheel > 0 && moduleWheel > 0) {
				moduleWheel--;
			}
			RenderUtil.drawBorderedRect(startX + 80, startY + 20, startX + 280, startY + 180, 0.5f, Colors.BLACK.c, -1);
			FontLoaders.kiona16.drawString(category.toString(), (float) startX + 85, (float) startY + 23,
					new Color(20, 20, 20).getRGB());
			RenderUtil.drawBorderedRect(startX + 85, startY + 35, startX + 275, startY + 170, 0.5f,
					new Color(176, 176, 176).getRGB(), new Color(236, 236, 236).getRGB());
			int mY = startY + 40;
			for (int i = 0; i < modsInType.size();) {
				if (mY > startY + 160)
					break;
				if (i < moduleWheel) {
					for (int j = 0; j < 3 && i < modsInType.size(); j++, i++) {
					}
					continue;
				}
				int mX = startX + 90;
				for (int j = 0; j < 3 && i < modsInType.size(); j++, i++) {
					Mod m = modsInType.get(i);
					if (m.isEnabled()) {
						RenderUtil.drawBorderedRect(mX + 2, mY + 2, mX + 48, mY + 13, 0.3f, new Color(80, 80, 80).getRGB(),
								new Color(150, 150, 150).getRGB());
						FontLoaders.kiona16.drawString(m.getName(), (float) mX + 5,
								(float) mY + (15 / 2 - FontLoaders.kiona16.getHeight() / 2),
								new Color(253, 253, 253).getRGB());
					} else {
						RenderUtil.drawBorderedRect(mX + 2, mY + 2, mX + 48, mY + 13, 0.3f, new Color(158, 158, 158).getRGB(),
								new Color(217, 217, 217).getRGB());
						FontLoaders.kiona16.drawString(m.getName(), (float) mX + 5,
								(float) mY + (15 / 2 - FontLoaders.kiona16.getHeight() / 2),
								new Color(67, 67, 67).getRGB());
					}
					/*if (m.hasValues())
						RenderUtil.drawIcon(mX + 39, mY + 3, 9, 9,
								new ResourceLocation("Invalid/icons/CSGOUI/settingexpand.png"));*/
					mX += 60;
				}
				mY += 25;
			}
		} else {
			valueInModule.clear();
			for (Value v : Value.list) {
				if (v.getValueName().split("_")[0].equalsIgnoreCase(currectMod.getName())) {
					valueInModule.add(v);
				}
			}
			if (wheel < 0 && valueWheel < valueInModule.size() - 1) {
				valueWheel++;
			}
			if (wheel > 0 && valueWheel > 0) {
				valueWheel--;
			}
			RenderUtil.drawRect(startX + 80, startY + 20, startX + 280, startY + 180,
					new Color(255, 255, 255).getRGB());
			RenderUtil.drawRect(startX + 90, startY + 35, startX + 270, startY + 165,
					new Color(183, 183, 183).getRGB());
			FontLoaders.kiona16.drawString(currectMod.getName() + " Settings", (float) startX + 85,
					(float) startY + 22, new Color(20, 20, 20).getRGB());
			
			/*RenderUtil.drawIcon(startX + 85 + FontLoaders.kiona16.getStringWidth(currectMod.getName() + " Settings") + 2,
					startY + 23, 9, 9, new ResourceLocation("Invalid/icons/CSGOUI/settingexpand.png"));*/
			
			int mY = startY + 40;
			for (int i = 0; i < valueInModule.size(); i++) {
				Value v = valueInModule.get(i);
				if (mY > startY + 150)
					break;
				if (i < valueWheel) {
					continue;
				}
				//»­Value±êÌâ
				FontLoaders.kiona16.drawString(v.getDisplayTitle(), (float) startX + 92, (float) mY + 2,Colors.BLACK.c);
				if (v.isValueBoolean) {
					Gui.drawRect(startX + 180 + 59, mY + 2, startX + 180 + 75, mY + 7,new Color(150, 150, 150).getRGB());
					if ((boolean) v.getValueState()) {
						Gui.drawRect(startX + 180 + 67, mY + 2, startX + 180 + 75, mY + 7,new Color(255, 255, 255).getRGB());
					} else {
						Gui.drawRect(startX + 180 + 59, mY + 2, startX + 180 + 67, mY + 7,new Color(68, 68, 68).getRGB());
					}
				}
				if (v.isValueMode) {
					double modeWidth = FontLoaders.kiona16
							.getStringWidth(v.getModeAt(v.getCurrentMode()).toString());
					Gui.drawRect((int) (startX + 180 + 73 - modeWidth), mY + 2, startX + 180 + 77, mY + 12,
							Colors.BLACK.c);
					Gui.drawRect((int) (startX + 180 + 74 - modeWidth), mY + 3, startX + 180 + 76, mY + 11, -1);
					FontLoaders.kiona16.drawString(v.getModeAt(v.getCurrentMode()),
							(float) ((float) startX + 180 + 55 + 20 - modeWidth), (float) mY + 20, new Color(23, 23, 23).getRGB());
				}
				if (v.isValueDouble) {
					float x = startX + 180;
					double percSlider = ((double) v.getValueState() - (double) v.getValueMin())
							/ ((double) v.getValueMax() - (double) v.getValueMin());
					double val = (startX + 180) + 75 * percSlider;
					RenderUtil.drawRect((float) x, mY + 5, (float) ((double) x + 75), mY + 8.3f,
							(new Color(10, 10, 10)).getRGB());
					RenderUtil.drawRect((float) x, mY + 5, (float) (val), mY + 8, (new Color(255, 255, 255)).getRGB());
					double widthAC = FontLoaders.kiona16.getStringWidth(v.getValueState().toString());
					FontLoaders.kiona16.drawString(((double) v.getValueState()) + "", (float) ((float) x - widthAC - 2),
							(float) mY + 3, Colors.BLACK.c);
					float lastMouseX = -1.0f;
					final double valAbs = mouseX - (startX + 180);
					double perc = valAbs / 75;
					perc = Math.min(Math.max(0.0, perc), 1.0);
					final double valRel = ((double) v.getValueMax() - (double) v.getValueMin()) * perc;
					double valuu = (double) v.getValueMin() + valRel;
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
						this.mouse = false;
					}
					if (isHoveringCoords(startX + 180, mY, 75, 10, mouseX, mouseY)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0) && !Click) {
							this.previousmouse = true;
							this.mouse = true;
						}
						if (mouse) {
							lastMouseX = (Math.min(Math.max(startX + 180, mouseX), startX + 180 + 75) - (float) startX
									+ 180) / 75;
							valuu = Math.round(valuu * (1.0 / v.getSteps())) / (1.0 / v.getSteps());
							v.setValueState(valuu);
							Sapphire.instance.fileUtil.saveMods();
						} else {
							valuu = Math.round((double) v.getValueState() * (1.0 / v.getSteps()))
									/ (1.0 / v.getSteps());
							v.setValueState(valuu);
						}
					}
				}
				mY += 15;
			}
		}
		int mY = startY + 35;
		for (int i = 0; i < Category.values().length; i++) {
			Category type = Category.values()[i];
			/*RenderUtil.drawImage(new ResourceLocation("Invalid/icons/category/" + type.name().toLowerCase() + ".png"), startX + 45, mY,
					20, 20);*/
			mY += 25;
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (currectMod == null) {
			int mY = startY + 40;
			for (int i = 0; i < modsInType.size();) {
				if (mY > startY + 150)
					break;
				if (i < moduleWheel) {
					for (int j = 0; j < 3 && i < modsInType.size(); j++, i++) {
					}
					continue;
				}
				int mX = startX + 90;
				for (int j = 0; j < 3 && i < modsInType.size(); j++, i++) {
					Mod m = modsInType.get(i);
					if (isHoveringCoords(mX , mY + (15 / 2 - FontLoaders.kiona16.getHeight() / 2), 40,
							FontLoaders.kiona16.getHeight(), mouseX, mouseY) && mouseButton == 0) {
						m.set(!m.isEnabled());
					}
					if (m.hasValues())
						if (isHoveringCoords(mX + 39, mY + 3, 9, 9, mouseX, mouseY) && (!Click && mouseButton != 0)) {
							currectMod = m;
							valueWheel = 0;
							Click = true;
							return;
						}
					mX += 60;
				}
				mY += 25;
			}
		} else {
			int mY = startY + 40;
			for (int i = 0; i < valueInModule.size(); i++) {
				Value v = valueInModule.get(i);
				if (mY > startY + 150)
					break;
				if (i < valueWheel) {
					continue;
				}
				if (v.isValueBoolean) {
					if (isHoveringCoords(startX + 180 + 56, mY, 20, 15, mouseX, mouseY)) {
						v.setValueState(!(boolean) v.getValueState());
					}
				}
				if (v.isValueMode) {
					double modeWidth = FontLoaders.kiona16
							.getStringWidth(v.getModeAt(v.getCurrentMode()).toString());
					if (isHoveringCoords((float) (startX + 180 + 71 - modeWidth), mY, 32, 15, mouseX, mouseY)) {
						if (v.getCurrentMode() < v.mode.size() - 1) {
							v.setCurrentMode(v.getCurrentMode() + 1);
						} else {
							v.setCurrentMode(0);
						}
					}
				}
				mY += 15;
			}
		}
		int mY = startY + 35;
		for (int i = 0; i < Category.values().length; i++) {
			if (isHoveringCoords(startX + 45, mY, 20, 20, mouseX, mouseY)) {
				Category type = Category.values()[i];
				category = type;
			}
			mY += 25;
		}
	}

	public void initGui() {
	/*	if (this.mc.theWorld != null && !this.mc.gameSettings.ofFastRender) {
			this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		}*/
		super.initGui();
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 && currectMod != null) {
			currectMod = null;
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {

	}

	public boolean isHoveringCoords(float x, float y, float width, float height, int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height - 0.5f;
	}

	public void onGuiClosed() {
	//	this.mc.entityRenderer.stopUseShader();
	}
}