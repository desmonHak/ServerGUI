package src.Errors;

public class PasswordError extends Exception {
    private static final long serialVersionUID = 1L;

    public PasswordError(String mensaje_de_error) {
        super(mensaje_de_error);
    }
}
