package today.Miscible.events;

import com.darkmagician6.eventapi.events.Event;

public class EventText implements Event {

	public String Text;

	public EventText(String txt) {
		this.Text = txt;
	}

	public String getText() {
		return Text;
	}

	public void setText(String txt) {
		this.Text = txt;
	}
}
