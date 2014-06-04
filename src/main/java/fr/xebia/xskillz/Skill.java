package fr.xebia.xskillz;

public class Skill {

    public String name;

    public Skill(String name) {
        assert name != null;
        this.name = name;
    }

    public Skill() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Skill skill = (Skill) o;

        if (!name.equals(skill.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
