package org.halvors.nuclearphysics.common.item.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.block.ItemBlockTooltip;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.type.Position;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockThermometer extends ItemBlockTooltip {
    public static final int energy = 1000;

    public ItemBlockThermometer(final Block block) {
        super(block);

        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull final ItemStack itemStack, @Nullable final World world, @Nonnull final List<String> list, @Nonnull final ITooltipFlag flag) {
        final Position position = getSavedCoordinate(itemStack);

        if (position != null) {
            list.add(LanguageUtility.transelate("tooltip.trackingCoordinate") + ": ");
            list.add(EnumColor.DARK_GREEN + "X: " + position.getIntX() + ", Y: " + position.getIntY() + ", Z: " + position.getIntZ());
        } else {
            list.add(EnumColor.DARK_RED + LanguageUtility.transelate("tooltip.notTrackingTemperature"));
        }

        super.addInformation(itemStack, world, list, flag);
    }

    @Override
    public boolean placeBlockAt(@Nonnull final ItemStack itemStack, @Nonnull final EntityPlayer player, final World world, @Nonnull final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, @Nonnull final IBlockState state) {
        final TileEntity tile = world.getTileEntity(pos);

        if (!world.isRemote && tile != null) {
            // Inject essential tile data.
            final NBTTagCompound essentialNBT = new NBTTagCompound();
            tile.writeToNBT(essentialNBT);

            final NBTTagCompound setNbt = InventoryUtility.getNBTTagCompound(itemStack);

            if (essentialNBT.hasKey("trackCoordinate")) {
                setNbt.setTag("trackCoordinate", essentialNBT.getCompoundTag("trackCoordinate"));
            }

            tile.readFromNBT(setNbt);
        }

        return super.placeBlockAt(itemStack, player, world, pos, side, hitX, hitY, hitZ, state);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, @Nonnull final EnumHand hand) {
        final ItemStack itemStack = player.getHeldItemMainhand();

        if (!world.isRemote) {
            setSavedCoordinate(itemStack, null);
            player.sendMessage(new TextComponentString(EnumColor.DARK_BLUE + "[" + Reference.NAME + "] " + EnumColor.GREY + LanguageUtility.transelate("tooltip.clearedTrackingCoordinate") + "."));

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        return super.onItemRightClick(world, player, hand);
    }

    /*
     * This is a workaround for buggy onItemUseFirst() function in 1.10.
     * TODO: Review this for 1.11 and 1.12.
     */
    @Override
    public boolean doesSneakBypassUse(final ItemStack stack, final IBlockAccess world, final BlockPos pos, final EntityPlayer player) {
        return true;
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final ItemStack itemStack = player.getHeldItemMainhand();

        if (player.isSneaking()) {
            if (!world.isRemote) {
                setSavedCoordinate(itemStack, new Position(pos));

                player.sendMessage(new TextComponentString(EnumColor.DARK_BLUE + "[" + Reference.NAME + "] " + EnumColor.GREY + LanguageUtility.transelate("tooltip.trackingCoordinate") + ": " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Position getSavedCoordinate(final ItemStack itemStack) {
        final NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (tag.hasKey("trackCoordinate")) {
            return new Position(tag.getCompoundTag("trackCoordinate"));
        }

        return null;
    }

    public void setSavedCoordinate(final ItemStack itemStack, final Position position) {
        final NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (position != null) {
            tag.setTag("trackCoordinate", position.writeToNBT(new NBTTagCompound()));
        } else {
            tag.removeTag("trackCoordinate");
        }
    }
}
