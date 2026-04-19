package models;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int numero;
    private int idCliente;
    private int idEmpresa;
    private String estado;
    private List<Produto> produtos;

    // xmldecoder
    public Pedido() {
        this.produtos = new ArrayList<>();
    }

    public Pedido(int numero, int idCliente, int idEmpresa) {
        this.numero = numero;
        this.idCliente = idCliente;
        this.idEmpresa = idEmpresa;
        this.estado = "aberto"; 
        this.produtos = new ArrayList<>();
    }
    
    public float getValorTotal() {
        float total = 0;
        if (produtos != null) {
            for (Produto p : produtos) {
                total += p.getValor();
            }
        }
        return total;
    }
    
    public void adicionarProduto(Produto p) {
        this.produtos.add(p);
    }
    
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}