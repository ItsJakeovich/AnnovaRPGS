package net.annova.environment.world;

import net.annova.object.person.quest.Condition;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

import java.util.Set;

import static net.annova.main.Keys.getNamespacedKey;

public enum RPGAdvancement {
    // S0 - forest
    DARK_FOREST("dark_forest"),
    WHISPER_OF_LEAVES("dark_forest/whisper_of_leaves"),
    BROKEN_BARREL("dark_forest/broken_barrel"),
    SENIOR_CORTEX("dark_forest/senior_cortex"),
    SHADOW_AMONG_THE_ROOTS("dark_forest/shadow_among_the_roots"),
    WHO_IS_MOANING("dark_forest/who_is_moaning_in_the_branches"),
    IN_THE_HEART("dark_forest/in_the_heart_of_the_thicket"),
    BURNT_FOREST("dark_forest/burnt_forest"),
    THE_CURSE_OF_THE_ELVES("dark_forest/the_curse_of_the_elves"),
    ROOTS_DANCE("dark_forest/roots_dance"),
    RUSTLING_OF_LEAVES("dark_forest/rustling_of_leaves_at_night"),
    // S0 - meteorite
    Metior("metior"),
    HEAT_OF_THE_FIELDS("metior/heat_of_the_fields"),
    SHARDS_AND_SCREAMS("metior/shards_and_screams"),
    SECOND_WIND("metior/second_wind"),
    CRACK_AND_BREAK("metior/crack_and_break"),
    STARDUST("metior/stardust"),
    OTHERWORLDLY_ECHO("metior/otherworldly_echo"),
    I_WAS_INSIDE_IT("metior/i_was_inside_it"),
    TEMPERATURES_DONT_MATTER("metior/temperatures_dont_matter"),
    OUCH_ITS_HOT("metior/ouch_its_hot"),
    THERE_ARE_DISAPPOINTMENTS("metior/there_are_disappointments_in_life"),
    // S0 - Mines
    MINES("mines"),
    WHOS_HERE("mines/whos_here"), // Убить моба шахты
    IVE_SEEN_THEM_ALL("mines/ive_seen_them_all"), // Встретить каждый тип зачарованной брони (можно думаю убить)
    HURRAY_SHINY("mines/hurray_shiny"), // получить волшебный лом
    DEATH_UNDER_A_STONE("mines/death_under_a_stone"), // Погибнуть в шахтах.
    SLEEPLESS_SHIFT("mines/sleepless_shift"), // Провести в шахтах более часа без сна.
    STONE_WARRIOR("mines/stone_warrior"), // Победить голема-босса
    MAGIC_SPRING("mines/magic_spring"), // Увдиеть магический взрыв
    CHAIN_REACTION("mines/chain_reaction"), // Пережить 3 и более взрыва подряд от одной волны (не понимаю)
    ASHES_AND_MAGIC("mines/ashes_and_magic"), // Наступить на останки магии после взрыва
    PHANTOM_WINNER("mines/phantom_winner"), // Убить фантома шахт
    // S0 - Oasis
    OASIS("oasis"), // Выполняется непосредвенно в Оазисе - первая встреча.
    GIME_ME_CRAB("oasis/give_me_crab"), // убить краба
    GOOD_CATCH("oasis/good_catch"), // 30% достижений оазиса.
    FRIENDS_ON_SCALES("oasis/friends_on_scales"), // Скинуть рыбу обратно в воду
    BUBBLE_SEEKER("oasis/bubble_seeker"), // трижды в пузырь
    PACIFICATION("oasis/pacification"),  // Постоять афк 30 минут на оазисе.
    CRAB_IS_HAPPY("oasis/crab_is_happy"), // покормить краба
    MOON_FISHERMAN("oasis/moon_fisherman"), // Выловить рыбу ночью.
    THATS_LUCK("oasis/thats_luck"), // поймать "Оазис".
    DROP_FROM_THE_SKY("oasis/drop_from_the_sky"), // единожды в пузырь
    GOOD_BITE("oasis/good_bite"), // Выловить рыбу в оазисе.
    LUCKY_MASTER("oasis/lucky_master"), // 100% достижений оазиса.
    WATER("oasis/water"),
    SWIM_LITTLE_FISH("oasis/swim_little_fish_big_and_small..."), // Отпустить редкую рыбу обратно в воду
    ;
    private final NamespacedKey key;
    private final Set<Condition> conditions;

    RPGAdvancement(String key, Condition... conditions) {
        this.key = getNamespacedKey(key);
        this.conditions = Set.of(conditions);
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
