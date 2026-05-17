package managers;

import models.Produto;
import exceptions.produto.*;
import exceptions.empresa.EmpresaNaoEncontradaException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdutoManager {
    private List<Produto> produtos = new ArrayList<>();
    private int proximoId = 1;
    private final String ARQUIVO = "data/produtos.xml";

    @SuppressWarnings("unchecked")
    public ProdutoManager() {
        List<Produto> dadosCarregados = (List<Produto>) PersistenceManager.carregar(ARQUIVO);
        if (dadosCarregados != null) {
            this.produtos = dadosCarregados;
            for (Produto p : produtos) {
                if (p.getId() >= proximoId) proximoId = p.getId() + 1;
            }
        }
    }

    public void salvarDados() {
        PersistenceManager.salvar(this.produtos, ARQUIVO);
    }

    /**
     * Cria um novo produto para uma empresa.
     *
     * @return id do produto criado
     * @throws DadosProdutoInvalidosException se nome, valor ou categoria forem inválidos
     * @throws ProdutoDuplicadoException se já existir produto com o mesmo nome na empresa
     */
    public int criarProduto(int empresa, String nome, float valor, String categoria)
            throws DadosProdutoInvalidosException, ProdutoDuplicadoException {
        validarCampos(nome, valor, categoria);
        for (Produto p : produtos) {
            if (p.getIdEmpresa() == empresa && p.getNome().equals(nome))
                throw new ProdutoDuplicadoException();
        }
        Produto p = new Produto(proximoId++, empresa, nome, valor, categoria);
        produtos.add(p);
        return p.getId();
    }

    /**
     * Edita os dados de um produto existente.
     *
     * @throws DadosProdutoInvalidosException se nome, valor ou categoria forem inválidos
     * @throws ProdutoNaoEncontradoException se o produto não for encontrado
     */
    public void editarProduto(int idProduto, String nome, float valor, String categoria)
            throws DadosProdutoInvalidosException, ProdutoNaoEncontradoException {
        validarCampos(nome, valor, categoria);
        Produto p = getProduto(idProduto);
        p.setNome(nome);
        p.setValor(valor);
        p.setCategoria(categoria);
    }

    /**
     * Lista os produtos de uma empresa.
     *
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public String listarProdutos(int idEmpresa, EmpresaManager em) throws EmpresaNaoEncontradaException {
        // Verifica se a empresa existe; lança com a mensagem esperada pelo teste
        boolean encontrada = false;
        try {
            em.getEmpresa(idEmpresa);
            encontrada = true;
        } catch (EmpresaNaoEncontradaException e) {
            throw new EmpresaNaoEncontradaException("Empresa nao encontrada");
        }
        StringBuilder sb = new StringBuilder("{[");
        boolean primeiro = true;
        for (Produto p : produtos) {
            if (p.getIdEmpresa() == idEmpresa) {
                if (!primeiro) sb.append(", ");
                sb.append(p.getNome());
                primeiro = false;
            }
        }
        return sb.append("]}").toString();
    }

    /**
     * Busca um produto pelo id.
     *
     * @throws ProdutoNaoEncontradoException se o produto não estiver cadastrado
     */
    public Produto getProduto(int id) throws ProdutoNaoEncontradoException {
        for (Produto p : produtos) {
            if (p.getId() == id) return p;
        }
        throw new ProdutoNaoEncontradoException();
    }

    /**
     * Retorna o valor de um atributo de um produto buscado pelo nome.
     *
     * @throws ProdutoNaoEncontradoException se o produto não for encontrado
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     * @throws ProdutoException se o atributo for inválido
     */
    public String getProduto(String nome, int idEmpresa, String atributo, EmpresaManager em)
            throws ProdutoNaoEncontradoException, EmpresaNaoEncontradaException, ProdutoException {
        Produto encontrado = null;
        for (Produto p : produtos) {
            if (p.getIdEmpresa() == idEmpresa && p.getNome().equals(nome)) { encontrado = p; break; }
        }
        if (encontrado == null) throw new ProdutoNaoEncontradoException("Produto nao encontrado");
        switch (atributo) {
            case "valor":     return String.format(Locale.US, "%.2f", encontrado.getValor());
            case "categoria": return encontrado.getCategoria();
            case "empresa":   return em.getEmpresa(idEmpresa).getNome();
            default: throw new ProdutoException("Atributo nao existe");
        }
    }

    private void validarCampos(String nome, float valor, String categoria) throws DadosProdutoInvalidosException {
        if (nome == null || nome.trim().isEmpty()) throw new DadosProdutoInvalidosException("Nome invalido");
        if (valor < 0) throw new DadosProdutoInvalidosException("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty()) throw new DadosProdutoInvalidosException("Categoria invalido");
    }
}
