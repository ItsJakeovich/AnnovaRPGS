package net.annova.environment.structure;

import net.annova.object.SimpleConstruction;

public class Structure {
    private final SimpleConstruction construction;
    //private final Map<Integer, List<String[]>> sign = new HashMap<>();
    private final int basePoint;
    private StructureBase base;

    public Structure(SimpleConstruction construction, int basePoint) {
        this.construction = construction;
        this.basePoint = basePoint;
    }

    public SimpleConstruction getConstruction() {
        return construction;
    }

    /*public Map<Integer, List<String[]>> getSigns() {
        return sign;
    }
    public void addSign(int point, List<String[]> block) {
        this.sign.put(point, block);
    }*/

    public int getBasePoint() {
        return basePoint;
    }

    public StructureBase getBase() {
        return base;
    }
    public void setBase(StructureBase base) {
        this.base = base;
    }
}
