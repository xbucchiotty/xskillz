package fr.xebia.xskillz;

public class Xebian {

    public XebiaId id;

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