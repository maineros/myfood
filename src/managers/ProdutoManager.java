package managers;

import models.Produto;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdutoManager {
    private List<Produto> produtos = new ArrayList<>();
    private int proximoId = 1;

    public int criarProduto(int empresa, String nome, float valor, String categoria) {
        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome invalido");
        if (valor < 0) throw new RuntimeException("Valor invalido");
        if (categoria == null || categoria.isEmpty()) throw new RuntimeException("Categoria invalido");

        for (Produto p : produtos) {
            if (p.getIdEmpresa() == empresa && p.getNome().equals(nome)) {
                throw new RuntimeException("Ja existe um produto com esse nome para essa empresa");
            }
        }

        Produto novo = new Produto(proximoId++, empresa, nome, valor, categoria);
        produtos.add(novo);
        return novo.getId();
    }

    public void editarProduto(int produtoId, String nome, float valor, String categoria) {
        if (nome == null || nome.trim().isEmpty()) throw new RuntimeException("Nome invalido");
        if (valor < 0) throw new RuntimeException("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty()) throw new RuntimeException("Categoria invalido");

        Produto p = getProduto(produtoId);
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

    public String getAtributoProduto(String nome, int idEmpresa, String atributo, EmpresaManager em) {
        for (Produto p : produtos) {
            if (p.getIdEmpresa() == idEmpresa && p.getNome().equals(nome)) {
                if (atributo.equals("valor")) return String.format(Locale.US, "%.2f", p.getValor());
                if (atributo.equals("categoria")) return p.getCategoria();
                if (atributo.equals("empresa")) return em.getEmpresa(idEmpresa).getNome();
                throw new RuntimeException("Atributo nao existe");
            }
        }
        throw new RuntimeException("Produto nao encontrado");
    }
}