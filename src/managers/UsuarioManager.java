package managers;

import models.*;
import factories.UsuarioFactory;
import exceptions.usuario.*;
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

    /**
     * Cria um novo Cliente.
     *
     * @throws DadosUsuarioInvalidosException se nome, email, senha ou endereço forem inválidos
     * @throws EmailDuplicadoException se já existir conta com o e-mail informado
     */
    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws DadosUsuarioInvalidosException, EmailDuplicadoException {
        validarDadosBasicos(nome, email, senha, endereco);
        verificarEmailDuplicado(email);
        Usuario novo = UsuarioFactory.criarCliente(nome, email, senha, endereco);
        novo.setId(proximoId++);
        usuarios.add(novo);
    }

    /**
     * Cria um novo DonoEmpresa.
     *
     * @throws DadosUsuarioInvalidosException se algum dado for inválido
     * @throws EmailDuplicadoException se já existir conta com o e-mail informado
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws DadosUsuarioInvalidosException, EmailDuplicadoException {
        validarDadosBasicos(nome, email, senha, endereco);
        if (cpf == null || cpf.trim().isEmpty() || cpf.length() != 14)
            throw new DadosUsuarioInvalidosException("CPF invalido");
        verificarEmailDuplicado(email);
        Usuario novo = UsuarioFactory.criarDonoEmpresa(nome, email, senha, endereco, cpf);
        novo.setId(proximoId++);
        usuarios.add(novo);
    }

    /**
     * Cria um novo Entregador.
     *
     * @throws DadosUsuarioInvalidosException se algum dado for inválido ou a placa já existir
     * @throws EmailDuplicadoException se já existir conta com o e-mail informado
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa)
            throws DadosUsuarioInvalidosException, EmailDuplicadoException {
        validarDadosBasicos(nome, email, senha, endereco);
        if (veiculo == null || veiculo.trim().isEmpty()) throw new DadosUsuarioInvalidosException("Veiculo invalido");
        if (placa == null || placa.trim().isEmpty()) throw new DadosUsuarioInvalidosException("Placa invalido");
        for (Usuario u : usuarios) {
            if (u.temPlaca(placa)) throw new DadosUsuarioInvalidosException("Placa invalido");
        }
        verificarEmailDuplicado(email);
        Usuario novo = UsuarioFactory.criarEntregador(nome, email, senha, endereco, veiculo, placa);
        novo.setId(proximoId++);
        usuarios.add(novo);
    }

    /**
     * Realiza o login de um usuário.
     *
     * @return id do usuário autenticado
     * @throws LoginInvalidoException se o e-mail ou senha forem incorretos
     */
    public int login(String email, String senha) throws LoginInvalidoException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) return u.getId();
        }
        throw new LoginInvalidoException();
    }

    /**
     * Busca um usuário pelo id.
     *
     * @return o usuário encontrado
     * @throws UsuarioNaoEncontradoException se nenhum usuário com o id existir
     */
    public Usuario getUsuario(int id) throws UsuarioNaoEncontradoException {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        throw new UsuarioNaoEncontradoException();
    }

    /**
     * Retorna o valor de um atributo do usuário.
     *
     * @throws UsuarioNaoEncontradoException se o usuário não existir
     * @throws AtributoUsuarioInvalidoException se o atributo for inválido ou não pertencer ao tipo do usuário
     */
    public String getAtributoUsuario(int id, String atributo)
            throws UsuarioNaoEncontradoException, AtributoUsuarioInvalidoException {
        Usuario u = getUsuario(id);
        if (atributo == null || atributo.trim().isEmpty()) throw new AtributoUsuarioInvalidoException();
        switch (atributo) {
            case "nome":     return u.getNome();
            case "email":    return u.getEmail();
            case "senha":    return u.getSenha();
            case "endereco": return u.getEndereco();
            default:
                try {
                    return u.getAtributoEspecifico(atributo);
                } catch (UsuarioException e) {
                    throw new AtributoUsuarioInvalidoException(e.getMessage());
                }
        }
    }

    private void validarDadosBasicos(String nome, String email, String senha, String endereco)
            throws DadosUsuarioInvalidosException {
        if (nome == null || nome.trim().isEmpty()) throw new DadosUsuarioInvalidosException("Nome invalido");
        if (email == null || email.trim().isEmpty() || !email.contains("@")) throw new DadosUsuarioInvalidosException("Email invalido");
        if (senha == null || senha.trim().isEmpty()) throw new DadosUsuarioInvalidosException("Senha invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new DadosUsuarioInvalidosException("Endereco invalido");
    }

    private void verificarEmailDuplicado(String email) throws EmailDuplicadoException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) throw new EmailDuplicadoException();
        }
    }
}
