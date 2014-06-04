package fr.xebia.xskillz;

import com.google.common.base.Predicate;

public class Skill {

    private String name;

    public Skill(String name) {
        assert name != null;
        this.name = name;
    }

    public Skill() {
    }

    static Predicate<Skill> skillPredicate(final String searchItem) {
        return new Predicate<Skill>() {
            @Override
            public boolean apply(Skill o) {
                return o.getName().toLowerCase().contains(searchItem.trim().toLowerCase());
            }
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
