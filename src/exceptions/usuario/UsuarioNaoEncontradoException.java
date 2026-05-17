package exceptions.usuario;

public class UsuarioNaoEncontradoException extends UsuarioException {
    public UsuarioNaoEncontradoException() {
        super("Usuario nao cadastrado.");
    }
}
