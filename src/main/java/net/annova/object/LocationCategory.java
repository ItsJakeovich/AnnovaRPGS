package net.annova.object;

import net.annova.object.person.RPGPerson;
import net.annova.object.person.advancement.RPGAdvancement;

import java.util.EnumSet;

import static net.annova.object.person.advancement.RPGAdvancement.GOOD_CATCH;
import static net.annova.object.person.advancement.RPGAdvancement.LUCKY_MASTER;

public enum LocationCategory {
    WITHOUT, // without category
    EVERYWHERE,

    D_PRISON,
    NETHER,

    S0_DRYAD,
    S0_FOREST,
    S0_METEORITE,
    S0_MINES,
    S0_OASIS(new LocationActions() {
        @Override
        public void onAdvancement(RPGPerson person) {
            EnumSet<RPGAdvancement> advancements = RPGAdvancement.BY_CATEGORY.get(S0_OASIS);

            double completed = advancements.stream().filter(person::hasAdvancement).count();
            int size = advancements.size();

            if (completed == size) {
                person.completeAdvancement(LUCKY_MASTER);
            } else if (completed / size >= 0.3) {
                person.completeAdvancement(GOOD_CATCH);
            }
        }
    }),
    S0_SWAMPS;

    private final LocationActions actions;

    LocationCategory() {
        this.actions = null;
    }
    LocationCategory(LocationActions actions) {
        this.actions = actions;
    }

    public LocationActions getActions() {
        return actions;
    }

    public interface LocationActions {
        void onAdvancement(RPGPerson person);
    }
}
