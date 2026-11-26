package projectred;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;

public class ModComponents {
	public static final DataComponentType<Integer> CHARGE = Registry.register(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		ProjectRed.id("charge"),
		DataComponentType.<Integer>builder()
			.persistent(Codec.INT)
			.networkSynchronized(ByteBufCodecs.VAR_INT)
			.build()
	);

	public static void init() {
	}
}
