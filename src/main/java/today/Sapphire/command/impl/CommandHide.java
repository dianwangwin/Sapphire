package today.Sapphire.command.impl;

import org.apache.commons.lang3.StringUtils;

import today.Sapphire.command.Command;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;

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
