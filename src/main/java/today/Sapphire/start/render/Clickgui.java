package today.Sapphire.start.render;

import today.Sapphire.Sapphire;
import today.Sapphire.start.Mod;

public class Clickgui extends Mod {
	

	public Clickgui() {
		super("Clickgui", Category.Render);
	}
	
	
	@Override
	public void onEnable() {
		mc.displayGuiScreen(Sapphire.instance.ui);
		this.toggle();
		super.onEnable();
	}
}
