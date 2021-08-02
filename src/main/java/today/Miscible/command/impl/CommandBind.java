package today.Miscible.command.impl;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import today.Miscible.Miscible;
import today.Miscible.command.Command;
import today.Miscible.start.Mod;
import today.Miscible.start.ModManager;

public class CommandBind extends Command {

    public CommandBind(String[] command) {
        super(command);
       // this.setArgs("-bind <mod> <key>");
    }

    @Override
    public void onCmd(String[] args) {
        if(args.length < 3) {
        } else {
        	
            String mod = args[1];
            int key = Keyboard.getKeyIndex(args[2].toUpperCase());
            for (Mod m : ModManager.modList) {
                if (!m.getName().equalsIgnoreCase(mod)) continue;
                
                if(args[2].length() > 1 && !m.getName().equalsIgnoreCase("clickgui"))
            		return;    
                
                m.setKey(key);
                Miscible.instance.fileUtil.saveKeys();
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("¡ì9¡ìlMiscible ¡ìr: " + args[1] + " to " + args[2] ));
                return;
            }
        }
    }
}
