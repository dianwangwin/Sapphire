package today.Miscible.utils.fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.lwjgl.input.Keyboard;


import net.minecraft.client.Minecraft;
import today.Miscible.Miscible;
import today.Miscible.start.Mod;
import today.Miscible.start.ModManager;
import today.Miscible.start.Value;

public class FileUtil {
    private Minecraft mc = Minecraft.getMinecraft();
    private String fileDir;

    public FileUtil() {
        this.fileDir = String.valueOf((Object) this.mc.mcDataDir.getAbsolutePath()) + "/" + Miscible.NAME;
        File fileFolder = new File(this.fileDir);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        try {

            this.loadKeys();
            this.loadValues();
            this.loadMods();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeys() {
        File f = new File(String.valueOf((Object) this.fileDir) + "/keys.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Mod m : ModManager.getModList()) {
                String keyName = m.getKey() < 0 ? "None" : Keyboard.getKeyName((int) m.getKey());
                pw.write(String.valueOf((Object) m.getName()) + ":" + keyName + "\n");
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadKeys() throws IOException {
        File f = new File(String.valueOf((Object) this.fileDir) + "/keys.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;

            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader((Reader) new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains((CharSequence) ":")) continue;
                String[] split = line.split(":");
                Mod m = ModManager.getModByName((String) split[0]);
                int key = Keyboard.getKeyIndex((String) split[1]);
                if (m == null || key == -1) continue;
                m.setKey(key);
            }

        }
    }

    public void saveMods() {
        File f = new File(this.fileDir + "/mods.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Mod m : ModManager.getModList()) {
                pw.print(String.valueOf((Object) m.getName()) + ":" + m.isEnabled() + "\n");
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMods() throws IOException {
        File f = new File( this.fileDir + "/mods.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains(":")) continue;
                String[] split = line.split(":");
                Mod m = ModManager.getModByName((String) split[0]);
                boolean state = Boolean.parseBoolean((String) split[1]);
                if (m == null) continue;
                m.set(state, false);
            }
        }
    }

    public void saveValues() {
        File f = new File(String.valueOf((Object) this.fileDir) + "/values.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Value value : Value.list) {
                String valueName = value.getValueName();
                if (value.isValueBoolean) {
                    pw.print(String.valueOf((Object) valueName) + ":b:" + value.getValueState() + "\n");
                    continue;
                }
                if (value.isValueDouble) {
                    pw.print(String.valueOf((Object) valueName) + ":d:" + value.getValueState() + "\n");
                    continue;
                }
                if (!value.isValueMode) continue;
                pw.print(String.valueOf((Object) valueName) + ":s:" + value.getModeTitle() + ":" + value.getCurrentMode() + "\n");
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadValues() throws IOException {
        File f = new File(String.valueOf((Object) this.fileDir) + "/values.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader((Reader) new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains((CharSequence) ":")) continue;
                String[] split = line.split(":");
                for (Value value : Value.list) {
                    if (!split[0].equalsIgnoreCase(value.getValueName())) continue;
                    if (value.isValueBoolean && split[1].equalsIgnoreCase("b")) {
                        value.setValueState((Object) Boolean.parseBoolean((String) split[2]));
                        continue;
                    }
                    if (value.isValueDouble && split[1].equalsIgnoreCase("d")) {
                        value.setValueState((Object) Double.parseDouble((String) split[2]));
                        continue;
                    }
                    if (!value.isValueMode || !split[1].equalsIgnoreCase("s") || !split[2].equalsIgnoreCase(value.getModeTitle()))
                        continue;
                    value.setCurrentMode(Integer.parseInt((String) split[3]));
                }
            }
        }
    }

    /*public void saveFriends() {
        File f = new File(String.valueOf(this.fileDir) + "/friend.txt");

        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            Iterator var4 = FriendManager.getFriends().iterator();
            while (var4.hasNext()) {
                Friend friend = (Friend) var4.next();
                pw.print(friend.getName() + ":" + friend.getAlias() + "\n");
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public void loadFriends() throws IOException {
        File f = new File(String.valueOf(this.fileDir) + "/friend.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(f));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    String[] split = line.split(":");
                    if (line.length() >= 2) {
                        Friend friend = new Friend(split[0], split[1]);
                        FriendManager.getFriends().add(friend);
                    }
                }
            }
        }
    }*/
}
