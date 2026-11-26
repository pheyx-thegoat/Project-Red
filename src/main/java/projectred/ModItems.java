package projectred;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.level.block.Block;
import projectred.item.CarmineLeviathanAxeItem;
import projectred.item.CarmineLongswordItem;
import projectred.item.CarmineScytheItem;

public class ModItems {
	public static final Item CARMINE_INGOT = register("carmine_ingot", Item::new, new Item.Properties());
	public static final Item CARMINE_NUGGET = register("carmine_nugget", Item::new, new Item.Properties());

	public static final Item CARMINE_BLOCK = blockItem("carmine_block", ModBlocks.CARMINE_BLOCK);
	public static final Item CARMINE_STAIRS = blockItem("carmine_stairs", ModBlocks.CARMINE_STAIRS);
	public static final Item CARMINE_SLAB = blockItem("carmine_slab", ModBlocks.CARMINE_SLAB);
	public static final Item CARMINE_WALL = blockItem("carmine_wall", ModBlocks.CARMINE_WALL);

	public static final Item CARMINE_LONGSWORD = register(
		"carmine_longsword",
		CarmineLongswordItem::new,
		new Item.Properties()
			.sword(ToolMaterial.NETHERITE, 3.0F, -2.4F)
			.component(DataComponents.ATTACK_RANGE, new AttackRange(0.0F, 4.0F, 0.0F, 6.0F, 0.3F, 1.0F))
			.fireResistant()
			.rarity(Rarity.RARE)
	);
	public static final Item CARMINE_LEVIATHAN_AXE = register(
		"carmine_leviathan_axe",
		p -> new CarmineLeviathanAxeItem(ToolMaterial.NETHERITE, 5.0F, -3.0F, p),
		new Item.Properties().fireResistant().rarity(Rarity.RARE)
	);
	public static final Item CARMINE_SCYTHE = register(
		"carmine_scythe",
		CarmineScytheItem::new,
		new Item.Properties().sword(ToolMaterial.NETHERITE, 5.0F, -2.7F).fireResistant().rarity(Rarity.RARE)
	);

	private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ProjectRed.id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	private static Item blockItem(String name, Block block) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ProjectRed.id(name));
		return Registry.register(
			BuiltInRegistries.ITEM,
			key,
			new BlockItem(block, new Item.Properties().setId(key).useBlockDescriptionPrefix())
		);
	}

	public static void init() {
	}
}
