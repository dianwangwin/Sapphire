package today.Miscible.start.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import today.Miscible.start.Mod;
import today.Miscible.start.Mod.Category;
import today.Miscible.start.ModManager;

public class Teams extends Mod {

	public Teams() {
		super("Teams", Category.World);
	}

	public static boolean isOnSameTeam(Entity entity) {
		if (ModManager.getModByName("Teams").isEnabled()) {
			if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
				if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2
						|| entity.getDisplayName().getUnformattedText().length() <= 2) {
					return false;
				}
				if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2)
						.equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
					return true;
				}
			}
		}
		return false;
	}
}
