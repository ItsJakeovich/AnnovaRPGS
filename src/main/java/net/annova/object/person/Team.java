package net.annova.object.person;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private final String name;
    private final RPGPerson leader;
    private final Set<RPGPerson> members = new HashSet<>();

    public Team(String name, RPGPerson leader) {
        this.name = name;
        this.leader = leader;
        this.members.add(leader);
    }

    public String getName() {
        return name;
    }

    public RPGPerson getLeader() {
        return leader;
    }

    public Set<RPGPerson> getMembers() {
        return members;
    }
    public void addMember(RPGPerson person) {
        this.members.add(person);
    }
    public void removeMember(RPGPerson person) {
        this.members.remove(person);
    }
}
