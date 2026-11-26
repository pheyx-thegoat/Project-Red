package projectred.mixin;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projectred.item.CarmineScytheItem;
import projectred.util.CarmineFx;

@Mixin(ServerPlayer.class)
public abstract class ScytheCritMixin {
	@Inject(method = "crit", at = @At("HEAD"))
	private void projectred$crit(Entity target, CallbackInfo ci) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		ItemStack stack = player.getMainHandItem();
		if (!(stack.getItem() instanceof CarmineScytheItem)) return;

		player.heal(1.0F);

		if (player.level() instanceof ServerLevel level && target instanceof LivingEntity living) {
			CarmineFx.crit(level, living);
			yank(player, living);
		}
	}

	private static void yank(ServerPlayer player, LivingEntity target) {
		double dx = player.getX() - target.getX();
		double dz = player.getZ() - target.getZ();
		double dist = Math.sqrt(dx * dx + dz * dz);
		if (dist < 0.05 || dist > 32.0) return;

		double speed = Math.min(2.0, 0.6 + dist * 0.3);
		double dy = target.onGround() ? 0.25 : target.getDeltaMovement().y;
		target.setDeltaMovement(dx / dist * speed, dy, dz / dist * speed);
		target.hurtMarked = true;
		target.needsSync = true;

		if (target instanceof ServerPlayer victim) {
			victim.connection.send(new ClientboundSetEntityMotionPacket(victim));
		}
	}
}
