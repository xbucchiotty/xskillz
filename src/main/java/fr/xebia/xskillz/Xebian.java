package fr.xebia.xskillz;

import java.util.HashSet;
import java.util.Set;

public class Xebian {

    private XebiaId id;

    private Set<Skill> skills = new HashSet<>();

    public Xebian(String xebiaId) {
        this.id = new XebiaId(xebiaId);
    }

    public Xebian() {
    }

    public XebiaId getId() {
        return id;
    }

    public void setId(XebiaId id) {
        this.id = id;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Xebian xebian = (Xebian) o;

        if (!id.equals(xebian.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}