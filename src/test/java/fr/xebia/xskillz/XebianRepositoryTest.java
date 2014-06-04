package fr.xebia.xskillz;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class XebianRepositoryTest {

    @Test
    public void repository_should_be_empty_at_startup() {
        XebianRepository repository = new XebianRepository();

        assertThat(repository.xebians()).isEmpty();
    }

    @Test
    public void repository_should_contains_added_xebian() {
        XebianRepository repository = new XebianRepository();

        Xebian aXebian = new Xebian("john@xebia.fr");
        repository.put(aXebian);

        assertThat(repository.xebians()).contains(aXebian);
    }

    @Test
    public void repository_should_not_contains_duplicates() {
        XebianRepository repository = new XebianRepository();

        repository.put(new Xebian("john@xebia.fr"));
        repository.put(new Xebian("john@xebia.fr"));

        assertThat(repository.xebians()).hasSize(1);
    }
}
