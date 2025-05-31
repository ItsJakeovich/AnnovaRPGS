package net.annova.item.base;

import com.google.common.collect.Maps;
import net.annova.item.DungeonKeyItem;
import net.annova.item.world0.forest.EntRoot;
import net.annova.item.component.ForiumFragmentItem;
import net.annova.item.component.ForiumIngotItem;
import net.annova.item.container.PrisonContainerItem;
import net.annova.item.crystal.*;
import net.annova.item.equipment.ControlMovementStickItem;
import net.annova.item.equipment.ControlStopStickItem;
import net.annova.item.equipment.oasis.TideBlade;
import net.annova.item.equipment.oasis.TideGraspRing;
import net.annova.item.equipment.prison.*;
import net.annova.item.token.*;
import net.annova.item.world0.oasis.*;
import net.annova.object.Person;
import net.minecraft.world.item.component.CustomModelData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.annova.config.AnnovaConfig.getLangFile;
import static net.annova.item.base.RPGItem.getItemId;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.Keys.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.MessageObjects.color;

public enum Item {
    // Components
    FORIUM_FRAGMENT(new ForiumFragmentItem()),
    FORIUM_INGOT(new ForiumIngotItem()),

    // Containers
    PRISON_CONTAINER(new PrisonContainerItem()),

    // Stones
    AIR_STONE(new AirStoneItem()),
    AQUA_STONE(new AquaStoneItem()),
    BALANCE_STONE(new BalanceStoneItem()),
    BIOME_STONE(new BiomeStoneItem()),
    CHRONO_STONE(new ChronoStoneItem()),
    DARK_STONE(new DarkStoneItem()),
    DEATH_STONE(new DeathStoneItem()),
    DEFENCE_STONE(new DefenceStoneItem()),
    DRAGON_STONE(new DragonStoneItem()),
    EARTH_STONE(new EarthStoneItem()),
    GRAVITY_STONE(new GravityStoneItem()),
    GREEN_STONE(new GreenStoneItem()),
    ICE_STONE(new IceStoneItem()),
    IGNIS_STONE(new IgnisStoneItem()),
    LIGHT_STONE(new LightStoneItem()),
    NATURE_STONE(new NatureStoneItem()),
    PSYONIC_STONE(new PsyonicStoneItem()),
    RED_STONE(new RedStoneItem()),
    REGENERATION_STONE(new RegenerationStoneItem()),
    REPAIR_STONE(new RepairStoneItem()),
    RETURN_STONE(new ReturnStoneItem()),
    RUNIC_DEFENCE_STONE(new RunicDefenceStoneItem()),
    RUNIC_STONE(new RunicStoneItem()),
    STRENGTH_STONE(new StrengthStoneItem()),
    TELEPORT_STONE(new TeleportStoneItem()),
    TIME_STONE(new TimeStoneItem()),
    VITAL_STONE(new VitalStoneItem()),

    // Equipment
    PRISON_BOOTS(new PrisonBootsItem()),
    PRISON_CHESTPLATE(new PrisonChestplateItem()),
    PRISON_HELMET(new PrisonHelmetItem()),
    PRISON_LEGGINGS(new PrisonLeggingsItem()),
    PRISON_STEEL_SWORD(new PrisonSteelSwordItem()),
    PRISON_SWORD(new PrisonSwordItem()),

    CONTROL_MOVEMENT_STICK(new ControlMovementStickItem()),
    CONTROL_STOP_STICK(new ControlStopStickItem()),

    // Tokens
    CREEPER_TOKEN(new CreeperTokenItem()),
    DURABILITY_TOKEN(new DurabilityTokenItem()),
    FLY_TOKEN(new FlyTokenItem()),
    PRISON_TOKEN(new PrisonTokenItem()),
    RANGE_TOKEN(new RangeTokenItem()),
    SATURATION_TOKEN(new SaturationTokenItem()),

    // S0 - OASIS
    TIDE_BLADE(new TideBlade()),
    TIDE_GRASP_RING(new TideGraspRing()),

    ANGEL_FISH(new AngelFish()),
    ANGLER_FISH_SPAWN_EGG(new AnglerFishSpawnEgg()),
    AZURE_FISH(new AzureFish()),
    BARK_BEETLE_FISH(new BarkBeetleFish()),
    BLOB_FISH_BLUE(new BlobFishBlue()),
    BLOB_FISH_GRAY(new BlobFishGray()),
    BLOB_FISH_YELLOW(new BlobFishYellow()),
    CRAB_CLAW(new CrabClaw()),
    ELK_FISH(new ElkFish()),
    FURY_CAVIAR(new FuryCaviar()),
    KOI_FISH(new KoiFish()),
    NALIM_FISH(new NalimFish()),
    OASIS_FISH(new OasisFish()),
    PEARL(new Pearl()),
    ROYAL_PUFFERFISH(new RoyalPufferfish()),
    SEASMITH(new Seasmith()),
    SILVER_PUMP(new SilverPump()),
    UNKNOWN_SOURCE_WATER(new UnknownSourceWater()),
    VOLCANIC_STREAMS_MALMA(new VolcanicStreamsMalma()),
    WHALE_FISH(new WhaleFish()),

    // S0 - FOREST
    ENT_ROOT(new EntRoot()),

    // unique
    DUNGEON_KEY(new DungeonKeyItem()),
    ;

    private final RPGItem item;
    private final String id;
    Item(RPGItem item) {
        this.item = item;
        this.id = this.name().toLowerCase();
    }

    public RPGItem getItem() {
        return item;
    }
    public String getID() {
        return id;
    }

    private static final Map<String, Item> BY_NAME = Maps.newHashMap();
    static {
        for (Item item : Item.values()) {
            BY_NAME.put(item.id, item);
        }
    }
    public static Item getItem(String itemName) {
        return BY_NAME.get(itemName);
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
        for (ItemStack item : player.getInventory().getContents()) {
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
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!this.id.equals(getItemId(item))) return false;
        RPGItem.subtractItem(player, item);
        return true;
    }

    @Nullable
    public ItemStack getFromInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (this.id.equals(getItemId(item))) return item;
        }
        return null;
    }

    public ItemStack create(Person person) {
        return this.create(person, 1);
    }
    public ItemStack create(Player player) {
        return this.create(getPerson(player), 1);
    }
    public ItemStack create(Person person, int amount) {
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
    public ItemStack create(Person person, int amount, double chance) {
        if (amount < 1 || (chance != 1 && !chance(chance))) return new ItemStack(Material.AIR);

        ItemStack item = new ItemStack(this.item.type(), amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(color(getLangFile(getInstance(), person == null ? "ru" : person.getLanguage())
                .getString(this.name(), "unknown-name"))
        );
        CustomModelData model = new CustomModelData(this.item.modelData(), List.of(), List.of(), List.of());
        meta.setCustomModelDataComponent(new CraftCustomModelDataComponent(model));

        if (!this.item.lore().isEmpty()) {
            List<String> newLore = new ArrayList<>();
            for (String string : this.item.lore()) {
                newLore.add(color(string));
            }
            meta.setLore(newLore);
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(NK_ID, PersistentDataType.STRING, this.id);

        if (this.item.unstackable()) {
            pdc.set(NamespacedKey.fromString(UUID.randomUUID().toString(), getInstance()), PersistentDataType.INTEGER, 0);
        }

        if (this.item instanceof RPGTool weapon) {
            meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE);
            meta.removeAttributeModifier(Attribute.ATTACK_SPEED);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);

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

        item.setItemMeta(meta);
        return item;
    }
}
