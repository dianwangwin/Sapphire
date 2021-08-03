package today.Sapphire.start.movent;

import java.util.ArrayList;
import java.util.Arrays;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import today.Sapphire.events.EventPreMotion;
import today.Sapphire.start.Mod;
import today.Sapphire.utils.MoveUtil;
import today.Sapphire.utils.timeUtils.TimeHelper;

public class Jesus extends Mod {
	private TimeHelper timer = new TimeHelper();
	private boolean wasWater = false;
	private int ticks = 0;

	public Jesus() {
		super("Jesus", Category.Movement);
	}

	@Override
	public void onEnable() {
		this.wasWater = false;
		super.onEnable();
	}

	private boolean canJeboos() {
		if (!(this.mc.thePlayer.fallDistance >= 3.0f || this.mc.gameSettings.keyBindJump.isPressed()
				|| MoveUtil.isInLiquid() || this.mc.thePlayer.isSneaking())) {
			return true;
		}
		return false;
	}

	boolean shouldJesus() {
		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY;
		double z = mc.thePlayer.posZ;
		ArrayList<BlockPos> pos = new ArrayList<BlockPos>(
				Arrays.asList(new BlockPos(x + 0.3, y, z + 0.3), new BlockPos(x - 0.3, y, z + 0.3),
						new BlockPos(x + 0.3, y, z - 0.3), new BlockPos(x - 0.3, y, z - 0.3)));
		for (BlockPos po : pos) {
			if (!(mc.theWorld.getBlockState(po).getBlock() instanceof BlockLiquid))
				continue;
			if (mc.theWorld.getBlockState(po).getProperties().get(BlockLiquid.LEVEL) instanceof Integer) {
				if ((int) mc.theWorld.getBlockState(po).getProperties().get(BlockLiquid.LEVEL) <= 4) {
					return true;
				}
			}
		}
		return false;
	}

	@EventTarget
	public void onPre(EventPreMotion e) {
		if (mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking() && this.shouldJesus()) {
			mc.thePlayer.motionY = 0.09;
		}
		if (this.mc.thePlayer.onGround || this.mc.thePlayer.isOnLadder()) {
			this.wasWater = false;
		}
		if (this.mc.thePlayer.motionY > 0.0 && this.wasWater) {
			if (this.mc.thePlayer.motionY <= 0.11) {
				EntityPlayerSP player = this.mc.thePlayer;
				player.motionY *= 0.971;
			}
			EntityPlayerSP player2 = this.mc.thePlayer;
			player2.motionY += 0.05172;
		}
		if (MoveUtil.isInLiquid() && !this.mc.thePlayer.isSneaking()) {
			if (this.ticks < 3) {
				this.mc.thePlayer.motionY = 0.13;
				++this.ticks;
				this.wasWater = false;
			} else {
				this.mc.thePlayer.motionY = 0.5;
				this.ticks = 0;
				this.wasWater = true;
			}
		}
	}

	public double getMotionY(double stage) {
		stage--;
		double[] motion = new double[] { 0.500, 0.484, 0.468, 0.436, 0.404, 0.372, 0.340, 0.308, 0.276, 0.244, 0.212,
				0.180, 0.166, 0.166, 0.156, 0.123, 0.135, 0.111, 0.086, 0.098, 0.073, 0.048, 0.06, 0.036, 0.0106, 0.015,
				0.004, 0.004, 0.004, 0.004, -0.013, -0.045, -0.077, -0.109 };
		if (stage < motion.length && stage >= 0)
			return motion[(int) stage];
		else
			return -999;

	}

	public static boolean isOnGround(double height) {
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static int getSpeedEffect() {
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
			return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
		else
			return 0;
	}

	public static void setMotion(double speed) {
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((forward == 0.0D) && (strafe == 0.0D)) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1;
				} else if (forward < 0.0D) {
					forward = -1;
				}
			}
			mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
			mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
		}
	}
}
