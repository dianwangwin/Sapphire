package today.Sapphire.start.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import today.Sapphire.events.EventReceivePacket;
import today.Sapphire.start.Mod;

public class AutoTool extends Mod {
	private static Minecraft mc = Minecraft.getMinecraft();

	public AutoTool() {
		super("AutoTool", Category.Player);
    }

	@EventTarget
    public void handle(EventReceivePacket event) {
        if (!(event.getPacket() instanceof C07PacketPlayerDigging)) {
            return;
        }
        C07PacketPlayerDigging packet = (C07PacketPlayerDigging)event.getPacket();
        if (packet.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            AutoTool.autotool(packet.getPosition());
        }
    }

    private static void autotool(BlockPos position) {
        Block block = mc.theWorld.getBlockState(position).getBlock();
        int itemIndex = AutoTool.getStrongestItem(block);
        if (itemIndex < 0) {
            return;
        }
        float itemStrength = AutoTool.getStrengthAgainstBlock(block, mc.thePlayer.inventory.mainInventory[itemIndex]);
        if (mc.thePlayer.getHeldItem() != null && AutoTool.getStrengthAgainstBlock(block, mc.thePlayer.getHeldItem()) >= itemStrength) {
            return;
        }
        mc.thePlayer.inventory.currentItem = itemIndex;
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(itemIndex));
    }

    private static int getStrongestItem(Block block) {
        float strength = Float.NEGATIVE_INFINITY;
        int strongest = -1;
        int i = 0;
        while (i < 9) {
            float itemStrength;
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() != null && (itemStrength = AutoTool.getStrengthAgainstBlock(block, itemStack)) > strength && itemStrength != 1.0f) {
                strongest = i;
                strength = itemStrength;
            }
            ++i;
        }
        return strongest;
    }

    public static float getStrengthAgainstBlock(Block block, ItemStack item) {
        float strength = item.getStrVsBlock(block);
        if (!EnchantmentHelper.getEnchantments(item).containsKey(Enchantment.efficiency.effectId) || strength == 1.0f) {
            return strength;
        }
        int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, item);
        return strength + (float)(enchantLevel * enchantLevel + 1);
    }

}
