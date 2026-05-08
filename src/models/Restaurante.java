package models;

public class Restaurante extends Empresa {
    private String tipoCozinha;

    public Restaurante() {}

    public Restaurante(int id, int idDono, String nome, String endereco, String tipoCozinha) {
        super(id, idDono, nome, endereco);
        this.tipoCozinha = tipoCozinha;
    }

    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
}
