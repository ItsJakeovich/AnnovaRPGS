package net.annova.config.crafts;

import net.annova.object.item.base.ItemType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.inventory.CraftItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.annova.config.RPGConfig.getAllCrafts;
import static net.annova.main.AnnovaAPI.getMinecraftServer;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.AnnovaRPG.loadInfo;

public class ConfigCrafts {
    private String id;
    private ItemStack item;
    private int amount = 1;
    private CraftType craftType;
    private final List<ItemIngredient> ingredients = new ArrayList<>();
    private RecipeHolder<?> recipe;
    private List<String> recipeCraft = new ArrayList<>();
    private Material source = null;
    private long experience = 0;
    private int meltingTime = 10;
    List<SubCraft> subCrafts = new ArrayList<>();

    public static void loadConfig(FileConfiguration conf, Map<String, ConfigCrafts> crafts) {
        crafts.clear();
        for (Map<?, ?> b : conf.getMapList("materials")) {
            ConfigCrafts craft = new ConfigCrafts();
            if (!((boolean) b.get("valid"))) continue;

            craft.id = (String) b.get("id");
            if (b.containsKey("amount")) craft.amount = (int) b.get("amount");
            craft.item = getItemStack(craft.id, craft.amount);

            craft.craftType = CraftType.valueOf(((String) b.get("craft_type")).toUpperCase());
            if (craft.craftType.equals(CraftType.FURNACE)) {
                craft.source = Material.getMaterial((String) b.get("source"));
                craft.experience = (long) (int) b.get("experience");
                craft.meltingTime = (int) b.get("melting_time");
            }
            if (craft.source != null) {
                craft.ingredients.add(new ItemIngredient(craft.source, 1, null));
            }
            if (b.containsKey("ingredients")) {
                for (Map<?, ?> i : (List<Map<?, ?>>) b.get("ingredients")) {
                    int count = i.containsKey("count") ? (int) i.get("count") : 1;

                    craft.ingredients.add(new ItemIngredient(getItemStack((String) i.get("id"), 1).getItem(), count, (String) i.get("letter")));
                    if (b.containsKey("recipe")) {
                        craft.recipeCraft = (List<String>) b.get("recipe");
                    }
                }
            }
            if (b.containsKey("sub-crafts")) {
                for (Map<?, ?> sc : (List<Map<?, ?>>) b.get("sub-crafts")) {
                    if (!(boolean) sc.get("valid")) continue;
                    CraftType craft_type = CraftType.valueOf(((String) sc.get("craft_type")).toUpperCase());
                    int amount = 1;
                    if (sc.containsKey("amount")) {
                        amount = (int) sc.get("amount");
                    }
                    List<ItemIngredient> ingredients = new ArrayList<>();
                    List<String> recipe = new ArrayList<>();
                    for (Map<?, ?> i : (List<Map<?, ?>>) sc.get("ingredients")) {
                        int count = 1;

                        if (i.containsKey("count")) {
                            count = (int) i.get("count");
                        }
                        ingredients.add(new ItemIngredient(getItemStack((String) i.get("id"), 1).getItem(), count, (String) i.get("letter")));
                        crafts.put(craft.id, craft);
                    }
                    if (sc.containsKey("recipe")) {
                        recipe = (List<String>) sc.get("recipe");
                    }
                    craft.subCrafts.add(new SubCraft(craft_type, amount, ingredients, recipe));
                }
            }
            crafts.put(craft.id, craft);
        }

        loadCrafts();

        loadInfo.add("{pc}| {mc}Крафты {sc}: {ac}" + crafts.size());
    }

    private static net.minecraft.world.item.ItemStack getItemStack(String id, int amount) {
        try {
            return new ItemStack(CraftItemType.bukkitToMinecraft(Material.valueOf(id)), amount);
        } catch (IllegalArgumentException e) {
            return ItemType.getItemType(id).create(amount);
        }
    }

    private static void loadCrafts() {
        for (ConfigCrafts mat : getAllCrafts()) {
            if (mat.craftType.equals(CraftType.FURNACE)) {
                mat.recipe = new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(getInstance().getNamespaceName(), mat.id)),
                        new SmeltingRecipe("", CookingBookCategory.MISC, Ingredient.of(CraftItemType.bukkitToMinecraft(mat.getSource())), mat.item, mat.getExperience(), mat.getMeltingTime()));
            } else {
                mat.loadWorkbenceRecipe();
            }

            int count = 0;
            for (SubCraft craft : mat.subCrafts) {
                mat.loadWorkbenceRecipe(craft.getCraftType(), mat.id + "-" + count, craft.getRecipe(), craft.getIngredients());
                count++;
            }
            MinecraftServer server = getMinecraftServer();
            server.getRecipeManager().addRecipe(mat.recipe);
            server.getPlayerList().reloadRecipes();
        }
    }

    private void loadWorkbenceRecipe() {
        this.loadWorkbenceRecipe(this.craftType, this.id, this.recipeCraft, this.ingredients);
    }
    private void loadWorkbenceRecipe(CraftType type, String id, List<String> recipeCraft, List<ItemIngredient> ingredients) {
        switch (this.craftType) {
            case SHAPED -> {
                Map<Character, Ingredient> data = new HashMap<>();
                for (ItemIngredient ing : ingredients) {
                    data.put(ing.getLetter(), Ingredient.of(ing.getMaterial()));
                }
                this.recipe = new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(getInstance().getNamespaceName(), id)),
                        new ShapedRecipe("", CraftingBookCategory.MISC, ShapedRecipePattern.of(data, recipeCraft.toArray(new String[0])), this.item));
            }
            case SHAPELESS -> {
                List<Ingredient> data = new ArrayList<>(ingredients.size());
                for (ItemIngredient ingredient : this.ingredients) {
                    for (int i = 0; i < ingredient.getCount(); i++) {
                        data.add(Ingredient.of(ingredient.getMaterial()));
                    }
                }
                this.recipe = new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(getInstance().getNamespaceName(), id)),
                        new ShapelessRecipe("", CraftingBookCategory.MISC, this.item, data));
            }
        }
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public ItemStack getItemStack() {
        return item.copy();
    }

    public CraftType getCraftType() {
        return craftType;
    }

    public List<ItemIngredient> getIngredients() {
        return ingredients;
    }

    public RecipeHolder<?> getRecipe() {
        return recipe;
    }

    public List<String> getRecipeCraft() {
        return recipeCraft;
    }

    public Material getSource() {
        return source;
    }

    public long getExperience() {
        return experience;
    }

    public int getMeltingTime() {
        return meltingTime;
    }
}
