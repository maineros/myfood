package models;

public class DonoEmpresa extends Usuario {
    private String cpf;

    // xmldecoder
    public DonoEmpresa() {}

    public DonoEmpresa(int id, String nome, String email, String senha, String endereco, String cpf) {
        super(id, nome, email, senha, endereco);
        this.cpf = cpf;
    }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}