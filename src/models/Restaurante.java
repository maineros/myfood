package models;

import exceptions.empresa.EmpresaException;

public class Restaurante extends Empresa {
    private String tipoCozinha;

    public Restaurante() {}

    public Restaurante(int id, int idDono, String nome, String endereco, String tipoCozinha) {
        super(id, idDono, nome, endereco);
        this.tipoCozinha = tipoCozinha;
    }

    /**
     * Retorna o valor de atributos específicos do Restaurante.
     *
     * @param atributo nome do atributo ("tipoCozinha")
     * @return valor do atributo como String
     * @throws EmpresaException se o atributo não existir para Restaurante
     */
    @Override
    public String getAtributoEspecifico(String atributo) throws EmpresaException {
        if ("tipoCozinha".equals(atributo)) return this.tipoCozinha;
        throw new EmpresaException("Atributo invalido");
    }

    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
}
