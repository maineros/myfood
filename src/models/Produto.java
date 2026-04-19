package models;

public class Produto {
    private int id;
    private int idEmpresa;
    private String nome;
    private float valor;
    private String categoria;

    public Produto(int id, int idEmpresa, String nome, float valor, String categoria) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
    }

    public int getId() { return id; }
    public int getIdEmpresa() { return idEmpresa; }
    public String getNome() { return nome; }
    public float getValor() { return valor; }
    public String getCategoria() { return categoria; }

    // setters para que os produtos possam ser editados (reatribuicao dos valores)
    public void setNome(String nome) { this.nome = nome; }
    public void setValor(float valor) { this.valor = valor; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}