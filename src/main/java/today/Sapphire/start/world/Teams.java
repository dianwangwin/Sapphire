package today.Sapphire.start.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;

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
