package today.Sapphire.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import today.Sapphire.ui.altmanager.GuiAltLogin;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu  extends GuiScreen {


	@Shadow
	private GuiButton realmsButton;
	/**
	 * @reason liquidslowly
	 * @author liquidslowly
	 */
	@Overwrite
	private void addSingleplayerMultiplayerButtons(int p_addSingleplayerMultiplayerButtons_1_, int p_addSingleplayerMultiplayerButtons_2_) {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_addSingleplayerMultiplayerButtons_1_, I18n.format("menu.singleplayer", new Object[0])));
	    
	    this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_addSingleplayerMultiplayerButtons_1_ + p_addSingleplayerMultiplayerButtons_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
	    
	    this.buttonList.add(this.realmsButton = new GuiButton(185485, this.width / 2 + 2, p_addSingleplayerMultiplayerButtons_1_ + p_addSingleplayerMultiplayerButtons_2_ * 2, 98, 20, I18n.format("menu.online", new Object[0]).replace("Minecraft", "").trim()));
	    this.realmsButton.visible = false;
	    
	    this.buttonList.add(new GuiButton(14, this.width / 2 + 2, p_addSingleplayerMultiplayerButtons_1_ + p_addSingleplayerMultiplayerButtons_2_ * 2, 98, 20, "AltLogin"));
	    
	    this.buttonList.add(new GuiButton(6, this.width / 2 - 100, p_addSingleplayerMultiplayerButtons_1_ + p_addSingleplayerMultiplayerButtons_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));	
    }
	
	@Inject(method="actionPerformed", at={@At("HEAD")})
	  private void onActionPerformed(GuiButton p_actionPerformed_1_, CallbackInfo info)
	  {
	    if (p_actionPerformed_1_.id == 14) {
	      Minecraft.getMinecraft().displayGuiScreen(new GuiAltLogin(this));
	    }
	  }
	
}
