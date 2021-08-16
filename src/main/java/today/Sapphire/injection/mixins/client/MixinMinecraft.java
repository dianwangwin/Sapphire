package today.Sapphire.injection.mixins.client;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import today.Sapphire.Sapphire;
import today.Sapphire.events.EventKey;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;
import today.Sapphire.utils.render.RenderUtil;

import java.io.IOException;

@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public abstract class MixinMinecraft {

	@Shadow
	public GuiScreen currentScreen;

	long lastFrame;

	@Shadow
	public Timer timer;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void minecraftConstructor(GameConfiguration gameConfig, CallbackInfo ci) {
		new Sapphire();
	}

	@Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
	private void startGame(CallbackInfo ci) {
		Sapphire.instance.onClientStart();
	}

	@Inject(method = "shutdown", at = @At("HEAD"))
	private void shutdown(CallbackInfo callbackInfo) {
		Sapphire.instance.stopClient();
	}

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
	private void onKey(CallbackInfo ci) throws IOException {

		for (Mod m : ModManager.modList) {
			m.Alwas();
		}

		if (Keyboard.getEventKeyState() && currentScreen == null)
			EventManager.call(new EventKey(
				Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));

		if (Keyboard.getEventKeyState() && currentScreen == null) {
			for (Mod m : ModManager.modList) {
				if (m.getKey() != (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256
					: Keyboard.getEventKey()))
					continue;
				m.set(!m.isEnabled());
			}
		}
	}

	@Inject(method = "runGameLoop", at = {@At("HEAD")})
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
