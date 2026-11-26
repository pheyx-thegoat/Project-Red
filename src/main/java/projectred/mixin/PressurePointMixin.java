package projectred.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import projectred.ModEnchantments;

@Mixin(Player.class)
public abstract class PressurePointMixin {
	@ModifyVariable(
		method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
		at = @At("HEAD"),
		argsOnly = true
	)
	private float projectred$pressurePoint(float damage, ServerLevel level, DamageSource source) {
		if (damage <= 0.0F || !(source.getEntity() instanceof Player attacker)) return damage;

		Player victim = (Player)(Object)this;
		if (attacker == victim) return damage;

		ItemStack weapon = source.getWeaponItem();
		if (weapon == null || !ModEnchantments.has(weapon, ModEnchantments.PRESSURE_POINT)) return damage;
		if (hasNetheriteOn(victim)) return damage;

		return damage + 4.0F;
	}

	private static boolean hasNetheriteOn(Player player) {
		return player.getItemBySlot(EquipmentSlot.HEAD).is(Items.NETHERITE_HELMET)
			|| player.getItemBySlot(EquipmentSlot.CHEST).is(Items.NETHERITE_CHESTPLATE)
			|| player.getItemBySlot(EquipmentSlot.LEGS).is(Items.NETHERITE_LEGGINGS)
			|| player.getItemBySlot(EquipmentSlot.FEET).is(Items.NETHERITE_BOOTS);
	}
}
