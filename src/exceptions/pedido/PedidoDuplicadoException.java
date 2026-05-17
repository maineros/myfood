package exceptions.pedido;

public class PedidoDuplicadoException extends PedidoException {
    public PedidoDuplicadoException() {
        super("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
    }
}
