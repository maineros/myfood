import managers.*;
import exceptions.usuario.*;
import exceptions.empresa.*;
import exceptions.pedido.*;
import exceptions.produto.*;

public class Facade {
    private static UsuarioManager usuarioManager = new UsuarioManager();
    private static EmpresaManager empresaManager = new EmpresaManager(usuarioManager);
    private static ProdutoManager produtoManager = new ProdutoManager();
    private static PedidoManager pedidoManager = new PedidoManager();

    public Facade() {}

    public void zerarSistema() {
        new java.io.File("data/usuarios.xml").delete();
        new java.io.File("data/empresas.xml").delete();
        new java.io.File("data/produtos.xml").delete();
        new java.io.File("data/pedidos.xml").delete();
        new java.io.File("data/entregas.xml").delete();

        usuarioManager = new UsuarioManager();
        empresaManager = new EmpresaManager(usuarioManager);
        produtoManager = new ProdutoManager();
        pedidoManager = new PedidoManager();

        factories.EmpresaFactory.emptyTimeCounter = 0;
    }

    public void encerrarSistema() {
        usuarioManager.salvarDados();
        empresaManager.salvarDados();
        produtoManager.salvarDados();
        pedidoManager.salvarDados();
    }

    // user stories 1 e 7
    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws DadosUsuarioInvalidosException, EmailDuplicadoException {
        usuarioManager.criarUsuario(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws DadosUsuarioInvalidosException, EmailDuplicadoException {
        usuarioManager.criarUsuario(nome, email, senha, endereco, cpf);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa)
            throws DadosUsuarioInvalidosException, EmailDuplicadoException {
        usuarioManager.criarUsuario(nome, email, senha, endereco, veiculo, placa);
    }

    public int login(String email, String senha) throws LoginInvalidoException {
        return usuarioManager.login(email, senha);
    }

    public String getAtributoUsuario(int id, String atributo)
            throws UsuarioNaoEncontradoException, AtributoUsuarioInvalidoException {
        return usuarioManager.getAtributoUsuario(id, atributo);
    }

    // user stories 2, 5 e 6
    public int criarEmpresa(String tipo, int dono, String nome, String endereco, String cozinha)
            throws DadosEmpresaInvalidosException, UsuarioNaoAutorizadoException, EmpresaDuplicadaException, UsuarioNaoEncontradoException {
        return empresaManager.criarEmpresa(tipo, dono, nome, endereco, cozinha);
    }

    public int criarEmpresa(String tipo, int dono, String nome, String endereco, String abre, String fecha, String tipoM)
            throws DadosEmpresaInvalidosException, HorarioInvalidoException, UsuarioNaoAutorizadoException, EmpresaDuplicadaException, UsuarioNaoEncontradoException {
        return empresaManager.criarEmpresa(tipo, dono, nome, endereco, abre, fecha, tipoM);
    }

    public int criarEmpresa(String tipo, int dono, String nome, String endereco, boolean aberto24, int func)
            throws DadosEmpresaInvalidosException, UsuarioNaoAutorizadoException, EmpresaDuplicadaException, UsuarioNaoEncontradoException {
        return empresaManager.criarEmpresa(tipo, dono, nome, endereco, aberto24, func);
    }

    public String getAtributoEmpresa(int empresa, String atributo)
            throws EmpresaNaoEncontradaException, AtributoEmpresaInvalidoException, UsuarioNaoEncontradoException {
        return empresaManager.getAtributoEmpresa(empresa, atributo);
    }

    public int getIdEmpresa(int dono, String nome, int indice)
            throws DadosEmpresaInvalidosException, EmpresaNaoEncontradaException, EmpresaException {
        return empresaManager.getIdEmpresa(dono, nome, indice);
    }

    public void alterarFuncionamento(int mercado, String abre, String fecha)
            throws EmpresaNaoEncontradaException, EmpresaException, HorarioInvalidoException {
        empresaManager.alterarFuncionamento(mercado, abre, fecha);
    }

    public String getEmpresasDoUsuario(int idDono)
            throws UsuarioNaoAutorizadoException, UsuarioNaoEncontradoException {
        return empresaManager.getEmpresasDoUsuario(idDono);
    }

    // user story 3
    public int criarProduto(int empresa, String nome, float valor, String categoria)
            throws DadosProdutoInvalidosException, ProdutoDuplicadoException {
        return produtoManager.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produto, String nome, float valor, String categoria)
            throws DadosProdutoInvalidosException, ProdutoNaoEncontradoException {
        produtoManager.editarProduto(produto, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo)
            throws ProdutoNaoEncontradoException, EmpresaNaoEncontradaException, ProdutoException {
        return produtoManager.getProduto(nome, empresa, atributo, empresaManager);
    }

    public String listarProdutos(int empresa) throws EmpresaNaoEncontradaException {
        return produtoManager.listarProdutos(empresa, empresaManager);
    }

    // user stories 4 e 8
    public int criarPedido(int cliente, int empresa)
            throws PedidoException, PedidoDuplicadoException, UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        return pedidoManager.criarPedido(cliente, empresa, usuarioManager, empresaManager);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) throws PedidoNaoEncontradoException {
        return pedidoManager.getNumeroPedido(cliente, empresa, indice);
    }

    public void adicionarProduto(int pedido, int produto)
            throws PedidoNaoEncontradoException, PedidoFechadoException, PedidoException {
        pedidoManager.adicionarProduto(pedido, produto, produtoManager);
    }

    public void removerProduto(int pedido, String produto)
            throws PedidoNaoEncontradoException, PedidoFechadoException, PedidoException {
        pedidoManager.removerProduto(pedido, produto);
    }

    public String getPedidos(int numero, String atributo)
            throws PedidoNaoEncontradoException, PedidoException, UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        return pedidoManager.getPedidos(numero, atributo, usuarioManager, empresaManager);
    }

    public void fecharPedido(int numero) throws PedidoNaoEncontradoException {
        pedidoManager.fecharPedido(numero);
    }

    public void liberarPedido(int numero) throws PedidoNaoEncontradoException, PedidoFechadoException {
        pedidoManager.liberarPedido(numero);
    }

    public int obterPedido(int entregador)
            throws PedidoException, EntregadorSemEmpresaException, SemPedidoParaEntregaException,
                   UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        return pedidoManager.obterPedido(entregador, empresaManager, usuarioManager);
    }

    public int criarEntrega(int pedido, int entregador, String destino)
            throws PedidoNaoEncontradoException, PedidoFechadoException, PedidoException,
                   EntregadorOcupadoException, UsuarioNaoEncontradoException {
        return pedidoManager.criarEntrega(pedido, entregador, destino, usuarioManager);
    }

    public String getEntrega(int id, String atributo)
            throws EntregaNaoEncontradaException, PedidoNaoEncontradoException, PedidoException,
                   UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        return pedidoManager.getEntrega(id, atributo, usuarioManager, empresaManager);
    }

    public int getIdEntrega(int pedido) throws EntregaNaoEncontradaException {
        return pedidoManager.getIdEntrega(pedido);
    }

    public void entregar(int entrega) throws EntregaNaoEncontradaException, PedidoNaoEncontradoException {
        pedidoManager.entregar(entrega);
    }

    // user story 7
    public void cadastrarEntregador(int empresa, int entregador)
            throws UsuarioNaoAutorizadoException, EmpresaNaoEncontradaException, UsuarioNaoEncontradoException {
        empresaManager.cadastrarEntregador(empresa, entregador);
    }

    public String getEntregadores(int empresa)
            throws EmpresaNaoEncontradaException, UsuarioNaoEncontradoException {
        return empresaManager.getEntregadores(empresa);
    }

    public String getEmpresas(int entregador)
            throws UsuarioNaoAutorizadoException, UsuarioNaoEncontradoException {
        return empresaManager.getEmpresasDoEntregador(entregador);
    }
}
