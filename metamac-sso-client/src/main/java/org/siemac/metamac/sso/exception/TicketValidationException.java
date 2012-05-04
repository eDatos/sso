package org.siemac.metamac.sso.exception;

public class TicketValidationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5128852747194957716L;

    public TicketValidationException(String message) {
        super(message);
    }

    public TicketValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketValidationException(Throwable cause) {
        super(cause);
    }

}
