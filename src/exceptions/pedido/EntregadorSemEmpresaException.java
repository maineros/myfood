package exceptions.pedido;

public class EntregadorSemEmpresaException extends PedidoException {
    public EntregadorSemEmpresaException() {
        super("Entregador nao estar em nenhuma empresa.");
    }
}
