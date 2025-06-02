package net.annova.object.entity.core.status;

public class EntityStatus {
    private final StatusType type;

    public EntityStatus(StatusType type) {
        this.type = type;
    }

    public StatusType getType() {
        return type;
    }
}
