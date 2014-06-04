package fr.xebia.xskillz;

public class XebianId {


    private String email;

    public XebianId(String email) {
        this.email = email;
    }

    public XebianId() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XebianId xebianId = (XebianId) o;

        if (!email.equals(xebianId.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
