package models;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int numero;
    private int idCliente;
    private int idEmpresa;
    private String estado;
    private List<Produto> produtos;

    public Pedido(int numero, int idCliente, int idEmpresa) {
        this.numero = numero;
        this.idCliente = idCliente;
        this.idEmpresa = idEmpresa;
        this.estado = "aberto"; 
        this.produtos = new ArrayList<>();
    }
    
    public float getValorTotal() {
        float total = 0;
        for (Produto p : produtos) {
            total += p.getValor();
        }
        return total;
    }
    
    public void adicionarProduto(Produto p) {
        this.produtos.add(p);
    }
    
    public int getNumero() { return numero; }
    public int getIdCliente() { return idCliente; }
    public int getIdEmpresa() { return idEmpresa; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<Produto> getProdutos() { return produtos; }
}