package models;

import exceptions.usuario.UsuarioException;

public class DonoEmpresa extends Usuario {
    private String cpf;

    // xmldecoder
    public DonoEmpresa() {}

    public DonoEmpresa(int id, String nome, String email, String senha, String endereco, String cpf) {
        super(id, nome, email, senha, endereco);
        this.cpf = cpf;
    }

    /**
     * Retorna o valor de atributos específicos do DonoEmpresa.
     *
     * @param atributo nome do atributo ("cpf")
     * @return valor do atributo como String
     * @throws UsuarioException se o atributo não existir para DonoEmpresa
     */
    @Override
    public String getAtributoEspecifico(String atributo) throws UsuarioException {
        if ("cpf".equals(atributo)) return this.cpf;
        throw new UsuarioException("Usuario nao possui este atributo");
    }

    @Override
    public boolean isDonoEmpresa() {
        return true;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
