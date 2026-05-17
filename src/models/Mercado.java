package models;

import exceptions.empresa.EmpresaException;

public class Mercado extends Empresa {
    private String abre;
    private String fecha;
    private String tipoMercado;

    public Mercado() {}

    public Mercado(int id, int idDono, String nome, String endereco, String abre, String fecha, String tipoMercado) {
        super(id, idDono, nome, endereco);
        this.abre = abre;
        this.fecha = fecha;
        this.tipoMercado = tipoMercado;
    }

    /**
     * Retorna o valor de atributos específicos do Mercado.
     *
     * @param atributo nome do atributo ("abre", "fecha", "tipoMercado")
     * @return valor do atributo como String
     * @throws EmpresaException se o atributo não existir para Mercado
     */
    @Override
    public String getAtributoEspecifico(String atributo) throws EmpresaException {
        switch (atributo) {
            case "abre":        return this.abre;
            case "fecha":       return this.fecha;
            case "tipoMercado": return this.tipoMercado;
            default: throw new EmpresaException("Atributo invalido");
        }
    }

    /**
     * Atualiza os horários de funcionamento do mercado.
     *
     * @param abre  novo horário de abertura
     * @param fecha novo horário de fechamento
     */
    @Override
    public void alterarFuncionamento(String abre, String fecha) {
        this.abre = abre;
        this.fecha = fecha;
    }

    public String getAbre() { return abre; }
    public void setAbre(String abre) { this.abre = abre; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getTipoMercado() { return tipoMercado; }
    public void setTipoMercado(String tipoMercado) { this.tipoMercado = tipoMercado; }
}
