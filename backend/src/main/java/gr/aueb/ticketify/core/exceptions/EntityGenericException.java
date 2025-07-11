package gr.aueb.ticketify.core.exceptions;

import lombok.Getter;

@Getter
public class EntityGenericException extends RuntimeException {
    private final String code;

    public EntityGenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
