package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.IRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class BlastingRecipeBuilder
		extends AbstractCookingRecipeBuilder<BlastingRecipeBuilder>
{
	public BlastingRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
		cookTime = 100;
	}

	@Override
	protected IRecipe<?> generateRecipe()
	{
		return new BlastingRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}