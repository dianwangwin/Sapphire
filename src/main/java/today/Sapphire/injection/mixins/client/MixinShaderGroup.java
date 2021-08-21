package today.Sapphire.injection.mixins.client;

import com.google.common.collect.Lists;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import today.Sapphire.injection.interfaces.IShaderGroup;

import java.util.List;

@Mixin(ShaderGroup.class)
public abstract class MixinShaderGroup implements IShaderGroup {

	@Shadow
	@Final
	private final List<Shader> listShaders = Lists.<Shader>newArrayList();

	@Shadow
	public abstract void createBindFramebuffers(int width, int height);

	@Shadow
	public abstract void loadShaderGroup(float partialTicks);


	@Override
	public List<Shader> getShaders() {
		return listShaders;
	}
}
