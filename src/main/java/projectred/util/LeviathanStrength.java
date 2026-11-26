package projectred.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import projectred.item.CarmineLeviathanAxeItem;

public class LeviathanStrength {
	private static final int DURATION = 20;
	private static final int REFRESH_BELOW = 10;

	public static void tick(MinecraftServer server) {
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			boolean holdsAxe = player.getMainHandItem().getItem() instanceof CarmineLeviathanAxeItem
				|| player.getOffhandItem().getItem() instanceof CarmineLeviathanAxeItem;
			if (!holdsAxe) continue;

			MobEffectInstance current = player.getEffect(MobEffects.STRENGTH);
			if (current == null || (current.getAmplifier() <= 0 && current.getDuration() < REFRESH_BELOW)) {
				player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, DURATION, 0, true, false, true));
			}

			if (player.level() instanceof ServerLevel level && level.getGameTime() % 4L == 0L) {
				CarmineFx.aura(level, player.getX(), player.getY() + 1.0, player.getZ());
			}
		}
	}
}
