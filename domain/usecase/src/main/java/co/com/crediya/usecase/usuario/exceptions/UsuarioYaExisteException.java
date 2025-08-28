package co.com.crediya.usecase.usuario.exceptions;

public class UsuarioYaExisteException extends NegocioException {
    private final String email;

    public UsuarioYaExisteException(String email) {
        super("USER_ALREADY_EXISTS", "Ya existe un usuario con el correo " + email);
        this.email = email;
    }

    public String getEmail() { return email; }
}
