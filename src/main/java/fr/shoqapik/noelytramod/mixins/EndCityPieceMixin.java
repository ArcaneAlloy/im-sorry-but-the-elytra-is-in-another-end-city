package fr.shoqapik.noelytramod.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EndCityPieces.EndCityPiece.class)
public class EndCityPieceMixin extends TemplateStructurePiece {

    public EndCityPieceMixin(StructurePieceType p_226886_, int p_226887_, StructureTemplateManager p_226888_, ResourceLocation p_226889_, String p_226890_, StructurePlaceSettings p_226891_, BlockPos p_226892_) {
        super(p_226886_, p_226887_, p_226888_, p_226889_, p_226890_, p_226891_, p_226892_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void handleDataMarker(String p_227505_, BlockPos p_227506_, ServerLevelAccessor p_227507_, RandomSource p_227508_, BoundingBox p_227509_) {
        if (p_227505_.startsWith("Chest")) {
            BlockPos blockpos = p_227506_.below();
            if (p_227509_.isInside(blockpos)) {
                RandomizableContainerBlockEntity.setLootTable(p_227507_, p_227508_, blockpos, BuiltInLootTables.END_CITY_TREASURE);
            }
        } else if (p_227509_.isInside(p_227506_) && Level.isInSpawnableBounds(p_227506_)) {
            if (p_227505_.startsWith("Sentry")) {
                Shulker shulker = EntityType.SHULKER.create(p_227507_.getLevel());
                shulker.setPos((double)p_227506_.getX() + 0.5D, (double)p_227506_.getY(), (double)p_227506_.getZ() + 0.5D);
                p_227507_.addFreshEntity(shulker);
            } else if (p_227505_.startsWith("Elytra")) {
                ItemFrame itemframe = new ItemFrame(p_227507_.getLevel(), p_227506_, this.placeSettings.getRotation().rotate(Direction.SOUTH));
                // Item configurable (config/noelytrasmod-common.toml); por defecto el libro de siempre
                ItemStack stack = fr.shoqapik.noelytramod.NoElytrasConfig.createFrameItem();
                itemframe.setItem(stack, false);
                p_227507_.addFreshEntity(itemframe);
            }
        }

    }

}
