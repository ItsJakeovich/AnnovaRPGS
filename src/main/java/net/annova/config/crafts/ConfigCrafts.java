package net.annova.config.crafts;

import net.annova.exception.AnnovaException;
import net.annova.object.item.base.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.config.RPGConfig.getAllCrafts;
import static net.annova.main.AnnovaRPG.loadInfo;
import static net.annova.main.Keys.getNamespacedKey;
import static net.annova.util.MessageObjects.color;

public class ConfigCrafts {
    public static Inventory materialInventories(int number) {
        List<Inventory> inventories = new ArrayList<>();
        int count = 0;
        int invCount = 0;
        inventories.add(Bukkit.createInventory(null, 54, color("&l&bAboba 1")));
        List<ConfigCrafts> confM = new ArrayList<>(getAllCrafts());
        for (ConfigCrafts mat : confM) {
            if (count > 53) {
                invCount++;
                inventories.add(Bukkit.createInventory(null, 54, color("&l&bAboba " + invCount + 1)));
                count = 0;
            }
            Inventory inv = inventories.get(invCount);
            inv.setItem(count, mat.getItemStack());
            count++;
        }
        return inventories.get(number);
    }

    private String id;
    private ItemStack item;
    private int amount = 1;
    private CraftType craftType;
    private final List<ItemIngredient> ingredients = new ArrayList<>();
    private Recipe recipe;
    private List<String> recipeCraft = new ArrayList<>();
    private Material source = null;
    private long experience = 0;
    private int meltingTime = 10;
    List<SubCraft> subCrafts = new ArrayList<>();

    public static void loadConfig(FileConfiguration conf, Map<String, ConfigCrafts> crafts) throws AnnovaException {
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
                craft.ingredients.add(new ItemIngredient(new ItemStack(craft.source), 1, null));
            }
            if (b.containsKey("ingredients")) {
                for (Map<?, ?> i : (List<Map<?, ?>>) b.get("ingredients")) {
                    int count = i.containsKey("count") ? (int) i.get("count") : 1;

                    craft.ingredients.add(new ItemIngredient(getItemStack((String) i.get("id"), 1), count, (String) i.get("letter")));
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
                        ingredients.add(new ItemIngredient(getItemStack((String) i.get("id"), 1), count, (String) i.get("letter")));
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

    private static ItemStack getItemStack(String id, int amount) {
        try {
            return new ItemStack(Material.valueOf(id), amount);
        } catch (IllegalArgumentException e) {
            return ItemType.getItemType(id).create(amount);
        }
    }

    private static void loadCrafts() {
        for (ConfigCrafts mat : getAllCrafts()) {
            if (mat.craftType.equals(CraftType.FURNACE)) {
                mat.recipe = new FurnaceRecipe(getNamespacedKey(mat.id), mat.item, mat.getSource(), mat.getExperience(), mat.getMeltingTime());
            } else {
                mat.loadWorkbenceRecipe();
            }

            int count = 0;
            for (SubCraft craft : mat.subCrafts) {
                mat.loadWorkbenceRecipe(craft.getCraftType(), mat.id + "-" + count, craft.getRecipe(), craft.getIngredients());
                count++;
            }
            Bukkit.addRecipe(mat.recipe);
        }
    }

    private void loadWorkbenceRecipe() {
        this.loadWorkbenceRecipe(this.craftType, this.id, this.recipeCraft, this.ingredients);
    }
    private void loadWorkbenceRecipe(CraftType type, String identifier, List<String> recipeCraft, List<ItemIngredient> ingredients) {
        switch (craftType) {
            case SHAPED -> {
                ShapedRecipe recipe = new ShapedRecipe(getNamespacedKey(identifier), this.item);
                recipe.shape(this.recipeCraft.get(0), this.recipeCraft.get(1), this.recipeCraft.get(2));
                for (ItemIngredient ingredient : this.ingredients) {
                    recipe.setIngredient(ingredient.getLetter(), new RecipeChoice.ExactChoice(ingredient.getMaterial()));
                }
                this.recipe = recipe;
            }
            case SHAPELESS -> {
                ShapelessRecipe recipe = new ShapelessRecipe(getNamespacedKey(identifier), this.item);
                for (ItemIngredient ingredient : this.ingredients) {
                    for (int i = 0; i < ingredient.getCount(); i++) {
                        recipe.addIngredient(new RecipeChoice.ExactChoice(ingredient.getMaterial()));
                    }
                }
                this.recipe = recipe;
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
        return item.clone();
    }

    public CraftType getCraftType() {
        return craftType;
    }

    public List<ItemIngredient> getIngredients() {
        return ingredients;
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
