package net.annova.object.item.base;

import net.annova.object.item.DungeonKeyItem;
import net.annova.object.item.base.attribute.ItemConfiguration;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.item.base.attribute.RPGTool;
import net.annova.object.item.sector0.forest.*;
import net.annova.object.item.sector0.dryad.WolfSkin;
import net.annova.object.item.sector0.meteorite.MeteoriteFragment;
import net.annova.object.item.sector0.mines.MagicScrap;
import net.annova.object.item.mineral.ForiumFragmentItem;
import net.annova.object.item.mineral.ForiumIngotItem;
import net.annova.object.item.container.PrisonContainerItem;
import net.annova.object.item.crystal.*;
import net.annova.object.item.equipment.ControlMovementStickItem;
import net.annova.object.item.equipment.ControlStopStickItem;
import net.annova.object.item.sector0.oasis.TideBlade;
import net.annova.object.item.sector0.oasis.TideGraspRing;
import net.annova.object.item.equipment.prison.*;
import net.annova.object.item.sector0.swamps.SpiderChitin;
import net.annova.object.item.token.*;
import net.annova.object.item.sector0.oasis.*;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.minecraft.world.item.component.CustomModelData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_21_R4.inventory.components.CraftCustomModelDataComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Set;

import static net.annova.config.AnnovaConfig.colorString;
import static net.annova.config.AnnovaConfig.getLangFile;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemCategory.CRYSTALL;
import static net.annova.object.item.base.RPGItem.getItemId;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.Keys.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.MessageObjects.color;

public enum ItemType {
    // Containers
    PRISON_CONTAINER(new PrisonContainerItem()),
    // Stones
    AIR_STONE(CRYSTALL, new AirStoneItem()),
    AQUA_STONE(CRYSTALL, new AquaStoneItem()),
    BALANCE_STONE(CRYSTALL, new BalanceStoneItem()),
    BIOME_STONE(CRYSTALL, new BiomeStoneItem()),
    CHRONO_STONE(CRYSTALL, new ChronoStoneItem()),
    DARK_STONE(CRYSTALL, new DarkStoneItem()),
    DEATH_STONE(CRYSTALL, new DeathStoneItem()),
    DEFENCE_STONE(CRYSTALL, new DefenceStoneItem()),
    DRAGON_STONE(CRYSTALL, new DragonStoneItem()),
    EARTH_STONE(CRYSTALL, new EarthStoneItem()),
    GRAVITY_STONE(CRYSTALL, new GravityStoneItem()),
    GREEN_STONE(CRYSTALL, new GreenStoneItem()),
    ICE_STONE(CRYSTALL, new IceStoneItem()),
    IGNIS_STONE(CRYSTALL, new IgnisStoneItem()),
    LIGHT_STONE(CRYSTALL, new LightStoneItem()),
    NATURE_STONE(CRYSTALL, new NatureStoneItem()),
    PSYONIC_STONE(CRYSTALL, new PsyonicStoneItem()),
    RED_STONE(CRYSTALL, new RedStoneItem()),
    REGENERATION_STONE(CRYSTALL, new RegenerationStoneItem()),
    REPAIR_STONE(CRYSTALL, new RepairStoneItem()),
    RETURN_STONE(CRYSTALL, new ReturnStoneItem()),
    RUNIC_DEFENCE_STONE(CRYSTALL, new RunicDefenceStoneItem()),
    RUNIC_STONE(CRYSTALL, new RunicStoneItem()),
    STRENGTH_STONE(CRYSTALL, new StrengthStoneItem()),
    TELEPORT_STONE(CRYSTALL, new TeleportStoneItem()),
    TIME_STONE(CRYSTALL, new TimeStoneItem()),
    VITAL_STONE(CRYSTALL, new VitalStoneItem()),
    // Equipment
    PRISON_BOOTS(new PrisonBootsItem()),
    PRISON_CHESTPLATE(new PrisonChestplateItem()),
    PRISON_HELMET(new PrisonHelmetItem()),
    PRISON_LEGGINGS(new PrisonLeggingsItem()),
    PRISON_STEEL_SWORD(new PrisonSteelSwordItem()),
    PRISON_SWORD(new PrisonSwordItem()),

    CONTROL_MOVEMENT_STICK(new ControlMovementStickItem()),
    CONTROL_STOP_STICK(new ControlStopStickItem()),
    // Components
    FORIUM_FRAGMENT(new ForiumFragmentItem()),
    FORIUM_INGOT(new ForiumIngotItem()),
    // Tokens
    CREEPER_TOKEN(new CreeperTokenItem()),
    DURABILITY_TOKEN(new DurabilityTokenItem()),
    FLY_TOKEN(new FlyTokenItem()),
    PRISON_TOKEN(new PrisonTokenItem()),
    RANGE_TOKEN(new RangeTokenItem()),
    SATURATION_TOKEN(new SaturationTokenItem()),
    // S0 - Dryad Pedestal
    WOLF_SKIN(new WolfSkin()),
    // S0 - FOREST
    ABSORBS_NIGHT_RING(new AbsorbsNightRing()),
    BLADE_IMBUED_WITH_MAGIC(new BladeImbuedWithMagic()),
    CREATING_NIGHT_AMULET(new CreatingNightAmulet()),
    DARK_ELF_BOW(new DarkElfBow()),
    DARKNESS_CONCENTRATION(new DarknessConcentration()),
    DARKNESS_FRAGMENT(new DarknessFragment()),
    ENT_CORE(new EntCore()),
    ENT_ROOT(new EntRoot()),
    ENTS_PATRON_AMULET(new EntsPatronAmulet()),
    ROOTS_LASH(new RootsLash()),
    // S0 - METEORITE
    METEORITE_FRAGMENT(new MeteoriteFragment()),
    // S0 - Mines
    MAGIC_SCRAP(new MagicScrap()),
    // S0 - OASIS
    TIDE_BLADE(new TideBlade()),
    TIDE_GRASP_RING(new TideGraspRing()),
    UNKNOWN_SOURCE_WATER(new UnknownSourceWater()),

    ANGEL_FISH(new AngelFish()),
    ANGLER_FISH_SPAWN_EGG(new AnglerfishSpawnEgg()),
    AZURE_FISH(new AzureFish()),
    BARK_BEETLE_FISH(new BarkBeetleFish()),
    BLOB_FISH_BLUE(new BlobFishBlue()),
    BLOB_FISH_GRAY(new BlobFishGray()),
    BLOB_FISH_YELLOW(new BlobFishYellow()),
    CRAB_CLAW(new CrabClaw()),
    ELK_FISH(new ElkFish()),
    FIRE_FISH(new FireFish()),
    FURY_CAVIAR(new FuryCaviar()),
    KOI_FISH(new KoiFish()),
    NALIM_FISH(new NalimFish()),
    OASIS_FISH(new OasisFish()),
    PEARL(new Pearl()),
    ROYAL_PUFFERFISH(new RoyalPufferfish()),
    SEASMITH(new Seasmith()),
    SILVER_PUMP(new SilverPump()),
    VOLCANIC_STREAMS_MALMA(new VolcanicStreamsMalma()),
    WHALE_FISH(new WhaleFish()),
    // S0 - Swamps
    SPIDER_CHITIN(new SpiderChitin()),
    // unique
    DUNGEON_KEY(new DungeonKeyItem()),
    ;

    private final RPGItem item;
    private final ItemCategory category;
    private final String id;
    ItemType(RPGItem item) {
        this(ItemCategory.WITHOUT, item);
    }
    ItemType(ItemCategory category, RPGItem item) {
        this.item = item;
        this.category = category;
        this.id = this.name().toLowerCase();
    }

    public RPGItem getItem() {
        return item;
    }

    private static final Map<ItemCategory, Set<ItemType>> ITEMS_BY_CATEGORY = new EnumMap<>(ItemCategory.class);
    private static final Map<String, ItemType> BY_NAME = new HashMap<>();
    static {
        for (ItemCategory cat : ItemCategory.values()) {
            ITEMS_BY_CATEGORY.put(cat, new HashSet<>());
        }
        for (ItemType itemType : ItemType.values()) {
            ITEMS_BY_CATEGORY.get(itemType.category).add(itemType);
            BY_NAME.put(itemType.id, itemType);
        }
    }
    public static ItemType getItemType(String itemName) {
        return BY_NAME.get(itemName);
    }
    @Nullable
    public static ItemType getItemType(ItemStack item) {
        return BY_NAME.get(getItemId(item));
    }
    public static Set<ItemType> getItemsByCategory(ItemCategory category) {
        return ITEMS_BY_CATEGORY.get(category);
    }

    public boolean hasPlayer(Entity entity) {
        if (entity instanceof Player player) return this.has(player);
        return false;
    }
    public boolean has(Entity entity) {
        if (entity instanceof Player player) return this.has(player);
        if (entity instanceof Mob mob) return this.has(mob);
        return false;
    }
    public boolean has(Mob mob) {
        EntityEquipment equipment = mob.getEquipment();
        return this.id.equals(getItemId(equipment.getItemInMainHand())) || this.id.equals(getItemId(equipment.getItemInOffHand()));
    }
    public boolean has(Player player) {
        if (player == null) return false;
        if (this.getItem() instanceof ItemEquipment) return this.hasEquipment(player);
        else return this.hasItem(player);
    }
    public boolean hasItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (this.id.equals(getItemId(item))) return true;
        }
        return false;
    }
    public boolean hasEquipment(Player player) {
        for (ItemStack item : getRPGPerson(player).getEquipment().getAllEquipment()) {
            if (this.id.equals(getItemId(item))) return true;
        }
        return false;
    }
    public boolean equals(ItemStack item) {
        return this.id.equals(getItemId(item));
    }

    /**
     * @return true if item found and decreased.
     */
    public boolean removeItem(LivingEntity entity) {
        if (entity instanceof Player player) {
            PlayerInventory inv = player.getInventory();
            for (ItemStack item : inv.getContents()) {
                if (!this.id.equals(getItemId(item))) continue;
                inv.removeItem(item);
                return true;
            }
        } else {
            EntityEquipment equipment = entity.getEquipment();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (!this.id.equals(getItemId(equipment.getItem(slot)))) continue;
                equipment.setItem(slot, null);
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if item similar and decreased.
     */
    public boolean use(Player player, ItemStack item) {
        if (!this.id.equals(getItemId(item))) return false;
        RPGItem.subtractItem(player, item);
        return true;
    }
    public boolean use(Player player) {
        return this.use(player, player.getInventory().getItemInMainHand());
    }

    @Nullable
    public ItemStack getFromInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (this.id.equals(getItemId(item))) return item;
        }
        return null;
    }

    public ItemStack create(Player player) {
        return this.create(getPerson(player));
    }
    public ItemStack create(Person person) {
        return this.create(person, 1);
    }
    public ItemStack create(RPGPerson person) {
        return this.create(person, 1);
    }
    public ItemStack create(int amount) {
        return this.create((Person) null, amount);
    }
    public ItemStack create(double chance) {
        return this.create((Person) null, 1, chance);
    }
    public ItemStack create(Person person, int amount) {
        return this.create(person, amount, 1.00);
    }
    public ItemStack create(RPGPerson person, int amount) {
        return this.create(person, amount, 1.00);
    }
    public ItemStack create(Player player, int amount) {
        return this.create(getPerson(player), amount, 1.00);
    }
    public ItemStack create(Person person, double chance) {
        return this.create(person, 1, chance);
    }
    public ItemStack create(Player player, double chance) {
        return this.create(getPerson(player), 1, chance);
    }
    public ItemStack create(Player player, int amount, double chance) {
        return this.create(getPerson(player), amount, chance);
    }
    public ItemStack create(RPGPerson person, int amount, double chance) {
        return this.create(person.getPerson(), amount, chance);
    }
    public ItemStack create(Person person, int amount, double chance) {
        if (amount < 1 || (chance != 1 && !chance(chance))) return new ItemStack(Material.AIR);

        ItemStack item = new ItemStack(this.item.type(), amount);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);

        FileConfiguration langFile = getLangFile(getInstance(), person == null ? "ru" : person.getLanguage());

        String name = langFile.getString(this.name(), "unknown-name");
        meta.setDisplayName(color(this.item.rarity().getColor() + name));
        CustomModelData model = new CustomModelData(this.item.modelData(), List.of(), List.of(), List.of());
        meta.setCustomModelDataComponent(new CraftCustomModelDataComponent(model));

        List<String> newLore = new ArrayList<>();
        String lore = langFile.getString(this.name() + "-lore", null);
        if (lore != null) {
            if (person != null) lore = person.colorString(lore);
            else                lore = colorString(lore);

            String[] loreString = lore.split("\n");
            for (String str : loreString) {
                newLore.add(color(str));
            }
            newLore.add("");
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(NK_ID, PersistentDataType.STRING, this.id);

        if (this.item.unstackable()) {
            pdc.set(NamespacedKey.fromString(UUID.randomUUID().toString(), getInstance()), PersistentDataType.INTEGER, 0);
        }

        if (this.item instanceof RPGTool weapon && !this.getItem().type().equals(Material.BOW)) {
            String damage = langFile.getString("tool-damage", "unknown") + weapon.damage() + "⚔";
            String speed = langFile.getString("tool-speed", "unknown") + weapon.attackSpeed() + "⚡";
            if (person != null) {
                damage = person.colorString(damage);
                speed = person.colorString(speed);
            } else {
                damage = colorString(damage);
                speed = colorString(speed);
            }
            newLore.add(damage);
            newLore.add(speed);

            meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE);
            meta.removeAttributeModifier(Attribute.ATTACK_SPEED);

            AttributeModifier damageModifier = new AttributeModifier(
                    NK_ATTACK_DAMAGE,
                    weapon.damage()-1,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.MAINHAND
            );

            AttributeModifier attackSpeedModifier = new AttributeModifier(
                    NK_ATTACK_SPEED,
                    weapon.attackSpeed()-4,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.MAINHAND
            );

            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
            meta.addAttributeModifier(Attribute.ATTACK_SPEED, attackSpeedModifier);
        }

        if (this.item instanceof ItemConfiguration configuration) {
            configuration.initConfiguration(meta);
        }

        if (!newLore.isEmpty()) {
            int lastIndex = newLore.size()-1;
            if (newLore.get(lastIndex).isEmpty()) {
                newLore.remove(lastIndex);
            }
        }
        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }
}
