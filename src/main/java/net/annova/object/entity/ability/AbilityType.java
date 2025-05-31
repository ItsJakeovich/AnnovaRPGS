package net.annova.object.entity.ability;

import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public enum AbilityType {
    // если когда то великий я это прочитаю, перепиши пожалуйста все activity из мобов сюда, пажалуста
    // у меня есть такая идея что нужно проверять наличие определенных способностей
    // при спавне моба и запускать под них таймера.

    // Короче, возможно он пытается сказать что некоторые способности можно сделать шаблонными, то есть не-уникальными
    // я сам не до конца понимаю о чем речь, но думаю это нечто подобное как AbilitiesEvent.java: 89

    RESISTANCE {
        @Override
        public void mechanic(Event event) {

        }
    }, // сопративление урону
    KNOCKBACK_RESISTANCE {
        @Override
        public void mechanic(Event event) {

        }
    }, // сопративление отталкиванию
    LEVELLER {
        @Override
        public void mechanic(Event event) {

        }
    }, // слепота при ударе
    SCHIVARE {
        @Override
        public void mechanic(Event event) {

        }
    }, // уклонение
    SLOWDOWN {
        @Override
        public void mechanic(Event event) {

        }
    }, // наложение замедление при нанесении урона
    AVOIDANCE {
        @Override
        public void mechanic(Event event) {

        }
    }, // телепорт после получения урона и невидимость
    VAMPIRISM {
        @Override
        public void mechanic(Event event) {

        }
    }, // вампиризм
    VITIATION {
        @Override
        public void mechanic(Event event) {

        }
    }, // накладывает один из 3: слепота, замедление, тошнота при нанесении урона
    REFLECT {
        @Override
        public void mechanic(Event event) {

        }
    }, // шанс отразить удар
    ORATOR {
        @Override
        public void mechanic(Event event) {

        }
    }, // увеличение урона ближайшим союзникам (урон, здоровье, регенерация)
    ;

    public abstract void mechanic(Event event);

    private final Map<String, Double> values = new HashMap<>();

    public Double getValue(String key) {
        if (!values.containsKey(key)) return 1.0;
        return values.get(key);
    }
    public AbilityType setValue(String key, double value) {
        this.values.put(key, value);
        return this;
    }
}
