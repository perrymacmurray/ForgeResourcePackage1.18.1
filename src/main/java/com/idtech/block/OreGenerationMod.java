package com.idtech.block;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class OreGenerationMod
{
    public static void registerOverworldOres() {
        
    }

    public static void registerNetherOres() {

    }

    // ---------------------------------------------------
    // ---------------------------------------------------
    // ===================================================
    // DO NOT READ OR MODIFY THE CODE UNDER THIS LINE!!!!!
    // ===================================================
    // ---------------------------------------------------
    // ---------------------------------------------------

    public static ArrayList<PlacedFeature> OVERWORLD_OREGEN = new ArrayList<>();
    public static ArrayList<PlacedFeature> NETHER_OREGEN = new ArrayList<>();

    public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.BiomeCategory.NETHER) {
            for (PlacedFeature f : NETHER_OREGEN)
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, f);

        } else if (event.getCategory() != Biome.BiomeCategory.THEEND) {
            for (PlacedFeature f : OVERWORLD_OREGEN)
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, f);
        }
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>>
    PlacedFeature registerPlacedFeature(String registryName, ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        PlacedFeature placed = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(registryName), feature).placed(placementModifiers);
        return PlacementUtils.register(registryName, placed);
    }

    public static void addToOverworld(Block block, String blockName, int minHeight, int maxHeight, int veinSize, int numTimesPerChunk) {
        //Normal stone
        if (maxHeight > 0)
        {
            OVERWORLD_OREGEN.add(registerPlacedFeature(blockName,
                                                       Feature.ORE.configured(new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES,
                                                                                                   block.defaultBlockState(),
                                                                                                   veinSize)),
                                                       CountPlacement.of(numTimesPerChunk),
                                                       InSquarePlacement.spread(),
                                                       BiomeFilter.biome(),
                                                       HeightRangePlacement.uniform(VerticalAnchor.absolute(Math.max(minHeight, 0)),
                                                                                    VerticalAnchor.absolute(maxHeight))));
        }

        //Deepslate
        if (minHeight < 0)
        {
            OVERWORLD_OREGEN.add(registerPlacedFeature(blockName,
                                                       Feature.ORE.configured(new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                                                                                                   block.defaultBlockState(),
                                                                                                   veinSize)),
                                                       CountPlacement.of(numTimesPerChunk),
                                                       InSquarePlacement.spread(),
                                                       BiomeFilter.biome(),
                                                       HeightRangePlacement.uniform(minHeight < -63 ? VerticalAnchor.bottom() : VerticalAnchor.absolute(minHeight),
                                                                                    VerticalAnchor.absolute(Math.min(maxHeight, 0)))));
        }
    }

    public static void addToNether(Block block, String blockName, int minHeight, int maxHeight, int veinSize, int numTimesPerChunk) {
        NETHER_OREGEN.add(registerPlacedFeature(blockName, Feature.ORE.configured(new OreConfiguration(OreFeatures.NETHER_ORE_REPLACEABLES,
                                                                                                          block.defaultBlockState(), veinSize)),
                                                   CountPlacement.of(numTimesPerChunk),
                                                   InSquarePlacement.spread(),
                                                   BiomeFilter.biome(),
                                                   HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight))));
    }

    public static void registerOreFeatures()
    {
        registerOverworldOres();
        registerNetherOres();
    }
}
