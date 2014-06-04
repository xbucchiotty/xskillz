package fr.xebia.xskillz;

import com.google.common.base.Predicate;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.FluentIterable.from;
import static fr.xebia.xskillz.Skill.skillPredicate;

public class Xebian {

    private XebianId id;

    private Set<Skill> skills = new HashSet<>();

    public Xebian(String xebiaId) {
        this.id = new XebianId(xebiaId);
    }

    public Xebian() {
    }

    public static Predicate<Xebian> byId(final XebianId id) {
        return new Predicate<Xebian>() {
            @Override
            public boolean apply(Xebian xebian) {
                return xebian.getId().equals(id);
            }
        };
    }

    public static Predicate<Xebian> withSkill(final String query) {
        return new Predicate<Xebian>() {
            @Override
            public boolean apply(Xebian xebian) {
                return from(xebian.getSkills()).anyMatch(skillPredicate(query));
            }
        };
    }

    public XebianId getId() {
        return id;
    }

    public void setId(XebianId id) {
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

    public void addSkill(Skill skill) {
        skills.add(skill);
    }
}