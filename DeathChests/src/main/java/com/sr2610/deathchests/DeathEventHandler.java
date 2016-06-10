package com.sr2610.deathchests;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathEventHandler {

	@SubscribeEvent
	public void handlePlayerDeath(PlayerDropsEvent event) {
		if (checkForChest(event.getDrops()))
			transferIntoChest(event);

	}

	private void transferIntoChest(PlayerDropsEvent event) {
		int stacksTransferred = 0;
		boolean isFull = false;
		EntityPlayer player = event.getEntityPlayer();
		BlockPos position = new BlockPos(player.posX, player.posY, player.posZ);
		player.worldObj.setBlockState(position, Blocks.CHEST.getDefaultState(), 2);
		TileEntityChest chest = (TileEntityChest) player.worldObj.getTileEntity(position);

		for (EntityItem droppedStack : event.getDrops()) {
			if (!isFull) {
				if(droppedStack.getEntityItem().stackSize==0)
					droppedStack.setDead();
				else{
				chest.setInventorySlotContents(stacksTransferred, droppedStack.getEntityItem());
				droppedStack.setDead();
				stacksTransferred++;
				if (stacksTransferred > chest.getSizeInventory())
					isFull = true;
			}
			}
		}

	}

	private boolean checkForChest(List<EntityItem> list) {

		for (EntityItem droppedStack : list) {
			if (droppedStack.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
				droppedStack.getEntityItem().stackSize --;
				return true;
			}
		}
		return false;

	}

}
