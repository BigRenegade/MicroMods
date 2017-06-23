package com.sr2610.jukebox.blocks;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityJukebox extends TileEntity implements IInventory {

	private NonNullList<ItemStack> contents = NonNullList.<ItemStack>withSize(12, ItemStack.EMPTY);
	private String customName;
	private boolean paused = false;
	public int selectedTrack = 0;

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.jukebox";
	}

	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomName(String name) {
		this.customName = name;
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new TextComponentString(this.getName())
				: new TextComponentTranslation(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return 12;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.contents) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.contents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

		ItemStackHelper.loadAllItems(compound, this.contents);

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}

		this.paused = compound.getBoolean("Paused");
		selectedTrack = compound.getInteger("Track");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		ItemStackHelper.saveAllItems(compound, this.contents);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		}

		compound.setBoolean("Paused", paused);
		compound.setInteger("Track", selectedTrack);
		return compound;
	}

	public ItemStack getStackInSlot(int index) {
		return (ItemStack) this.contents.get(index);
	}

	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.contents, index, count);

		if (!itemstack.isEmpty()) {
			this.markDirty();
		}

		return itemstack;
	}

	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.contents, index);
	}

	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		this.contents.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.getItem() instanceof ItemRecord)
			return true;
		else
			return false;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.selectedTrack;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.selectedTrack = value;
			break;
		}

	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public void clear() {

	}

	public void nextSong() {
		if (this.isEmpty())
			return;
		selectedTrack++;
		if (selectedTrack >= 12)
			selectedTrack = 0;
		while (contents.get(selectedTrack).isEmpty() || contents.get(selectedTrack) == null) {
			selectedTrack++;
			if (selectedTrack >= 11)
				selectedTrack = 0;
		}
		paused = true;
		togglePause();

	}

	public void previousSong() {
		if (this.isEmpty())
			return;
		selectedTrack--;
		if (selectedTrack <= -1)
			selectedTrack = 11;
		while (contents.get(selectedTrack).isEmpty() || contents.get(selectedTrack) == null) {
			selectedTrack--;
			if (selectedTrack <= 0)
				selectedTrack = 11;
		}
		paused = true;
		togglePause();
	}

	public void togglePause() {
		if (!paused) {
			world.playEvent(1010, pos, 0);
			world.playRecord(pos, (SoundEvent) null);
			paused = true;
		} else if (!contents.get(selectedTrack).isEmpty()) {
			world.playEvent((EntityPlayer) null, 1010, pos, Item.getIdFromItem(contents.get(selectedTrack).getItem()));
			paused = false;
		}

	}

}
