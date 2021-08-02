package today.Miscible;


import java.io.IOException;

import net.minecraftforge.fml.common.Mod;
import today.Miscible.command.CommandManager;
import today.Miscible.gui.clickgui.CSGOUI;
import today.Miscible.gui.clickgui.UIClick;
import today.Miscible.start.ModManager;
import today.Miscible.start.render.Hud;
import today.Miscible.utils.fileUtils.FileUtil;
import today.Miscible.utils.fontManager.FontManager;
@Mod(modid = Miscible.MODID, version = Miscible.VERSION, name = Miscible.NAME, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class Miscible {
	public static final String MODID = "37";
	public static final String NAME = "Miscible";
	public static final String VERSION = "1.0";
	public static Miscible instance; 
	public FileUtil fileUtil;
	public ModManager modUtil;
	public CommandManager commandUtil;
	public UIClick ui;
	public CSGOUI CSGO;
	public FontManager fontManger;
	
	public void onClientStart() {
		System.err.println("Miscible Loging ........");
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
	
	public Miscible(){
		instance = this;
	}
	
}
