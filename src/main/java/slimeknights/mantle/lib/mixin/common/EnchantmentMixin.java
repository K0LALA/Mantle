package slimeknights.mantle.lib.mixin.common;

import com.simibubi.create.lib.util.EnchantmentUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
	private void create$canEnchant(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		Set<Supplier<Enchantment>> enchants = EnchantmentUtil.ITEMS_TO_ENCHANTS.get(itemStack.getItem());
		if (enchants != null) {
			for (Supplier<Enchantment> enchant : enchants) {
				if (enchant.get() == (Object) this) {
					cir.setReturnValue(true);
				}
			}
		}
	}
}
