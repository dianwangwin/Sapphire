package today.Sapphire.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.client.renderer.GlStateManager;

public class EventAnimation implements Event {

	public float pre, post;

	public EventAnimation(float f, float f1) {
		this.pre = f;
		this.post = f1;
	}

	public void doBlockTransformations() {
		GlStateManager.translate(-0.5F, 0.2F, 0.0F);
		GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
	}
}
