package today.Sapphire.utils.fontManager;

public class FontManager {
	
	public static FontManager the;
	private int size;

	public FontManager(int fontsize) {
		this.size = fontsize;
	}
	
	public int getSize() {
		return this.size;
	}
	
}
