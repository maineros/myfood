package managers;

import models.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioManager {
    private List<Usuario> usuarios = new ArrayList<>();
    private int proximoId = 1;
    private final String ARQUIVO = "data/usuarios.xml";

    @SuppressWarnings("unchecked")
    public UsuarioManager() {
        List<Usuario> dadosCarregados = (List<Usuario>) PersistenceManager.carregar(ARQUIVO);
        if (dadosCarregados != null) {
            this.usuarios = dadosCarregados;
            for (Usuario u : usuarios) {
                if (u.getId() >= proximoId) proximoId = u.getId() + 1;
            }
        }
    }

    public void salvarDados() {
        PersistenceManager.salvar(this.usuarios, ARQUIVO);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        validarString(nome, "Nome invalido");
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new RuntimeException("Email invalido");
        }
        validarString(senha, "Senha invalido");
        validarString(endereco, "Endereco invalido");

        if (cpf != null) {
            if (cpf.trim().isEmpty() || cpf.length() != 14) throw new RuntimeException("CPF invalido");
        }

        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                throw new RuntimeException("Conta com esse email ja existe");
            }
        }

        if (cpf != null) {
            usuarios.add(new DonoEmpresa(proximoId++, nome, email, senha, endereco, cpf));
        } else {
            usuarios.add(new Cliente(proximoId++, nome, email, senha, endereco));
        }
    }

    public int login(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                return u.getId();
            }
        }
        throw new RuntimeException("Login ou senha invalidos");
    }

    public Usuario getUsuario(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        throw new RuntimeException("Usuario nao cadastrado.");
    }

    public String getAtributoUsuario(int id, String atributo) {
        Usuario u = getUsuario(id);
        if (atributo == null || atributo.trim().isEmpty()) throw new RuntimeException("Atributo invalido");
        
        switch (atributo) {
            case "nome": return u.getNome();
            case "email": return u.getEmail();
            case "senha": return u.getSenha();
            case "endereco": return u.getEndereco();
            case "cpf": 
                if (u instanceof DonoEmpresa) return ((DonoEmpresa) u).getCpf();
                throw new RuntimeException("Usuario nao possui este atributo");
            default: throw new RuntimeException("Atributo invalido");
        }
    }

    private void validarString(String valor, String erro) {
        if (valor == null || valor.trim().isEmpty()) throw new RuntimeException(erro);
    }
}