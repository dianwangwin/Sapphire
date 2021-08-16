package today.Sapphire.injection.mixins.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import today.Sapphire.events.EventAnimation;
import today.Sapphire.start.ModManager;
import today.Sapphire.start.combat.Killaura;
import today.Sapphire.start.misc.Animation;

@Mixin({ ItemRenderer.class })
@SideOnly(Side.CLIENT)
public abstract class MixinItemRenderer {
	
	@Shadow
    private float prevEquippedProgress;

    @Shadow
    private float equippedProgress;

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract void rotateArroundXAndY(float angle, float angleY);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);

    @Shadow
    protected abstract void transformFirstPersonItem(float equipProgress, float swingProgress);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void doItemUsedTransformations(float swingProgress);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, TransformType transform);

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);


    /**
     * @reason liquidslowly
     * @author liquidslowly
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations(abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if(this.itemToRender != null) {
            final Killaura killAura = (Killaura) ModManager.getModuleForClass(Killaura.class);
            final Animation animations = (Animation) ModManager.getModuleForClass(Animation.class);

            if(this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0 || (itemToRender.getItem() instanceof ItemSword && killAura.blockingStatus)) {
                EnumAction enumaction = killAura.blockingStatus ? EnumAction.BLOCK : this.itemToRender.getItemUseAction();

                switch(enumaction) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, f1);
                        break;
                    case BLOCK:
                        if(animations.isEnabled()){
                            EventManager.call(new EventAnimation(f,f1));
                            break;
                        } else {
                            this.transformFirstPersonItem(f, f1);
                            this.doBlockTransformations();
                        }
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, f1);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                }
            }else{
                    this.doItemUsedTransformations(f1);
                    this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(abstractclientplayer, this.itemToRender, TransformType.FIRST_PERSON);
        }else if(!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
}
