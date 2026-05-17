package exceptions.empresa;

public class UsuarioNaoAutorizadoException extends EmpresaException {
    public UsuarioNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
