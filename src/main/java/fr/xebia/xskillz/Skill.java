package fr.xebia.xskillz;

import com.google.common.base.Predicate;

public class Skill {

    private SkillId id;

    private String name;

    public Skill(long id, String name) {
        this.id = new SkillId(id);
        this.name = name;
    }

    public Skill() {
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

    public static Predicate<Skill> searchForItem(final String searchedItem) {
        return new Predicate<Skill>() {
            @Override
            public boolean apply(Skill o) {
                return o.getName().toLowerCase().contains(searchedItem.trim().toLowerCase());
            }
        };
    }

    public static interface Properties {
        String NAME = "name";
    }
}
