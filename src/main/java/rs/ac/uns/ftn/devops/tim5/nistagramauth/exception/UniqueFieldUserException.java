package rs.ac.uns.ftn.devops.tim5.nistagramauth.exception;

public class UniqueFieldUserException extends Exception {
    public UniqueFieldUserException(String username) {
        super(String.format("%s must be unique.", username));
    }
}
