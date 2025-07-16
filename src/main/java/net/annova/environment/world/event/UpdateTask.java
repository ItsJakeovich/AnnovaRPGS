package net.annova.environment.world.event;

public abstract class UpdateTask {
    public abstract void update(); // Задача
    /**
     * Повторение в минутах
     * @return Minute delay
     */
    public abstract int period(); // Повторение в минутах.

    private int cooldown = this.period();
    public boolean isAvailable() {
        if (this.cooldown == 0) {
            this.cooldown = this.period(); // если 0 - сбрасывает
            return true;
        }
        this.cooldown--;
        return false;
    }
}
