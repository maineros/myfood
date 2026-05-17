package exceptions.usuario;

public class DadosUsuarioInvalidosException extends UsuarioException {
    public DadosUsuarioInvalidosException(String mensagem) {
        super(mensagem);
    }
}
