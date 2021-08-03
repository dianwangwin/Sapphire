package today.Sapphire.events;

import net.minecraft.util.BlockPos;
import com.darkmagician6.eventapi.events.Event;

public class EventXray implements Event {

	   public BlockPos pos;

	   public EventXray( BlockPos p) {
	      this.pos = p;
	   }
	   
}
