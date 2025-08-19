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

    public String serialize() {
        return this.tech.name()+":"+progress;
    }
    public static Research deserialize(String serialized) {
        if (serialized.isBlank()) return null;
        String[] data = serialized.split(":");
        Research research = new Research(AnnovaTech.valueOf(data[0]));
        research.progress = Float.parseFloat(data[1]);
        return research;
    }
}
