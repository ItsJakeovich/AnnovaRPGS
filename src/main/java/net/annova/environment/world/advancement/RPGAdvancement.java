package net.annova.environment.world.advancement;

import net.annova.object.person.quest.Condition;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

import java.util.Set;

import static net.annova.main.Keys.getNamespacedKey;

public enum RPGAdvancement {
    // S0 - Caves
    WHOS_HERE("mines/whos_here"), // Убить моба шахты
    IVE_SEEN_THEM_ALL("mines/ive_seen_them_all"), // Встретить каждый тип зачарованной брони (можно думаю убить)
    HACKED("mines/hacked"), // Получить волшебный лом.
    DEATH_UNDER_A_STONE("mines/death_under_a_stone"), // Погибнуть в шахтах.
    SLEEPLESS_SHIFT("mines/sleepless_shift"), // Провести в шахтах более часа без сна.
    STONE_WARRIOR("mines/stone_warrior"), // Победить голема-босса
    MAGIC_SPRING("mines/magic_spring"), // Увдиеть магический взрыв
    CHAIN_REACTION("mines/chain_reaction"), // Пережить 3 и более взрыва подряд от одной волны (не понимаю)
    ASHES_AND_MAGIC("mines/ashes_and_magic"), // Наступить на останки магии после взрыва
    PHANTOM_WINNER("mines/phantom_winner"), // Убить фантома шахт
    // S0 - forest

    // S0 - Oasis
    OASIS("oasis"), // Выполняется непосредвенно в Оазисе - первая встреча.
    BUBBLE_SEEKER("oasis/bubble_seeker"), // трижды в пузырь
    CRAB_IS_HAPPY("oasis/crab_is_happy"), // покормить краба
    DROP_FROM_THE_SKY("oasis/drop_from_the_sky"), // единожды в пузырь
    FRIENDS_ON_SCALES("oasis/friends_on_scales"), // Скинуть рыбу обратно в воду
    GIME_ME_CRAB("oasis/give_me_crab"), // убить краба
    GOOD_BITE("oasis/good_bite"), // Выловить рыбу в оазисе.
    MOON_FISHERMAN("oasis/moon_fisherman"), // Выловить рыбу ночью.
    PACIFICATION("oasis/pacification"),  // Постоять афк 30 минут на оазисе.
    SWIM_LITTLE_FISH("oasis/swim_little_fish_big_and_small..."), // Отпустить редкую рыбу обратно в воду
    THATS_LUCK("oasis/thats_luck"), // поймать "Оазис".
        // Набрать воду из неизвестного источника
    GOOD_CATCH("oasis/good_catch"), // 30% достижений оазиса.
    LUCKY_MASTER("oasis/lucky_master"), // 100% достижений оазиса.
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
