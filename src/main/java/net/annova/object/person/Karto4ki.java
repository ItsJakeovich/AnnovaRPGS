package net.annova.object.person;

public record Karto4ki(String name, byte age, short height, Gender gender) {
    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN,
        HAMSTER,
    }
}
