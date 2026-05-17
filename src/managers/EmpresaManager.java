package managers;

import models.*;
import factories.EmpresaFactory;
import exceptions.empresa.*;
import exceptions.usuario.UsuarioNaoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaManager {
    private List<Empresa> empresas = new ArrayList<>();
    private int proximoId = 1;
    private final String ARQUIVO = "data/empresas.xml";

    private final UsuarioManager usuarioManager;

    @SuppressWarnings("unchecked")
    public EmpresaManager(UsuarioManager usuarioManager) {
        this.usuarioManager = usuarioManager;
        List<Empresa> dadosCarregados = (List<Empresa>) PersistenceManager.carregar(ARQUIVO);
        if (dadosCarregados != null) {
            this.empresas = dadosCarregados;
            for (Empresa e : empresas) {
                if (e.getId() >= proximoId) proximoId = e.getId() + 1;
            }
        }
    }

    public void salvarDados() {
        PersistenceManager.salvar(this.empresas, ARQUIVO);
    }

    /**
     * Cria um Restaurante.
     *
     * @throws DadosEmpresaInvalidosException se os dados forem inválidos
     * @throws UsuarioNaoAutorizadoException se o dono não for DonoEmpresa
     * @throws EmpresaDuplicadaException se já existir empresa com o mesmo nome/endereço
     * @throws UsuarioNaoEncontradoException se o dono não for encontrado
     */
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha)
            throws DadosEmpresaInvalidosException, UsuarioNaoAutorizadoException, EmpresaDuplicadaException, UsuarioNaoEncontradoException {
        validarDadosBasicos(tipoEmpresa, nome, endereco);
        validarRegrasComuns(idDono, nome, endereco);
        return persistirEmpresa(EmpresaFactory.criarEmpresa(tipoEmpresa, idDono, nome, endereco, tipoCozinha));
    }

    /**
     * Cria um Mercado.
     *
     * @throws DadosEmpresaInvalidosException se os dados forem inválidos
     * @throws HorarioInvalidoException se os horários forem inválidos
     * @throws UsuarioNaoAutorizadoException se o dono não for DonoEmpresa
     * @throws EmpresaDuplicadaException se já existir empresa com o mesmo nome/endereço
     * @throws UsuarioNaoEncontradoException se o dono não for encontrado
     */
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String abre, String fecha, String tipoMercado)
            throws DadosEmpresaInvalidosException, HorarioInvalidoException, UsuarioNaoAutorizadoException, EmpresaDuplicadaException, UsuarioNaoEncontradoException {
        validarDadosBasicos(tipoEmpresa, nome, endereco);
        EmpresaFactory.validarHorarios(abre, fecha);
        if (tipoMercado == null || tipoMercado.trim().isEmpty()) throw new DadosEmpresaInvalidosException("Tipo de mercado invalido");
        validarRegrasComuns(idDono, nome, endereco);
        return persistirEmpresa(EmpresaFactory.criarEmpresa(tipoEmpresa, idDono, nome, endereco, abre, fecha, tipoMercado));
    }

    /**
     * Cria uma Farmacia.
     *
     * @throws DadosEmpresaInvalidosException se os dados forem inválidos
     * @throws UsuarioNaoAutorizadoException se o dono não for DonoEmpresa
     * @throws EmpresaDuplicadaException se já existir empresa com o mesmo nome/endereço
     * @throws UsuarioNaoEncontradoException se o dono não for encontrado
     */
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios)
            throws DadosEmpresaInvalidosException, UsuarioNaoAutorizadoException, EmpresaDuplicadaException, UsuarioNaoEncontradoException {
        validarDadosBasicos(tipoEmpresa, nome, endereco);
        validarRegrasComuns(idDono, nome, endereco);
        return persistirEmpresa(EmpresaFactory.criarEmpresa(tipoEmpresa, idDono, nome, endereco, aberto24Horas, numeroFuncionarios));
    }

    /**
     * Altera o horário de funcionamento de um Mercado.
     *
     * @throws EmpresaNaoEncontradaException se a empresa não existir
     * @throws EmpresaException se a empresa não for um Mercado
     * @throws HorarioInvalidoException se os horários forem inválidos
     */
    public void alterarFuncionamento(int mercadoId, String abre, String fecha)
            throws EmpresaNaoEncontradaException, EmpresaException, HorarioInvalidoException {
        Empresa e = getEmpresa(mercadoId);
        EmpresaFactory.validarHorarios(abre, fecha);
        e.alterarFuncionamento(abre, fecha);
    }

    /**
     * Retorna as empresas de um dono.
     *
     * @throws UsuarioNaoAutorizadoException se o usuário não for DonoEmpresa
     * @throws UsuarioNaoEncontradoException se o usuário não for encontrado
     */
    public String getEmpresasDoUsuario(int idDono)
            throws UsuarioNaoAutorizadoException, UsuarioNaoEncontradoException {
        if (!usuarioManager.getUsuario(idDono).isDonoEmpresa())
            throw new UsuarioNaoAutorizadoException("Usuario nao pode criar uma empresa");
        StringBuilder sb = new StringBuilder("{[");
        boolean primeiro = true;
        for (Empresa e : empresas) {
            if (e.getIdDono() == idDono) {
                if (!primeiro) sb.append(", ");
                sb.append("[").append(e.getNome()).append(", ").append(e.getEndereco()).append("]");
                primeiro = false;
            }
        }
        return sb.append("]}").toString();
    }

    /**
     * Cadastra um entregador em uma empresa.
     *
     * @throws UsuarioNaoAutorizadoException se o usuário não for Entregador
     * @throws EmpresaNaoEncontradaException se a empresa não existir
     * @throws UsuarioNaoEncontradoException se o usuário não for encontrado
     */
    public void cadastrarEntregador(int idEmpresa, int idEntregador)
            throws UsuarioNaoAutorizadoException, EmpresaNaoEncontradaException, UsuarioNaoEncontradoException {
        if (!usuarioManager.getUsuario(idEntregador).isEntregador())
            throw new UsuarioNaoAutorizadoException("Usuario nao e um entregador");
        Empresa e = getEmpresa(idEmpresa);
        if (e.getEntregadores() == null) e.setEntregadores(new ArrayList<>());
        if (!e.getEntregadores().contains(idEntregador)) e.getEntregadores().add(idEntregador);
    }

    /**
     * Retorna os entregadores de uma empresa.
     *
     * @throws EmpresaNaoEncontradaException se a empresa não existir
     * @throws UsuarioNaoEncontradoException se algum entregador não for encontrado
     */
    public String getEntregadores(int idEmpresa)
            throws EmpresaNaoEncontradaException, UsuarioNaoEncontradoException {
        Empresa e = getEmpresa(idEmpresa);
        StringBuilder sb = new StringBuilder("{[");
        boolean primeiro = true;
        if (e.getEntregadores() != null) {
            for (int idEntregador : e.getEntregadores()) {
                if (!primeiro) sb.append(", ");
                sb.append(usuarioManager.getUsuario(idEntregador).getEmail());
                primeiro = false;
            }
        }
        return sb.append("]}").toString();
    }

    /**
     * Retorna as empresas nas quais um entregador está cadastrado.
     *
     * @throws UsuarioNaoAutorizadoException se o usuário não for Entregador
     * @throws UsuarioNaoEncontradoException se o usuário não for encontrado
     */
    public String getEmpresasDoEntregador(int idEntregador)
            throws UsuarioNaoAutorizadoException, UsuarioNaoEncontradoException {
        if (!usuarioManager.getUsuario(idEntregador).isEntregador())
            throw new UsuarioNaoAutorizadoException("Usuario nao e um entregador");
        StringBuilder sb = new StringBuilder("{[");
        boolean primeiro = true;
        for (Empresa e : empresas) {
            if (e.getEntregadores() != null && e.getEntregadores().contains(idEntregador)) {
                if (!primeiro) sb.append(", ");
                sb.append("[").append(e.getNome()).append(", ").append(e.getEndereco()).append("]");
                primeiro = false;
            }
        }
        return sb.append("]}").toString();
    }

    public boolean isEntregadorDaEmpresa(int idEmpresa, int idEntregador) throws EmpresaNaoEncontradaException {
        Empresa e = getEmpresa(idEmpresa);
        return e.getEntregadores() != null && e.getEntregadores().contains(idEntregador);
    }

    public boolean entregadorTemEmpresa(int idEntregador) {
        for (Empresa e : empresas) {
            if (e.getEntregadores() != null && e.getEntregadores().contains(idEntregador)) return true;
        }
        return false;
    }

    /**
     * Retorna o id de uma empresa pelo nome e índice.
     *
     * @throws DadosEmpresaInvalidosException se o nome ou índice forem inválidos
     * @throws EmpresaException se a empresa não for encontrada ou o índice for maior que o esperado
     */
    public int getIdEmpresa(int idDono, String nome, int indice)
            throws DadosEmpresaInvalidosException, EmpresaException {
        if (nome == null || nome.trim().isEmpty()) throw new DadosEmpresaInvalidosException("Nome invalido");
        if (indice < 0) throw new DadosEmpresaInvalidosException("Indice invalido");
        int cont = 0;
        for (Empresa e : empresas) {
            if (e.getIdDono() == idDono && e.getNome().equals(nome)) {
                if (cont == indice) return e.getId();
                cont++;
            }
        }
        if (cont == 0) throw new EmpresaException("Nao existe empresa com esse nome");
        throw new EmpresaException("Indice maior que o esperado");
    }

    /**
     * Retorna o valor de um atributo de uma empresa.
     *
     * @throws EmpresaNaoEncontradaException se a empresa não existir
     * @throws AtributoEmpresaInvalidoException se o atributo for inválido ou não pertencer ao tipo da empresa
     * @throws UsuarioNaoEncontradoException se o dono não for encontrado
     */
    public String getAtributoEmpresa(int id, String atributo)
            throws EmpresaNaoEncontradaException, AtributoEmpresaInvalidoException, UsuarioNaoEncontradoException {
        Empresa e = getEmpresa(id);
        if (atributo == null || atributo.trim().isEmpty()) throw new AtributoEmpresaInvalidoException();
        switch (atributo) {
            case "nome":     return e.getNome();
            case "endereco": return e.getEndereco();
            case "dono":     return usuarioManager.getUsuario(e.getIdDono()).getNome();
            default:
                try {
                    return e.getAtributoEspecifico(atributo);
                } catch (exceptions.empresa.EmpresaException ex) {
                    throw new AtributoEmpresaInvalidoException(ex.getMessage());
                }
        }
    }

    /**
     * Busca uma empresa pelo id.
     *
     * @throws EmpresaNaoEncontradaException se a empresa não estiver cadastrada
     */
    public Empresa getEmpresa(int id) throws EmpresaNaoEncontradaException {
        for (Empresa e : empresas) {
            if (e.getId() == id) return e;
        }
        throw new EmpresaNaoEncontradaException();
    }

    private void validarDadosBasicos(String tipo, String nome, String endereco) throws DadosEmpresaInvalidosException {
        if (tipo == null || tipo.trim().isEmpty()) throw new DadosEmpresaInvalidosException("Tipo de empresa invalido");
        if (nome == null || nome.trim().isEmpty()) throw new DadosEmpresaInvalidosException("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new DadosEmpresaInvalidosException("Endereco da empresa invalido");
    }

    private void validarRegrasComuns(int idDono, String nome, String endereco)
            throws UsuarioNaoAutorizadoException, EmpresaDuplicadaException, UsuarioNaoEncontradoException {
        if (!usuarioManager.getUsuario(idDono).isDonoEmpresa())
            throw new UsuarioNaoAutorizadoException("Usuario nao pode criar uma empresa");
        for (Empresa e : empresas) {
            if (e.getNome().equals(nome)) {
                if (e.getIdDono() != idDono) throw new EmpresaDuplicadaException("Empresa com esse nome ja existe");
                if (e.getEndereco().equals(endereco)) throw new EmpresaDuplicadaException("Proibido cadastrar duas empresas com o mesmo nome e local");
            }
        }
    }

    private int persistirEmpresa(Empresa e) {
        e.setId(proximoId++);
        empresas.add(e);
        return e.getId();
    }
}
