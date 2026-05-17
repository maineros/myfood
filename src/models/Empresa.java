package models;

import exceptions.empresa.EmpresaException;
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

    /**
     * Retorna o valor de um atributo específico da subclasse.
     * Subclasses devem sobrescrever para expor seus atributos próprios.
     *
     * @param atributo nome do atributo desejado
     * @return valor do atributo como String
     * @throws EmpresaException se o atributo não existir para este tipo de empresa
     */
    public String getAtributoEspecifico(String atributo) throws EmpresaException {
        throw new EmpresaException("Atributo invalido");
    }

    /**
     * Aplica uma alteração de horário de funcionamento.
     * Sobrescrito apenas por Mercado.
     *
     * @throws EmpresaException se a empresa não suportar esta operação
     */
    public void alterarFuncionamento(String abre, String fecha) throws EmpresaException {
        throw new EmpresaException("Nao e um mercado valido");
    }

    /**
     * Retorna true se esta empresa é uma Farmacia.
     * Sobrescrito por Farmacia para evitar instanceof.
     */
    public boolean isFarmacia() {
        return false;
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
