package exceptions.usuario;

public class AtributoUsuarioInvalidoException extends UsuarioException {
    public AtributoUsuarioInvalidoException() {
        super("Atributo invalido");
    }

    public AtributoUsuarioInvalidoException(String mensagem) {
        super(mensagem);
    }
}
