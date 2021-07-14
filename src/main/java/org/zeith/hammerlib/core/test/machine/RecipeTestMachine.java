package org.zeith.hammerlib.core.test.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.api.crafting.IItemIngredient;
import org.zeith.hammerlib.api.crafting.NamespacedRecipeRegistry;
import org.zeith.hammerlib.api.crafting.impl.BaseNameableRecipe;
import org.zeith.hammerlib.api.crafting.impl.ItemStackResult;
import org.zeith.hammerlib.api.crafting.impl.MCIngredient;
import org.zeith.hammerlib.api.crafting.impl.TagIngredient;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.test.TestPreferences;
import org.zeith.hammerlib.event.recipe.ReloadRecipeRegistryEvent;

public class RecipeTestMachine
		extends BaseNameableRecipe
{
	public static final NamespacedRecipeRegistry<RecipeTestMachine> REGISTRY = new NamespacedRecipeRegistry<>(RecipeTestMachine.class, new ResourceLocation("hammerlib", "test_machine"));

	public final IItemIngredient<?> in1, in2;
	public final ItemStackResult output;
	public final int time;

	public RecipeTestMachine(ResourceLocation id, int time, ItemStack output, Object a, Object b)
	{
		this(id, time, new ItemStackResult(output), new MCIngredient(RecipeHelper.fromComponent(a)), new MCIngredient(RecipeHelper.fromComponent(b)));
	}

	public RecipeTestMachine(ResourceLocation id, int time, ItemStack output, IItemIngredient<?> a, IItemIngredient<?> b)
	{
		this(id, time, new ItemStackResult(output), a, b);
	}

	public RecipeTestMachine(ResourceLocation id, int time, ItemStackResult output, IItemIngredient<?> a, IItemIngredient<?> b)
	{
		super(id, output, NonNullList.of(a, a, b));
		this.time = time;
		this.output = output;
		this.in1 = a;
		this.in2 = b;
	}

	public int getTime()
	{
		return time;
	}

	public ItemStack getRecipeOutput(TileTestMachine executor)
	{
		return output.getOutput(executor);
	}

	@Setup
	@OnlyIf(owner = TestPreferences.class, member = "enableTestMachine")
	public static void setup()
	{
		HammerLib.LOG.info("Setup Test Machine recipes!");

		REGISTRY.reload();
		MinecraftForge.EVENT_BUS.addGenericListener(RecipeTestMachine.class, RecipeTestMachine::addTestMachineRecipes);
	}

	public static void addTestMachineRecipes(ReloadRecipeRegistryEvent.AddRecipes<RecipeTestMachine> evt)
	{
		if(evt.is(RecipeTestMachine.REGISTRY))
		{
			evt.addRecipe(
					new RecipeTestMachine(
							new ResourceLocation("hammerlib", "testrecipe"),
							400,
							new ItemStackResult(new ItemStack(Items.REDSTONE)),
							new TagIngredient(Tags.Items.COBBLESTONE).quantify(4), new TagIngredient(ItemTags.LOGS)
					)
			);

			evt.addRecipe(
					new RecipeTestMachine(
							new ResourceLocation("hammerlib", "testrecipe2"),
							100,
							new ItemStackResult(new ItemStack(Items.CHEST)),
							new TagIngredient(ItemTags.LOGS).quantify(2), new TagIngredient(Tags.Items.INGOTS_IRON)
					)
			);
		}
	}
}