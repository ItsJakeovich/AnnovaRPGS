package net.annova.object.person.advancement;

import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.quest.Condition;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static net.annova.main.Keys.getNamespacedKey;
import static net.annova.object.LocationCategory.*;

public enum RPGAdvancement {
    // S0 - forest
    DARK_FOREST(S0_FOREST, "dark_forest"),
    WHISPER_OF_LEAVES(S0_FOREST, "dark_forest/whisper_of_leaves"),
    BROKEN_BARREL(S0_FOREST, "dark_forest/broken_barrel"),
    SENIOR_CORTEX(S0_FOREST, "dark_forest/senior_cortex"),
    SHADOW_AMONG_THE_ROOTS(S0_FOREST, "dark_forest/shadow_among_the_roots"),
    WHO_IS_MOANING(S0_FOREST, "dark_forest/who_is_moaning_in_the_branches"),
    IN_THE_HEART(S0_FOREST, "dark_forest/in_the_heart_of_the_thicket"),
    BURNT_FOREST(S0_FOREST, "dark_forest/burnt_forest"),
    THE_CURSE_OF_THE_ELVES(S0_FOREST, "dark_forest/the_curse_of_the_elves"),
    ROOTS_DANCE(S0_FOREST, "dark_forest/roots_dance"),
    RUSTLING_OF_LEAVES(S0_FOREST, "dark_forest/rustling_of_leaves_at_night"),
    // S0 - meteorite
    METIOR(S0_METEORITE, "metior"),
    HEAT_OF_THE_FIELDS(S0_METEORITE, "metior/heat_of_the_fields"),
    SHARDS_AND_SCREAMS(S0_METEORITE, "metior/shards_and_screams"),
    SECOND_WIND(S0_METEORITE, "metior/second_wind"),
    CRACK_AND_BREAK(S0_METEORITE, "metior/crack_and_break"),
    STARDUST(S0_METEORITE, "metior/stardust"),
    OTHERWORLDLY_ECHO(S0_METEORITE, "metior/otherworldly_echo"),
    I_WAS_INSIDE_IT(S0_METEORITE, "metior/i_was_inside_it"),
    TEMPERATURES_DONT_MATTER(S0_METEORITE, "metior/temperatures_dont_matter"),
    OUCH_ITS_HOT(S0_METEORITE, "metior/ouch_its_hot"),
    THERE_ARE_DISAPPOINTMENTS(S0_METEORITE, "metior/there_are_disappointments_in_life"),
    // S0 - Mines
    MINES(S0_MINES, "mines"),
    WHOS_HERE(S0_MINES, "mines/whos_here"), // Убить моба шахты
    IVE_SEEN_THEM_ALL(S0_MINES, "mines/ive_seen_them_all"), // Встретить каждый тип зачарованной брони (можно думаю убить)
    HURRAY_SHINY(S0_MINES, "mines/hurray_shiny"), // получить волшебный лом
    DEATH_UNDER_A_STONE(S0_MINES, "mines/death_under_a_stone"), // Погибнуть в шахтах.
    SLEEPLESS_SHIFT(S0_MINES, "mines/sleepless_shift"), // Провести в шахтах более часа без сна.
    STONE_WARRIOR(S0_MINES, "mines/stone_warrior"), // Победить голема-босса
    MAGIC_SPRING(S0_MINES, "mines/magic_spring"), // Увдиеть магический взрыв
    CHAIN_REACTION(S0_MINES, "mines/chain_reaction"), // Пережить 3 и более взрыва подряд от одной волны (не понимаю)
    ASHES_AND_MAGIC(S0_MINES, "mines/ashes_and_magic"), // Наступить на останки магии после взрыва
    PHANTOM_WINNER(S0_MINES, "mines/phantom_winner"), // Убить фантома шахт
    // S0 - Oasis
    OASIS(S0_OASIS, "oasis"), // Выполняется непосредвенно в Оазисе - первая встреча.
    GIVE_ME_CRAB(S0_OASIS, "oasis/give_me_crab"), // убить краба
    FRIENDS_ON_SCALES(S0_OASIS, "oasis/friends_on_scales"), // Скинуть рыбу обратно в воду
    BUBBLE_SEEKER(S0_OASIS, "oasis/bubble_seeker"), // трижды в пузырь
    PACIFICATION(S0_OASIS, "oasis/pacification"),  // Постоять афк 30 минут на оазисе.
    CRAB_IS_HAPPY(S0_OASIS, "oasis/crab_is_happy"), // покормить краба
    MOON_FISHERMAN(S0_OASIS, "oasis/moon_fisherman"), // Выловить рыбу ночью.
    THATS_LUCK(S0_OASIS, "oasis/thats_luck"), // поймать "Оазис".
    DROP_FROM_THE_SKY(S0_OASIS, "oasis/drop_from_the_sky"), // единожды в пузырь
    GOOD_BITE(S0_OASIS, "oasis/good_bite"), // Выловить рыбу в оазисе.
    WATER(S0_OASIS, "oasis/water"),
    SWIM_LITTLE_FISH(S0_OASIS, "oasis/swim_little_fish_big_and_small..."), // Отпустить редкую рыбу обратно в воду
    GOOD_CATCH("oasis/good_catch"), // 30% достижений оазиса.
    LUCKY_MASTER("oasis/lucky_master"), // 100% достижений оазиса.
    ;
    private final LocationCategory category;
    private final NamespacedKey key;
    private final Set<Condition> conditions;
    public static final Map<LocationCategory, EnumSet<RPGAdvancement>> BY_CATEGORY = new EnumMap<>(LocationCategory.class);

    static {
        for (LocationCategory cat : LocationCategory.values()) {
            BY_CATEGORY.put(cat, EnumSet.noneOf(RPGAdvancement.class));
        }
        for (RPGAdvancement type : values()) {
            BY_CATEGORY.get(type.category).add(type);
        }
    }

    RPGAdvancement(String key, Condition... conditions) {
        this(WITHOUT, key, conditions);
    }
    RPGAdvancement(LocationCategory category, String key, Condition... conditions) {
        this.category = category;
        this.key = getNamespacedKey(key);
        this.conditions = Set.of(conditions);
    }

    public LocationCategory getCategory() {
        return category;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public Set<Condition> getConditions() {
        return conditions;
    }

    public Advancement resolve() {
        return Bukkit.getAdvancement(this.key);
    }
}
