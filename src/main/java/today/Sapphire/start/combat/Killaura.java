package today.Sapphire.start.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import today.Sapphire.events.EventPreMotion;
import today.Sapphire.events.EventRender;
import today.Sapphire.start.Mod;
import today.Sapphire.start.Value;
import today.Sapphire.utils.RotationUtil;
import today.Sapphire.utils.rotationLib.Angle;
import today.Sapphire.utils.rotationLib.AngleUtility;
import today.Sapphire.utils.rotationLib.Vector3;
import today.Sapphire.utils.timeUtils.TimeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Killaura extends Mod {

	private final Value<String> mode;
	private final Value<Double> MaxSpeed;
	private final Value<Double> MinSpeed;
	public final Value<Double> hurtTime;
	private final Value<Double> blockRange;
	private final Value<Double> range;

	private final Value<Boolean> players;
	private final Value<Boolean> Teams;
	private final Value<Boolean> animals;
	public final Value<Boolean> mobs;
	private final Value<Boolean> autoblock;
	private final Value<Boolean> invisibles;
	public final Value<Boolean> blockRayTrace;

	public static EntityLivingBase target;
	public static EntityLivingBase blockTarget;

	public static boolean blockingStatus = false;

	public static long randomClickDelay(final int minCPS, final int maxCPS) {
		return (long) ((Math.random() * (((1000 / minCPS) - (1000 / maxCPS)) + 1)) + (1000 / maxCPS));
	}

	private final List<EntityLivingBase> targets = new ArrayList();

	private final TimeHelper attacktimer = new TimeHelper();
	private int click = 0;


	public Killaura() {
		super("Killaura", Category.Combat);
		mode = new Value("Killaura", "mode", new String[]{"Switch", "Single"}, 0);

		MaxSpeed = new Value("Killaura", "MaxCps", 12.0, 1.0, 20.0, 1.0);
		MinSpeed = new Value("Killaura", "MinCps", 8.0, 1.0, 20.0, 1.0);
		hurtTime = new Value("Killaura", "HurtTime", 10, 0, 10, 1);
		blockRange = new Value("Killaura", "BlockRange", 4.8, 1.0, 12.0, 0.1);
		range = new Value("Killaura", "Range", 4.2, 1.0, 7.0, 0.1);
		players = new Value("Killaura", "Players", true);
		Teams = new Value("Killaura", "Teams", true);
		animals = new Value("Killaura", "Animals", false);
		mobs = new Value("Killaura", "Mobs", false);
		autoblock = new Value("Killaura", "AutoBlock", true);
		invisibles = new Value("Killaura", "Invisibles", false);
		blockRayTrace = new Value("Killaura", "blockRayTrace", true);
	}

	@Override
	public void Alwas() {
		setDisplayName(mode.getModeName());
	}

	@Override
	public void onDisable() {
		this.targets.clear();
		target = null;
		blockTarget = null;
		if (this.autoblock.getValueState()) {
			this.stopBlocking();
		}
	}

	@Override
	public void onEnable() {
		targets.clear();
		target = null;
		blockTarget = null;
	}

	@EventTarget
	public void onUpdate(final EventPreMotion event) {
		targets.clear();
		target = getBestTarget(this.range.getValueState());
		blockTarget = getBestTarget(this.blockRange.getValueState());

		if (target != null) {
			event.setRotation(RotationLib());

		} else {

			blockTarget = null;
			target = null;
			targets.clear();

			if (blockingStatus) {
				stopBlocking();
			}
		}

		if (target != null) {
			while (click > 0) {
				doAttack();
				click--;
			}
		}
	}

	@EventTarget
	public void onRender(EventRender render) { // Copy
		if (target != null && attacktimer.isDelayComplete(randomClickDelay(Math.min(MinSpeed.getValue().intValue(), MaxSpeed.getValue().intValue()), Math.max(MinSpeed.getValue().intValue(), MaxSpeed.getValue().intValue())))) {
			click++;
			attacktimer.reset();
		}
	}

	private void doAttack() {
		if (blockTarget != null && mc.thePlayer.getDistanceToEntity(blockTarget) <= blockRange.getValueState()) {

			if ((mc.thePlayer.isBlocking() || blockingStatus) && isHoldingSword())
				stopBlocking();

			if (mc.thePlayer.getDistanceToEntity(target) <= this.range.getValueState())
				attack(target);

			startBlocking(mc.thePlayer.isBlocking());
		}
	}

	public float[] RotationLib() {
		AngleUtility angleUtility = new AngleUtility(6, 60, 3, 30);
		Vector3<Double> enemyCoords = new Vector3<>(target.posX, target.posY, target.posZ);
		Vector3<Double> myCoords = new Vector3<>(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
		Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
		Angle smoothedAngle1 = angleUtility.smoothAngle(dstAngle, dstAngle);
		return new float[]{smoothedAngle1.getYaw(), smoothedAngle1.getPitch()};
	}

	/**
	 * Start blocking
	 */
	private void startBlocking(final boolean always) {
		if (isHoldingSword() && (!blockingStatus || always)) {
			mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
			blockingStatus = true;
		}

	}

	/**
	 * Stop blocking
	 */
	private void stopBlocking() {
		if (blockingStatus) {
			mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
				C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			blockingStatus = false;
		}
	}

	private void attack(final EntityLivingBase target) {
		mc.thePlayer.swingItem();
		mc.playerController.attackEntity(mc.thePlayer, target);
	}

	private static int randomNumber(double min, double max) {
		Random random = new Random();
		return (int) (min + (random.nextDouble() * (max - min)));
	}

	public boolean isHoldingSword() {
		return autoblock.getValueState() && (mc.thePlayer.getCurrentEquippedItem() != null)
			&& (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)
			;
	}

	private void sortList(List<EntityLivingBase> weed) {
		weed.sort((o1, o2) -> {
			float[] rot1 = RotationUtil.getRotations(o1);
			float[] rot2 = RotationUtil.getRotations(o2);
			return (int) ((o1.getDistanceToEntity(mc.thePlayer) * 1000)
				- (o2.getDistanceToEntity(mc.thePlayer) * 1000));
		});
	}

	private EntityLivingBase getBestTarget(double range) {
		this.targets.clear();
		for (final Entity e : mc.theWorld.loadedEntityList) {
			if (e instanceof EntityLivingBase) {
				final EntityLivingBase ent = (EntityLivingBase) e;
				if (this.validEntity(ent, range)) {
					this.targets.add(ent);
				}
			}
		}
		sortList(targets);
		if (this.targets.isEmpty()) {
			return null;
		}
		return this.targets.get(0);
	}

	boolean validEntity(EntityLivingBase entity, double range) {
		if ((mc.thePlayer.isEntityAlive()) && !(entity instanceof EntityPlayerSP)) {
			if (mc.thePlayer.getDistanceToEntity(entity) <= range) {

				if (!RotationUtil.canEntityBeSeen(entity))
					return false;
				if (entity.isPlayerSleeping())
					return false;

				if (entity instanceof EntityPlayer) {
					if (this.players.getValueState()) {
						EntityPlayer player = (EntityPlayer) entity;
						if (!player.isEntityAlive() && player.getHealth() == 0.0) {
							return false;
						} else if (isOnSameTeam(player, this.Teams.getValueState()))
							return false;
						else if (player.isInvisible() && !this.invisibles.getValueState()) {
							return false;
						} else
							return true;
					}
				} else {
					if (!entity.isEntityAlive())
						return false;
				}

				if (entity instanceof EntityMob && !mobs.getValueState()) {
					return false;
				}

				if((entity instanceof EntityIronGolem || entity instanceof EntityAnimal || entity instanceof EntityVillager) && !this.animals.getValueState())
					return false;

				return  true;
			}
		}
		return false;
	}

	public static boolean isOnSameTeam(final Entity entity, final boolean teams) {
		if (teams) {
			if (mc.thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
				if ((mc.thePlayer.getDisplayName().getUnformattedText().length() <= 2)
					|| (entity.getDisplayName().getUnformattedText().length() <= 2)) {
					return false;
				}
				if (mc.thePlayer.getDisplayName().getUnformattedText().substring(0, 2)
					.equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
					return true;
				}
			}
		}
		return false;
	}
}
