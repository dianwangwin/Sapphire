package today.Sapphire.start.movent;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.block.material.Material;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import today.Sapphire.events.EventMove;
import today.Sapphire.events.EventPreMotion;
import today.Sapphire.events.EventReceivePacket;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;
import today.Sapphire.start.Value;
import today.Sapphire.start.player.Scaffold;
import today.Sapphire.utils.MoveUtil;

public class Speed extends Mod {

	public Value<String> mode;
	private double moveSpeed, distance;
	int stage;
	int stop;

	public Speed() {
		super("Speed", Category.Movement);
		mode = new Value("Speed", "mode", 0);
		mode.addValue("Hypixel");
	}

	@EventTarget
	public void onUpdate(EventPreMotion event) {
		double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
		double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
		this.distance = Math.sqrt(xDist * xDist + zDist * zDist);
	}

	private boolean canZoom() {
		if (MoveUtil.moving() && this.mc.thePlayer.onGround) {
			return true;
		}
		return false;
	}

	public boolean isBlockUnder(Material blockMaterial) {
		return mc.theWorld.getBlockState(this.underPlayer()).getBlock().getMaterial() == blockMaterial;
	}

	public BlockPos underPlayer() {
		return new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.getEntityBoundingBox().minY - 1.0,
				this.mc.thePlayer.posZ);
	}

	@EventTarget
	private void onMove(EventMove event)  {
		this.setDisplayName(this.mode.getModeName(this.mode));

		
		switch (this.mode.getModeName(this.mode)) {
		
		case "Hypixel": {
			stage += 1;

			Scaffold scaffold = (Scaffold) ModManager.getModuleForClass(Scaffold.class);

			if (!MoveUtil.moving() || mc.thePlayer.isInWater() || scaffold.isEnabled() || stop > 1) {
				moveSpeed = MoveUtil.getBaseMovementSpeed();
				distance = 0.0;
				return;
			}

			if (stage > 0) {
				if (stage > 3) {
					if (mc.thePlayer.isCollidedVertically) {
						moveSpeed = MoveUtil.getBaseMovementSpeed();
						mc.timer.timerSpeed = 1.0f;
						stage = 0;
						return;
					}
				} else if (stage == 2 && mc.thePlayer.onGround && MoveUtil.moving()) {

					mc.thePlayer.motionY = 0.42 + MoveUtil.getJumpEff() * 0.1f + Math.random() / 36000;
					event.y = mc.thePlayer.motionY;
				}

				moveSpeed = getWachdogSpeed();
				setMotion(event, moveSpeed);
			}
			break;
		}
		}
	}

	@EventTarget
	private void onPacket(EventReceivePacket event) {

		if (event.packet instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) event.packet;
			s08.yaw = mc.thePlayer.rotationYaw;
			s08.pitch = mc.thePlayer.rotationPitch;
			stop = 1000;
		} else if (stop > 1) {
			stop -= 100;
			mc.thePlayer.jumpTicks = 0;
		}
	}

	private double getWachdogSpeed() {
		boolean needslow = false;
		
		
		if (stage == 2 && canZoom()) {
			
			moveSpeed *= 2.1f;

		} else if (stage == 3) {
			final double strafe = (0.7095 + MoveUtil.getBaseMovementSpeed() / 10) 
					* (distance - MoveUtil.getBaseMovementSpeed());
			moveSpeed = distance - strafe;

		} else {

			if (stage == 2 && mc.thePlayer.fallDistance > 0.0)
				needslow = true;
			if (MoveUtil.resetJump() && this.stage > 0)
				this.stage = MoveUtil.moving() ? 1 : 0;

			moveSpeed = distance - distance / 159;
		}

		if(needslow)
			mc.timer.timerSpeed = 1.07f;
		
		return Math.max(moveSpeed - Math.abs((needslow) ? (distance + moveSpeed) * 0.2249336 : 0.0039336 * (distance + moveSpeed)),
				MoveUtil.getBaseMovementSpeed());
	}

	private void setMotion(final EventMove em, final double speed) {
		double forward = this.mc.thePlayer.movementInput.moveForward;
		double strafe = this.mc.thePlayer.movementInput.moveStrafe;
		float yaw = this.mc.thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {
			em.setX(0.0);
			em.setZ(0.0);
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0) {
					yaw += ((forward > 0.0) ? -45 : 45);
				} else if (strafe < 0.0) {
					yaw += ((forward > 0.0) ? 45 : -45);
				}
				strafe = 0.0;
				if (forward > 0.0) {
					forward = 1.0;
				} else if (forward < 0.0) {
					forward = -1.0;
				}
			}
			em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
			em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
		}
		mc.timer.timerSpeed = mc.thePlayer.motionY <= -0.009999999776482582D && !isBlockUnder(Material.air) ? 1.07f
				: 1.0F;
	}
}
