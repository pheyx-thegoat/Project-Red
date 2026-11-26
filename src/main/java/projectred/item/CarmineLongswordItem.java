package projectred.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import projectred.ModComponents;
import projectred.ModEnchantments;
import projectred.util.CarmineFx;
import projectred.util.ScreenShake;

public class CarmineLongswordItem extends Item {
	public static final int MAX_CHARGE = 3;
	private static final float DETONATION_RADIUS = 8.0F;

	public CarmineLongswordItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker.level().isClientSide() || !(attacker instanceof Player player)) return;
		if (player.getCooldowns().isOnCooldown(stack)) return;

		int charge = stack.getOrDefault(ModComponents.CHARGE, 0);
		if (charge >= MAX_CHARGE) return;

		stack.set(ModComponents.CHARGE, charge + 1);

		if (attacker.level() instanceof ServerLevel level) {
			CarmineFx.chargeGain(level, attacker, charge + 1);
		}
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int charge = stack.getOrDefault(ModComponents.CHARGE, 0);
		if (charge <= 0) return InteractionResult.PASS;

		boolean detonation = charge >= MAX_CHARGE && ModEnchantments.has(stack, ModEnchantments.DETONATION);

		if (level.isClientSide()) {
			if (detonation && !player.getCooldowns().isOnCooldown(stack)) {
				ScreenShake.detonation();
			}
			return InteractionResult.SUCCESS;
		}

		if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;

		if (detonation) {
			detonate(serverLevel, player, stack);
		} else {
			dash(serverLevel, player, stack, charge);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null) return InteractionResult.PASS;

		return use(context.getLevel(), player, context.getHand());
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return stack.getOrDefault(ModComponents.CHARGE, 0) > 0 || stack.isDamaged();
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		int charge = stack.getOrDefault(ModComponents.CHARGE, 0);
		if (charge <= 0) return super.getBarWidth(stack);

		return Math.max(1, Math.round(13.0F * charge / MAX_CHARGE));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return stack.getOrDefault(ModComponents.CHARGE, 0) > 0 ? 0xC81018 : super.getBarColor(stack);
	}

	private void dash(ServerLevel level, Player player, ItemStack stack, int charge) {
		Vec3 dir = player.getLookAngle();
		player.setDeltaMovement(dir.x * 1.8, Math.max(0.25, dir.y * 1.4 + 0.25), dir.z * 1.8);
		player.hurtMarked = true;
		player.resetFallDistance();
		stack.set(ModComponents.CHARGE, charge - 1);

		level.playSound(null, player.getX(), player.getY(), player.getZ(),
			SoundEvents.TRIDENT_RIPTIDE_1.value(), SoundSource.PLAYERS, 1.0F, 1.35F);
		CarmineFx.dash(level, player, dir);
	}

	private void detonate(ServerLevel level, Player player, ItemStack stack) {
		double x = player.getX();
		double y = player.getY() + 0.6;
		double z = player.getZ();

		CarmineFx.detonation(level, x, y, z);
		level.explode(
			player,
			level.damageSources().explosion(player, player),
			new NoSelfDamage(player),
			x, y, z,
			DETONATION_RADIUS,
			false,
			Level.ExplosionInteraction.TNT
		);

		stack.set(ModComponents.CHARGE, 0);
		player.getCooldowns().addCooldown(stack, 15 * 20);
	}

	private static class NoSelfDamage extends ExplosionDamageCalculator {
		private final Player owner;

		NoSelfDamage(Player owner) {
			this.owner = owner;
		}

		@Override
		public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
			return entity != owner && super.shouldDamageEntity(explosion, entity);
		}

		@Override
		public float getKnockbackMultiplier(Entity entity) {
			return entity == owner ? 0.0F : super.getKnockbackMultiplier(entity);
		}
	}
}
