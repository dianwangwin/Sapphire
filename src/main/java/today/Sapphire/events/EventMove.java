package today.Sapphire.events;

import com.darkmagician6.eventapi.events.Event;

public class EventMove
implements Event{
    public double x;
    public double y;
    public double z;

    public EventMove(double a2, double b2, double c2) {
        this.x = a2;
        this.y = b2;
        this.z = c2;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x2) {
        this.x = x2;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y2) {
        this.y = y2;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z2) {
        this.z = z2;
    }
}

