package fr.shoqapik.noelytramod.mixins;

import fr.shoqapik.noelytramod.NoElytraConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCityPieces.EndCityPiece.class)
public abstract class EndCityPieceMixin extends TemplateStructurePiece {

    protected EndCityPieceMixin() {
        super(null, 0, null, null, null, null, null);
    }

    /**
     * Intercepts only the "Elytra" data marker, leaving Chest and Sentry to vanilla.
     *
     * Fixes:
     * 1. LootR compatibility: LootR converts item frames into its own chest blocks during
     *    structure generation. This can leave conflicting entities at the frame position,
     *    causing a crash when entering the End. We discard any existing entities at the
     *    position before spawning our frame.
     * 2. Config: item and name are now read from config/noelytramod.json instead of
     *    being hardcoded.
     */
    @Inject(method = "handleDataMarker", at = @At("HEAD"), cancellable = true)
    protected void onHandleDataMarker(String marker, BlockPos pos, ServerLevelAccessor level,
                                      RandomSource random, BoundingBox boundingBox, CallbackInfo ci) {
        if (!marker.startsWith("Elytra")) return;

        // Cancel vanilla Elytra frame placement
        ci.cancel();

        if (!boundingBox.isInside(pos) || !Level.isInSpawnableBounds(pos)) return;

        // LootR fix: remove any entities already occupying this position
        level.getLevel().getEntities(null, new AABB(pos).inflate(0.5))
                .forEach(e -> e.discard());

        // Resolve configured item, fall back to Book if ID is unknown
        Item item = BuiltInRegistries.ITEM
                .getOptional(ResourceLocation.parse(NoElytraConfig.itemId))
                .orElse(Items.BOOK);

        ItemStack stack = new ItemStack(item);

        if (NoElytraConfig.itemName != null && !NoElytraConfig.itemName.isBlank()) {
            stack.set(DataComponents.CUSTOM_NAME, Component.literal(NoElytraConfig.itemName));
        }

        Direction facing = this.placeSettings.getRotation().rotate(Direction.SOUTH);
        ItemFrame frame = new ItemFrame(level.getLevel(), pos, facing);
        frame.setItem(stack, false);
        level.addFreshEntity(frame);
    }
}
