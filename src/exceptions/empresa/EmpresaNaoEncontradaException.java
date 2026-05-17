package exceptions.empresa;

public class EmpresaNaoEncontradaException extends EmpresaException {
    public EmpresaNaoEncontradaException() {
        super("Empresa nao cadastrada");
    }

    public EmpresaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
