import managers.*;

public class Facade {
    private static UsuarioManager usuarioManager = new UsuarioManager();
    private static EmpresaManager empresaManager = new EmpresaManager();
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
        empresaManager = new EmpresaManager();
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
    public void criarUsuario(String nome, String email, String senha, String endereco) {
        usuarioManager.criarUsuario(nome, email, senha, endereco); // Alterado aqui (removido o ", null")
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        usuarioManager.criarUsuario(nome, email, senha, endereco, cpf);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) {
        usuarioManager.criarUsuario(nome, email, senha, endereco, veiculo, placa);
    }

    public int login(String email, String senha) {
        return usuarioManager.login(email, senha);
    }

    public String getAtributoUsuario(int id, String atributo) {
        return usuarioManager.getAtributoUsuario(id, atributo);
    }

    // user stories 2, 5 e 6
    public int criarEmpresa(String tipo, int dono, String nome, String endereco, String cozinha) {
        return empresaManager.criarEmpresa(tipo, dono, nome, endereco, cozinha, usuarioManager);
    }

    public int criarEmpresa(String tipo, int dono, String nome, String endereco, String abre, String fecha, String tipoM) {
        return empresaManager.criarEmpresa(tipo, dono, nome, endereco, abre, fecha, tipoM, usuarioManager);
    }

    public int criarEmpresa(String tipo, int dono, String nome, String endereco, boolean aberto24, int func) {
        return empresaManager.criarEmpresa(tipo, dono, nome, endereco, aberto24, func, usuarioManager);
    }

    public String getAtributoEmpresa(int empresa, String atributo) {
        return empresaManager.getAtributoEmpresa(empresa, atributo, usuarioManager);
    }

    public int getIdEmpresa(int dono, String nome, int indice) {
        return empresaManager.getIdEmpresa(dono, nome, indice);
    }

    public void alterarFuncionamento(int mercado, String abre, String fecha) {
        empresaManager.alterarFuncionamento(mercado, abre, fecha);
    }

    public String getEmpresasDoUsuario(int idDono) {
        return empresaManager.getEmpresasDoUsuario(idDono, usuarioManager);
    }

    // user story 3
    public int criarProduto(int empresa, String nome, float valor, String categoria) {
        return produtoManager.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produto, String nome, float valor, String categoria) {
        produtoManager.editarProduto(produto, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo) {
        return produtoManager.getProduto(nome, empresa, atributo, empresaManager);
    }

    public String listarProdutos(int empresa) {
        return produtoManager.listarProdutos(empresa, empresaManager);
    }

    // user stories 4 e 8
    public int criarPedido(int cliente, int empresa) {
        return pedidoManager.criarPedido(cliente, empresa, usuarioManager, empresaManager);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) {
        return pedidoManager.getNumeroPedido(cliente, empresa, indice);
    }

    public void adicionarProduto(int pedido, int produto) {
        pedidoManager.adicionarProduto(pedido, produto, produtoManager);
    }

    public void removerProduto(int pedido, String produto) {
        pedidoManager.removerProduto(pedido, produto);
    }

    public String getPedidos(int numero, String atributo) {
        return pedidoManager.getPedidos(numero, atributo, usuarioManager, empresaManager);
    }

    public void fecharPedido(int numero) {
        pedidoManager.fecharPedido(numero);
    }

    public void liberarPedido(int numero) {
        pedidoManager.liberarPedido(numero);
    }

    public int obterPedido(int entregador) {
        return pedidoManager.obterPedido(entregador, empresaManager, usuarioManager);
    }

    public int criarEntrega(int pedido, int entregador, String destino) {
        return pedidoManager.criarEntrega(pedido, entregador, destino, usuarioManager);
    }

    public String getEntrega(int id, String atributo) {
        return pedidoManager.getEntrega(id, atributo, usuarioManager, empresaManager);
    }

    public int getIdEntrega(int pedido) {
        return pedidoManager.getIdEntrega(pedido);
    }

    public void entregar(int entrega) {
        pedidoManager.entregar(entrega);
    }

    // user story 7 
    public void cadastrarEntregador(int empresa, int entregador) {
        empresaManager.cadastrarEntregador(empresa, entregador, usuarioManager);
    }

    public String getEntregadores(int empresa) {
        return empresaManager.getEntregadores(empresa, usuarioManager);
    }

    public String getEmpresas(int entregador) {
        return empresaManager.getEmpresasDoEntregador(entregador, usuarioManager);
    }
}