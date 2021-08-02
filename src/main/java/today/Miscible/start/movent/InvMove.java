package today.Miscible.start.movent;

import java.util.Objects;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import today.Miscible.events.EventReceivePacket;
import today.Miscible.events.EventUpdate;
import today.Miscible.start.Mod;
import today.Miscible.start.Mod.Category;
import today.Miscible.start.Value;

public class InvMove extends Mod {

	KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack,
			this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint,
			this.mc.gameSettings.keyBindJump };
	KeyBinding[] array;

	public InvMove() {
		super("InvMove", Category.Movement);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!(mc.currentScreen instanceof GuiChat)) {
			if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
				for (int length = (array = key).length, i = 0; i < length; ++i) {
					KeyBinding b = array[i];
					KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
				}
			} else if (Objects.isNull(mc.currentScreen)) {
				for (int length = (array = key).length, i = 0; i < length; ++i) {
					KeyBinding b = array[i];
					if (!Keyboard.isKeyDown(b.getKeyCode())) {
						KeyBinding.setKeyBindState(b.getKeyCode(), false);
					}
				}
			}
		}
	}
}