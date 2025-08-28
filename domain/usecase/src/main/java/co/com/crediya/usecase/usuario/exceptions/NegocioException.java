package co.com.crediya.usecase.usuario.exceptions;

public class NegocioException extends RuntimeException {
    private final String code;

    public NegocioException(String code, String message) {
        super(message);
        this.code = code;
    }
    public String getCode() { return code; }
}
