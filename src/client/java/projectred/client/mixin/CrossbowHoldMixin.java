package projectred.client.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import projectred.item.CarmineLongswordItem;

@Mixin(AvatarRenderer.class)
public abstract class CrossbowHoldMixin {
	@Inject(
		method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void projectred$crossbowHold(
		Avatar avatar,
		HumanoidArm arm,
		CallbackInfoReturnable<HumanoidModel.ArmPose> cir
	) {
		if (arm == avatar.getMainArm()
			&& !avatar.swinging
			&& avatar.getMainHandItem().getItem() instanceof CarmineLongswordItem) {
			cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
		}
	}
}
