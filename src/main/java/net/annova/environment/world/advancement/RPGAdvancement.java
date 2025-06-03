package net.annova.environment.world.advancement;

import net.annova.object.person.quest.Condition;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

import java.util.Set;

import static net.annova.main.Keys.getNamespacedKey;

public enum RPGAdvancement {
    // S0 - Oasis
    OASIS("oasis"), // Выполняется непосредвенно в Оазисе - первая встреча.
    BUBBLE_SEEKER("oasis/bubble_seeker"), // трижды в пузырь
    CRAB_IS_HAPPY("oasis/crab_is_happy"),
    DROP_FROM_THE_SKY("oasis/drop_from_the_sky"), // единожды в пузырь
    FRIENDS_ON_SCALES("oasis/friends_on_scales"),
    GIME_ME_CRAB("oasis/give_me_crab"),
    GOOD_BITE("oasis/good_bite"),
    GOOD_CATCH("oasis/good_catch"),
    MOON_FISHERMAN("oasis/moon_fisherman"),
    PACIFICATION("oasis/pacification"),
    SWIM_LITTLE_FISH("oasis/swim_little_fish_big_and_small..."),
    THATS_LUCK("oasis/thats_luck"),
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
