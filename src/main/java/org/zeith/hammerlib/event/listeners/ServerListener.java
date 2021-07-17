package org.zeith.hammerlib.event.listeners;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.zeith.hammerlib.api.LanguageHelper;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.HLTargetPoint;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SyncTileEntityPacket;
import org.zeith.hammerlib.net.properties.IPropertyTile;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(bus = Bus.FORGE)
public class ServerListener
{
	public static final List<TileEntity> NEED_SYNC = new ArrayList<>();
	public static final List<TileEntity> NEED_PROP_SYNC = new ArrayList<>();

	@SubscribeEvent
	public static void serverTick(ServerTickEvent e)
	{
		if(e.phase == Phase.START && FMLEnvironment.dist == Dist.DEDICATED_SERVER)
			LanguageHelper.update();

		// After we're done with ticking tiles...
		if(e.phase == Phase.END)
		{
			while(!NEED_SYNC.isEmpty())
			{
				TileEntity tile = NEED_SYNC.remove(0);
				if(tile instanceof ISyncableTile)
					((ISyncableTile) tile).syncNow();
				else
					Network.sendToArea(new HLTargetPoint(tile.getBlockPos(), 256, tile.getLevel().dimension()), new SyncTileEntityPacket(tile));
			}

			while(!NEED_PROP_SYNC.isEmpty())
			{
				TileEntity tile = NEED_PROP_SYNC.remove(0);
				if(tile instanceof IPropertyTile)
					((IPropertyTile) tile).syncPropertiesNow();
			}
		}
	}

	public static void syncProperties(TileEntity tileEntity)
	{
		NEED_PROP_SYNC.add(tileEntity);
	}

	public static void syncTileEntity(TileEntity tileEntity)
	{
		NEED_SYNC.add(tileEntity);
	}
}