package today.Miscible.injection.mixins.client;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import today.Miscible.Miscible;
import today.Miscible.events.EventKey;
import today.Miscible.start.Mod;
import today.Miscible.start.ModManager;
import today.Miscible.utils.render.RenderUtil;

@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public abstract class MixinMinecraft {
	@Shadow
	public GuiScreen currentScreen;
	long lastFrame;
	@Shadow
	private Timer timer;
	@Shadow
	private int leftClickCounter;
	@Shadow
	private LanguageManager mcLanguageManager;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void minecraftConstructor(GameConfiguration gameConfig, CallbackInfo ci) {
		new Miscible();
	}

	// this.ingameGUI = new GuiIngame(this);
	@Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
	private void startGame(CallbackInfo ci) {
		Miscible.instance.onClientStart();
	}
	
	@Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo callbackInfo) {
		 Miscible.instance.stopClient();
    }

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
	private void onKey(CallbackInfo ci) throws IOException {
		if (Keyboard.getEventKeyState() && currentScreen == null)
			EventManager.call(new EventKey(
					Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));

		if (Keyboard.getEventKeyState() && currentScreen == null) {
			for (Mod m : ModManager.modList) {
				if (m.getKey() != (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256
						: Keyboard.getEventKey()))
					continue;
				m.set(m.isEnabled() == false);
			}
		}
	}

	@Inject(method = "runGameLoop", at = { @At("HEAD") })
	private void runGameLoop(CallbackInfo ci) throws IOException {
		long i = System.nanoTime();
		long thisFrame = System.currentTimeMillis();
		RenderUtil.delta = (float) (thisFrame - this.lastFrame) / 1000.0f;
		this.lastFrame = thisFrame;
	}

	public Timer getTimer() {
		return this.timer;
	}

}
