package managers;

import models.*;
import factories.UsuarioFactory;
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

    // 1. Criar Cliente (Sem CPF)
    public void criarUsuario(String nome, String email, String senha, String endereco) {
        validarDadosBasicos(nome, email, senha, endereco);
        verificarEmailDuplicado(email);
        
        Usuario novo = UsuarioFactory.criarCliente(nome, email, senha, endereco);
        novo.setId(proximoId++);
        usuarios.add(novo);
    }

    // 2. Criar Dono de Empresa (Com CPF)
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        validarDadosBasicos(nome, email, senha, endereco);
        
        // Validação blindada: se o EasyAccept chamar este método, TEM que ter CPF válido!
        if (cpf == null || cpf.trim().isEmpty() || cpf.length() != 14) {
            throw new RuntimeException("CPF invalido");
        }
        
        verificarEmailDuplicado(email);
        
        Usuario novo = UsuarioFactory.criarDonoEmpresa(nome, email, senha, endereco, cpf);
        novo.setId(proximoId++);
        usuarios.add(novo);
    }

    // 3. Criar Entregador (Com Veículo e Placa)
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) {
        validarDadosBasicos(nome, email, senha, endereco);
        
        if (veiculo == null || veiculo.trim().isEmpty()) throw new RuntimeException("Veiculo invalido");
        if (placa == null || placa.trim().isEmpty()) throw new RuntimeException("Placa invalido");
        
        // Checagem de unicidade da placa exigida pelo teste
        for (Usuario u : usuarios) {
            if (u instanceof Entregador && ((Entregador) u).getPlaca().equals(placa)) {
                throw new RuntimeException("Placa invalido");
            }
        }
        
        verificarEmailDuplicado(email);

        Usuario novo = UsuarioFactory.criarEntregador(nome, email, senha, endereco, veiculo, placa);
        novo.setId(proximoId++);
        usuarios.add(novo);
    }

    public int login(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) return u.getId();
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
            case "veiculo":
                if (u instanceof Entregador) return ((Entregador) u).getVeiculo();
                throw new RuntimeException("Usuario nao possui este atributo");
            case "placa":
                if (u instanceof Entregador) return ((Entregador) u).getPlaca();
                throw new RuntimeException("Usuario nao possui este atributo");
            default: throw new RuntimeException("Atributo invalido");
        }
    }

    private void validarDadosBasicos(String nome, String email, String senha, String endereco) {
        if (nome == null || nome.trim().isEmpty()) throw new RuntimeException("Nome invalido");
        if (email == null || email.trim().isEmpty() || !email.contains("@")) throw new RuntimeException("Email invalido");
        if (senha == null || senha.trim().isEmpty()) throw new RuntimeException("Senha invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new RuntimeException("Endereco invalido");
    }

    private void verificarEmailDuplicado(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new RuntimeException("Conta com esse email ja existe");
            }
        }
    }
}