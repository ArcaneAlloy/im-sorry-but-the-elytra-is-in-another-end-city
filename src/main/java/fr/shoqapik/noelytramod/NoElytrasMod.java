package fr.shoqapik.noelytramod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NoElytrasMod.MODID)
public class NoElytrasMod {

    public static final String MODID = "noelytrasmod";

    public NoElytrasMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NoElytraConfig.load();
    }
}
