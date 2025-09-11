package co.com.crediya.usecase.usuario.exceptions;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException() {
        super("UNAUTHORIZED", "Credenciales inválidas", 401);
    }

    public UnauthorizedException(String mensaje) {
        super("UNAUTHORIZED", mensaje, 401);
    }

    public UnauthorizedException(String mensaje, Throwable causa) {
        super("UNAUTHORIZED", mensaje, 401, causa);
    }
}
