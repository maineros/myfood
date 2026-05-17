package exceptions.pedido;

public class PedidoNaoEncontradoException extends PedidoException {
    public PedidoNaoEncontradoException() {
        super("Pedido nao encontrado");
    }
}
