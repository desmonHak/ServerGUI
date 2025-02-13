package src.Errors;

public class PasswordError extends Exception {
    public PasswordError(String mensaje_de_error) {
        super(mensaje_de_error);
    }
}
