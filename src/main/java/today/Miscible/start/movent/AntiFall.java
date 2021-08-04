package today.Miscible.start.movent;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import today.Miscible.events.EventMove;
import today.Miscible.events.EventReceivePacket;
import today.Miscible.start.Mod;
import today.Miscible.start.ModManager;
import today.Miscible.start.Value;
import today.Miscible.utils.timeUtils.TimeHelper;

public class AntiFall extends Mod {

    public static Value<Boolean> VOID = new Value("AntiFall", "Void", true);
    public Value<Double> DISTANCE = new Value<Double>("AntiFall", "distance", 5.0, 1.0, 20.0, 1.0);
    public Value<String> mode = new Value("AntiFall", "Mode", 0);
    private TimeHelper timer = new TimeHelper();
    private boolean saveMe;

    public AntiFall() {
        super("AntiFall", Category.Movement);
        this.mode.mode.add("Hypixel");

    }

    @EventTarget
    public void onPre(EventMove em) {

        if (this.isBlockUnder() && !this.saveMe) {
            if (ModManager.getModByName("NoFall").isEnabled())
                return;
        }

        if (mc.thePlayer.capabilities.isFlying) {
            return;
        }
        if (this.saveMe && this.timer.delay(350.0f) || mc.thePlayer.isCollidedVertically) {
            this.saveMe = false;
            this.timer.reset();
            return;
        }
        int dist = this.DISTANCE.getValueState().intValue();
        if (!(mc.thePlayer.fallDistance < (float) dist // || ModManager.getModByName("Fly").isEnabled()
                //	|| ModManager.getModByName("ZoomFly").isEnabled()
                || mc.theWorld
                .getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
                .getBlock() != Blocks.air
                || ((Boolean) this.VOID.getValueState().booleanValue() && this.isBlockUnder()))
                && this.timer.delay(900.0f)) {
            if (this.timer.delay(600.0f))
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (double) 5, mc.thePlayer.posZ);
            timer.reset();
        }

    }

    @EventTarget
    public void onPre(EventReceivePacket ep) {
        if (!(ep.getPacket() instanceof S08PacketPlayerPosLook))
            return;
        if (mc.thePlayer.fallDistance > 0.0f) {
            mc.thePlayer.fallDistance = 0.0f;
        }
        mc.thePlayer.motionZ = 0.0;
        mc.thePlayer.motionX = 0.0;
        this.saveMe = false;
        this.timer.reset();
    }

    private boolean isBlockUnder() {
        int i = (int) mc.thePlayer.posY;
        while (i > 0) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, (double) i, mc.thePlayer.posZ);
            if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
            --i;
        }
        return false;
    }

}
