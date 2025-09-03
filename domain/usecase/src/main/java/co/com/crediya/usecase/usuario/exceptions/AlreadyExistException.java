package co.com.crediya.usecase.usuario.exceptions;

public class AlreadyExistException extends BusinessException {

    public AlreadyExistException(String mensaje) {
        super("ALREADY_EXIST", mensaje, 409);
    }
}