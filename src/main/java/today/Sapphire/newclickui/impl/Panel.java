package today.Sapphire.newclickui.impl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import today.Sapphire.newclickui.ClickUI;
import today.Sapphire.start.Mod;
import today.Sapphire.start.ModManager;
import today.Sapphire.utils.TranslateUtil;
import today.Sapphire.utils.fontManager.CFontRenderer;
import today.Sapphire.utils.fontManager.FontLoaders;
import today.Sapphire.utils.render.RenderUtil;
import today.Sapphire.utils.timeUtils.TimerUtil;

import java.awt.*;

public class Panel {
    private Mod.Category category;

    private float desX, desY, x, y, dragX, dragY;

    private boolean needMove;

    private long delay;

    private int wheel;

    private String name;

    private TranslateUtil anima = new TranslateUtil(1,0);
    private TranslateUtil translate = new TranslateUtil(0,0);

    private TranslateUtil enableranima = new TranslateUtil(1,0);

    private TimerUtil timer = new TimerUtil();

    public Panel(float x, float y, long delay, Mod.Category category) {
        this.desX = x;
        this.desY = y;
        this.delay = delay;
        this.category = category;
        this.name = category.toString();
        this.anima.setXY(1, 0);
        this.needMove = false;
        this.dragX = 0;
        this.dragY = 0;
    }

    public void draw(float mouseX, float mouseY) {
        //处理拖动
        if(needMove) setXY(mouseX - dragX, mouseY - dragY);
        if (!Mouse.isButtonDown(0) && needMove) needMove = false;

        //处理动画
        float alpha = 0.01f;
        float yani = 0;
        if(timer.hasElapsed(delay)) {
            anima.interpolate(100, 20, 4.0E-2f);
            alpha = anima.getX() / 100;
            yani = anima.getY();
        }

        x = desX;
        y = desY + 20 - yani;

       // UnicodeFontRenderer keybindfont = Hanabi.INSTANCE.fontManager.raleway13;
        CFontRenderer infofont = FontLoaders.kiona11;
        CFontRenderer titlefont = FontLoaders.robote18;
       // UnicodeFontRenderer font = Hanabi.INSTANCE.fontManager.raleway17;
        CFontRenderer font = FontLoaders.kiona17;
       // UnicodeFontRenderer icon = Hanabi.INSTANCE.fontManager.icon30;

        float mstartY = y + 40;
        float maddY = 15;

        //Panel背景
        RenderUtil.drawRect(x,y + 10,x + 100,y + 200, new Color(23,23,23,(int)(255 * alpha)).getRGB());
        RenderUtil.drawRect(x,y + 10,x + 100,y + 34, new Color(80,100,255,(int)(240 * alpha)).getRGB());
        RenderUtil.drawRect(x,y + 20,x + 70,y + 34, new Color(90,100,255,(int)(240 * alpha)).getRGB());
        RenderUtil.drawRect(x + 70,y + 10,x + 100,y + 34, new Color(110,100,255,(int)(240 * alpha)).getRGB());

        RenderUtil.drawRect(x,y + 198,x + 100,y + 200 , new Color(40, 40 ,40 , 255).getRGB());

        //Panel标题
        titlefont.drawString(this.name, x + 8, y + 13, new Color(50,50,50,(int)(255 * alpha)).getRGB());


        String info = "";
        String iconstr = "";
        switch (category.toString()){
            case "Combat":{
               // iconstr = HanabiFonts.ICON_CLICKGUI_COMBAT;
                info = "The COMBAT Module";
                break;
            }
            case "Movement":{
               // iconstr = HanabiFonts.ICON_CLICKGUI_MOVEMENT;
                info = "The MOVEMENT Module";
                break;
            }
            case "Player":{
               // iconstr = HanabiFonts.ICON_CLICKGUI_PLAYER;
                info = "The PLAYER Module";
                break;
            }
            case "Render":{
               // iconstr = HanabiFonts.ICON_CLICKGUI_RENDER;
                info = "The RENDER Module";
                break;
            }
            case "World":{
               // iconstr = HanabiFonts.ICON_CLICKGUI_WORLD;
                info = "The WORLD Module";
                break;
            }
            case "Ghost":{
               // iconstr = HanabiFonts.ICON_CLICKGUI_GHOST;
                info = "The GHOST Module";
                break;
            }
        }
        infofont.drawString(info, x + 8, y + 23, new Color(50,50,50,(int)(255 * alpha)).getRGB());
       // icon.drawString(iconstr, x + 80, y + 13, new Color(255,255,255,(int)(255 * alpha)).getRGB());

        //Mod显示
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor((int)x, (int)mstartY - 4, 100,(int) (y + 205 - 5 - (mstartY - 4)));
        float modY = translate.getX();
        for(Mod m : ModManager.getModules(category)){
            //判断搜索栏
            if(ClickUI.isSearching && !ClickUI.searchcontent.equalsIgnoreCase("") && ClickUI.searchcontent != null){
                if(!m.getName().toLowerCase().contains(ClickUI.searchcontent.toLowerCase())) continue;
            }
            boolean mhover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, x, mstartY + modY - 4, x + 100, mstartY + modY + 10) && ClickUI.isHover(mouseX, mouseY, x, mstartY - 4, x + 100, y + 260 - 5);

            /*if(m.getKeybind() != 0 && mhover) {
                RenderUtil.drawRect(x,y + 200,x + 100,y + 210 , new Color(60, 60 ,60 , 200).getRGB());
                String keybindinfo = "KeyBind is [ " + Keyboard.getKeyName(m.getKeybind()) + " ]";
                keybindfont.drawStringWithShadow(keybindinfo , x + 10 , y + 201, new Color(120,120,120,(int)(255 * alpha)).getRGB());
            }*/


            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.doGlScissor((int)x, (int)mstartY - 4, 100,(int) (y + 200 - 5 - (mstartY - 4)));

            if(m.isEnabled())
                font.drawStringWithShadow(m.getName(), x + 15, mstartY + modY, mhover ? new Color(100,255,100,(int)(255 * alpha)).getRGB() : new Color(120,120,120,(int)(255 * alpha)).getRGB());
            else
            font.drawStringWithShadow(m.getName(), x + 15, mstartY + modY, mhover ? new Color(180,180,180,(int)(255 * alpha)).getRGB() : new Color(255,255,255,(int)(255 * alpha)).getRGB());
            if(m.isEnabled())
                FontLoaders.micon15.drawString("B", x + 5, mstartY + modY + 1, mhover ? new Color(180,180,180,(int)(255 * alpha)).getRGB() : new Color(255,255,255,(int)(255 * alpha)).getRGB());
            if(m.hasValues())
                font.drawOutlinedString(">", x + 90, mstartY + modY, mhover ? new Color(180,180,180,(int)(255 * alpha)).getRGB() : new Color(255,255,255,(int)(255 * alpha)).getRGB() , new Color(0 ,0 ,0 ).getRGB());

            GL11.glDisable(3089);
            GL11.glPopMatrix();
            modY += maddY;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();

        //处理滚动
        float moduleHeight = modY - translate.getX() - 1;
        if (Mouse.hasWheel() && ClickUI.isHover(mouseX, mouseY, x, mstartY - 4, x + 100, y + 200 - 5) && ClickUI.currentMod == null) {
            if ((ClickUI.real > 0 && wheel < 0)) {
                for (int i = 0; i < 5; i++) {
                    if (!(wheel < 0))
                        break;
                    wheel += 5;
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (!(ClickUI.real < 0 && moduleHeight > y + 200 - 5 - (mstartY - 4) && Math.abs(wheel) < (moduleHeight - (y + 200 - 5 - (mstartY - 4)))))
                        break;
                    wheel -= 5;
                }
            }
        }
        translate.interpolate(wheel, 0, 20.0E-2f);

        //滚动条
        float sliderh = Math.min(y + 200 - 5 - (mstartY - 4), (y + 200 - 5 - (mstartY - 4)) * (y + 200 - 5 - (mstartY - 4)) / moduleHeight);
        float slidert =  -(y + 200 - 5 - (mstartY - 4) - sliderh) * (translate.getX()) / (moduleHeight - (y + 200 - 5 - (mstartY - 4)));
        if(sliderh < y + 200 - 5 - (mstartY - 4)) {
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.doGlScissor((int)x + 1, (int)mstartY - 4, 2,(int) (y + 200 - 5 - (mstartY - 4)));
            RenderUtil.drawRect(x + 1, mstartY - 4 + slidert, x + 2, mstartY - 4 + slidert + sliderh, new Color(255,255,255,(int)(255 * alpha)).getRGB());
            GL11.glDisable(3089);
            GL11.glPopMatrix();
        }
    }

    public void drawShadow(float mouseX, float mouseY) {
       /* //处理拖动
        if(needMove) setXY(mouseX - dragX, mouseY - dragY);
        if (!Mouse.isButtonDown(0) && needMove) needMove = false;

        //处理动画
        float alpha = 0.01f;
        float yani = 0;
        if(timer.hasElapsed(delay)) {
            anima.interpolate(100, 20, 4.0E-2f);
            alpha = anima.getX() / 100;
            yani = anima.getY();
        }

        x = desX;
        y = desY + 20 - yani;

        CFontRenderer titlefont = FontLoaders.robote18;
        CFontRenderer font = FontLoaders.kiona18;
        //UnicodeFontRenderer icon = Hanabi.INSTANCE.fontManager.icon30;

        float mstartY = y + 40;
        float maddY = 15;

        //背景阴影
        int spread = 2;
        //RenderUtil.drawRect(x - spread,y - spread,x + 100 + spread,y + 260 + spread, new Color(0,0,0,(int)(120 * alpha)).getRGB());

        //标题阴影
        /*titlefont.drawString(this.name, x + 8, y + 12, new Color(255,255,255,(int)(255 * alpha)).getRGB());
        String iconstr = "";
        switch (category.toString()){
            case "Combat":{
                iconstr = HanabiFonts.ICON_CLICKGUI_COMBAT;
                break;
            }
            case "Movement":{
                iconstr = HanabiFonts.ICON_CLICKGUI_MOVEMENT;
                break;
            }
            case "Player":{
                iconstr = HanabiFonts.ICON_CLICKGUI_PLAYER;
                break;
            }
            case "Render":{
                iconstr = HanabiFonts.ICON_CLICKGUI_RENDER;
                break;
            }
            case "World":{
                iconstr = HanabiFonts.ICON_CLICKGUI_WORLD;
                break;
            }
            case "Ghost":{
                iconstr = HanabiFonts.ICON_CLICKGUI_GHOST;
                break;
            }
        }
        icon.drawString(iconstr, x + 80, y + 13, new Color(255,255,255,(int)(255 * alpha)).getRGB());


        //Mod字体阴影
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor((int)x, (int)mstartY - 4, 100,(int) (y + 200 - 5 - (mstartY - 4)));
        float modY = translate.getX();
        for(Mod m : ModManager.getModules(category)){
            //判断搜索栏
            if(ClickUI.isSearching && !ClickUI.searchcontent.equalsIgnoreCase("") && ClickUI.searchcontent != null){
                if(!m.getName().toLowerCase().contains(ClickUI.searchcontent.toLowerCase())) continue;
            }
            boolean mhover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, x, mstartY + modY - 4, x + 140, mstartY + modY + 17) && ClickUI.isHover(mouseX, mouseY, x, mstartY - 4, x + 140, y + 260 - 5);
            font.drawString(m.getName(), x + 25, mstartY + modY, mhover ? new Color(70,70,70,(int)(255 * alpha)).getRGB() : new Color(0,0,0,(int)(255 * alpha)).getRGB());
            if(m.isEnabled())
                FontLoaders.micon15.drawString("B", x + 5, mstartY + modY + 2, mhover ? new Color(180,180,180,(int)(255 * alpha)).getRGB() : new Color(255,255,255,(int)(255 * alpha)).getRGB());
            if(m.hasValues())
                font.drawString(">", x + 90, mstartY + modY, mhover ? new Color(180,180,180,(int)(255 * alpha)).getRGB() : new Color(255,255,255,(int)(255 * alpha)).getRGB());

            modY += maddY;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();

        //处理滚动
        float moduleHeight = modY - translate.getX() - 1;
        if (Mouse.hasWheel() && ClickUI.isHover(mouseX, mouseY, x, mstartY - 4, x + 100, y + 200 - 5) && ClickUI.currentMod == null) {
            if ((ClickUI.real > 0 && wheel < 0)) {
                for (int i = 0; i < 5; i++) {
                    if (!(wheel < 0))
                        break;
                    wheel += 5;
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (!(ClickUI.real < 0 && moduleHeight > y + 200 - 5 - (mstartY - 4) && Math.abs(wheel) < (moduleHeight - (y + 200 - 5 - (mstartY - 4)))))
                        break;
                    wheel -= 5;
                }
            }
        }
        translate.interpolate(wheel, 0, 20.0E-2f);

        //滚动条阴影
        float sliderh = Math.min(y + 200 - 5 - (mstartY - 4), (y + 200 - 5 - (mstartY - 4)) * (y + 200 - 5 - (mstartY - 4)) / moduleHeight);
        float slidert =  -(y + 200 - 5 - (mstartY - 4) - sliderh) * (translate.getX()) / (moduleHeight - (y + 200 - 5 - (mstartY - 4)));
        if(sliderh < y + 200 - 5 - (mstartY - 4)) {
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.doGlScissor((int)x + 1, (int)mstartY - 4, 2,(int) (y + 200 - 5 - (mstartY - 4)));
            RenderUtil.drawRect(x + 1, mstartY - 4 + slidert, x + 2, mstartY - 4 + slidert + sliderh, new Color(255,255,255,(int)(120 * alpha)).getRGB());
            GL11.glDisable(3089);
            GL11.glPopMatrix();
        }
        */
    }

    public void handleMouseClicked(float mouseX, float mouseY, int key) {
        float mstartY = y + 40;
        float maddY = 15;

        //处理拖动
        boolean tophover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, x, y + 10, x + 100, mstartY);
        if(tophover && key == 0){
            dragX = mouseX - desX;
            dragY = mouseY - desY;
            needMove = true;
        }

        //处理Mod的MouseClicked的Event
        float modY = mstartY + translate.getX();
        for(Mod m : ModManager.getModules(category)){
            //判断搜索栏
            if(ClickUI.isSearching && !ClickUI.searchcontent.equalsIgnoreCase("") && ClickUI.searchcontent != null){
                if(!m.getName().toLowerCase().contains(ClickUI.searchcontent.toLowerCase())) continue;
            }
            boolean mhover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, x, modY - 4, x + 100, modY + 10) && ClickUI.isHover(mouseX, mouseY, x, mstartY - 4, x + 100, y + 260 - 7);
            if(mhover){
                if(key == 0) m.set(!m.isEnabled(),false);
                if(key == 1 && m.hasValues()) {
                    ClickUI.currentMod = m;
                    ClickUI.settingwheel = 0;
                    ClickUI.settingtranslate.setXY(0, 0);
                    ClickUI.animatranslate.setXY(0, 0);
                }
            }
            modY += maddY;
        }
    }

    public void handleMouseReleased(float mouseX, float mouseY, int key) {
        float mstartY = y + 40;

        //处理拖动
        boolean tophover = ClickUI.currentMod == null && ClickUI.isHover(mouseX, mouseY, x, y, x + 100, mstartY);
        if(tophover && key == 0){
            dragX = mouseX - desX;
            dragY = mouseY - desY;
            needMove = false;
        }
    }

    public void resetAnimation(){
        timer.reset();
        anima.setXY(1,0);
        needMove = false;
        dragX = 0;
        dragY = 0;
    }

    public void resetTranslate(){
        translate.setXY(0,0);
        wheel = 0;
    }

    public void setXY(float x, float y){
        this.desX = x;
        this.desY = y;
    }
}
