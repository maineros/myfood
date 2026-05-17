package exceptions.pedido;

public class EntregadorOcupadoException extends PedidoException {
    public EntregadorOcupadoException() {
        super("Entregador ainda em entrega");
    }
}
