package cz.cvut.kbss.ear.project.exception;

public class EnrolmentException extends EarException{
    public EnrolmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnrolmentException(Throwable cause) {
        super(cause);
    }
}
