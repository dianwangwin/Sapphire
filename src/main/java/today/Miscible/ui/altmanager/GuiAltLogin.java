package today.Miscible.ui.altmanager;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public final class GuiAltLogin
extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    private GuiButton loginButton;
    private GuiButton backButton;
    private GuiButton importbutton;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 2:
    			String data = null;
    			try {
    				data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    			} catch (Exception ignored) {
    				break;
    			}
    			if (data.contains(":")) {
    				String[] credentials = data.split(":");
    				this.username.setText(credentials[0]);
    				this.password.setText(credentials[1]);
    			}
    			break;
            case 0: {
                this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                this.thread.start();
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
        this.drawCenteredString(mc.fontRendererObj, this.thread == null ? "Idle..." : this.thread.getStatus(), width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.loginButton = new GuiButton(0, width / 2 - 100, var3 + 72 , "Login");
        this.backButton = new GuiButton(1, width / 2 - 100, var3 + 72  + 48, "Back");
        this.importbutton = new GuiButton(2, width / 2 - 100, var3 + 72 + 24,"Import user:pass");
        this.buttonList.add(this.loginButton);
        this.buttonList.add(this.backButton);
        this.buttonList.add(this.importbutton);
        this.username = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}