package slimeknights.mantle.lib.mixin.accessor;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.phys.shapes.ArrayVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

@Mixin(ArrayVoxelShape.class)
public interface ArrayVoxelShapeAccessor {
	@Invoker("<init>")
	static ArrayVoxelShape create$init(DiscreteVoxelShape discreteVoxelShape, DoubleList doubleList, DoubleList doubleList2, DoubleList doubleList3) {
		throw new AssertionError("Mixin application failed!");
	}
}
