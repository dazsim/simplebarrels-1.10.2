package com.workshopcraft.simplebarrels.handlers;

import com.workshopcraft.simplebarrels.tiles.TileEntityBarrel;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BarrelHandler {

	/*
	@SubscribeEvent
	public void onBlockActivated(PlayerInteractEvent event)
	{
		
		if (!event.getWorld().isRemote)
		{
			if (event instanceof RightClickBlock)
			{
		
				TileEntity te = (event.getWorld().getTileEntity(event.getPos()));
				if (te instanceof TileEntityBarrel)
				{
					TileEntityBarrel te2 = (TileEntityBarrel) te;
					if (te2.barrelContents[0] == null)
					{
						if (event.getItemStack() != null)
						{
							te2.barrelContents[0] = new ItemStack(event.getItemStack().getItem(),event.getItemStack().stackSize,event.getItemStack().getMetadata());
							te2.count = event.getItemStack().stackSize;
							event.getEntityPlayer().inventory.mainInventory[event.getEntityPlayer().inventory.currentItem].stackSize=0;
							event.getEntityPlayer().inventory.inventoryChanged=true;
							this.updateBarrel(te2);
							event.setCanceled(true);
						}
					} else if (event.getItemStack()!=null) 
					{
						if (te2.barrelContents[0].isItemEqual(event.getItemStack()))
						{
							
						
							if (te2.count < te2.size)
							{
								
								int r = event.getItemStack().stackSize+te2.count-te2.size;
								if (r<0)
								{
									te2.count = (event.getItemStack().stackSize+te2.count);
									event.getEntityPlayer().inventory.mainInventory[event.getEntityPlayer().inventory.currentItem].stackSize=0;
									event.getEntityPlayer().inventory.inventoryChanged=true;
									this.updateBarrel(te2);
									event.setCanceled(true);
								} else if (r<event.getItemStack().stackSize)
								{
									te2.count = te2.size;
									event.getEntityPlayer().inventory.mainInventory[event.getEntityPlayer().inventory.currentItem].stackSize=r;
									event.getEntityPlayer().inventory.inventoryChanged=true;
									this.updateBarrel(te2);
									event.setCanceled(true);
								} else 
								{
									event.setCanceled(true);
								}
							}
						} else
						{
							event.setCanceled(true);
						}
					} else
					{
						event.setCanceled(true);
					}
				}
			}
		}
	} */
	
	public void updateBarrel(TileEntityBarrel t)
	{
		NBTTagCompound compound = new NBTTagCompound();
		t.writeToNBT(compound );
		t.markDirty();
		t.getWorld().notifyBlockUpdate(t.getPos(), t.getWorld().getBlockState(t.getPos()), t.getWorld().getBlockState(t.getPos()), 3);
		
	}
	
	public BlockPos sideToOffset(PlayerInteractEvent event)
	{
		EnumFacing f = event.getFace();
		if (f == null)
		{
			return null;
		}
		if (f == EnumFacing.NORTH)
		{
			return new BlockPos(0,0,-1.0);
		}
		if (f == EnumFacing.WEST)
		{
			return new BlockPos(-1.0,0,0);
		}
		if (f == EnumFacing.SOUTH)
		{
			return new BlockPos(0,0,1.0);
		}
		if (f == EnumFacing.EAST)
		{
			return new BlockPos(1.0,0,0);
		}
		
		
		
		if (f == EnumFacing.DOWN)
		{
			return new BlockPos(0,-1.0,0);
		}
		if (f == EnumFacing.UP)
		{
			return new BlockPos(0,1.0,0);
		}
		return null;
	}
	
	@SubscribeEvent
	public void LeftClickBlock(PlayerInteractEvent event)
	{
		int dx,dy,dz;
		
		//this is for left click.
		if (!event.getWorld().isRemote)
		{
		TileEntity te = (event.getWorld().getTileEntity(event.getPos()));
		
		if (event instanceof LeftClickBlock)
		{
			
			if (te instanceof TileEntityBarrel)
			{
				
				TileEntityBarrel te2 = (TileEntityBarrel) te;
				if (te2.itemHandler.barrelContents[0] == null)
				{
					//do nothing
					
					return;
				
				} else
				if (event.getEntityPlayer().isSneaking())
				{
					if (te2.itemHandler.count>0)
					{
						ItemStack istack = new ItemStack(te2.itemHandler.barrelContents[0].getItem(),1,te2.itemHandler.barrelContents[0].getItemDamage());
						istack.setTagCompound(te2.itemHandler.barrelContents[0].getTagCompound());
						BlockPos o = sideToOffset(event);
						
						InventoryHelper.spawnItemStack(event.getWorld(), event.getPos().getX()+o.getX(), event.getPos().getY()+o.getY(), event.getPos().getZ()+o.getZ(),istack);
						te2.itemHandler.count--;
						if (te2.itemHandler.count==0)
						{
							te2.itemHandler.barrelContents[0]=null;
						}
						updateBarrel(te2);
						event.setCanceled(true);
						return;
					}
				} else
				//if (te2.barrelContents[0].stackSize<64)
				if (te2.itemHandler.count<te2.itemHandler.barrelContents[0].getMaxStackSize())
				{
					//EntityItem e = new EntityItem(event.getWorld(),event.getPos().getX(),event.getPos().getY(),event.getPos().getZ(),te2.barrelContents[0]);
					
					//event.getWorld().spawnEntityInWorld(e);
					ItemStack istack = new ItemStack(te2.itemHandler.barrelContents[0].getItem(),te2.itemHandler.count,te2.itemHandler.barrelContents[0].getItemDamage());
					istack.setTagCompound(te2.itemHandler.barrelContents[0].getTagCompound());
					/*if (te2.tags!=null)
					{
						istack.writeToNBT(te2.tags);
					}*/
					//istack.setItemDamage(te2.barrelContents[0].getItemDamage());
					//InventoryHelper.spawnItemStack(event.getWorld(), event.getPos().getX(), event.getPos().getY()+1.0, event.getPos().getZ(),istack);
					BlockPos o = sideToOffset(event);
					
					InventoryHelper.spawnItemStack(event.getWorld(), event.getPos().getX()+o.getX(), event.getPos().getY()+o.getY(), event.getPos().getZ()+o.getZ(),istack);
					//event.getEntityPlayer().inventory.inventoryChanged=true;
					te2.itemHandler.barrelContents[0] = null;
					te2.itemHandler.count = 0;
					//SimpleBarrels.BarrelNet.sendTo(new BarrelSyncClient(Items.APPLE.getUnlocalizedName(),0,te2.getPos().getX(),te2.getPos().getY(),te2.getPos().getZ()), (EntityPlayerMP) event.getEntityPlayer());
					updateBarrel(te2);
					event.setCanceled(true);
					return;
				
				
					//return all
				} else
				{
					ItemStack istack = new ItemStack(te2.itemHandler.barrelContents[0].getItem(),te2.itemHandler.barrelContents[0].getMaxStackSize(),te2.itemHandler.barrelContents[0].getItemDamage());
					istack.setTagCompound(te2.itemHandler.barrelContents[0].getTagCompound());
					/*if (te2.tags!=null)
					{
						istack.writeToNBT(te2.tags);
					}*/
					
					//InventoryHelper.spawnItemStack(event.getWorld(), event.getPos().getX(), event.getPos().getY()+1.0, event.getPos().getZ(),istack);
					BlockPos o = sideToOffset(event);
					
					InventoryHelper.spawnItemStack(event.getWorld(), event.getPos().getX()+o.getX(), event.getPos().getY()+o.getY(), event.getPos().getZ()+o.getZ(),istack);
					//event.getEntityPlayer().inventory.inventoryChanged=true;
					
					te2.itemHandler.count -= te2.itemHandler.barrelContents[0].getMaxStackSize();
					if (te2.itemHandler.count == 0)
					{
						te2.itemHandler.barrelContents[0] = null;
					}
							
					//SimpleBarrels.BarrelNet.sendTo(new BarrelSyncClient(Items.APPLE.getUnlocalizedName(),0,te2.getPos().getX(),te2.getPos().getY(),te2.getPos().getZ()), (EntityPlayerMP) event.getEntityPlayer());
					updateBarrel(te2);
					event.setCanceled(true);
					return;
				}
			}
		}
	}}
	
}
