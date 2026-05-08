package managers;

import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PedidoManager {
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Entrega> entregas = new ArrayList<>();
    private int proximoNumeroPedido = 1;
    private int proximoIdEntrega = 1;
    private final String ARQUIVO_PEDIDOS = "data/pedidos.xml";
    private final String ARQUIVO_ENTREGAS = "data/entregas.xml";

    @SuppressWarnings("unchecked")
    public PedidoManager() {
        List<Pedido> pCarregados = (List<Pedido>) PersistenceManager.carregar(ARQUIVO_PEDIDOS);
        if (pCarregados != null) {
            this.pedidos = pCarregados;
            for (Pedido p : pedidos) if (p.getNumero() >= proximoNumeroPedido) proximoNumeroPedido = p.getNumero() + 1;
        }
        List<Entrega> eCarregadas = (List<Entrega>) PersistenceManager.carregar(ARQUIVO_ENTREGAS);
        if (eCarregadas != null) {
            this.entregas = eCarregadas;
            for (Entrega e : entregas) if (e.getId() >= proximoIdEntrega) proximoIdEntrega = e.getId() + 1;
        }
    }

    public void salvarDados() {
        PersistenceManager.salvar(this.pedidos, ARQUIVO_PEDIDOS);
        PersistenceManager.salvar(this.entregas, ARQUIVO_ENTREGAS);
    }

    public int criarPedido(int idCliente, int idEmpresa, UsuarioManager um, EmpresaManager em) {
        if (um.getUsuario(idCliente) instanceof DonoEmpresa) throw new RuntimeException("Dono de empresa nao pode fazer um pedido");
        
        for (Pedido p : pedidos) {
            if (p.getIdCliente() == idCliente && p.getIdEmpresa() == idEmpresa && p.getEstado().equals("aberto")) {
                throw new RuntimeException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }
        
        Pedido novo = new Pedido();
        novo.setNumero(proximoNumeroPedido++);
        novo.setIdCliente(idCliente);
        novo.setIdEmpresa(idEmpresa);
        novo.setEstado("aberto");
        novo.setProdutos(new ArrayList<>());
        novo.setValorTotal(0);
        pedidos.add(novo);
        return novo.getNumero();
    }

    public void adicionarProduto(int numero, int idProduto, ProdutoManager pm) {
        Pedido p = null;
        for (Pedido ped : pedidos) {
            if (ped.getNumero() == numero) p = ped;
        }
        if (p == null) throw new RuntimeException("Nao existe pedido em aberto");
        if (!p.getEstado().equals("aberto")) throw new RuntimeException("Nao e possivel adcionar produtos a um pedido fechado");
        
        Produto prod = pm.getProduto(idProduto);
        if (prod.getIdEmpresa() != p.getIdEmpresa()) throw new RuntimeException("O produto nao pertence a essa empresa");
        
        p.getProdutos().add(prod);
        p.setValorTotal(p.getValorTotal() + prod.getValor());
    }

    public void removerProduto(int numero, String nomeProduto) {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) throw new RuntimeException("Produto invalido");
        Pedido p = getPedido(numero);
        if (!p.getEstado().equals("aberto")) throw new RuntimeException("Nao e possivel remover produtos de um pedido fechado");
        
        Produto aRemover = null;
        for (Produto prod : p.getProdutos()) {
            if (prod.getNome().equals(nomeProduto)) {
                aRemover = prod;
                break;
            }
        }
        if (aRemover == null) throw new RuntimeException("Produto nao encontrado");
        p.getProdutos().remove(aRemover);
        p.setValorTotal(p.getValorTotal() - aRemover.getValor());
    }

    public String getPedidos(int numero, String atributo, UsuarioManager um, EmpresaManager em) {
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

    public void liberarPedido(int numero) {
        Pedido p = getPedido(numero);
        if (p.getEstado().equals("pronto")) throw new RuntimeException("Pedido ja liberado");
        if (!p.getEstado().equals("preparando")) throw new RuntimeException("Nao e possivel liberar um produto que nao esta sendo preparado");
        p.setEstado("pronto");
    }

    public int obterPedido(int idEntregador, EmpresaManager em, UsuarioManager um) {
        Usuario u = um.getUsuario(idEntregador);
        if (!(u instanceof Entregador)) throw new RuntimeException("Usuario nao e um entregador");
        
        // Exatamente a mensagem de erro bizarra do teste
        if (!em.entregadorTemEmpresa(idEntregador)) throw new RuntimeException("Entregador nao estar em nenhuma empresa.");

        Pedido maisAntigoFarmacia = null;
        Pedido maisAntigoOutro = null;

        for (Pedido p : pedidos) {
            if (p.getEstado().equals("pronto") && em.isEntregadorDaEmpresa(p.getIdEmpresa(), idEntregador)) {
                boolean isFarmacia = em.getEmpresa(p.getIdEmpresa()) instanceof Farmacia;
                if (isFarmacia) {
                    if (maisAntigoFarmacia == null || p.getNumero() < maisAntigoFarmacia.getNumero()) maisAntigoFarmacia = p;
                } else {
                    if (maisAntigoOutro == null || p.getNumero() < maisAntigoOutro.getNumero()) maisAntigoOutro = p;
                }
            }
        }
        if (maisAntigoFarmacia != null) return maisAntigoFarmacia.getNumero();
        if (maisAntigoOutro != null) return maisAntigoOutro.getNumero();
        
        throw new RuntimeException("Nao existe pedido para entrega");
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

    public int obterPedido(int idEntregador, EmpresaManager em) {
        Pedido maisAntigoFarmacia = null;
        Pedido maisAntigoOutro = null;

        for (Pedido p : pedidos) {
            if (p.getEstado().equals("pronto") && em.isEntregadorDaEmpresa(p.getIdEmpresa(), idEntregador)) {
                boolean isFarmacia = em.getEmpresa(p.getIdEmpresa()) instanceof Farmacia;
                if (isFarmacia) {
                    if (maisAntigoFarmacia == null || p.getNumero() < maisAntigoFarmacia.getNumero()) maisAntigoFarmacia = p;
                } else {
                    if (maisAntigoOutro == null || p.getNumero() < maisAntigoOutro.getNumero()) maisAntigoOutro = p;
                }
            }
        }
        if (maisAntigoFarmacia != null) return maisAntigoFarmacia.getNumero();
        if (maisAntigoOutro != null) return maisAntigoOutro.getNumero();
        
        throw new RuntimeException("Nao existe pedido para entrega");
    }

    public int criarEntrega(int idPedido, int idEntregador, String destino, UsuarioManager um) {
        Pedido p = getPedido(idPedido);
        if (!p.getEstado().equals("pronto")) throw new RuntimeException("Pedido nao esta pronto para entrega");
        
        Usuario u = um.getUsuario(idEntregador);
        if (!(u instanceof Entregador)) throw new RuntimeException("Nao e um entregador valido");
        
        for (Entrega e : entregas) {
            if (e.getIdEntregador() == idEntregador && getPedido(e.getIdPedido()).getEstado().equals("entregando")) {
                throw new RuntimeException("Entregador ainda em entrega");
            }
        }

        p.setEstado("entregando");
        
        String enderecoFinal = (destino == null || destino.trim().isEmpty()) ? um.getUsuario(p.getIdCliente()).getEndereco() : destino;
        
        Entrega nova = new Entrega(proximoIdEntrega++, idPedido, idEntregador, enderecoFinal);
        entregas.add(nova);
        return nova.getId();
    }

    public void entregar(int idEntrega) {
        Entrega e = getEntregaObj(idEntrega);
        getPedido(e.getIdPedido()).setEstado("entregue");
    }

    public int getIdEntrega(int idPedido) {
        for (Entrega e : entregas) {
            if (e.getIdPedido() == idPedido) return e.getId();
        }
        throw new RuntimeException("Nao existe entrega com esse id");
    }

    public String getEntrega(int id, String atributo, UsuarioManager um, EmpresaManager em) {
        if (atributo == null || atributo.trim().isEmpty()) throw new RuntimeException("Atributo invalido");
        Entrega e = getEntregaObj(id);
        Pedido p = getPedido(e.getIdPedido());

        switch (atributo) {
            case "cliente": return um.getUsuario(p.getIdCliente()).getNome();
            case "empresa": return em.getEmpresa(p.getIdEmpresa()).getNome();
            case "pedido": return String.valueOf(p.getNumero());
            case "entregador": return um.getUsuario(e.getIdEntregador()).getNome();
            case "destino": return e.getDestino();
            case "produtos": return getPedidos(p.getNumero(), "produtos", um, em);
            default: throw new RuntimeException("Atributo nao existe"); // Atualizado
        }
    }

    private Pedido getPedido(int numero) {
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) return p;
        }
        throw new RuntimeException("Pedido nao encontrado");
    }

    private Entrega getEntregaObj(int id) {
        for (Entrega e : entregas) {
            if (e.getId() == id) return e;
        }
        throw new RuntimeException("Nao existe nada para ser entregue com esse id");
    }
}