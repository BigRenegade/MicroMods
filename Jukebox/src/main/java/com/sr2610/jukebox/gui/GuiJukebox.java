package com.sr2610.jukebox.gui;

import com.sr2610.jukebox.blocks.TileEntityJukebox;
import com.sr2610.jukebox.container.ContainerJukebox;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiJukebox extends GuiContainer {

	private IInventory playerInv;
	private TileEntityJukebox te;

	public GuiJukebox(IInventory playerInv, TileEntityJukebox te) {
		super(new ContainerJukebox(playerInv, te));

		this.playerInv = playerInv;
		this.te = te;

		this.xSize = 176;
		this.ySize = 166;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(new ResourceLocation("jukebox:textures/gui/jukebox.png"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.te.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, 88 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752); // #404040
		this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 4210752); // #404040
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(new ResourceLocation("jukebox:textures/gui/jukebox.png"));
		int index = te.selectedTrack;
		int x, y;
		x = index % 6;
		y = index / 6;
		this.drawTexturedModalRect(32 + x * 18, 21 + y * 18, 234, 0, 22, 22);

	}
}
