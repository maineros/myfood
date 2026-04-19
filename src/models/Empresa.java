package models;

public class Empresa {
    private int id;
    private int idDono;
    private String nome;
    private String endereco;
    private String tipoCozinha;

    public Empresa(int id, int idDono, String nome, String endereco, String tipoCozinha) {
        this.id = id;
        this.idDono = idDono;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
    }

    public int getId() { return id; }
    public int getIdDono() { return idDono; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getTipoCozinha() { return tipoCozinha; }
}