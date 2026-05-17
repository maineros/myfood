package exceptions.empresa;

public class AtributoEmpresaInvalidoException extends EmpresaException {
    public AtributoEmpresaInvalidoException() {
        super("Atributo invalido");
    }

    public AtributoEmpresaInvalidoException(String mensagem) {
        super(mensagem);
    }
}
