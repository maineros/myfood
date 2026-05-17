package models;

import exceptions.usuario.UsuarioException;

public abstract class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String endereco;

    // xmldecoder
    public Usuario() {}

    public Usuario(int id, String nome, String email, String senha, String endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
    }

    /**
     * Retorna o valor de um atributo específico da subclasse.
     * Subclasses devem sobrescrever para expor seus atributos próprios.
     *
     * @param atributo nome do atributo desejado
     * @return valor do atributo como String
     * @throws UsuarioException se o atributo não existir para este tipo de usuário
     */
    public String getAtributoEspecifico(String atributo) throws UsuarioException {
        throw new UsuarioException("Usuario nao possui este atributo");
    }

    /**
     * Verifica se a placa informada pertence a este usuário.
     * Sobrescrito apenas por Entregador.
     */
    public boolean temPlaca(String placa) {
        return false;
    }

    /**
     * Retorna true se este usuário é um DonoEmpresa.
     * Sobrescrito por DonoEmpresa para evitar instanceof.
     */
    public boolean isDonoEmpresa() {
        return false;
    }

    /**
     * Retorna true se este usuário é um Entregador.
     * Sobrescrito por Entregador para evitar instanceof.
     */
    public boolean isEntregador() {
        return false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
