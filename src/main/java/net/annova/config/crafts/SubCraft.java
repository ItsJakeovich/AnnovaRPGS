package net.annova.config.crafts;

import java.util.List;

public class SubCraft {
    private final CraftType craftType;
    private final int amount;
    private final List<ItemIngredient> ingredients;
    private final List<String> recipe;

    public SubCraft(CraftType craftType, int amount, List<ItemIngredient> ingredients, List<String> recipe) {
        this.craftType = craftType;
        this.amount = amount;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }

    public CraftType getCraftType() {
        return craftType;
    }

    public int getAmount() {
        return amount;
    }

    public List<ItemIngredient> getIngredients() {
        return ingredients;
    }

    public List<String> getRecipe() {
        return recipe;
    }
}
