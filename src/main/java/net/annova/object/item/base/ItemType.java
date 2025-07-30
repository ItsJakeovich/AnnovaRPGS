package net.annova.object.item.base;

import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.annova.object.item.*;
import net.annova.object.item.base.attribute.ItemConfiguration;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.item.base.attribute.RPGArmor;
import net.annova.object.item.base.attribute.RPGTool;
import net.annova.object.item.civilization.BraidedLeather;
import net.annova.object.item.civilization.Camp;
import net.annova.object.item.civilization.FirstAidKit;
import net.annova.object.item.civilization.Log;
import net.annova.object.item.sector0.meteorite.ChitinBoots;
import net.annova.object.item.sector0.meteorite.ChitinChestplate;
import net.annova.object.item.sector0.meteorite.ChitinHelmet;
import net.annova.object.item.sector0.meteorite.ChitinLeggings;
import net.annova.object.item.sector0.dryad.DarkWolfFang;
import net.annova.object.item.sector0.dryad.PackLeaderAmulet;
import net.annova.object.item.sector0.forest.*;
import net.annova.object.item.sector0.dryad.WolfSkin;
import net.annova.object.item.sector0.meteorite.FireCrystal;
import net.annova.object.item.sector0.meteorite.MagmaFragment;
import net.annova.object.item.sector0.meteorite.MeteoriteFragment;
import net.annova.object.item.sector0.meteorite.MeteoriteGolemCore;
import net.annova.object.item.sector0.mines.MagicScrap;
import net.annova.object.item.mineral.ForiumFragmentItem;
import net.annova.object.item.mineral.ForiumIngotItem;
import net.annova.object.item.container.PrisonContainerItem;
import net.annova.object.item.crystal.*;
import net.annova.object.item.ControlMovementStickItem;
import net.annova.object.item.ControlStopStickItem;
import net.annova.object.item.sector0.mines.PurpleCrystalFragment;
import net.annova.object.item.sector0.oasis.TideBlade;
import net.annova.object.item.sector0.oasis.TideGraspRing;
import net.annova.object.item.sector0.swamps.SpiderChitin;
import net.annova.object.item.token.*;
import net.annova.object.item.sector0.oasis.*;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.annova.lang.AISString;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Set;

import static net.annova.config.AnnovaConfig.*;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemCategory.CRYSTALL;
import static net.annova.object.item.base.RPGItem.getItemId;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.Keys.*;
import static net.annova.object.person.RPGPerson.colorizeNumber;
import static net.annova.util.BukkitObjects.chance;
import static org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy;

public enum ItemType {
    EMPTY(new EmptyItem()),
    // Civilization
    BRAIDED_LEATHER(new BraidedLeather()),
    CAMP(new Camp()),
    FIRST_AID_KIT(new FirstAidKit()),
    LOG(new Log()),
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
    DARK_WOLF_FANG(new DarkWolfFang()),
    PACK_LEADER_AMULET(new PackLeaderAmulet()),
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
    CHITIN_HELMET(new ChitinHelmet()),
    CHITIN_CHESTPLATE(new ChitinChestplate()),
    CHITIN_LEGGINGS(new ChitinLeggings()),
    CHITIN_BOOTS(new ChitinBoots()),

    FIRE_CRYSTAL(new FireCrystal()),
    MAGMA_FRAGMENT(new MagmaFragment()),
    METEORITE_FRAGMENT(new MeteoriteFragment()),
    METEORITE_GOLEM_CORE(new MeteoriteGolemCore()),
    // S0 - Mines
    MAGIC_SCRAP(new MagicScrap()),
    PURPLE_CRYSTAL_FRAGMENT(new PurpleCrystalFragment()),
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
        BY_NAME.put("", EMPTY);
    }
    public static ItemType getItemType(String itemName) {
        return BY_NAME.get(itemName);
    }
    @Nullable
    public static ItemType getItemType(org.bukkit.inventory.ItemStack item) {
        return getItemType(asNMSCopy(item));
    }
    @Nullable
    public static ItemType getItemType(ItemStack item) {
        return BY_NAME.get(getItemId(item));
    }
    public static Set<ItemType> getItemsByCategory(ItemCategory category) {
        return ITEMS_BY_CATEGORY.get(category);
    }
    public static RPGItem getRPGItem(ItemStack item) {
        return getItemType(item).getItem();
    }
    public static boolean isInstance(ItemStack item, Class<?> clazz) {
        return getItemType(item).isInstance(clazz);
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
    public boolean has(net.minecraft.world.entity.LivingEntity living) {
        net.minecraft.world.entity.EntityEquipment equipment = living.equipment;
        return this.id.equals(getItemId(equipment.get(net.minecraft.world.entity.EquipmentSlot.MAINHAND)))
                || this.id.equals(getItemId(equipment.get(net.minecraft.world.entity.EquipmentSlot.OFFHAND)));
    }
    public boolean has(Mob mob) {
        EntityEquipment equipment = mob.getEquipment();
        return this.id.equals(getItemId(equipment.getItemInMainHand())) || this.id.equals(getItemId(equipment.getItemInOffHand()));
    }
    public boolean has(net.minecraft.world.entity.player.Player player) {
        if (player == null) return false;
        if (this.getItem() instanceof ItemEquipment) return this.hasEquipment(player);
        else                                         return this.hasItem(player);
    }
    public boolean has(Player player) {
        if (player == null) return false;
        if (this.getItem() instanceof ItemEquipment) return this.hasEquipment(player);
        else                                         return this.hasItem(player);
    }
    public boolean hasItem(net.minecraft.world.entity.player.Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (this.id.equals(getItemId(item))) return true;
        }
        return false;
    }
    public boolean hasItem(Player player) {
        for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
            if (this.id.equals(getItemId(item))) return true;
        }
        return false;
    }
    public boolean hasEquipment(net.minecraft.world.entity.player.Player player) {
        for (ItemStack item : getRPGPerson(player.getUUID()).getEquipment().getAllEquipment()) {
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
    public boolean equals(org.bukkit.inventory.ItemStack item) {
        return this.id.equals(getItemId(item));
    }
    public boolean equals(ItemStack item) {
        return this.id.equals(getItemId(item));
    }
    public boolean isInstance(Class<?> clazz) {
        return clazz.isInstance(this.item);
    }

    /**
     * @return true if item found and decreased.
     */
    public boolean removeItem(LivingEntity entity) {
        if (entity instanceof Player player) {
            PlayerInventory inv = player.getInventory();
            for (org.bukkit.inventory.ItemStack item : inv.getContents()) {
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

    public boolean use(Player player) {
        return this.use(player, player.getInventory().getItemInMainHand());
    }
    public boolean use(Player player, String itemId) {
        return this.use(player, itemId, player.getInventory().getItemInMainHand());
    }
    public boolean use(Player player, org.bukkit.inventory.ItemStack item) {
        return this.use(player, getItemId(item), item);
    }
    public boolean use(Player player, String itemId, org.bukkit.inventory.ItemStack item) {
        if (!this.id.equals(itemId)) return false;
        RPGItem.subtractItem(player, item);
        return true;
    }
    public boolean use(ServerPlayer player, ItemStack item) {
        if (!this.id.equals(getItemId(item))) return false;
        RPGItem.subtractItem(player, item);
        return true;
    }

    @Nullable
    public org.bukkit.inventory.ItemStack getFromInventory(Player player) {
        for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
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
        if (this.item.type() == Items.AIR|| amount < 1 || (chance != 1 && !chance(chance))) return ItemStack.EMPTY;

        ItemStack nms = new ItemStack(this.item.type(), amount);

        Map<String, AISString> local = getLangMap(getInstance(), person == null ? "ru" : person.getLanguage());

        nms.set(DataComponents.CUSTOM_NAME, local.getOrDefault(this.name(), local.get("unknown-name")).clone()
                .getDefined(person, this.item.rarity().getColor())
        );
        nms.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(this.item.modelData(), List.of(), List.of(), List.of()));

        List<Component> newLore = new ArrayList<>();
        AISString lore = local.get(this.name() + "-lore");
        if (lore != null) newLore.addAll(lore.getDefinedSplitted(person));

        CompoundTag tag = new CompoundTag();
        tag.putString(NK_ID.toString(), this.id);

        nms.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        ItemAttributeModifiers attribute = nms.get(DataComponents.ATTRIBUTE_MODIFIERS);
        List<ItemAttributeModifiers.Entry> modifiers = new ArrayList<>(attribute.modifiers());
        if (this.item instanceof RPGTool weapon && this.getItem().type() != Items.BOW) {
            newLore.add(Component.empty());

            MutableComponent damage = local.getOrDefault("tool-damage", local.get("unknown")).clone()
                    .append(colorizeNumber(weapon.damage()))
                    .getDefined(person);
            MutableComponent speed = local.getOrDefault("tool-speed", local.get("unknown")).clone()
                    .append(colorizeNumber((double) Math.round((weapon.attackSpeed() / 1.6 - 1) * 10000) / 100))
                    .append("%")
                    .getDefined(person);
            newLore.add(damage);
            newLore.add(speed);

            List<ItemAttributeModifiers.Entry> toRemove = new ArrayList<>();
            for (ItemAttributeModifiers.Entry entry : modifiers) {
                if (entry.attribute() == Attributes.ATTACK_DAMAGE || entry.attribute() == Attributes.ATTACK_SPEED) {
                    toRemove.add(entry);
                }
            }
            modifiers.removeAll(toRemove);

            modifiers.add(new ItemAttributeModifiers.Entry(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(
                            RESOURCE_ATTACK_DAMAGE,
                            weapon.damage()-1,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
            ));
            modifiers.add(new ItemAttributeModifiers.Entry(
                    Attributes.ATTACK_SPEED,
                    new AttributeModifier(
                            RESOURCE_ATTACK_SPEED,
                            weapon.attackSpeed()-4,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
            ));
        }

        if (this.item instanceof RPGArmor armor) {
            newLore.add(Component.empty());

            MutableComponent armorLang = local.getOrDefault("armor-defence", local.get("unknown")).clone()
                    .append(colorizeNumber((double) Math.round(armor.armor() * 4 * 100) / 100))
                    .append("%")
                    .getDefined(person);
            MutableComponent toughness = local.getOrDefault("armor-toughness", local.get("unknown")).clone()
                    .append(colorizeNumber(armor.toughness()))
                    .getDefined(person);
            MutableComponent speed = local.getOrDefault("armor-speed", local.get("unknown")).clone()
                    .append(colorizeNumber(armor.speed()*100))
                    .append("%")
                    .getDefined(person);
            newLore.add(armorLang);
            newLore.add(toughness);
            newLore.add(speed);

            List<ItemAttributeModifiers.Entry> toRemove = new ArrayList<>();
            for (ItemAttributeModifiers.Entry entry : modifiers) {
                if (entry.attribute() == Attributes.ARMOR
                        || entry.attribute() == Attributes.ARMOR_TOUGHNESS
                        || entry.attribute() == Attributes.MOVEMENT_SPEED) {
                    toRemove.add(entry);
                }
            }
            modifiers.removeAll(toRemove);

            modifiers.add(new ItemAttributeModifiers.Entry(
                    Attributes.ARMOR,
                    new AttributeModifier(
                            RESOURCE_ARMOR,
                            armor.armor(),
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.ARMOR
            ));
            modifiers.add(new ItemAttributeModifiers.Entry(
                    Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(
                            RESOURCE_TOUGHNESS,
                            armor.toughness(),
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.ARMOR
            ));
            modifiers.add(new ItemAttributeModifiers.Entry(
                    Attributes.ARMOR,
                    new AttributeModifier(
                            RESOURCE_MOVEMENT,
                            armor.speed(),
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.ARMOR
            ));
        }

        SequencedSet<DataComponentType<?>> toHide = new ReferenceLinkedOpenHashSet<>();
        toHide.add(DataComponents.ATTRIBUTE_MODIFIERS);
        nms.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, toHide));

        nms.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(modifiers));
        nms.set(DataComponents.LORE, new ItemLore(newLore, newLore));

        if (this.item instanceof ItemConfiguration configuration) {
            configuration.initConfiguration(nms);
        }

        return nms;
    }
}