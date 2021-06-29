package rs.ac.uns.ftn.devops.tim5.nistagramauth.model.enums;

public enum AgentRegistrationEnum {

    REQUESTED("REQUESTED"), ACCEPTED("ACCEPTED"), REJECTED("REJECTED");
    private final String name;

    AgentRegistrationEnum(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
