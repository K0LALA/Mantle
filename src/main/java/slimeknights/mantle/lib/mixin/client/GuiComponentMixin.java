package slimeknights.mantle.lib.mixin.client;

import com.simibubi.create.lib.util.ScreenHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.gui.GuiComponent;

@Environment(EnvType.CLIENT)
@Mixin(GuiComponent.class)
public abstract class GuiComponentMixin {
	@ModifyVariable(
			method = "fillGradient(Lcom/mojang/math/Matrix4f;Lcom/mojang/blaze3d/vertex/BufferBuilder;IIIIIII)V",
			at = @At("HEAD"),
			ordinal = 5,
			argsOnly = true
	)
	private static int create$replaceA(int a) {
		return create$getColor(a);
	}

	@ModifyVariable(
			method = "fillGradient(Lcom/mojang/math/Matrix4f;Lcom/mojang/blaze3d/vertex/BufferBuilder;IIIIIII)V",
			at = @At("HEAD"),
			ordinal = 6,
			argsOnly = true
	)
	private static int create$replaceB(int b) {
		return create$getColor(b);
	}

	private static int create$getColor(int original) {
		if (ScreenHelper.CURRENT_COLOR != null) {
			if (original == ScreenHelper.DEFAULT_BORDER_COLOR_START) {
				return ScreenHelper.CURRENT_COLOR.getBorderColorStart();
			} else if (original == ScreenHelper.DEFAULT_BORDER_COLOR_END) {
				return ScreenHelper.CURRENT_COLOR.getBorderColorEnd();
			}
		}
		return original;
	}
}
