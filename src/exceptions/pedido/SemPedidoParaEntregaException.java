package exceptions.pedido;

public class SemPedidoParaEntregaException extends PedidoException {
    public SemPedidoParaEntregaException() {
        super("Nao existe pedido para entrega");
    }
}
