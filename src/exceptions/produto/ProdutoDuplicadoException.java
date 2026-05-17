package exceptions.produto;

public class ProdutoDuplicadoException extends ProdutoException {
    public ProdutoDuplicadoException() {
        super("Ja existe um produto com esse nome para essa empresa");
    }
}
