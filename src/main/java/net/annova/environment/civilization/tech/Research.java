package net.annova.environment.civilization.tech;

public class Research {
    private final AnnovaTech tech;
    private float progress = 0;

    public Research(AnnovaTech tech) {
        this.tech = tech;
    }

    public AnnovaTech getTech() {
        return tech;
    }
    public AnnovaTech.Type getType() {
        return tech.getCategory().getType();
    }

    public float getProgress() {
        return progress;
    }
    /**
     * @param progress by science (14800 / 16000)
     */
    public float addProgress(float progress) {
        return this.progress += progress;
    }
}
