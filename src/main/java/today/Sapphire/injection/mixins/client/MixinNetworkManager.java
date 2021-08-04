package today.Sapphire.injection.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import today.Sapphire.events.EventReceivePacket;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

	@Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
	private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
		final EventReceivePacket event = new EventReceivePacket(packet);
		EventManager.call(event);

		if (event.isCancelled())
			callback.cancel();
	}

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void send(Packet<?> packet, CallbackInfo callback) {
		final EventReceivePacket event = new EventReceivePacket(packet);
		EventManager.call(event);

		if (event.isCancelled())
			callback.cancel();
	}

}
