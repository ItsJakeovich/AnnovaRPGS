package net.annova.item.base;

public abstract class RPGSet {
    public abstract int health();
    public abstract float damage();
    /**
     * @return additional speed in percent. 0.2 = +20%;
     */
    public abstract float speed();
    /**
     * @return additional regen hearts in EntityRegainHealth event;
     */
    public abstract float regeneration();
}
