package managers;

import models.Produto;
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

    public int criarProduto(int empresa, String nome, float valor, String categoria) {
        validarCampos(nome, valor, categoria);
        for (Produto p : produtos) {
            if (p.getIdEmpresa() == empresa && p.getNome().equals(nome)) {
                throw new RuntimeException("Ja existe um produto com esse nome para essa empresa");
            }
        }
        Produto p = new Produto(proximoId++, empresa, nome, valor, categoria);
        produtos.add(p);
        return p.getId();
    }

    public void editarProduto(int idProduto, String nome, float valor, String categoria) {
        validarCampos(nome, valor, categoria);
        Produto p = getProduto(idProduto);
        p.setNome(nome);
        p.setValor(valor);
        p.setCategoria(categoria);
    }

    public String listarProdutos(int idEmpresa, EmpresaManager em) {
        try {
            em.getEmpresa(idEmpresa);
        } catch (RuntimeException e) {
            throw new RuntimeException("Empresa nao encontrada"); 
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

    public Produto getProduto(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) return p;
        }
        throw new RuntimeException("Produto nao cadastrado");
    }

    public String getProduto(String nome, int idEmpresa, String atributo, EmpresaManager em) {
        Produto encontrado = null;
        for (Produto p : produtos) {
            if (p.getIdEmpresa() == idEmpresa && p.getNome().equals(nome)) {
                encontrado = p;
                break;
            }
        }
        if (encontrado == null) throw new RuntimeException("Produto nao encontrado");

        switch (atributo) {
            case "valor": return String.format(Locale.US, "%.2f", encontrado.getValor());
            case "categoria": return encontrado.getCategoria();
            case "empresa": return em.getEmpresa(idEmpresa).getNome();
            default: throw new RuntimeException("Atributo nao existe");
        }
    }

    private void validarCampos(String nome, float valor, String categoria) {
        if (nome == null || nome.trim().isEmpty()) throw new RuntimeException("Nome invalido");
        if (valor < 0) throw new RuntimeException("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty()) throw new RuntimeException("Categoria invalido");
    }
}