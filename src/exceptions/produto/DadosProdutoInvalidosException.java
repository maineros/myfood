package exceptions.produto;

public class DadosProdutoInvalidosException extends ProdutoException {
    public DadosProdutoInvalidosException(String mensagem) {
        super(mensagem);
    }
}
