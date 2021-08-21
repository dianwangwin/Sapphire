package today.Sapphire.start.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.StringUtils;
import today.Sapphire.Sapphire;
import today.Sapphire.events.EventRender2D;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;
import today.Sapphire.start.Value;
import today.Sapphire.utils.fontManager.CFontRenderer;
import today.Sapphire.utils.fontManager.FontLoaders;
import today.Sapphire.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Hud extends Mod {

	public final Value<Boolean> waterMark;
	public final Value<String> mode;
	public final Value<String> animationmode;
	public final Value<Boolean> blackRect;
	public final Value<Double> R;
	public final Value<Double> G;
	public final Value<Double> B;

	private float time;

	public Hud() {
		super("Hud", Category.Render);
		animationmode = new Value("Hud", "Animationmode", new String[]{"Slide", "streamlined"}, 0);
		mode = new Value("Hud", "mode", new String[]{"Custom", "CustomRainbow"}, 0);
		blackRect = new Value<Boolean>("Hud", "BlackRect", true);
		R = new Value("Hud", "R", 90.0, 1.0, 255.0, 1.0);
		G = new Value("Hud", "G", 150.0, 1.0, 255.0, 1.0);
		B = new Value("Hud", "B", 255.0, 1.0, 255.0, 1.0);
		waterMark = new Value<Boolean>("Hud", "WaterMark", true);
	}

	public static Color fade(Color color, int index, int count) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		float brightness = Math.abs(
			((float) (System.currentTimeMillis() % 2000L) / 1000.0f + (float) index / (float) count * 2.0f) % 2.0f
				- 1.0f);
		brightness = 0.5f + 0.5f * brightness;
		hsb[2] = brightness % 2.0f;
		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}

	@EventTarget
	private void mainHud(EventRender2D event) {
		CFontRenderer font = FontLoaders.kiona18;
		ScaledResolution res = new ScaledResolution(mc);

		if (waterMark.getValueState()) {
			font.drawStringWithShadow(Sapphire.NAME.substring(0, 1), 3, 5, fade(
				new Color(R.getValueState().intValue(), G.getValueState().intValue(), B.getValueState().intValue()), 1, 1).brighter().getRGB());
			font.drawStringWithShadow(Sapphire.NAME.substring(1) + " " + Sapphire.VERSION, font.getStringWidth(Sapphire.NAME.substring(0, 1)) + 3, 5, Color.WHITE.getRGB());
		}
		ModManager.sortedModList.sort(new Comparator<Mod>() {
			@Override
			public int compare(Mod m1, Mod m2) {
				return font.getStringWidth(String.valueOf(m2.getName()) + m2.getDisplayName())
					- font.getStringWidth(String.valueOf(m1.getName()) + m1.getDisplayName());
			}
		});

		ArrayList listToUse = ModManager.sortedModList;
		Iterator var7 = listToUse.iterator();
		int countMod = 0;
		float y = 2;

		while (var7.hasNext()) {
			Mod m = (Mod) var7.next();
			String name = StringUtils.remove(
				m.getDisplayName().isEmpty() ? m.getName() : String.format("%s%s", m.getName(), m.getDisplayName()),
				"Hud");

			switch (this.animationmode.getModeName()) {
				case "Slide": {
					time = 200f;
					break;
				}
				case "streamlined": {
					time = Math.max(50.0F, Math.abs(m.posX - 0.0F) * 10);
					break;
				}
			}

			if (m.isEnabled()) {
				if (m.posX > 0.0F) {
					m.posX -= RenderUtil.delta * (time + 50);
				}

				if (m.posX < 0.0F) {
					m.posX = 0.0F;
				}
			} else {
				double max = (double) (font.getStringWidth(name) + 5);
				if ((double) m.posX < max) {
					m.posX += RenderUtil.delta * time;
				}

				if ((double) m.posX > max) {
					m.posX = (float) max;
				}
			}

			Color rianbows = fade(
				new Color(R.getValueState().intValue(), G.getValueState().intValue(), B.getValueState().intValue()),
				ModManager.sortedModList.indexOf(m),
				font.getStringHeight(name) + 5);

			if (m.posX < font.getStringWidth(name)) {
				float X = res.getScaledWidth() - font.getStringWidth(name) - 8 + m.posX;
				float mY = font.getHeight() + 3.5f;

				GlStateManager.pushMatrix();
				GlStateManager.translate(X, y, 0);

				if (blackRect.getValueState()) {
					RenderUtil.drawRect(3, 0, res.getScaledWidth(), mY, new Color(0, 0, 0, 180).getRGB());
					RenderUtil.drawRect(font.getStringWidth(name) + 7, 0, res.getScaledWidth(), mY, rianbows.getRGB());
				}

				GlStateManager.resetColor();
				font.drawStringWithShadow(name, 5, 1.5, rianbows.getRGB());
				GlStateManager.popMatrix();

				y += mY;
			}

			countMod++;
		}
	}
}
