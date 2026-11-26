package projectred;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ModCreativeTabs {
	public static final ResourceKey<CreativeModeTab> CARMINE = ResourceKey.create(
		Registries.CREATIVE_MODE_TAB, ProjectRed.id("carmine")
	);

	public static void init() {
		Registry.register(
			BuiltInRegistries.CREATIVE_MODE_TAB,
			CARMINE,
			FabricCreativeModeTab.builder()
				.title(Component.translatable("itemGroup.project-red.carmine"))
				.icon(() -> new ItemStack(ModItems.CARMINE_LONGSWORD))
				.build()
		);

		CreativeModeTabEvents.modifyOutputEvent(CARMINE).register(ModCreativeTabs::fill);
	}

	private static void fill(FabricCreativeModeTabOutput output) {
		output.accept(ModItems.CARMINE_INGOT);
		output.accept(ModItems.CARMINE_NUGGET);
		output.accept(ModItems.CARMINE_BLOCK);
		output.accept(ModItems.CARMINE_STAIRS);
		output.accept(ModItems.CARMINE_SLAB);
		output.accept(ModItems.CARMINE_WALL);
		output.accept(ModItems.CARMINE_LONGSWORD);
		output.accept(ModItems.CARMINE_LEVIATHAN_AXE);
		output.accept(ModItems.CARMINE_SCYTHE);
		addBook(output, ModEnchantments.DETONATION);
		addBook(output, ModEnchantments.PRESSURE_POINT);
		addBook(output, ModEnchantments.PREY);
	}

	private static void addBook(FabricCreativeModeTabOutput output, ResourceKey<Enchantment> enchantment) {
		HolderLookup.RegistryLookup<Enchantment> lookup = output.getContext().holders().lookupOrThrow(Registries.ENCHANTMENT);
		Holder<Enchantment> holder = lookup.getOrThrow(enchantment);
		ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		enchantments.set(holder, 1);
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		book.set(DataComponents.STORED_ENCHANTMENTS, enchantments.toImmutable());
		output.accept(book);
	}
}
