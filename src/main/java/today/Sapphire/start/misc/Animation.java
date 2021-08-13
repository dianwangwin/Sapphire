package today.Sapphire.start.misc;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import today.Sapphire.events.EventAnimation;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;

public class Animation extends Mod {

	public final Value<String> mode;

	public Animation() {
		super("Animation", Category.Misc);
		mode = new Value("Animation", "mode", new String[]{"Swank" ,"Miscible" }, 0);
		setDisplayName(mode.getModeName());
	}

	@EventTarget
	private void onAnimation(EventAnimation event) {
		switch (this.mode.getModeName()) {
		case "Swank": {
			Swank(event.pre, event.post);
			event.doBlockTransformations();
			GlStateManager.translate( 0.5f, 0.0f, 0.0f);
			break;
		}
		case "Miscible": {
			Miscible(event.pre, event.post);
			event.doBlockTransformations();
			break;
		}
		}
	}

	private void Swank(float p_178096_1_, float p_178096_2_) {
		GlStateManager.translate(0.54F, -0.28F, -0.60999997F);
		GlStateManager.rotate(45.0F, 0.0F, 0.6F, 0.0F);
		float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
		float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.131415927F);
		GlStateManager.rotate(-var4 * 30.0F, 1.0F, 0.5f, 0.4F);
		GlStateManager.scale(0.4f, 0.4f, 0.4f);
	}

	private void Miscible(float p_178096_1_, float p_178096_2_) {
		GlStateManager.translate(0.62F, -0.44F, -0.70999997F);
		GlStateManager.rotate(45.0F, 0.0F, 0.6F, 0.0F);
		float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
		float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.131415927F);
		GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(0.4f, 0.4f, 0.4f);
	}
}
