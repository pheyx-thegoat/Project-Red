package projectred;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {
	public static final Block CARMINE_BLOCK = register(
		"carmine_block",
		Block::new,
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_RED)
			.strength(5.0F, 6.0F)
			.requiresCorrectToolForDrops()
			.sound(SoundType.NETHERITE_BLOCK)
	);
	public static final Block CARMINE_STAIRS = register(
		"carmine_stairs",
		p -> new StairBlock(CARMINE_BLOCK.defaultBlockState(), p),
		props()
	);
	public static final Block CARMINE_SLAB = register("carmine_slab", SlabBlock::new, props());
	public static final Block CARMINE_WALL = register("carmine_wall", WallBlock::new, props());

	private static BlockBehaviour.Properties props() {
		return BlockBehaviour.Properties.ofFullCopy(CARMINE_BLOCK);
	}

	private static <T extends Block> T register(
		String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties
	) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, ProjectRed.id(name));
		return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
	}

	public static void init() {
	}
}
