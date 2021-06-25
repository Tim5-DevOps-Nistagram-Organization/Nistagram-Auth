package rs.ac.uns.ftn.devops.tim5.nistagramauth.model;

public enum Role {

    ROLE_REGULAR("ROLE_REGULAR"), ROLE_AGENT("ROLE_AGENT"), ROLE_ADMIN("ROLE_ADMIN");
    private final String name;

    Role(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
