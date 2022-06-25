package com.idtech.world;

import com.idtech.block.BlockMod;

public class OreGenerationMod
{
    public static void registerOreFeatures()
    {
        OreGenerationUtil.addToOverworld(BlockMod.HOT_COALS, "hotcoals", -64, -10, 9, 3);
    }
}
