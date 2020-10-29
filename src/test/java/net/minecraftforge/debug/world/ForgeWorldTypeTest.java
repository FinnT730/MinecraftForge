package net.minecraftforge.debug.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraftforge.client.ForgeWorldTypeScreens;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;

@Mod("forge_world_type_test")
public class ForgeWorldTypeTest
{
    public ForgeWorldTypeTest()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ForgeWorldType.class, this::registerWorldTypes);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerWorldTypeScreenFactories);
    }

    private void registerWorldTypes(RegistryEvent.Register<ForgeWorldType> event)
    {
        event.getRegistry().registerAll(
                new ForgeWorldType(DimensionGeneratorSettings::func_242750_a).setRegistryName("test_world_type")
        );
    }

    @ObjectHolder("forge_world_type_test:test_world_type")
    public static ForgeWorldType testWorldType;

    private void registerWorldTypeScreenFactories(FMLClientSetupEvent event)
    {
        ForgeWorldTypeScreens.registerFactory(testWorldType, (returnTo, dimensionGeneratorSettings) -> new Screen(testWorldType.getDisplayName())
        {
            @Override
            protected void init()
            {
                super.init();

                addButton(new Button(0, 0, 120, 20, new StringTextComponent("close"), btn -> {
                    Minecraft.getInstance().displayGuiScreen(returnTo);
                }));
            }
        });
    }
}
