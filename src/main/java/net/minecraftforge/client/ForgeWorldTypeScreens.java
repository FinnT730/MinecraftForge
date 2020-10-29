package net.minecraftforge.client;

import com.google.common.collect.Maps;
import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class ForgeWorldTypeScreens
{
    private static final Map<ForgeWorldType, BiomeGeneratorTypeScreens.IFactory> WORLD_TYPE_SCREEN_FACTORIES = Maps.newHashMap();

    public static synchronized void registerFactory(ForgeWorldType type, BiomeGeneratorTypeScreens.IFactory factory)
    {
        if (WORLD_TYPE_SCREEN_FACTORIES.containsKey(type))
            throw new IllegalStateException("Factory has already been registered for: " + type);

        WORLD_TYPE_SCREEN_FACTORIES.put(type, factory);
    }

    static BiomeGeneratorTypeScreens.IFactory getBiomeGeneratorTypeScreenFactory(Optional<BiomeGeneratorTypeScreens> generator, @Nullable BiomeGeneratorTypeScreens.IFactory biomegeneratortypescreens$ifactory)
    {
        return generator.filter(gen -> gen instanceof GeneratorType).map(type -> {
            GeneratorType forge = ((GeneratorType)type);
            return WORLD_TYPE_SCREEN_FACTORIES.get(forge.getWorldType());
        }).orElse(biomegeneratortypescreens$ifactory);
    }

    static void registerTypes()
    {
        ForgeRegistries.WORLD_TYPES.forEach(wt -> BiomeGeneratorTypeScreens.registerGenerator(new GeneratorType(wt)));
    }

    private static class GeneratorType extends BiomeGeneratorTypeScreens
    {
        private final ForgeWorldType worldType;

        public GeneratorType(ForgeWorldType wt)
        {
            super(wt.getDisplayName());
            worldType = wt;
        }

        public ForgeWorldType getWorldType()
        {
            return worldType;
        }

        @Nonnull
        @Override
        protected ChunkGenerator func_241869_a(@Nonnull Registry<Biome> p_241869_1_, @Nonnull Registry<DimensionSettings> p_241869_2_, long p_241869_3_)
        {
            return worldType.createChunkGenerator(p_241869_1_, p_241869_2_, p_241869_3_, "");
        }
    }
}