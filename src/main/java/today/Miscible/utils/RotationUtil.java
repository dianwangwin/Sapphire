package today.Miscible.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {
	private static Minecraft mc = Minecraft.getMinecraft();
	
    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY-1.2D;
        double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
        return new float[]{yaw, pitch};
     }
	
	
	public static float[] getRotations(EntityLivingBase ent) {
		final double x = ent.posX - Minecraft.getMinecraft().thePlayer.posX;
		final double z = ent.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double y = ent.posY - Minecraft.getMinecraft().thePlayer.posY - 0.65;
		y /= Minecraft.getMinecraft().thePlayer.getDistanceToEntity(ent);
		final float yaw = (float) (-(Math.atan2(x, z) * 60));
		final float pitch = (float) (-(Math.asin(y) * 60));
		return new float[] { yaw, pitch };
	}

	public static float[] getFacePosEntity(Entity en) {
		if (en == null) {
			float[] arrf = new float[2];
			Minecraft.getMinecraft();
			arrf[0] = mc.thePlayer.rotationYawHead;
			Minecraft.getMinecraft();
			arrf[1] = mc.thePlayer.rotationPitch;
			return arrf;
		}
		return getFacePos(new Vec3(en.posX, en.posY + ((double) en.getEyeHeight() - (double) en.height / 2), en.posZ));
	}

	public static float[] getFacePos(Vec3 vec) {
		Minecraft.getMinecraft();
		double diffX = vec.xCoord - mc.thePlayer.posX;
		Minecraft.getMinecraft();
		Minecraft.getMinecraft();
		double diffY = vec.yCoord + 1.0 - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
		Minecraft.getMinecraft();
		double diffZ = vec.zCoord - mc.thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) ((-Math.atan2(diffY, dist)) * 180.0 / 3.141592653589793);
		float[] arrf = new float[2];
		Minecraft.getMinecraft();
		Minecraft.getMinecraft();
		arrf[0] = mc.thePlayer.rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
		Minecraft.getMinecraft();
		Minecraft.getMinecraft();
		arrf[1] = mc.thePlayer.rotationPitch
				+ MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
		return arrf;
	}
	
	public static boolean canEntityBeSeen(Entity e){
		Vec3 vec1 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),mc.thePlayer.posZ);

		AxisAlignedBB box = e.getEntityBoundingBox();
	    Vec3 vec2 = new Vec3(e.posX, e.posY + (e.getEyeHeight()/1.32F),e.posZ);	
	    double minx = e.posX - 0.25;
	    double maxx = e.posX + 0.25;
	    double miny = e.posY;
	    double maxy = e.posY + Math.abs(e.posY - box.maxY) ;
	    double minz = e.posZ - 0.25;
	    double maxz = e.posZ + 0.25;
	    boolean see =  mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    
	    vec2 = new Vec3(maxx, maxy,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,minz);	

	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,maxz - 0.1);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx, maxy,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    

		return false;
	}

}
