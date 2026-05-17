package exceptions.produto;

public class ProdutoNaoEncontradoException extends ProdutoException {
    public ProdutoNaoEncontradoException() {
        super("Produto nao cadastrado");
    }

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
