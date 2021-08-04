package today.Sapphire.command.impl;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import today.Sapphire.Sapphire;
import today.Sapphire.command.Command;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;

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
                Sapphire.instance.fileUtil.saveKeys();
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE+""+EnumChatFormatting.BOLD+"Sapphire "+EnumChatFormatting.RESET+": " + args[1] + " to " + args[2] ));
                return;
            }
        }
    }
}
