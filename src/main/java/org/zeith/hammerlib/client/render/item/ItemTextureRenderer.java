package org.zeith.hammerlib.client.render.item;

import com.google.common.io.Files;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class ItemTextureRenderer
		implements Closeable
{
	private ItemRenderer renderer;
	private Framebuffer smallBuffer, largeBuffer;

	public ItemTextureRenderer(ItemRenderer renderer, int imageSize)
	{
		this.renderer = renderer;
		smallBuffer = new Framebuffer(imageSize, imageSize, true, true);
		largeBuffer = new Framebuffer(3 * imageSize, 3 * imageSize, true, true);
	}

	public void renderItemstack(ItemStack is, File output, boolean includesBorders) throws IOException
	{
		Framebuffer fb = includesBorders ? largeBuffer : smallBuffer;
		fb.bindWrite(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.clearColor(0.f, 0.f, 0.f, 0.f);
		RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, true);
		RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, true);
		RenderSystem.matrixMode(GL11.GL_PROJECTION);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		if(includesBorders)
		{
			RenderSystem.ortho(-16, 32, 32, -16, -1000.0D, 3000.0D);
		} else
		{
			RenderSystem.ortho(0, 16, 16, 0, -1000.0D, 3000.0D);
		}
		RenderSystem.matrixMode(GL11.GL_MODELVIEW);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(0.f, 0.f, -2000.f);
		RenderHelper.turnBackOn();
		RenderSystem.enableRescaleNormal();
		renderer.renderGuiItem(is, 0, 0);
		RenderHelper.turnOff();
		RenderSystem.disableRescaleNormal();

		RenderSystem.matrixMode(GL11.GL_PROJECTION);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(GL11.GL_MODELVIEW);
		RenderSystem.popMatrix();

		fb.bindRead();
		try(NativeImage ni = new NativeImage(fb.width, fb.height, true))
		{
			ni.downloadTexture(fb.getColorTextureId(), false);
			ni.flipY();
			Files.createParentDirs(output);
			ni.writeToFile(output);
		}
		fb.unbindWrite();
		fb.unbindRead();
	}

	@Override
	public void close()
	{
		smallBuffer.destroyBuffers();
		largeBuffer.destroyBuffers();
	}
}