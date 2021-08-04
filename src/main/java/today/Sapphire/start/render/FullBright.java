package today.Sapphire.start.render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import today.Sapphire.events.EventUpdate;
import today.Sapphire.start.Mod;

public class FullBright extends Mod{

	public FullBright() {
		super("FullBright", Category.Render);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate eventUpdate) {
		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
		this.mc.gameSettings.gammaSetting = 10.0f;
	}
	
    @Override
	public void onDisable() {
		super.onDisable();
		 this.mc.gameSettings.gammaSetting = 1.0f;
		 this.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
	}
}
