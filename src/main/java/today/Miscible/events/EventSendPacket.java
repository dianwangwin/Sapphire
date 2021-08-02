package today.Miscible.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.network.Packet;

public class EventSendPacket extends EventCancellable {
    public Packet packet;

    public EventSendPacket( Packet packet) {
//        this.eventType = eventType;
        this.packet = packet;
    }

//    public EventType getEventType() {
//        return eventType;
//    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
