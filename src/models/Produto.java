package models;

public class Produto {
    private int id;
    private int idEmpresa;
    private String nome;
    private float valor;
    private String categoria;
    
    // xmldecoder
    public Produto() {}
    
    public Produto(int id, int idEmpresa, String nome, float valor, String categoria) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public float getValor() { return valor; }
    public void setValor(float valor) { this.valor = valor; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}