package exceptions.pedido;

public class EntregaNaoEncontradaException extends PedidoException {
    public EntregaNaoEncontradaException() {
        super("Nao existe nada para ser entregue com esse id");
    }

    public EntregaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
