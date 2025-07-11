package gr.aueb.ticketify.core.exceptions;

import lombok.Getter;

@Getter
public class AppServerException extends RuntimeException {
    private final String code;

    public AppServerException(String code, String message) {
        super(message);
        this.code = code;
    }
}
