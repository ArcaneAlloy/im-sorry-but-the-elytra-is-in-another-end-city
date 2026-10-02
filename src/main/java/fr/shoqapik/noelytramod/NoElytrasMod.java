package fr.shoqapik.noelytramod;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(NoElytrasMod.MODID)
public class NoElytrasMod
{
    public static final String MODID = "noelytrasmod";

    public NoElytrasMod()
    {
        // config/noelytrasmod-common.toml: item que sustituye a la Elytra en el marco del barco
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NoElytrasConfig.SPEC);
    }
}
