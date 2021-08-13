package today.Sapphire.start.render;

import com.darkmagician6.eventapi.EventTarget;
import com.ibm.icu.text.NumberFormat;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import today.Sapphire.events.EventRender;
import today.Sapphire.events.EventRender2D;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;
import today.Sapphire.utils.render.Colors;
import today.Sapphire.utils.render.RenderUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ESP2D extends Mod {

	public static Value<String> mode = new Value
		("ESP2D", "Mode", new String[]{"Box", "Corner"}, 0);

	public Value<Boolean> player = new Value
		("ESP2D", "Player", true);

	public Value<Boolean> invis = new Value
		("ESP2D", "Invisibles", true);

	public Value<Boolean> ARMOR = new Value
		("ESP2D", "Armor", false);

	public Value<Boolean> HEALTH = new Value
		("ESP2D", "Health", false);
	FontRenderer fr = mc.fontRendererObj;
	private Map<EntityLivingBase, double[]> entityConvertedPointsMap = new HashMap<EntityLivingBase, double[]>();

	public ESP2D() {
		super("ESP2D", Category.Render);
		setDisplayName(mode.getModeName());
	}

	public static Color blendColors(float[] fractions, Color[] colors, float progress) {
		Color color = null;
		if (fractions != null) {
			if (colors != null) {
				if (fractions.length == colors.length) {
					int[] indicies = getFractionIndicies(fractions, progress);
					float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
					Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
					float max = range[1] - range[0];
					float value = progress - range[0];
					float weight = value / max;
					color = blend(colorRange[0], colorRange[1], (double) (1.0F - weight));
					return color;
				} else {
					throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
				}
			} else {
				throw new IllegalArgumentException("Colours can't be null");
			}
		} else {
			throw new IllegalArgumentException("Fractions can't be null");
		}
	}

	public static Color blend(Color color1, Color

		color2, double ratio) {
		float r = (float) ratio;
		float ir = 1.0f - r;
		float[] rgb1 = new float[3];
		float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		float red = rgb1[0] * r + rgb2[0] *

			ir;
		float green = rgb1[1] * r + rgb2[1] *

			ir;
		float blue = rgb1[2] * r + rgb2[2] *

			ir;
		if (red < 0.0f) {
			red = 0.0f;
		} else if (red > 255.0f) {
			red = 255.0f;
		}
		if (green < 0.0f) {
			green = 0.0f;
		} else if (green > 255.0f) {
			green = 255.0f;
		}
		if (blue < 0.0f) {
			blue = 0.0f;
		} else if (blue > 255.0f) {
			blue = 255.0f;
		}
		Color color = null;
		try {
			color = new Color(red, green,

				blue);
		} catch (IllegalArgumentException exp) {
			NumberFormat nf =

				NumberFormat.getNumberInstance();
			System.out.println(nf.format(red)

				+ "; " + nf.format(green) + "; " + nf.format(blue));
			exp.printStackTrace();
		}
		return color;
	}

	public static int[] getFractionIndicies

		(float[] fractions, float progress) {
		int startPoint;
		int[] range = new int[2];
		for (startPoint = 0; startPoint <

			fractions.length && fractions[startPoint] <= progress;

		     ++startPoint) {
		}
		if (startPoint >= fractions.length) {
			startPoint = fractions.length - 1;
		}
		range[0] = startPoint - 1;
		range[1] = startPoint;
		return range;
	}

	public static double getIncremental(double val, double inc) {
		double one = 1.0 / inc;
		return (double) Math.round(val * one) / one;
	}

	@EventTarget
	public void onRender(EventRender event) {
		try {
			this.updatePositions();
		} catch (Exception exception) {
		}
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (mode.isCurrentMode("Box")) {
			this.setDisplayName("Box");
		}
		if (mode.isCurrentMode("Corner")) {
			this.setDisplayName("Corner");
		}
		boolean hovering;
		GlStateManager.pushMatrix();
		for (Entity entity :
			this.entityConvertedPointsMap.keySet()) {
			boolean shouldRender;
			EntityPlayer ent =

				(EntityPlayer) entity;

			double[] renderPositions =

				this.entityConvertedPointsMap.get(ent);
			double[] renderPositionsBottom = new double[]

				{renderPositions[4], renderPositions[5],

					renderPositions[6]};
			double[] renderPositionsX = new double[]

				{renderPositions[7], renderPositions[8],

					renderPositions[9]};
			double[] renderPositionsX1 = new double[]

				{renderPositions[10], renderPositions[11],

					renderPositions[12]};
			double[] renderPositionsZ = new double[]

				{renderPositions[13], renderPositions[14],

					renderPositions[15]};
			double[] renderPositionsZ1 = new double[]

				{renderPositions[16], renderPositions[17],

					renderPositions[18]};
			double[] renderPositionsTop1 = new double[]

				{renderPositions[19], renderPositions[20],

					renderPositions[21]};
			double[] renderPositionsTop2 = new double[]

				{renderPositions[22], renderPositions[23],

					renderPositions[24]};
			boolean bl = shouldRender = renderPositions[3]

				> 0.0 && renderPositions[3] <= 1.0 &&

				renderPositionsBottom[2] > 0.0 &&

				renderPositionsBottom[2] <= 1.0 && renderPositionsX[2]

				> 0.0 && renderPositionsX[2] <= 1.0 &&

				renderPositionsX1[2] > 0.0 && renderPositionsX1[2] <=

				1.0 && renderPositionsZ[2] > 0.0 && renderPositionsZ

				[2] <= 1.0 && renderPositionsZ1[2] > 0.0 &&

				renderPositionsZ1[2] <= 1.0 && renderPositionsTop1[2]

				> 0.0 && renderPositionsTop1[2] <= 1.0 &&

				renderPositionsTop2[2] > 0.0 && renderPositionsTop2[2]

				<= 1.0;
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5, 0.5, 0.5);

			if (((this.invis.getValueState() || !

				ent.isInvisible()) && ent instanceof EntityPlayer &&

				!(ent instanceof EntityPlayerSP))) {
				try {
					double[] xValues = new double[]

						{renderPositions[0], renderPositionsBottom[0],

							renderPositionsX[0], renderPositionsX1[0],

							renderPositionsZ[0], renderPositionsZ1[0],

							renderPositionsTop1[0], renderPositionsTop2[0]};
					double[] yValues = new double[]

						{renderPositions[1], renderPositionsBottom[1],

							renderPositionsX[1], renderPositionsX1[1],

							renderPositionsZ[1], renderPositionsZ1[1],

							renderPositionsTop1[1], renderPositionsTop2[1]};
					double x = renderPositions[0];
					double y = renderPositions[1];
					double endx = renderPositionsBottom[0];
					double endy = renderPositionsBottom[1];

					for (double bdubs : xValues) {
						if (bdubs >= x)
							continue;
						x = bdubs;
					}
					for (double bdubs : xValues) {
						if (bdubs <= endx)
							continue;
						endx = bdubs;
					}
					for (double bdubs : yValues) {
						if (bdubs >= y)
							continue;
						y = bdubs;
					}
					for (double bdubs : yValues) {
						if (bdubs <= endy)
							continue;
						endy = bdubs;
					}
					double xDiff = (endx - x) / 4.0;
					double x2Diff = (endx - x) / (double) 4;
					double yDiff = xDiff;
					int color = Colors.getColor(255, 255);

					if (ent.hurtTime > 0) {
						color = Colors.getColor(255, 0, 0,
							255);
					} else if (ent.isInvisible()) {
						color = Colors.getColor(255, 255, 0,
							255);
					} else {
						color = Colors.getColor(255, 255, 255,
							255);
					}

					if (this.mode.isCurrentMode("Box")) {
						RenderUtil.rectangleBordered(x + 0.5, y +

								0.5, endx - 0.5, endy - 0.5, 1.0, Colors.getColor(0,

							0, 0, 0),
							color);
						RenderUtil.rectangleBordered(x - 0.5, y -

								0.5, endx + 0.5, endy + 0.5, 1.0, Colors.getColor(0,

							0),
							Colors.getColor(0, 150));
						RenderUtil.rectangleBordered(x + 1.5, y +

								1.5, endx - 1.5, endy - 1.5, 1.0, Colors.getColor(0,

							0),
							Colors.getColor(0, 150));
					}
					if (this.mode.isCurrentMode("Corner")) {

						RenderUtil.rectangle(x + 0.5, y + 0.5, x +

							1.5, y + yDiff + 0.5, color);
						RenderUtil.rectangle(x + 0.5, endy - 0.5, x +

							1.5, endy - yDiff - 0.5, color);
						RenderUtil.rectangle(x - 0.5, y + 0.5, x +

							0.5, y + yDiff + 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x + 1.5, y + 2.5, x +

							2.5, y + yDiff + 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x - 0.5, y + yDiff +

							0.5, x + 2.5, y + yDiff + 1.5, Colors.getColor(0,

							150));
						RenderUtil.rectangle(x - 0.5, endy - 0.5, x +

							0.5, endy - yDiff - 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x + 1.5, endy - 2.5, x +

							2.5, endy - yDiff - 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x - 0.5, endy - yDiff -

							0.5, x + 2.5, endy - yDiff - 1.5, Colors.getColor(0,

							150));
						RenderUtil.rectangle(x + 1.0, y + 0.5, x +

							x2Diff, y + 1.5, color);
						RenderUtil.rectangle(x - 0.5, y - 0.5, x +

							x2Diff, y + 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x + 1.5, y + 1.5, x +

							x2Diff, y + 2.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x + x2Diff, y - 0.5, x +

							x2Diff + 1.0, y + 2.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x + 1.0, endy - 0.5, x

							+ x2Diff, endy - 1.5, color);
						RenderUtil.rectangle(x - 0.5, endy + 0.5, x

							+ x2Diff, endy - 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x + 1.5, endy - 1.5, x

							+ x2Diff, endy - 2.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(x + x2Diff, endy + 0.5,

							x + x2Diff + 1.0, endy - 2.5, Colors.getColor(0,

								150));
						RenderUtil.rectangle(endx - 0.5, y + 0.5,

							endx - 1.5, y + yDiff + 0.5, color);
						RenderUtil.rectangle(endx - 0.5, endy - 0.5,

							endx - 1.5, endy - yDiff - 0.5, color);
						RenderUtil.rectangle(endx + 0.5, y + 0.5,

							endx - 0.5, y + yDiff + 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(endx - 1.5, y + 2.5,

							endx - 2.5, y + yDiff + 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(endx + 0.5, y + yDiff +

							0.5, endx - 2.5, y + yDiff + 1.5, Colors.getColor(0,

							150));
						RenderUtil.rectangle(endx + 0.5, endy - 0.5,

							endx - 0.5, endy - yDiff - 0.5, Colors.getColor(0,

								150));
						RenderUtil.rectangle(endx - 1.5, endy - 2.5,

							endx - 2.5, endy - yDiff - 0.5, Colors.getColor(0,

								150));
						RenderUtil.rectangle(endx + 0.5, endy -

								yDiff - 0.5, endx - 2.5, endy - yDiff - 1.5,

							Colors.getColor(0, 150));
						RenderUtil.rectangle(endx - 1.0, y + 0.5,

							endx - x2Diff, y + 1.5, color);
						RenderUtil.rectangle(endx + 0.5, y - 0.5,

							endx - x2Diff, y + 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(endx - 1.5, y + 1.5,

							endx - x2Diff, y + 2.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(endx - x2Diff, y - 0.5,

							endx - x2Diff - 1.0, y + 2.5, Colors.getColor(0,

								150));
						RenderUtil.rectangle(endx - 1.0, endy - 0.5,

							endx - x2Diff, endy - 1.5, color);
						RenderUtil.rectangle(endx + 0.5, endy + 0.5,

							endx - x2Diff, endy - 0.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(endx - 1.5, endy - 1.5,

							endx - x2Diff, endy - 2.5, Colors.getColor(0, 150));
						RenderUtil.rectangle(endx - x2Diff, endy +

							0.5, endx - x2Diff - 1.0, endy - 2.5, Colors.getColor

							(0, 150));
					}

					if (this.HEALTH.getValueState()) {
						float health = ent.getHealth();
						float[] fractions = new float[]{0.0f,

							0.5f, 1.0f};
						Color[] colors = new Color[]{Color.RED,

							Color.YELLOW, Color.GREEN};
						float progress = health /

							ent.getMaxHealth();
						Color customColor = health >= 0.0f ?

							ESP2D.blendColors(fractions, colors,

								progress).brighter()
							: Color.RED;
						double difference = y - endy + 0.5;
						double healthLocation = endy + difference

							* (double) progress;
						RenderUtil.rectangleBordered(x - 6.5, y

								- 0.5, x - 2.5, endy, 1.0, Colors.getColor(0, 100),

							Colors.getColor(0, 150));
						RenderUtil.rectangle(x - 5.5, endy - 1.0,

							x - 3.5, healthLocation, customColor.getRGB());
						if (-difference > 50.0) {
							for (int i = 1; i < 10; ++i) {
								double dThing = difference / 10.0

									* (double) i;
								RenderUtil.rectangle(x - 6.5,

									endy - 0.5 + dThing, x - 2.5, endy - 0.5 + dThing -

										1.0,
									Colors.getColor(0));
							}
						}
						if ((int) getIncremental

							(progress * 100.0f, 1.0) <= 40) {
							GlStateManager.pushMatrix();
							GlStateManager.scale(2.0f, 2.0f,
								2.0f);

							String nigger = "" + (int)

								getIncremental(health * 5.0f, 1.0) + "HP";

							mc.fontRendererObj.drawStringWithShadow(nigger, (float)

									(x - 6.0 - (double) (mc.fontRendererObj.getStringWidth(nigger)

										* 2.0f)) / 2.0f, ((float) ((int) healthLocation) +

									mc.fontRendererObj.getStringWidth(nigger) / 2.0f) / 2.0f,
								-1);
							GlStateManager.popMatrix();
						}

					}
				} catch (Exception xValues) {
					// empty catch block

				}
			}
			GlStateManager.popMatrix();
			GL11.glColor4f((float) 1.0f, (float) 1.0f,

				(float) 1.0f, (float) 1.0f);
		}

		GL11.glScalef((float) 1.0f, (float) 1.0f,

			(float) 1.0f);
		GL11.glColor4f((float) 1.0f, (float) 1.0f,

			(float) 1.0f, (float) 1.0f);
		GlStateManager.popMatrix();
		RenderUtil.rectangle(0.0, 0.0, 0.0, 0.0, -

			1);
	}

	private void updatePositions() {

		this.entityConvertedPointsMap.clear();
		float pTicks =

			mc.timer.renderPartialTicks;
		for (Entity e2 :

			mc.theWorld.getLoadedEntityList()) {
			EntityPlayer ent;
			if (!(e2 instanceof EntityPlayer)

				|| (ent = (EntityPlayer) e2) == mc.thePlayer)
				continue;
			double x = ent.lastTickPosX +

				(ent.posX - ent.lastTickPosX) * (double) pTicks -

				mc.getRenderManager().viewerPosX + 0.36;
			double y = ent.lastTickPosY +

				(ent.posY - ent.lastTickPosY) * (double) pTicks -

				mc.getRenderManager().viewerPosY;
			double z = ent.lastTickPosZ +

				(ent.posZ - ent.lastTickPosZ) * (double) pTicks -

				mc.getRenderManager().viewerPosZ + 0.36;
			double topY = y += (double)

				ent.height + 0.15;
			double[] convertedPoints =

				RenderUtil.convertTo2D(x, y, z);
			double[] convertedPoints22 =

				RenderUtil.convertTo2D(x - 0.36, y, z - 0.36);
			double xd = 0.0;
			if (convertedPoints22[2] < 0.0 ||

				convertedPoints22[2] >= 1.0)
				continue;
			x = ent.lastTickPosX + (ent.posX -

				ent.lastTickPosX) * (double) pTicks -

				mc.getRenderManager().viewerPosX - 0.36;
			z = ent.lastTickPosZ + (ent.posZ -

				ent.lastTickPosZ) * (double) pTicks -

				mc.getRenderManager().viewerPosZ - 0.36;
			double[] convertedPointsBottom =

				RenderUtil.convertTo2D(x, y, z);
			y = ent.lastTickPosY + (ent.posY -

				ent.lastTickPosY) * (double) pTicks -

				mc.getRenderManager().viewerPosY - 0.05;
			double[] convertedPointsx =

				RenderUtil.convertTo2D(x, y, z);
			x = ent.lastTickPosX + (ent.posX -

				ent.lastTickPosX) * (double) pTicks -

				mc.getRenderManager().viewerPosX - 0.36;
			z = ent.lastTickPosZ + (ent.posZ -

				ent.lastTickPosZ) * (double) pTicks -

				mc.getRenderManager().viewerPosZ + 0.36;
			double[] convertedPointsTop1 =

				RenderUtil.convertTo2D(x, topY, z);
			double[] convertedPointsx1 =

				RenderUtil.convertTo2D(x, y, z);
			x = ent.lastTickPosX + (ent.posX -

				ent.lastTickPosX) * (double) pTicks -

				mc.getRenderManager().viewerPosX + 0.36;
			z = ent.lastTickPosZ + (ent.posZ -

				ent.lastTickPosZ) * (double) pTicks -

				mc.getRenderManager().viewerPosZ + 0.36;
			double[] convertedPointsz =

				RenderUtil.convertTo2D(x, y, z);
			x = ent.lastTickPosX + (ent.posX -

				ent.lastTickPosX) * (double) pTicks -

				mc.getRenderManager().viewerPosX + 0.36;
			z = ent.lastTickPosZ + (ent.posZ -

				ent.lastTickPosZ) * (double) pTicks -

				mc.getRenderManager().viewerPosZ - 0.36;
			double[] convertedPointsTop2 =

				RenderUtil.convertTo2D(x, topY, z);
			double[] convertedPointsz1 =

				RenderUtil.convertTo2D(x, y, z);
			this.entityConvertedPointsMap.put

				(ent, new double[]{convertedPoints[0],

					convertedPoints[1], xd, convertedPoints[2],

					convertedPointsBottom[0], convertedPointsBottom[1],

					convertedPointsBottom[2], convertedPointsx[0],

					convertedPointsx[1], convertedPointsx[2],

					convertedPointsx1[0], convertedPointsx1[1],

					convertedPointsx1[2], convertedPointsz[0],

					convertedPointsz[1], convertedPointsz[2],

					convertedPointsz1[0], convertedPointsz1[1],

					convertedPointsz1[2], convertedPointsTop1[0],

					convertedPointsTop1[1], convertedPointsTop1[2],

					convertedPointsTop2[0], convertedPointsTop2[1],

					convertedPointsTop2[2]});
		}
	}

	private String getColor(int level) {
		if (level == 2) {
			return "\u00a7a";
		}
		if (level == 3) {
			return "\u00a73";
		}
		if (level == 4) {
			return "\u00a74";
		}
		if (level >= 5) {
			return "\u00a76";
		}
		return "\u00a7f";
	}
}
