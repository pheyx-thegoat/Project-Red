package projectred.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import projectred.util.CarmineFx;
import projectred.util.ScreenShake;

public class CarmineLeviathanAxeItem extends AxeItem {
	public CarmineLeviathanAxeItem(
		ToolMaterial material,
		float attackDamageBaseline,
		float attackSpeedBaseline,
		Item.Properties properties
	) {
		super(material, attackDamageBaseline, attackSpeedBaseline, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null || !player.onGround()) return InteractionResult.PASS;

		if (context.getLevel().isClientSide()) {
			ScreenShake.smash();
			return InteractionResult.SUCCESS;
		}

		if (!(context.getLevel() instanceof ServerLevel level)) return InteractionResult.PASS;

		smash(level, player, context.getClickedPos().above());
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!player.onGround()) return InteractionResult.PASS;

		if (level.isClientSide()) {
			ScreenShake.smash();
			return InteractionResult.SUCCESS;
		}

		if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;

		smash(serverLevel, player, player.blockPosition());
		return InteractionResult.SUCCESS;
	}

	private void smash(ServerLevel level, Player player, BlockPos impact) {
		double x = impact.getX() + 0.5;
		double y = impact.getY() + 0.15;
		double z = impact.getZ() + 0.5;

		level.playSound(null, x, y, z, SoundEvents.MACE_SMASH_GROUND, SoundSource.PLAYERS, 1.6F, 0.9F);
		CarmineFx.smash(level, x, y, z);

		player.setDeltaMovement(player.getDeltaMovement().x, 1.8, player.getDeltaMovement().z);
		player.hurtMarked = true;
		player.resetFallDistance();

		AABB area = new AABB(x - 4.5, y - 1.0, z - 4.5, x + 4.5, y + 2.5, z + 4.5);
		for (Entity entity : level.getEntities(player, area, e -> e instanceof LivingEntity)) {
			if (!(entity instanceof LivingEntity target) || !target.isAlive()) continue;

			target.hurtServer(level, level.damageSources().playerAttack(player), 8.0F);

			Vec3 push = target.position().subtract(x, target.getY(), z);
			if (push.lengthSqr() < 0.01) {
				push = Vec3.ZERO;
			} else {
				push = push.normalize().scale(0.55);
			}

			target.setDeltaMovement(push.x, 1.15, push.z);
			target.hurtMarked = true;
		}
	}
}
