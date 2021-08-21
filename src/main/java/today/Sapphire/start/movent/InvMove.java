package today.Sapphire.start.movent;

import java.util.Objects;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import today.Sapphire.events.EventUpdate;
import today.Sapphire.start.Mod;

public class InvMove extends Mod {

	KeyBinding[] key = { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
			mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindSprint,
			mc.gameSettings.keyBindJump };
	KeyBinding[] array;

	public InvMove() {
		super("InvMove", Category.Movement);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!(mc.currentScreen instanceof GuiChat)) {
			if (mc.currentScreen != null) {
				for (int length = (array = key).length, i = 0; i < length; ++i) {
					KeyBinding b = array[i];
					KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
				}
			} else {
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