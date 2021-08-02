package today.Miscible.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import scala.collection.immutable.List;
import scala.tools.nsc.doc.Settings;
import today.Miscible.Miscible;
import today.Miscible.command.Command;
import today.Miscible.start.Mod;
import today.Miscible.start.ModManager;
import today.Miscible.utils.hideUtil;

public class CommandHide extends Command {

	public CommandHide(String[] command) {
		super(command);
	}

	@Override
	public void onCmd(String[] args) {
		if(args.length < 2) {
        } else {
			String mod = args[1];
			for (Mod m : ModManager.modList) {
				if (!m.getName().equalsIgnoreCase(mod))
					continue;

				StringUtils.remove(m.getName(), args[2]);
				return;
			}
		}
	}
}
