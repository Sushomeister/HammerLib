package org.zeith.hammerlib.net.lft;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants.NBT;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.PacketContext;

public class PacketTransport
		implements INBTPacket
{
	public String id;
	public byte[] data;

	public PacketTransport(String id, byte[] data)
	{
		this.id = id;
		this.data = data;
	}

	@Override
	public void write(CompoundNBT nbt)
	{
		nbt.putString("i", id);
		if(data != null)
			nbt.putByteArray("r", data);
	}

	@Override
	public void read(CompoundNBT nbt)
	{
		id = nbt.getString("i");
		if(nbt.contains("r", NBT.TAG_BYTE_ARRAY))
			data = nbt.getByteArray("r");
	}

	@Override
	public void execute(PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(ctx.getSide(), id);
		if(s != null && s.pos != null && data != null)
		{
			s.accept(data);
			ctx.withReply(new PacketRequestFurther(id, true));
		} else
			ctx.withReply(new PacketRequestFurther(id, false));
	}
}