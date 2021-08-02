package today.Miscible.start.render;

import today.Miscible.Miscible;
import today.Miscible.gui.clickgui.UIClick;
import today.Miscible.start.Mod;

public class Clickgui extends Mod {
	

	public Clickgui() {
		super("Clickgui", Category.Render);
	}
	
	
	@Override
	public void onEnable() {
		this.mc.displayGuiScreen(Miscible.instance.ui);
		this.toggle();
		super.onEnable();
	}
}
