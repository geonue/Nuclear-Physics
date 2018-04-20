package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.init.ModPotions;

import java.util.List;

public class RadioactiveExplosion extends ExplosionBase {
    public RadioactiveExplosion(IBlockAccess world, Entity entity, BlockPos pos, float size, boolean flaming, boolean damagesTerrain) {
        super(world, entity, pos, size, flaming, damagesTerrain);
    }

    @Override
    public void doExplosionA() {
        float radius = size * 4;
        AxisAlignedBB bounds = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
        List<EntityLiving> entitiesNearby = world.getEntitiesWithinAABB(EntityLiving.class, bounds);

        for (EntityLiving entity : entitiesNearby) {
            ModPotions.poisonRadiation.poisonEntity(entity);
        }

        super.doExplosionA();
    }
}
