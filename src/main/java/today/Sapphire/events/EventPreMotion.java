package today.Sapphire.events;

import com.darkmagician6.eventapi.events.Cancellable;
import com.darkmagician6.eventapi.events.Event;

public class EventPreMotion implements Event, Cancellable {
	public static float YAW, PITCH, PREVYAW, PREVPITCH;
	public static boolean SNEAKING;
	public double y;
	public float yaw;
	public float pitch;
	public boolean onGround;
	public boolean cancel;

	public EventPreMotion(double y, float yaw, float pitch, boolean onGround) {
		this.y = y;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	public void fire() {
		PREVYAW = YAW;
		PREVPITCH = PITCH;
		YAW = this.yaw;
		PITCH = this.pitch;
	}

	public void setRotation(float[] idk) {
		setYaw(idk[0]);
		setPitch(idk[1]);
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public boolean isCancelled() {
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean state) {
		this.cancel = state;
	}
}
