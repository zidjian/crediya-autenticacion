package co.com.crediya.usecase.usuario.exceptions;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException() {
        super("Usuario no encontrado");
    }

    public UsuarioNotFoundException(String mensaje) {
        super(mensaje);
    }

    public UsuarioNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
