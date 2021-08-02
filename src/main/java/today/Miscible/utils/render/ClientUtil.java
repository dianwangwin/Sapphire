package today.Miscible.utils.render;

import java.awt.*;

public enum ClientUtil {
    INSTANCE;

  /*  public static void sendClientMessage(String message, Type type) {
        notifications.add(new ClientNotification(message, type));
    }*/

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = ((float) 1 / 255) * c.getRed();
        float g = ((float) 1 / 255) * c.getGreen();
        float b = ((float) 1 / 255) * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }
}
