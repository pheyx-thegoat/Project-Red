package projectred.util;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CarmineFx {
	public static final int CARMINE_RED = 0xB01218;
	private static final DustParticleOptions DUST = new DustParticleOptions(CARMINE_RED, 1.2F);
	private static final DustParticleOptions DUST_SMALL = new DustParticleOptions(CARMINE_RED, 0.7F);

	public static void aura(ServerLevel level, double x, double y, double z) {
		double ox = (level.getRandom().nextDouble() - 0.5) * 0.8;
		double oy = (level.getRandom().nextDouble() - 0.3) * 1.1;
		double oz = (level.getRandom().nextDouble() - 0.5) * 0.8;
		level.sendParticles(DUST_SMALL, x + ox, y + oy, z + oz, 2, 0.05, 0.05, 0.05, 0.0);
	}

	public static void chargeGain(ServerLevel level, LivingEntity entity, int charge) {
		double centerY = entity.getY() + entity.getBbHeight() * 0.55;
		int points = 6 + charge * 5;

		for (int i = 0; i < points; i++) {
			double angle = Math.PI * 2.0 * i / points + level.getRandom().nextDouble() * 0.4;
			double radius = 0.55 + charge * 0.12;
			level.sendParticles(
				DUST,
				entity.getX() + Math.cos(angle) * radius,
				centerY + (level.getRandom().nextDouble() - 0.5) * 0.6,
				entity.getZ() + Math.sin(angle) * radius,
				1, 0.0, 0.05, 0.0, 0.0
			);
		}

		level.sendParticles(ParticleTypes.CRIT, entity.getX(), centerY, entity.getZ(), 5 + charge * 2, 0.35, 0.35, 0.35, 0.2);
	}

	public static void dash(ServerLevel level, Player player, Vec3 direction) {
		Vec3 start = new Vec3(player.getX(), player.getY() + 1.0, player.getZ());

		for (int i = 0; i < 28; i++) {
			Vec3 point = start.subtract(direction.scale(i / 28.0 * 3.2));
			level.sendParticles(DUST, point.x, point.y, point.z, 1, 0.04, 0.1, 0.04, 0.0);
		}

		level.sendParticles(ParticleTypes.SWEEP_ATTACK, start.x, start.y, start.z, 3, 0.35, 0.35, 0.35, 0.0);
		level.sendParticles(ParticleTypes.CRIT, start.x, start.y, start.z, 10, 0.3, 0.4, 0.3, 0.25);
	}

	public static void crit(ServerLevel level, LivingEntity target) {
		double centerY = target.getY() + target.getBbHeight() * 0.6;
		level.sendParticles(ParticleTypes.CRIT, target.getX(), centerY, target.getZ(), 12, 0.3, 0.35, 0.3, 0.35);
		level.sendParticles(DUST, target.getX(), centerY, target.getZ(), 18, 0.45, 0.45, 0.45, 0.1);
		ring(level, target.getX(), centerY - 0.4, target.getZ(), 0.8, 16);
	}

	public static void smash(ServerLevel level, double x, double y, double z) {
		level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 3, 0.6, 0.1, 0.6, 0.0);
		level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y + 0.2, z, 14, 0.6, 0.15, 0.6, 0.02);
		ring(level, x, y + 0.1, z, 1.2, 28);
		ring(level, x, y + 0.1, z, 2.4, 40);
		ring(level, x, y + 0.15, z, 3.6, 52);
	}

	public static void detonation(ServerLevel level, double x, double y, double z) {
		level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 3, 1.5, 1.0, 1.5, 0.0);
		level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 24, 4.0, 2.4, 4.0, 0.0);
		level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y + 0.4, z, 90, 4.5, 2.6, 4.5, 0.04);
		level.sendParticles(ParticleTypes.CRIT, x, y, z, 140, 5.5, 3.2, 5.5, 0.5);
		level.sendParticles(DUST, x, y, z, 120, 5.0, 3.0, 5.0, 0.2);

		for (double radius = 2.0; radius <= 9.0; radius += 1.75) {
			ring(level, x, y - 0.4 + radius * 0.18, z, radius, (int)(radius * 14));
		}
	}

	public static void prey(ServerLevel level, Player caster, Player target) {
		double targetY = target.getY() + target.getBbHeight() * 0.6;

		Vec3 from = caster.getEyePosition();
		Vec3 to = target.position().add(0.0, target.getBbHeight() * 0.5, 0.0);
		Vec3 delta = to.subtract(from);
		int steps = (int)Math.min(80.0, delta.length() * 2.0);

		for (int i = 0; i <= steps; i++) {
			Vec3 point = from.add(delta.scale(i / (double)steps));
			level.sendParticles(DUST, point.x, point.y, point.z, 1, 0.02, 0.02, 0.02, 0.0);
			if (i % 4 == 0) {
				level.sendParticles(ParticleTypes.GLOW, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0);
			}
		}

		level.sendParticles(ParticleTypes.GLOW, target.getX(), targetY, target.getZ(), 32, 0.5, 0.8, 0.5, 0.02);
		level.sendParticles(ParticleTypes.CRIT, target.getX(), targetY, target.getZ(), 20, 0.4, 0.6, 0.4, 0.25);
		ring(level, target.getX(), target.getY() + 0.15, target.getZ(), 1.1, 24);
	}

	private static void ring(ServerLevel level, double x, double y, double z, double radius, int points) {
		for (int i = 0; i < points; i++) {
			double angle = Math.PI * 2.0 * i / points;
			level.sendParticles(
				DUST,
				x + Math.cos(angle) * radius,
				y,
				z + Math.sin(angle) * radius,
				1, 0.0, 0.02, 0.0, 0.01
			);
		}
	}
}
