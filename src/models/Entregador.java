package models;

import exceptions.usuario.UsuarioException;

public class Entregador extends Usuario {
    private String veiculo;
    private String placa;

    public Entregador() {}

    public Entregador(int id, String nome, String email, String senha, String endereco, String veiculo, String placa) {
        super(id, nome, email, senha, endereco);
        this.veiculo = veiculo;
        this.placa = placa;
    }

    /**
     * Retorna o valor de atributos específicos do Entregador.
     *
     * @param atributo nome do atributo ("veiculo" ou "placa")
     * @return valor do atributo como String
     * @throws UsuarioException se o atributo não existir para Entregador
     */
    @Override
    public String getAtributoEspecifico(String atributo) throws UsuarioException {
        switch (atributo) {
            case "veiculo": return this.veiculo;
            case "placa":   return this.placa;
            default: throw new UsuarioException("Usuario nao possui este atributo");
        }
    }

    @Override
    public boolean isEntregador() {
        return true;
    }

    @Override
    public boolean temPlaca(String placa) {
        return this.placa != null && this.placa.equals(placa);
    }

    public String getVeiculo() { return veiculo; }
    public void setVeiculo(String veiculo) { this.veiculo = veiculo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
}
