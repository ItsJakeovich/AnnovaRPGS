package net.annova.entity.traits.ability;

import java.util.HashMap;
import java.util.Map;

public enum Ability {
    // если когда то великий я это прочитаю, перепиши пожалуйста все activity из мобов сюда, пажалуста
    // у меня есть такая идея что нужно проверять наличие определенных способностей
    // при спавне моба и запускать под них таймера.

    // Короче, возможно он пытается сказать что некоторые способности можно сделать шаблонными, то есть не-уникальными
    // я сам не до конца понимаю о чем речь, но думаю это нечто подобное как AbilitiesEvent.java: 89

    RESISTANCE, // сопративление урону
    KNOCKBACK_RESISTANCE, // сопративление отбрасыванию
    LEVELLER, // слепота при ударе
    SCHIVARE, // уклонение
    SLOWDOWN, // наложение замедление при нанесении урона
    AVOIDANCE, // телепорт после получения урона и невидимость
    VAMPIRISM, // вампиризм
    ORATOR, // увеличение характеристик мобов // вряд-ли пока что будет готов
    VITIATION, // накладывает один из 3: слепота, замедление, тошнота при нанесении урона
    ICENAGE, // замедление всех вокруг
    REFLECT, // шанс отразить удар
    DROP_FORIUM
    ;

    private final Map<String, Double> values = new HashMap<>();

    public Double getValue(String key) {
        if (!values.containsKey(key)) return 1.0;
        return values.get(key);
    }
    public Ability setValue(String key, double value) {
        this.values.put(key, value);
        return this;
    }
}
