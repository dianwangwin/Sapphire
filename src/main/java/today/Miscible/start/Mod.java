package today.Miscible.start;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import today.Miscible.Miscible;

public class Mod {

	public enum Category {
		Combat, Movement, Render, Playe, World, Misc;
	}

	public static Minecraft mc = Minecraft.getMinecraft();

	public String displayName = "";
	public Value showValue;
	public String suffix;
	public String name;

	public Category category;

	public boolean openValues;
	public boolean isEnabled;
	
	public double hoverOpacity;
	public double arrowAnlge;
	
	public float posX;
	public float posY;
	
	public int key;

	public Mod(String name, Category category) {
		this.key = -1;
		this.name = name;
		this.arrowAnlge = 0.0;
		this.category = category;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		String suffix = displayName.toString();
		if (suffix.isEmpty()) {
			this.displayName = suffix;
		} else {
			this.displayName = String.format(" %s", EnumChatFormatting.GRAY + displayName);
		}
	}

	public void onEnable() {

	}

	public void onDisable() {

	}

	public void onToggle() {
	}

	public void toggle() {
		try {
			if (this.isEnabled()) {
				this.set(false);
			} else {
				this.set(true);
			}
		} catch (Exception var2) {
			var2.printStackTrace();
		}
	}

	public void disableValues() {
	}

	public String getValue() {
		if (this.showValue == null) {
			return "";
		}
		return this.showValue.isValueMode ? this.showValue.getModeAt(this.showValue.getCurrentMode())
				: String.valueOf(this.showValue.getValueState());
	}

	public void set(boolean state) {
		this.set(state, true);
	}

	public void set(boolean state, boolean safe) {
		this.isEnabled = state;

		if (state) {
			if (this.mc.theWorld != null) {

				this.onEnable();
			}

			EventManager.register(this);
		} else {

			if (this.mc.theWorld != null) {

				this.onDisable();
			}
			EventManager.unregister(this);
		}
		Miscible.instance.fileUtil.saveMods();
	}

	public String getName() {
		return this.name;
	}

	public int getKey() {
		return this.key;
	}

	public Category getCategory() {
		return this.category;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean hasValues() {
		for (Value value : Value.list) {
			String name = value.getValueName().split("_")[0];
			if (!name.equalsIgnoreCase(this.getName()))
				continue;
			return true;
		}
		return false;
	}

	public void onUpdate() {

	}

}
