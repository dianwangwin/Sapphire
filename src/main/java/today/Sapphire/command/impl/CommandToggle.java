package today.Sapphire.command.impl;

import today.Sapphire.command.Command;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;

public class CommandToggle extends Command {

    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs("-toggle <module>");
    }

    @Override
    public void onCmd(String[] args) {
        if(args.length < 2) {
        } else {
            String mod = args[1];
            for (Mod m : ModManager.getModList()) {
                if(m.getName().equalsIgnoreCase(mod)) {
                    m.toggle();
                    return;
                }
            }
        }
    }
}
