package today.Sapphire.start.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.darkmagician6.eventapi.EventTarget;
import com.ibm.icu.text.NumberFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import today.Sapphire.events.EventRender;
import today.Sapphire.events.EventRender2D;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;
import today.Sapphire.utils.RotationUtil;
import today.Sapphire.utils.render.Colors;
import today.Sapphire.utils.render.RenderUtil;

public class NameTag2D extends Mod {

	private boolean hideInvisibles;
	private double gradualFOVModifier;
	private Character formatChar = new Character('\247');
	public static Map<EntityLivingBase, double[]> entityPositions = new HashMap();

	public Value<Boolean> Health = new Value("NameTag2D_Health", true);
	public Value<Boolean> Armor = new Value("NameTag2D_Armor", true);
	public Value<Boolean> Antibot = new Value("NameTag2D_Antibot", false);
	public Value<Boolean> Effect = new Value("NameTag2D_PotEffect", false);
	public Value<Boolean> distance = new Value("NameTag2D_Distance", false);
	public Value<Boolean> invis = new Value("NameTag2D_Invisibles", true);
	public Value<Boolean> random = new Value("NameTag2D_Random", false);

	public NameTag2D() {
		super("NameTag2D", Category.Render);
	}

	@EventTarget
	public void onRender(EventRender event) {
		try {
			updatePositions();
		} catch (Exception e) {

		}
	}
	
	private static int randomNumber(int min, int max) {
		Random random = new Random();
		return  (min + (random.nextInt() * (max - min)));
	}

	@EventTarget
	public void on2D(EventRender2D er) {
		GlStateManager.pushMatrix();
		ScaledResolution res = new ScaledResolution(mc);
		for (Entity ent : entityPositions.keySet()) {
			if (ent != mc.thePlayer && (invis.getValueState()) || !ent.isInvisible()) {
				/*
				 * if (AntiBot.bot.contains(ent) &&
				 * !this.Antibot.getValueState().booleanValue()) { continue; }
				 */

				GlStateManager.pushMatrix();
				if ((ent instanceof EntityPlayer)) {
					String str = ent.getDisplayName().getFormattedText();
					String name = ent.getName();
					str = str.replace(ent.getDisplayName().getFormattedText(),"\247f" + ent.getDisplayName().getFormattedText());

					double[] renderPositions = entityPositions.get(ent);

					if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D)) {
						GlStateManager.popMatrix();
						continue;
					}

					if (random.getValueState().booleanValue()) {
						String newstr = "";
						char[] rdm = { 'w', 'd', 'n', 'n', 'm', 'd', 'L' };
						for (int i = 0; i < name.length(); i++) {
							char ch = rdm[randomNumber(rdm.length - 1, 0)];
							newstr = newstr.concat(ch + "");
						}
						str = "��r" + newstr;
					}

					FontRenderer font = mc.fontRendererObj;
					GlStateManager.translate(renderPositions[0] / res.getScaleFactor(),
							renderPositions[1] / res.getScaleFactor(), 0.0D);
					scale();
					String healthInfo = (int) ((EntityLivingBase) ent).getHealth() + "";
					GlStateManager.translate(0.0D, -2.5D, 0.0D);
					float strWidth = font.getStringWidth(str);
					float strWidth2 = font.getStringWidth(healthInfo);
					int dist = (int) mc.thePlayer.getDistanceToEntity(ent);
					RenderUtil.rectangle(-strWidth / 2 - 1, -10.0D, strWidth / 2 + 1, 0, Colors.getColor(0, 130));
					int x3 = ((int) (renderPositions[0] + -strWidth / 2 - 3) / 2) - 26;
					int x4 = ((int) (renderPositions[0] + strWidth / 2 + 3) / 2) + 20;
					int y1 = ((int) (renderPositions[1] + -30) / 2);
					int y2 = ((int) (renderPositions[1] + 11) / 2);
					int mouseY = (res.getScaledHeight() / 2);
					int mouseX = (res.getScaledWidth() / 2);

					font.drawStringWithShadow(str, -strWidth / 2, -9F, Colors.getColor(255, 50, 50 , 255));

					boolean healthOption = !(Health.getValueState());
					boolean armor = !(Armor.getValueState());
					boolean hovered = x3 < mouseX && mouseX < x4 && y1 < mouseY && mouseY < y2;

					if (!healthOption || hovered) {
						float health = ((EntityPlayer) ent).getHealth();
						float[] fractions = new float[] { 0f, 0.5f, 1f };
						Color[] colors = new Color[] { Color.RED, Color.YELLOW, Color.GREEN };
						float progress = (health * 5) * 0.01f;
						Color customColor = blendColors(fractions, colors, progress).brighter();
						try {
							RenderUtil.rectangle(strWidth / 2 + 1, -10.0D, strWidth / 2 + 3.5 + strWidth2, 0,
									Colors.getColor(0, 130));
							font.drawStringWithShadow(healthInfo, strWidth / 2 + 3, (int) -9.0D, customColor.getRGB());
						} catch (Exception e) {

						}
					}

					int borderColor = 0;
					
					if (hovered || (distance.getValueState())) {
						String distanceStr = "\u00a7a[" + dist + "m]\u00a7r ";
						float witdh = font.getStringWidth(distanceStr);
						RenderUtil.rectangleBordered(-strWidth / 2.0f - witdh, -10.0, -strWidth / 2.0f - 1.0f, 0.0, 0.5,
								Colors.getColor(0, 130), Colors.getColor(0, 130));

						font.drawStringWithShadow(distanceStr, -strWidth / 2.0f - witdh + 1.0f, -9.0f,
								Colors.getColor(255, (int) (255.0f * 1)));

					}

					if (hovered || !armor) {
						List<ItemStack> itemsToRender = new ArrayList<>();
						for (int i = 0; i < 5; i++) {
							ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(i);
							if (stack != null) {
								itemsToRender.add(stack);
							}
						}
						int x = -(itemsToRender.size() * 7);
						for (ItemStack stack : itemsToRender) {
							RenderHelper.enableGUIStandardItemLighting();
							mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, -28);
							mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, -28);
							x += 3;
							RenderHelper.disableStandardItemLighting();
							if (stack != null) {
								int y = 21;
								int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId,
										stack);
								int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,
										stack);
								int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId,
										stack);
								if (sLevel > 0) {
									drawEnchantTag("Sh" + getColor(sLevel) + sLevel, x, y);
									y -= 9;
								}
								if (fLevel > 0) {
									drawEnchantTag("Fir" + getColor(fLevel) + fLevel, x, y);
									y -= 9;
								}
								if (kLevel > 0) {
									drawEnchantTag("Kb" + getColor(kLevel) + kLevel, x, y);
								} else if ((stack.getItem() instanceof ItemArmor)) {
									int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId,
											stack);
									int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId,
											stack);
									int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId,
											stack);
									if (pLevel > 0) {
										drawEnchantTag("P" + getColor(pLevel) + pLevel, x, y);
										y -= 9;
									}
									if (tLevel > 0) {
										drawEnchantTag("Th" + getColor(tLevel) + tLevel, x, y);
										y -= 9;
									}
									if (uLevel > 0) {
										drawEnchantTag("Unb" + getColor(uLevel) + uLevel, x, y);
									}
								} else if ((stack.getItem() instanceof ItemBow)) {
									int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId,
											stack);
									int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId,
											stack);
									int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId,
											stack);
									if (powLevel > 0) {
										drawEnchantTag("Pow" + getColor(powLevel) + powLevel, x, y);
										y -= 9;
									}
									if (punLevel > 0) {
										drawEnchantTag("Pun" + getColor(punLevel) + punLevel, x, y);
										y -= 9;
									}
									if (fireLevel > 0) {
										drawEnchantTag("Fir" + getColor(fireLevel) + fireLevel, x, y);
									}
								} else if (stack.getRarity() == EnumRarity.EPIC) {
									drawEnchantTag("\2476\247lGod", x, y);
								}
								int var7 = (int) Math.round(255.0D
										- (double) stack.getItemDamage() * 255.0D / (double) stack.getMaxDamage());
								int var10 = 255 - var7 << 16 | var7 << 8;
								Color customColor = new Color(var10).brighter();

								float x2 = (float) (x * 1.75D);
								y = -20 - 33;
								x += 12;
							}
						}
					}
				}
				GlStateManager.popMatrix();
			}
		}
		GlStateManager.popMatrix();
	}

	private String getColor(int level) {
		if (level == 1) {

		} else if (level == 2) {
			return "\247a";
		} else if (level == 3) {
			return "\2473";
		} else if (level == 4) {
			return "\2474";
		} else if (level >= 5) {
			return "\2476";
		}
		return "\247f";
	}

	private static void drawEnchantTag(String Enchant, int x, int y) {
		String Enchants = Enchant;
		String[] LIST = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f",
				"m", "o", "r", "g" };
		for (String str : LIST) {
			Enchant = Enchant.replaceAll("§" + str, "");
		}
		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		x = (int) (x * 2);
		y -= 48;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		Minecraft.getMinecraft().fontRendererObj.drawString(Enchant, x + 1, y * 2, 0);
		Minecraft.getMinecraft().fontRendererObj.drawString(Enchant, x - 1, y * 2, 0);
		Minecraft.getMinecraft().fontRendererObj.drawString(Enchant, x, y * 2 + 1, 0);
		Minecraft.getMinecraft().fontRendererObj.drawString(Enchant, x, y * 2 - 1, 0);
		Minecraft.getMinecraft().fontRendererObj.drawString(Enchants, x, y * 2, Colors.getColor(255));
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}

	private void scale() {
		float scale = 1;
		GlStateManager.scale(scale, scale, scale);
	}

	private void updatePositions() {
		entityPositions.clear();
		float pTicks = mc.timer.renderPartialTicks;
		for (Object o : mc.theWorld.loadedEntityList) {
			Entity ent = (Entity) o;
			if ((ent != mc.thePlayer) && ((ent instanceof EntityPlayer))
					&& ((!ent.isInvisible()) || (!this.hideInvisibles))) {
				double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - mc.getRenderManager().viewerPosX;
				double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
				double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - mc.getRenderManager().viewerPosZ;
				y += ent.height + 0.2D;
				if ((convertTo2D(x, y, z)[2] >= 0.0D) && (convertTo2D(x, y, z)[2] < 1.0D)) {
					entityPositions.put((EntityPlayer) ent,
							new double[] { convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1],
									Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]),
									convertTo2D(x, y, z)[2] });
				}
			}
		}
	}

	private double[] convertTo2D(double x, double y, double z, Entity ent) {
		float pTicks = mc.timer.renderPartialTicks;
		float prevYaw = mc.thePlayer.rotationYaw;
		float prevPrevYaw = mc.thePlayer.prevRotationYaw;
		float[] rotations = RotationUtil.getRotationFromPosition(
				ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks,
				ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks,
				ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6D);
		mc.getRenderViewEntity().rotationYaw = (mc.getRenderViewEntity().prevRotationYaw = rotations[0]);
		mc.entityRenderer.setupCameraTransform(pTicks, 0);
		double[] convertedPoints = convertTo2D(x, y, z);
		mc.getRenderViewEntity().rotationYaw = prevYaw;
		mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
		mc.entityRenderer.setupCameraTransform(pTicks, 0);
		return convertedPoints;
	}

	private double[] convertTo2D(double x, double y, double z) {
		FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(2982, modelView);
		GL11.glGetFloat(2983, projection);
		GL11.glGetInteger(2978, viewport);
		boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
		if (result) {
			return new double[] { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
		}
		return null;
	}

	public static Color blendColors(float[] fractions, Color[] colors, float progress) {
		Color color = null;
		if (fractions != null) {
			if (colors != null) {
				if (fractions.length == colors.length) {
					int[] indicies = getFractionIndicies(fractions, progress);

					if (indicies[0] < 0 || indicies[0] >= fractions.length || indicies[1] < 0
							|| indicies[1] >= fractions.length) {
						return colors[0];
					}
					float[] range = new float[] { fractions[indicies[0]], fractions[indicies[1]] };
					Color[] colorRange = new Color[] { colors[indicies[0]], colors[indicies[1]] };

					float max = range[1] - range[0];
					float value = progress - range[0];
					float weight = value / max;

					color = blend(colorRange[0], colorRange[1], 1f - weight);
				} else {
					throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
				}
			} else {
				throw new IllegalArgumentException("Colours can't be null");
			}
		} else {
			throw new IllegalArgumentException("Fractions can't be null");
		}
		return color;
	}

	public static int[] getFractionIndicies(float[] fractions, float progress) {
		int[] range = new int[2];

		int startPoint = 0;
		while (startPoint < fractions.length && fractions[startPoint] <= progress) {
			startPoint++;
		}

		if (startPoint >= fractions.length) {
			startPoint = fractions.length - 1;
		}

		range[0] = startPoint - 1;
		range[1] = startPoint;

		return range;
	}

	public static Color blend(Color color1, Color color2, double ratio) {
		float r = (float) ratio;
		float ir = (float) 1.0 - r;

		float rgb1[] = new float[3];
		float rgb2[] = new float[3];

		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);

		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;

		if (red < 0) {
			red = 0;
		} else if (red > 255) {
			red = 255;
		}
		if (green < 0) {
			green = 0;
		} else if (green > 255) {
			green = 255;
		}
		if (blue < 0) {
			blue = 0;
		} else if (blue > 255) {
			blue = 255;
		}

		Color color = null;
		try {
			color = new Color(red, green, blue);
		} catch (IllegalArgumentException exp) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
			exp.printStackTrace();
		}
		return color;
	}

}
