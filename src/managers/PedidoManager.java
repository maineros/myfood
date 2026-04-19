package managers;

import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PedidoManager {
    private List<Pedido> pedidos = new ArrayList<>();
    private int proximoNumero = 1;

    public int criarPedido(int idCliente, int idEmpresa, UsuarioManager um) {
        if (um.getUsuario(idCliente) instanceof DonoEmpresa) throw new RuntimeException("Dono de empresa nao pode fazer um pedido");

        for (Pedido p : pedidos) {
            if (p.getIdCliente() == idCliente && p.getIdEmpresa() == idEmpresa && p.getEstado().equals("aberto")) {
                throw new RuntimeException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }

        Pedido novo = new Pedido(proximoNumero++, idCliente, idEmpresa);
        pedidos.add(novo);
        return novo.getNumero();
    }

    public void adicionarProduto(int numero, Produto p) {
        Pedido ped = null;
        for (Pedido pObj : pedidos) {
            if (pObj.getNumero() == numero) {
                ped = pObj;
                break;
            }
        }
        
        if (ped == null) throw new RuntimeException("Nao existe pedido em aberto");
        if (!ped.getEstado().equals("aberto")) throw new RuntimeException("Nao e possivel adcionar produtos a um pedido fechado");
        if (ped.getIdEmpresa() != p.getIdEmpresa()) throw new RuntimeException("O produto nao pertence a essa empresa");
        
        ped.adicionarProduto(p);
    }

    public void removerProduto(int numeroPedido, String nomeProduto) {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new RuntimeException("Produto invalido");
        }
        
        Pedido p = getPedido(numeroPedido);
        if (!p.getEstado().equals("aberto")) {
            throw new RuntimeException("Nao e possivel remover produtos de um pedido fechado");
        }
        
        boolean removido = false;
        for (int i = 0; i < p.getProdutos().size(); i++) {
            if (p.getProdutos().get(i).getNome().equals(nomeProduto)) {
                p.getProdutos().remove(i);
                removido = true;
                break; 
            }
        }
        
        if (!removido) {
            throw new RuntimeException("Produto nao encontrado");
        }
    }

    public String getAtributoPedido(int numero, String atributo, UsuarioManager um, EmpresaManager em) {
        if (atributo == null || atributo.trim().isEmpty()) throw new RuntimeException("Atributo invalido");
        
        Pedido p = getPedido(numero);
        switch (atributo) {
            case "cliente": return um.getUsuario(p.getIdCliente()).getNome();
            case "empresa": return em.getEmpresa(p.getIdEmpresa()).getNome();
            case "estado": return p.getEstado();
            case "valor": return String.format(Locale.US, "%.2f", p.getValorTotal());
            case "produtos":
                StringBuilder sb = new StringBuilder("{[");
                for (int i = 0; i < p.getProdutos().size(); i++) {
                    sb.append(p.getProdutos().get(i).getNome());
                    if (i < p.getProdutos().size() - 1) sb.append(", ");
                }
                return sb.append("]}").toString();
            default: throw new RuntimeException("Atributo nao existe");
        }
    }

    public void fecharPedido(int numero) {
        getPedido(numero).setEstado("preparando");
    }

    public int getNumeroPedido(int idCliente, int idEmpresa, int indice) {
        int cont = 0;
        for (Pedido p : pedidos) {
            if (p.getIdCliente() == idCliente && p.getIdEmpresa() == idEmpresa) {
                if (cont == indice) return p.getNumero();
                cont++;
            }
        }
        throw new RuntimeException("Pedido nao encontrado");
    }

    private Pedido getPedido(int numero) {
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) return p;
        }
        throw new RuntimeException("Pedido nao encontrado");
    }
}