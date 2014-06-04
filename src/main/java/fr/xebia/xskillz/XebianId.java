package fr.xebia.xskillz;

public class XebianId {

    private Long value;

    public XebianId(long value) {
        this.value = value;
    }

    public XebianId() {
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XebianId xebianId = (XebianId) o;

        if (!value.equals(xebianId.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
