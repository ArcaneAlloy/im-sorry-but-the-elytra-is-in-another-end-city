package fr.shoqapik.noelytramod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(NoElytrasMod.MODID)
public class NoElytrasMod {

    public static final String MODID = "noelytrasmod";

    public NoElytrasMod(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NoElytraConfig.load();
    }
}
