package net.minecraftforge.common.world;

import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ForgeWorldType extends ForgeRegistryEntry<ForgeWorldType>
{
    private static final Logger LOGGER = LogManager.getLogger();

    private static ForgeWorldType defaultWorldType = null; // If null, the default vanilla world type "default" will be used.

    public static void setDefaultWorldType(ForgeWorldType wt)
    {
        if (defaultWorldType != null)
        {
            LOGGER.warn("Default world type was already set, was '{}', is now '{}'", defaultWorldType.getRegistryName(), wt.getRegistryName());
        }
        defaultWorldType = wt;
    }

    @Nullable
    public static ForgeWorldType getDefaultWorldType()
    {
        return defaultWorldType;
    }

    private final ISettingsChunkGeneratorFactory factory;

    public ForgeWorldType(ISettingsChunkGeneratorFactory factory)
    {
        this.factory = factory;
    }

    public ForgeWorldType(IBasicChunkGeneratorFactory factory)
    {
        this.factory = (biomes,dimSettings,seed,genSettings) -> factory.createChunkGenerator(biomes,dimSettings,seed);
    }

    public String getTranslationKey()
    {
        return Util.makeTranslationKey("generator", getRegistryName());
    }

    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent(getTranslationKey());
    }

    /**
     * Called from both the dedicated server and the world creation screen in the client.
     * to construct the DimensionGEneratorSettings:
     * @return The constructed chunk generator.
     */
    public ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed, String generatorSettings)
    {
        return this.factory.createChunkGenerator(biomeRegistry, dimensionSettingsRegistry, seed, generatorSettings);
    }

    public interface ISettingsChunkGeneratorFactory
    {
        ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed, String generatorSettings);
    }

    public interface IBasicChunkGeneratorFactory
    {
        ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed);
    }
}
