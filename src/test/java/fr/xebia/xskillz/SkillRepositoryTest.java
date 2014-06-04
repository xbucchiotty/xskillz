package fr.xebia.xskillz;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SkillRepositoryTest {

    @Test
    public void repository_should_be_empty_at_startup() {
        SkillRepository repository = new SkillRepository();

        assertThat(repository.skills()).isEmpty();
    }

    @Test
    public void repository_should_contains_added_skills() {
        SkillRepository repository = new SkillRepository();

        Skill newSkill = new Skill("Fest-assert");
        repository.put(newSkill);

        assertThat(repository.skills()).contains(newSkill);
    }

    @Test
    public void repository_should_not_contains_duplicates() {
        SkillRepository repository = new SkillRepository();

        repository.put(new Skill("Fest-assert"));
        repository.put(new Skill("Fest-assert"));

        assertThat(repository.skills()).hasSize(1);
    }
}
