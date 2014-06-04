package fr.xebia.xskillz;

public class XebiaId {


    private String email;

    public XebiaId(String email) {
        this.email = email;
    }

    public XebiaId() {
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

        XebiaId xebiaId = (XebiaId) o;

        if (!email.equals(xebiaId.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
