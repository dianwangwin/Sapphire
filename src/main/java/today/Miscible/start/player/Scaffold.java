package today.Miscible.start.player;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import today.Miscible.events.EventPostMotion;
import today.Miscible.events.EventPreMotion;
import today.Miscible.events.EventRender2D;
import today.Miscible.start.Mod;
import today.Miscible.start.Value;
import today.Miscible.utils.MoveUtil;
import today.Miscible.utils.timeUtils.TimeHelper;

public class Scaffold extends Mod {
	private BlockData blockData;
	private TimeHelper time = new TimeHelper();
	private Value<Boolean> tower = new Value<Boolean>("Scaffold","Tower", true);
	private Value<Boolean> noSwing = new Value<Boolean>("Scaffold","NoSwing", false);
	private Value<Boolean> silent = new Value<Boolean>("Scaffold","Silent", false);
	public Value<Double> delayValue = new Value<Double>("Scaffold","Delay", 250.0, 40.0, 1000.0, 10.0);
	public Value<String> mode = new Value("Scaffold", "Mode", 0);
	private BlockPos blockpos;
	private EnumFacing facing;
	private List<Block> blacklisted;
	private List<Block> blacklistedBlocks;
	int slot;

	public Scaffold() {
		super("Scaffold", Category.Movement);
		mode.mode.add("Normal");
		mode.mode.add("Hypixel");
		this.showValue = mode;
		this.setDisplayName(this.getValue());
		blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
				Blocks.flowing_lava, Blocks.enchanting_table, Blocks.ender_chest, Blocks.yellow_flower, Blocks.carpet,
				Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.crafting_table,
				Blocks.snow_layer, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
				Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.gold_ore,
				Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.redstone_ore,
				Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
				Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.cactus,
				Blocks.lever, Blocks.activator_rail, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail,
				Blocks.furnace, Blocks.ladder, Blocks.oak_fence, Blocks.redstone_torch, Blocks.iron_trapdoor,
				Blocks.trapdoor, Blocks.tripwire_hook, Blocks.hopper, Blocks.acacia_fence_gate, Blocks.birch_fence_gate,
				Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate, Blocks.oak_fence_gate,
				Blocks.dispenser, Blocks.sapling, Blocks.tallgrass, Blocks.deadbush, Blocks.web, Blocks.red_flower,
				Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.nether_brick_fence, Blocks.vine, Blocks.double_plant,
				Blocks.flower_pot, Blocks.beacon, Blocks.pumpkin, Blocks.lit_pumpkin);
		blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
				Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button,
				Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon);

	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		ScaledResolution res = new ScaledResolution(this.mc);
		int color = new Color(255, 0, 0, 150).getRGB();
		if (this.getBlockCount() >= 64 && 128 > this.getBlockCount()) {
			color = new Color(255, 255, 0, 150).getRGB();
		} else if (this.getBlockCount() >= 128) {
			color = new Color(0, 255, 0, 150).getRGB();
		}
		mc.fontRendererObj.drawStringWithShadow(String.valueOf(this.getBlockCount()), res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(String.valueOf(this.getBlockCount())) / 2,
				res.getScaledHeight() / 2 - 15, color);
	}

	@EventTarget
	public void onPre(EventPreMotion event) {
		if (invCheck() || this.getBlockCount() == -1) {
			return;
		}

		if (invCheck()) {
			return;
		}

		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY - 1.0;
		double z = mc.thePlayer.posZ;
		double forward = this.mc.thePlayer.movementInput.moveForward;
		double strafe = this.mc.thePlayer.movementInput.moveStrafe;
		float yaw = this.mc.thePlayer.rotationYaw;
		x += forward * 0.4 * Math.cos(Math.toRadians(yaw + 90.0f))
				+ strafe * 0.4 * Math.sin(Math.toRadians(yaw + 90.0f));
		z += forward * 0.4 * Math.sin(Math.toRadians(yaw + 90.0f))
				- strafe * 0.4 * Math.cos(Math.toRadians(yaw + 90.0f));
		BlockPos blockBelow = new BlockPos(x, y, z);

		if (Minecraft.getMinecraft().thePlayer != null) {
			this.blockData = this.getBlockData(blockBelow, this.blacklistedBlocks);
			if (this.blockData == null)
				this.blockData = this.getBlockData(blockBelow.down(), this.blacklistedBlocks);
			if ((mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.air)) {
				if (this.blockData != null) {
					event.pitch = 69.5f;
				}

				if (blockData != null && mc.gameSettings.keyBindJump.pressed && tower.getValueState()
						&& !MoveUtil.moving()) {
					mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
					mc.thePlayer.motionY = 0.4196;
				}
			}
		}

		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			if (MoveUtil.moving()) {
				if (MoveUtil.isOnGround(0.76) && !MoveUtil.isOnGround(0.75) && mc.thePlayer.motionY > 0.23
						&& mc.thePlayer.motionY < 0.25) {
					mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
				}
				if (MoveUtil.isOnGround(1.0E-4)) {
					mc.thePlayer.motionY = 0.42;
				} else if (mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 1.0E-4
						&& mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 1.0E-4) {
					mc.thePlayer.motionY = 0;
				}
			}
		}
	}

	@EventTarget
	public void onSafe(EventPostMotion event) {// Normal

		for (int i = 36; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (item instanceof ItemBlock && !blacklisted.contains(((ItemBlock) item).getBlock())
						&& !((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest")
						&& blockData != null) {
					int currentItem = mc.thePlayer.inventory.currentItem;

					// Swap to block.

					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
					mc.thePlayer.inventory.currentItem = i - 36;
					mc.playerController.updateController();

					Vec3 Power = getVec3(blockData.position, blockData.face);

					// Caused a null pointer for some reason, will look into soon.
					try {
						if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld,
								this.mc.thePlayer.getHeldItem(), BlockData.position, BlockData.face, Power)) {
							if (this.noSwing.getValueState().booleanValue()) {
								Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
							} else {
								Minecraft.getMinecraft().thePlayer.swingItem();
							}
						}
					} catch (Exception ignored) {

					}
					// Reset to current hand.
					mc.thePlayer.inventory.currentItem = currentItem;
					mc.playerController.updateController();
					return;
				}
			}
		}

		if (this.invCheck()) {
			int i = 9;
			while (i < 36) {
				Item item;
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
						&& (item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock
						&& !blacklisted.contains(((ItemBlock) item).getBlock())
						&& !((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest")) {
					this.swap(i, 7);
					break;
				}
				++i;
			}
		}
	}

	public static float randomFloat(long seed) {
		seed = System.currentTimeMillis() + seed;
		return 0.3f + (float) new Random(seed).nextInt(70000000) / 1.0E8f + 1.458745E-8f;
	}

	protected void swap(int slot, int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	private boolean invCheck() {
		int i = 36;
		while (i < 45) {
			Item item;
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					&& (item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock
					&& !blacklisted.contains(((ItemBlock) item).getBlock())) {
				return false;
			}
			++i;
		}
		return true;
	}

	private double getDoubleRandom(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	public static double randomNumber(double max, double min) {
		return (Math.random() * (max - min)) + min;
	}

	private boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos,
			EnumFacing side, Vec3 vec3) {
		if (heldStack.getItem() instanceof ItemBlock) {
			return ((ItemBlock) heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack);
		}
		return false;
	}

	public static Vec3 getVec3(BlockPos pos, EnumFacing face) {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		x += (double) face.getFrontOffsetX() / 2;
		z += (double) face.getFrontOffsetZ() / 2;
		y += (double) face.getFrontOffsetY() / 2;
		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += randomNumber(0.3, -0.3);
			z += randomNumber(0.3, -0.3);
		} else {
			y += randomNumber(0.3, -0.3);
		}
		if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
			z += randomNumber(0.3, -0.3);
		}
		if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
			x += randomNumber(0.3, -0.3);
		}
		return new Vec3(x, y, z);
	}

	private void setBlockAndFacing(BlockPos bp) {
		if (Minecraft.getMinecraft().theWorld.getBlockState(bp.add(0, -1, 0)).getBlock() != Blocks.air) {
			this.blockpos = bp.add(0, -1, 0);
			this.facing = EnumFacing.UP;
		} else if (Minecraft.getMinecraft().theWorld.getBlockState(bp.add(-1, 0, 0)).getBlock() != Blocks.air) {
			this.blockpos = bp.add(-1, 0, 0);
			this.facing = EnumFacing.EAST;
		} else if (Minecraft.getMinecraft().theWorld.getBlockState(bp.add(1, 0, 0)).getBlock() != Blocks.air) {
			this.blockpos = bp.add(1, 0, 0);
			this.facing = EnumFacing.WEST;
		} else if (Minecraft.getMinecraft().theWorld.getBlockState(bp.add(0, 0, -1)).getBlock() != Blocks.air) {
			this.blockpos = bp.add(0, 0, -1);
			this.facing = EnumFacing.SOUTH;
		} else if (Minecraft.getMinecraft().theWorld.getBlockState(bp.add(0, 0, 1)).getBlock() != Blocks.air) {
			this.blockpos = bp.add(0, 0, 1);
			this.facing = EnumFacing.NORTH;
		} else {
			bp = null;
			this.facing = null;
		}
	}

	private int getBlockCount() {
		int blockCount = 0;
		int i = 0;
		while (i < 45) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock && !blacklisted.contains(((ItemBlock) item).getBlock())) {
					blockCount += is.stackSize;
				}
			}
			++i;
		}
		return blockCount;
	}

	private int getBlockSlot() {
		for (int i = 36; i < 45; ++i) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock) || itemStack.stackSize <= 0)
				continue;
			if (blacklisted.stream().anyMatch(e -> e.equals(((ItemBlock) itemStack.getItem()).getBlock())))
				continue;
			return i - 36;
		}
		return -1;
	}

	private BlockData getBlockData(final BlockPos pos, final List list) {
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add = pos.add(-1, 0, 0);
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
			return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
			return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
			return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add2 = pos.add(1, 0, 0);
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
			return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
			return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add3 = pos.add(0, 0, -1);
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
			return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
			return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add4 = pos.add(0, 0, 1);
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
			return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
			return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
			return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
		}
		return null;
	}

	public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
		if (face == EnumFacing.NORTH) {
			return new Vec3(pos.getX(), pos.getY(), (double) pos.getZ() - 0.5);
		}
		if (face == EnumFacing.EAST) {
			return new Vec3((double) pos.getX() + 0.5, pos.getY(), pos.getZ());
		}
		if (face == EnumFacing.SOUTH) {
			return new Vec3(pos.getX(), pos.getY(), (double) pos.getZ() + 0.5);
		}
		if (face == EnumFacing.WEST) {
			return new Vec3((double) pos.getX() - 0.5, pos.getY(), pos.getZ());
		}
		return new Vec3(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		mc.timer.timerSpeed = 1.0f;
	}

	private static class BlockData {
		public static BlockPos position;
		public static EnumFacing face;

		public BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}

		private BlockPos getPosition() {
			return this.position;
		}

		private EnumFacing getFacing() {
			return this.face;
		}

		static BlockPos access$0(BlockData var0) {
			return var0.getPosition();
		}

		static EnumFacing access$1(BlockData var0) {
			return var0.getFacing();
		}

		static BlockPos access$2(BlockData var0) {
			return var0.position;
		}

		static EnumFacing access$3(BlockData var0) {
			return var0.face;
		}
	}
}
