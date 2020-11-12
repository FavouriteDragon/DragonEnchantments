package com.favouritedragon.dragonenchants.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class DragonUtils {

    public static boolean canRainAtPos(BlockPos pos, World world) {
        return world.getBiome(pos).getPrecipitation().equals(Biome.RainType.NONE);
    }
}
