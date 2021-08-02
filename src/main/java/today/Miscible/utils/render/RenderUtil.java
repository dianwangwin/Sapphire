package today.Miscible.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import scala.reflect.internal.Trees.This;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public enum RenderUtil {
	INSTANCE;

	public static float delta;

	public static double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) (delta * speed);
		if (animation < finalState) {
			if (animation + add < finalState)
				animation += add;
			else
				animation = finalState;
		} else {
			if (animation - add > finalState)
				animation -= add;
			else
				animation = finalState;
		}
		return animation;
	}

	public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor,
			int borderColor) {
		rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		rectangle(x + width, y, x1 - width, y + width, borderColor);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		rectangle(x, y, x + width, y1, borderColor);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		rectangle(x1 - width, y, x1, y1, borderColor);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		rectangle(x + width, y1 - width, x1 - width, y1, borderColor);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void rectangle(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			double var5 = top;
			top = bottom;
			bottom = var5;
		}
		float var11 = (color >> 24 & 0xFF) / 255.0F;
		float var6 = (color >> 16 & 0xFF) / 255.0F;
		float var7 = (color >> 8 & 0xFF) / 255.0F;
		float var8 = (color & 0xFF) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(left, bottom, 0.0D).endVertex();
		worldRenderer.pos(right, bottom, 0.0D).endVertex();
		worldRenderer.pos(right, top, 0.0D).endVertex();
		worldRenderer.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.resetColor();
	}

	public static void drawRoundedRect(float x, float y, float x2, float y2, final float round, final int color) {
		x += (float) (round / 2.0f + 0.5);
		y += (float) (round / 2.0f + 0.5);
		x2 -= (float) (round / 2.0f + 0.5);
		y2 -= (float) (round / 2.0f + 0.5);
		Gui.drawRect((int) x, (int) y, (int) x2, (int) y2, color);
		circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
		circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
		circle(x + round / 2.0f, y + round / 2.0f, round, color);
		circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
		Gui.drawRect((int) (x - round / 2.0f - 0.5f), (int) (y + round / 2.0f), (int) x2, (int) (y2 - round / 2.0f),
				color);
		Gui.drawRect((int) x, (int) (y + round / 2.0f), (int) (x2 + round / 2.0f + 0.5f), (int) (y2 - round / 2.0f),
				color);
		Gui.drawRect((int) (x + round / 2.0f), (int) (y - round / 2.0f - 0.5f), (int) (x2 - round / 2.0f),
				(int) (y2 - round / 2.0f), color);
		Gui.drawRect((int) (x + round / 2.0f), (int) y, (int) (x2 - round / 2.0f), (int) (y2 + round / 2.0f + 0.5f),
				color);
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void circle(final float x, final float y, final float radius, final int fill) {
		arc(x, y, 0.0f, 360.0f, radius, fill);
	}

	public static void arc(final float x, final float y, final float start, final float end, final float radius,
			final int color) {
		arcEllipse(x, y, start, end, radius, radius, color);
	}

	public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h,
			final int color) {
		GlStateManager.color(0.0f, 0.0f, 0.0f);
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		float temp = 0.0f;
		if (start > end) {
			temp = end;
			end = start;
			start = temp;
		}
		final float var11 = (color >> 24 & 0xFF) / 255.0f;
		final float var12 = (color >> 16 & 0xFF) / 255.0f;
		final float var13 = (color >> 8 & 0xFF) / 255.0f;
		final float var14 = (color & 0xFF) / 255.0f;
		final Tessellator var15 = Tessellator.getInstance();
		final WorldRenderer var16 = var15.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var12, var13, var14, var11);
		if (var11 > 0.5f) {
			GL11.glEnable(2848);
			GL11.glLineWidth(2.0f);
			GL11.glBegin(3);
			for (float i = end; i >= start; i -= 4.0f) {
				final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
				final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
				GL11.glVertex2f(x + ldx, y + ldy);
			}
			GL11.glEnd();
			GL11.glDisable(2848);
		}
		GL11.glBegin(6);
		for (float i = end; i >= start; i -= 4.0f) {
			final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w;
			final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h;
			GL11.glVertex2f(x + ldx, y + ldy);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawFilledCircle(double x, double y, double radius, int c) {
		float f2 = (float) (c >> 24 & 255) / 255.0f;
		float f22 = (float) (c >> 16 & 255) / 255.0f;
		float f3 = (float) (c >> 8 & 255) / 255.0f;
		float f4 = (float) (c & 255) / 255.0f;
		GlStateManager.alphaFunc(516, 0.001f);
		GlStateManager.color(f22, f3, f4, f2);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		Tessellator tes = Tessellator.getInstance();
		double i = 0.0;
		while (i < 360.0) {
			double f5 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
			double f6 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
			GL11.glVertex2d((double) ((double) f3 + x), (double) ((double) f4 + y));
			i += 1.0;
		}
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.alphaFunc(516, 0.1f);
	}

	public static void drawRect(float x1, float y1, float x2, float y2, int color) {
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GL11.glPushMatrix();
		RenderUtil.color(color);
		GL11.glBegin((int) 7);
		GL11.glVertex2d((double) x2, (double) y1);
		GL11.glVertex2d((double) x1, (double) y1);
		GL11.glVertex2d((double) x1, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glDisable((int) 2848);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void doGlScissor(int x, int y, int width, int height) {
		Minecraft mc = Minecraft.getMinecraft();
		int scaleFactor = 1;
		int k = mc.gameSettings.guiScale;
		if (k == 0) {
			k = 1000;
		}
		while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320
				&& mc.displayHeight / (scaleFactor + 1) >= 240) {
			++scaleFactor;
		}
		GL11.glScissor((int) (x * scaleFactor), (int) (mc.displayHeight - (y + height) * scaleFactor),
				(int) (width * scaleFactor), (int) (height * scaleFactor));
	}

	public static void prepareScissorBox(float x, float y, float x2, float y2) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		GL11.glScissor((int) (x * (float) factor), (int) (((float) scale.getScaledHeight() - y2) * (float) factor),
				(int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
	}

	public static void beginCrop(float x, float y, float width, float height) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		int scaleFactor =factor;
		GL11.glScissor((int) (x * scaleFactor), (int) (Display.getHeight() - y * scaleFactor),
				(int) (width * scaleFactor), (int) (height * scaleFactor));
	}

	public static void color(int color) {
		float f = (float) (color >> 24 & 255) / 255.0f;
		float f1 = (float) (color >> 16 & 255) / 255.0f;
		float f2 = (float) (color >> 8 & 255) / 255.0f;
		float f3 = (float) (color & 255) / 255.0f;
		GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
	}

	public static double[] convertTo2D(double x, double topY, double z) {
		FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(2982, modelView);
		GL11.glGetFloat(2983, projection);
		GL11.glGetInteger(2978, viewport);
		boolean result = GLU.gluProject((float) x, (float) topY, (float) z, modelView, projection, viewport,
				screenCoords);
		return result
				? new double[] { (double) screenCoords.get(0),
						(double) ((float) Display.getHeight() - screenCoords.get(1)), (double) screenCoords.get(2) }
				: null;
	}

	public static Color rainbow(long time, float count, float fade) {
		float hue = ((float) time + (1.0F + count) * 4.0E7F) / 3.0E9F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 0.5F, 1.0F)).intValue()),
				16);
		Color zh = new Color((int) color);
		return new Color((float) zh.getRed() / 255.0F * fade, (float) zh.getGreen() / 255.0F * fade,
				(float) zh.getBlue() / 255.0F * fade, (float) zh.getAlpha() / 255.0F);
	}

	public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1,
			final int col1, final int col2) {
		drawRect(x, y, x2, y2, col2);
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}
}
