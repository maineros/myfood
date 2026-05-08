package models;

public class Entrega {
    private int id, idPedido, idEntregador;
    private String destino;
    public Entrega() {}
    public Entrega(int id, int idPedido, int idEntregador, String destino) {
        this.id = id; this.idPedido = idPedido; this.idEntregador = idEntregador; this.destino = destino;
    }
    
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdPedido() { return idPedido; } public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public int getIdEntregador() { return idEntregador; } public void setIdEntregador(int idEntregador) { this.idEntregador = idEntregador; }
    public String getDestino() { return destino; } public void setDestino(String destino) { this.destino = destino; }
}