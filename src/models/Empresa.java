package models;

public class Empresa {
    private int id;
    private int idDono;
    private String nome;
    private String endereco;
    private String tipoCozinha;

    // xmldecoder
    public Empresa() {}

    public Empresa(int id, int idDono, String nome, String endereco, String tipoCozinha) {
        this.id = id;
        this.idDono = idDono;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdDono() { return idDono; }
    public void setIdDono(int idDono) { this.idDono = idDono; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
}