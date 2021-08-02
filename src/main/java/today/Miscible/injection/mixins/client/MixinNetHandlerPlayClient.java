package today.Miscible.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import today.Miscible.events.EventSendPacket;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
	
	
	@Inject(method = "addToSendQueue", at = @At("HEAD"), cancellable = true)
    private void senpacket(Packet<?> packet, final CallbackInfo callbackInfo) {
        final EventSendPacket event = new EventSendPacket(packet);
        EventManager.call(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

}
