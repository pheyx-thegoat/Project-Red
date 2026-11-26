package projectred.client;

public class ClientShake {
	private static final long DETONATION_MILLIS = 900L;
	private static final long SMASH_MILLIS = 450L;

	private static long startedAt = -1L;
	private static long durationMillis;
	private static float strength;

	public static void detonate() {
		trigger(1.0F, DETONATION_MILLIS);
	}

	public static void smash() {
		trigger(0.55F, SMASH_MILLIS);
	}

	private static void trigger(float amount, long millis) {
		long now = System.currentTimeMillis();
		float remaining = remaining(now);
		strength = remaining > 0.0F ? Math.max(amount, strength * remaining) : amount;
		durationMillis = millis;
		startedAt = now;
	}

	private static float remaining(long now) {
		if (startedAt < 0L || durationMillis <= 0L) return 0.0F;

		long elapsed = now - startedAt;
		if (elapsed < 0L || elapsed >= durationMillis) return 0.0F;

		return 1.0F - (float)elapsed / (float)durationMillis;
	}

	public static float[] sample() {
		float remaining = remaining(System.currentTimeMillis());
		if (remaining <= 0.0F) return null;

		float amount = strength * remaining * remaining;
		return new float[] {
			jitter() * amount * 8.0F,
			jitter() * amount * 5.5F,
			jitter() * amount * 0.07F,
			jitter() * amount * 0.07F,
			jitter() * amount * 0.07F
		};
	}

	private static float jitter() {
		return (float)(Math.random() * 2.0 - 1.0);
	}
}
