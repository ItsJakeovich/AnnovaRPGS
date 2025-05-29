package net.annova.object.npc;

import net.annova.object.NPC;
import net.annova.object.person.RPGPerson;

public abstract class RPGNPC {
    private final NPC npc;
    private final RPGPerson person;

    public abstract String whenTouch();

    public RPGNPC(String name, boolean global) {
        this.npc = new NPC(name, global);
        this.person = new RPGPerson(npc.getPerson());
    }

    public NPC getNPC() {
        return npc;
    }

    public RPGPerson getPerson() {
        return person;
    }
}
