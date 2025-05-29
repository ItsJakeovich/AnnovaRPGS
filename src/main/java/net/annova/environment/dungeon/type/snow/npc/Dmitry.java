package net.annova.environment.dungeon.type.snow.npc;

import net.annova.object.npc.RPGNPC;

public class Dmitry extends RPGNPC {
    public Dmitry() {
        super("Dmitry", false);
    }

    @Override
    public String whenTouch() {
        return "Jaka ne Jaka kogda on Jaka";
    }
}
