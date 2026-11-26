package projectred.item;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import projectred.ModEnchantments;
import projectred.util.CarmineFx;

public class CarmineScytheItem extends Item {
	private static final double PREY_RADIUS = 100.0;
	private static final int PREY_DURATION = 10 * 20;
	private static final int PREY_COOLDOWN = 30 * 20;

	public CarmineScytheItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		return tryPrey(level, player, player.getItemInHand(hand));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null) return InteractionResult.PASS;

		return tryPrey(context.getLevel(), player, context.getItemInHand());
	}

	private InteractionResult tryPrey(Level level, Player player, ItemStack stack) {
		if (!ModEnchantments.has(stack, ModEnchantments.PREY)) return InteractionResult.PASS;
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;
		if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;

		List<ServerPlayer> targets = new ArrayList<>();
		for (ServerPlayer other : serverLevel.players()) {
			if (other == player || !other.isAlive() || other.isSpectator()) continue;
			if (other.distanceToSqr(player) > PREY_RADIUS * PREY_RADIUS) continue;
			targets.add(other);
		}

		if (targets.isEmpty()) return InteractionResult.FAIL;

		ServerPlayer target = targets.get(serverLevel.getRandom().nextInt(targets.size()));
		target.addEffect(new MobEffectInstance(MobEffects.GLOWING, PREY_DURATION, 0, false, false, true));
		player.getCooldowns().addCooldown(stack, PREY_COOLDOWN);

		serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
			SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1.1F, 1.6F);
		CarmineFx.prey(serverLevel, player, target);
		return InteractionResult.SUCCESS;
	}
}
