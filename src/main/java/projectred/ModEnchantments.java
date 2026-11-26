package projectred;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
	public static final ResourceKey<Enchantment> DETONATION = key("detonation");
	public static final ResourceKey<Enchantment> PRESSURE_POINT = key("pressure_point");
	public static final ResourceKey<Enchantment> PREY = key("prey");

	private static ResourceKey<Enchantment> key(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, ProjectRed.id(name));
	}

	public static int level(ItemStack stack, ResourceKey<Enchantment> enchantment) {
		for (var entry : stack.getEnchantments().entrySet()) {
			Holder<Enchantment> holder = entry.getKey();
			if (holder.is(enchantment)) {
				return entry.getIntValue();
			}
		}

		return 0;
	}

	public static boolean has(ItemStack stack, ResourceKey<Enchantment> enchantment) {
		return level(stack, enchantment) > 0;
	}
}
