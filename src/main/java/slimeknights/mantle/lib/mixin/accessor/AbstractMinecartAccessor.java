package slimeknights.mantle.lib.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

@Mixin(AbstractMinecart.class)
public interface AbstractMinecartAccessor {
	@Invoker("getMaxSpeed")
	double create$getMaxSpeed();
}
