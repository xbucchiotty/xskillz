package fr.xebia.xskillz;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static fr.xebia.xskillz.Skill.searchForItem;

public class Xebian {

    private XebianId id;

    private String email;

    private Set<Skill> skills = new HashSet<>();

    public Xebian() {
    }

    public Xebian(XebianId newId) {
        this.id = newId;
    }

    public Xebian(long id) {
        this(new XebianId(id));
    }

    public Xebian(long id, String email) {
        this(id);
        this.email = email;
    }

    public Xebian(long id, String email, Set<Skill> skills) {
        this(id, email);
        this.skills = skills;
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

    public static Predicate<Xebian> withSkill(final String query) {
        return xebian -> xebian.getSkills().stream().anyMatch(searchForItem(query));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean matches(String expectedSkill) {
        return skills.stream().anyMatch(skill -> skill.matches(expectedSkill));
    }

    public static interface Properties {
        String EMAIL = "email";
    }
}