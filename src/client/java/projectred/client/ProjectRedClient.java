package projectred.client;

import net.fabricmc.api.ClientModInitializer;
import projectred.util.ScreenShake;

public class ProjectRedClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenShake.install(ClientShake::detonate, ClientShake::smash);
	}
}
