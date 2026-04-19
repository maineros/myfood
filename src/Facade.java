import managers.*;
import models.Produto;

public class Facade {
    private static UsuarioManager usuarioManager = new UsuarioManager();
    private static EmpresaManager empresaManager = new EmpresaManager();
    private static ProdutoManager produtoManager = new ProdutoManager();
    private static PedidoManager pedidoManager = new PedidoManager();

    public Facade() {
        
    }

    public void zerarSistema() {
        // Aponta para a nova pasta 'data'
        new java.io.File("data/usuarios.xml").delete();
        new java.io.File("data/empresas.xml").delete();
        new java.io.File("data/produtos.xml").delete();
        new java.io.File("data/pedidos.xml").delete();
        System.out.println("Arquivos XML antigos apagados da pasta 'data'.");

        usuarioManager = new UsuarioManager();
        empresaManager = new EmpresaManager();
        produtoManager = new ProdutoManager();
        pedidoManager = new PedidoManager();
        System.out.println("Sistema zerado na memoria.");
    }

    public void encerrarSistema() {
        System.out.println("Encerrando o sistema e salvando os dados em XML...");
        usuarioManager.salvarDados();
        empresaManager.salvarDados();
        produtoManager.salvarDados();
        pedidoManager.salvarDados();
        System.out.println("Dados salvos com sucesso!");
    }

    // user story 1
    public void criarUsuario(String nome, String email, String senha, String endereco) {
        usuarioManager.criarUsuario(nome, email, senha, endereco, null);
    }
    
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        if (cpf == null) cpf = "";
        usuarioManager.criarUsuario(nome, email, senha, endereco, cpf);
    }

    public int login(String email, String senha) {
        return usuarioManager.login(email, senha);
    }

    public String getAtributoUsuario(int id, String atributo) {
        return usuarioManager.getAtributoUsuario(id, atributo);
    }

    // user story 2
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha) {
        return empresaManager.criarEmpresa(dono, nome, endereco, tipoCozinha, usuarioManager);
    }

    public String getEmpresasDoUsuario(int idDono) {
        return empresaManager.getEmpresasDoUsuario(idDono, usuarioManager);
    }

    public int getIdEmpresa(int idDono, String nome, int indice) {
        return empresaManager.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int empresa, String atributo) {
        return empresaManager.getAtributoEmpresa(empresa, atributo, usuarioManager);
    }

    // user story 3
    public int criarProduto(int empresa, String nome, float valor, String categoria) {
        return produtoManager.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produtoId, String nome, float valor, String categoria) {
        produtoManager.editarProduto(produtoId, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo) {
        return produtoManager.getAtributoProduto(nome, empresa, atributo, empresaManager);
    }

    public String listarProdutos(int empresa) {
        return produtoManager.listarProdutos(empresa, empresaManager);
    }

    // user story 4
    public int criarPedido(int cliente, int empresa) {
        return pedidoManager.criarPedido(cliente, empresa, usuarioManager);
    }

    public void adicionarProduto(int numero, int produtoId) {
        Produto p = produtoManager.getProduto(produtoId);
        pedidoManager.adicionarProduto(numero, p);
    }

    public String getPedidos(int numero, String atributo) {
        return pedidoManager.getAtributoPedido(numero, atributo, usuarioManager, empresaManager);
    }

    public void fecharPedido(int numero) {
        pedidoManager.fecharPedido(numero);
    }

    public void removerProduto(int pedido, String nomeProduto) {
        pedidoManager.removerProduto(pedido, nomeProduto);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) {
        return pedidoManager.getNumeroPedido(cliente, empresa, indice);
    }
}