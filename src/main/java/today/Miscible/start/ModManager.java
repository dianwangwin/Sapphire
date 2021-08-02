package today.Miscible.start;

import ibxm.Module;
import today.Miscible.start.combat.*;
import today.Miscible.start.misc.*;
import today.Miscible.start.movent.*;
import today.Miscible.start.player.*;
import today.Miscible.start.render.*;
import today.Miscible.start.world.*;

import java.util.ArrayList;
import java.util.List;

public class ModManager {
    public static ArrayList<Mod> modList = new ArrayList();
    public static ArrayList<Mod> sortedModList = new ArrayList();
    public static List<Module> modules = new ArrayList<Module>();
    public static List<Module> moduleList;

    public ModManager() {
    	// COMBAT
    	addMod(new AutoSword());
    	addMod(new Autoarmor());
    	addMod(new Criticals());
    	addMod(new Killaura());
    	addMod(new Velocity());
    	addMod(new AutoPot());
    	
    	//RENDER
    	addMod(new MoreParticleMod());
    	addMod(new FullBright());
    	addMod(new NameTag2D());
    	addMod(new NoHurtCam());
    	addMod(new Clickgui());
    	addMod(new ESP2D());
    	addMod(new Hud());
    	
    	//Move
    	addMod(new NoSlowdown());
    	addMod(new AntiFall());
    	addMod(new InvMove());
    	addMod(new Sprint());
    	addMod(new Speed());
    	addMod(new Jesus());
    	
    	//PLAYER
    	addMod(new ChestStealer());
    	addMod(new NoRotateSet());
    	addMod(new InvCleaner());
    	addMod(new FastPlace());
    	addMod(new Scaffold());
    	addMod(new AutoTool());
    	addMod(new NoFall());
    	
    	addMod(new Teams());
    	addMod(new PingSpoof());
    	
    	
    	
    	//MISC
    	addMod(new Animation());
        sortedModList = (ArrayList) modList.clone();
    }


    public void addMod(Mod m) {
        modList.add(m);
    }

    public ArrayList<Mod> getToggled() {
        ArrayList<Mod> toggled = new ArrayList();
        for (Mod m : this.modList) {
            if (m.isEnabled()) {
                toggled.add(m);
            }
        }
        return toggled;
    }

    public Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls)
                continue;
            return m;
        }
        return null;
    }
    
    public static Mod getModuleForClass(Class<? extends Mod> cls) {
        for (Mod m : modList) {
            if (m.getClass() != cls)
                continue;
            return m;
        }
        return null;
    }


    public static Mod getModByName(String mod) {
        for (Mod m : modList) {
            if (!m.getName().equalsIgnoreCase(mod))
                continue;
            return m;
        }
        return null;
    }

    public static List<Module> getModules() {
        return ModManager.moduleList;
    }

    public static ArrayList<Mod> getModList() {
        return modList;
    }

    public static List<Module> getModList2() {
        return modules;
    }
}
