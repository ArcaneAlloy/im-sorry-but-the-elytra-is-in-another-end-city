package fr.shoqapik.noelytramod;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

/**
 * Configuración (config/noelytrasmod-common.toml): el item que se pone en el marco donde iba la Elytra
 * en el barco de las End Cities.
 */
public final class NoElytrasConfig {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String DEFAULT_NAME = "I'm sorry but the Elytra is in another End City";

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<String> ITEM;
    public static final ForgeConfigSpec.ConfigValue<String> NBT;
    public static final ForgeConfigSpec.ConfigValue<String> NAME;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("Item placed in the item frame where the Elytra used to be (End City ship).").push("elytra_frame");
        ITEM = b.comment("Item id, e.g. \"minecraft:book\" or \"supplementaries:cartographers_quill\".")
                .define("item", "minecraft:book");
        NBT = b.comment("Optional NBT for the item in SNBT format, e.g. {targetStructure:\"dungeons_arise:keep_kayra\"}.",
                        "Leave empty for no NBT. A display name set here (display:{Name:...}) is kept.")
                .define("nbt", "");
        NAME = b.comment("Optional plain-text name for the item. Leave empty to not rename it",
                        "(use this empty if the name comes from the NBT).")
                .define("name", DEFAULT_NAME);
        b.pop();
        SPEC = b.build();
    }

    private NoElytrasConfig() {}

    /** Crea el item configurado. Si el id o el NBT no son válidos, avisa en el log y usa lo que se pueda (o el libro). */
    public static ItemStack createFrameItem() {
        Item item = Items.BOOK;
        String id = ITEM.get().trim();
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl != null && ForgeRegistries.ITEMS.containsKey(rl)) {
            item = ForgeRegistries.ITEMS.getValue(rl);
        } else {
            LOGGER.warn("[NoElytras] Item '{}' not found, using minecraft:book", id);
        }

        ItemStack stack = new ItemStack(item);

        String nbt = NBT.get().trim();
        if (!nbt.isEmpty()) {
            try {
                CompoundTag tag = TagParser.parseTag(nbt);
                stack.setTag(tag);
            } catch (CommandSyntaxException e) {
                LOGGER.warn("[NoElytras] Invalid NBT '{}': {}", nbt, e.getMessage());
            }
        }

        String name = NAME.get();
        if (name != null && !name.isEmpty()) {
            stack.setHoverName(Component.literal(name));
        }
        return stack;
    }
}
