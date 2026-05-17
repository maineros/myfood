package exceptions.usuario;

public class LoginInvalidoException extends UsuarioException {
    public LoginInvalidoException() {
        super("Login ou senha invalidos");
    }
}
