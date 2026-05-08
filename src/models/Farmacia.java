package models;

public class Farmacia extends Empresa {
    private boolean aberto24Horas;
    private int numeroFuncionarios;

    public Farmacia() {}

    public Farmacia(int id, int idDono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) {
        super(id, idDono, nome, endereco);
        this.aberto24Horas = aberto24Horas;
        this.numeroFuncionarios = numeroFuncionarios;
    }

    public boolean isAberto24Horas() { return aberto24Horas; }
    public void setAberto24Horas(boolean aberto24Horas) { this.aberto24Horas = aberto24Horas; }
    public int getNumeroFuncionarios() { return numeroFuncionarios; }
    public void setNumeroFuncionarios(int numeroFuncionarios) { this.numeroFuncionarios = numeroFuncionarios; }
}