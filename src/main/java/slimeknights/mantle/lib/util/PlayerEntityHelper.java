package slimeknights.mantle.lib.util;

import com.simibubi.create.lib.mixin.accessor.PlayerAccessor;

import net.minecraft.world.entity.player.Player;

public class PlayerEntityHelper {
	public static void closeScreen (Player player) {
		get(player).create$closeScreen();
	}

	private static PlayerAccessor get(Player player) {
		return MixinHelper.cast(player);
	}

	private PlayerEntityHelper() {}
}
