package models;

import exceptions.empresa.EmpresaException;

public class Farmacia extends Empresa {
    private boolean aberto24Horas;
    private int numeroFuncionarios;

    public Farmacia() {}

    public Farmacia(int id, int idDono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) {
        super(id, idDono, nome, endereco);
        this.aberto24Horas = aberto24Horas;
        this.numeroFuncionarios = numeroFuncionarios;
    }

    /**
     * Retorna o valor de atributos específicos da Farmacia.
     *
     * @param atributo nome do atributo ("aberto24Horas", "numeroFuncionarios")
     * @return valor do atributo como String
     * @throws EmpresaException se o atributo não existir para Farmacia
     */
    @Override
    public String getAtributoEspecifico(String atributo) throws EmpresaException {
        switch (atributo) {
            case "aberto24Horas":      return String.valueOf(this.aberto24Horas);
            case "numeroFuncionarios": return String.valueOf(this.numeroFuncionarios);
            default: throw new EmpresaException("Atributo invalido");
        }
    }

    @Override
    public boolean isFarmacia() {
        return true;
    }

    public boolean isAberto24Horas() { return aberto24Horas; }
    public void setAberto24Horas(boolean aberto24Horas) { this.aberto24Horas = aberto24Horas; }
    public int getNumeroFuncionarios() { return numeroFuncionarios; }
    public void setNumeroFuncionarios(int numeroFuncionarios) { this.numeroFuncionarios = numeroFuncionarios; }
}
