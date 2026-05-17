package exceptions.usuario;

public class EmailDuplicadoException extends UsuarioException {
    public EmailDuplicadoException() {
        super("Conta com esse email ja existe");
    }
}
