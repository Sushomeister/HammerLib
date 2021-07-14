package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public class ItemStackTooltipInfo
		implements IRenderableInfo
{
	public ItemStack stack;
	public int width, height;

	public ItemStackTooltipInfo(ItemStack stack, int width, int height)
	{
		this.stack = stack;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth()
	{
		return this.width;
	}

	@Override
	public int getHeight()
	{
		return this.height;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix, float x, float y, float partialTime)
	{
		RenderHelper.setupFor3DItems();

		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glScaled(width / 16., height / 16., 1);
		Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, 0, 0);
		GL11.glPopMatrix();
	}
}