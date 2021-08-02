package today.Miscible.start.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.item.ItemBlock;
import today.Miscible.events.EventUpdate;
import today.Miscible.start.Mod;

public class FastPlace extends Mod {
	
	public FastPlace() {
		super("FastPlace", Category.World);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.thePlayer.inventory.getCurrentItem() != null) {
			if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
				mc.rightClickDelayTimer = 0;
			} else {
				mc.rightClickDelayTimer = 4;
			}
		}
	}
	
	@Override
	public void onDisable() {
		mc.rightClickDelayTimer = 4;
		super.onDisable();
	}
}
