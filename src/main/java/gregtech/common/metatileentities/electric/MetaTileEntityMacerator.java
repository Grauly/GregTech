package gregtech.common.metatileentities.electric;

import gregtech.api.GTValues;
import gregtech.api.capability.impl.EnergyRecipeMapWorkableHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.util.GTUtility;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class MetaTileEntityMacerator extends SimpleMachineMetaTileEntity {

    private int outputAmount;

    public MetaTileEntityMacerator(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, int outputAmount, OrientedOverlayRenderer renderer, int tier) {
        super(metaTileEntityId, recipeMap, renderer, tier);
        this.outputAmount = outputAmount;
    }

    @Override
    protected EnergyRecipeMapWorkableHandler createWorkable(RecipeMap<?> recipeMap) {
        return new EnergyRecipeMapWorkableHandler(this, recipeMap, () -> energyContainer) {
            @Override
            protected int getByproductChanceMultiplier(Recipe recipe) {
                int byproductChanceMultiplier = 1;
                int tier = GTUtility.getTierByVoltage(getMaxVoltage());
                if (tier > GTValues.HV) {
                    byproductChanceMultiplier = 1 << (GTValues.HV - tier);
                }
                return byproductChanceMultiplier;
            }
        };
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new ItemStackHandler(outputAmount);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityMacerator(metaTileEntityId, workable.recipeMap, outputAmount, renderer, getTier());
    }
}