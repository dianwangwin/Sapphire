package today.Sapphire;


import java.io.IOException;

import net.minecraftforge.fml.common.Mod;
import today.Sapphire.command.CommandManager;
import today.Sapphire.gui.clickgui.CSGOUI;
import today.Sapphire.gui.clickgui.UIClick;
import today.Sapphire.start.ModManager;
import today.Sapphire.start.render.Hud;
import today.Sapphire.utils.fileUtils.FileUtil;
import today.Sapphire.utils.fontManager.FontManager;
@Mod(modid = Sapphire.MODID, version = Sapphire.VERSION, name = Sapphire.NAME, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class Sapphire {
	public static final String MODID = "37";
	public static final String NAME = "Sapphire";
	public static final String VERSION = "1.0";
	public static Sapphire instance;
	public FileUtil fileUtil;
	public ModManager modUtil;
	public CommandManager commandUtil;
	public UIClick ui;
	public CSGOUI CSGO;
	public FontManager fontManger;
	
	public void onClientStart() {
		System.err.println("Sapphire Loging ........");
		fileUtil = new FileUtil();
		System.err.println("FileUtil Loging .......");
		modUtil = new ModManager();
		System.err.println("FileUtil Loging ......");
		commandUtil = new CommandManager();
		System.err.println("ComdUtil Loging .....");
		ui = new UIClick();
		CSGO = new CSGOUI();
		System.err.println("Clickgui Loging ...");
		try {
			System.err.println("Try config Loging");
			fileUtil.loadMods();
			fileUtil.loadValues();
			fileUtil.loadKeys();
		} catch (IOException e) {
			System.err.println("ConFig Loging erro");
			e.printStackTrace();
		}
		
		Hud hud = (Hud)ModManager.getModuleForClass(Hud.class);
		
	}
	
	public void stopClient() {
		fileUtil.saveKeys();
		fileUtil.saveValues();
		fileUtil.saveKeys();
	}
	
	public Sapphire(){
		instance = this;
	}
	
}
