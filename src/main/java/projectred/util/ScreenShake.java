package projectred.util;

public class ScreenShake {
	private static Runnable detonationHook;
	private static Runnable smashHook;

	public static void install(Runnable detonation, Runnable smash) {
		detonationHook = detonation;
		smashHook = smash;
	}

	public static void detonation() {
		if (detonationHook != null) detonationHook.run();
	}

	public static void smash() {
		if (smashHook != null) smashHook.run();
	}
}
