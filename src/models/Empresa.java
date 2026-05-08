package models;

import java.util.ArrayList;
import java.util.List;

public abstract class Empresa {
    private int id;
    private int idDono;
    private String nome;
    private String endereco;
    private List<Integer> entregadores = new ArrayList<>();
    
    public Empresa() {}

    public Empresa(int id, int idDono, String nome, String endereco) {
        this.id = id;
        this.idDono = idDono;
        this.nome = nome;
        this.endereco = endereco;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdDono() { return idDono; }
    public void setIdDono(int idDono) { this.idDono = idDono; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public List<Integer> getEntregadores() { return entregadores; }
    public void setEntregadores(List<Integer> entregadores) { this.entregadores = entregadores; }
}