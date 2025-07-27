package net.annova.environment.civilization;

import net.annova.object.SimpleConstruction;

public class TownBuild {
    private final SimpleConstruction construction;

    public TownBuild(SimpleConstruction construction) {
        this.construction = construction;
    }

    public SimpleConstruction getConstruction() {
        return construction;
    }
}
