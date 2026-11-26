package projectred.client.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projectred.client.ClientShake;

@Mixin(Camera.class)
public abstract class CameraShakeMixin {
	@Shadow
	private float yRot;

	@Shadow
	private float xRot;

	@Shadow
	public abstract Vec3 position();

	@Shadow
	protected abstract void setRotation(float yRot, float xRot);

	@Shadow
	protected abstract void setPosition(Vec3 position);

	@Inject(method = "alignWithEntity", at = @At("TAIL"))
	private void projectred$shakeCamera(float partialTicks, CallbackInfo ci) {
		float[] shake = ClientShake.sample();
		if (shake == null) return;

		setRotation(yRot + shake[0], xRot + shake[1]);
		setPosition(position().add(shake[2], shake[3], shake[4]));
	}
}
