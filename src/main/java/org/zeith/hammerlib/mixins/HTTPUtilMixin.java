package org.zeith.hammerlib.mixins;

import net.minecraft.util.HTTPUtil;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.event.GetSuitableLanPortEvent;
import org.zeith.hammerlib.util.java.NumberUtils;

@Mixin(HTTPUtil.class)
public class HTTPUtilMixin
{
	/**
	 * @author Zeitheron @ HammerLib
	 * @reason GetSuitableLanPortEvent
	 */
	@Inject(
			method = "getAvailablePort",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getAvailablePort(CallbackInfoReturnable<Integer> cir)
	{
		GetSuitableLanPortEvent event = new GetSuitableLanPortEvent();
		NumberUtils.tryParse(System.getProperty("hammerlib.lanport"), NumberUtils.EnumNumberType.SHORT).ifPresent(n -> event.setNewPort(n.intValue()));
		MinecraftForge.EVENT_BUS.post(event);
		Integer i = event.getNewPort();
		if(i != null) cir.setReturnValue(i);
	}
}