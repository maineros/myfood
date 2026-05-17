package managers;

import models.*;
import exceptions.pedido.*;
import exceptions.empresa.EmpresaNaoEncontradaException;
import exceptions.usuario.UsuarioNaoEncontradoException;
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

    /**
     * Cria um novo pedido para um cliente em uma empresa.
     *
     * @return número do pedido criado
     * @throws PedidoException se o cliente for DonoEmpresa
     * @throws PedidoDuplicadoException se já existir pedido em aberto para a mesma empresa
     * @throws UsuarioNaoEncontradoException se o cliente não for encontrado
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public int criarPedido(int idCliente, int idEmpresa, UsuarioManager um, EmpresaManager em)
            throws PedidoException, PedidoDuplicadoException, UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        if (um.getUsuario(idCliente).isDonoEmpresa())
            throw new PedidoException("Dono de empresa nao pode fazer um pedido");
        for (Pedido p : pedidos) {
            if (p.getIdCliente() == idCliente && p.getIdEmpresa() == idEmpresa && p.getEstado().equals("aberto"))
                throw new PedidoDuplicadoException();
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

    /**
     * Adiciona um produto a um pedido em aberto.
     *
     * @throws PedidoException se o pedido não existir, estiver fechado ou o produto não pertencer à empresa
     */
    public void adicionarProduto(int numero, int idProduto, ProdutoManager pm) throws PedidoException {
        // Busca inline para preservar a mensagem exigida pelos testes
        Pedido p = null;
        for (Pedido ped : pedidos) {
            if (ped.getNumero() == numero) { p = ped; break; }
        }
        if (p == null) throw new PedidoException("Nao existe pedido em aberto");
        if (!p.getEstado().equals("aberto")) throw new PedidoException("Nao e possivel adcionar produtos a um pedido fechado");
        Produto prod = pm.getProduto(idProduto);
        if (prod.getIdEmpresa() != p.getIdEmpresa()) throw new PedidoException("O produto nao pertence a essa empresa");
        p.getProdutos().add(prod);
        p.setValorTotal(p.getValorTotal() + prod.getValor());
    }

    /**
     * Remove um produto de um pedido em aberto.
     *
     * @throws PedidoNaoEncontradoException se o pedido não existir
     * @throws PedidoFechadoException se o pedido não estiver em aberto
     * @throws PedidoException se o produto não for encontrado no pedido
     */
    public void removerProduto(int numero, String nomeProduto)
            throws PedidoNaoEncontradoException, PedidoFechadoException, PedidoException {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) throw new PedidoException("Produto invalido");
        Pedido p = getPedido(numero);
        if (!p.getEstado().equals("aberto")) throw new PedidoFechadoException("Nao e possivel remover produtos de um pedido fechado");
        Produto aRemover = null;
        for (Produto prod : p.getProdutos()) {
            if (prod.getNome().equals(nomeProduto)) { aRemover = prod; break; }
        }
        if (aRemover == null) throw new PedidoException("Produto nao encontrado");
        p.getProdutos().remove(aRemover);
        p.setValorTotal(p.getValorTotal() - aRemover.getValor());
    }

    /**
     * Retorna o valor de um atributo de um pedido.
     *
     * @throws PedidoNaoEncontradoException se o pedido não existir
     * @throws PedidoException se o atributo for inválido
     * @throws UsuarioNaoEncontradoException se o cliente não for encontrado
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public String getPedidos(int numero, String atributo, UsuarioManager um, EmpresaManager em)
            throws PedidoNaoEncontradoException, PedidoException, UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        if (atributo == null || atributo.trim().isEmpty()) throw new PedidoException("Atributo invalido");
        Pedido p = getPedido(numero);
        switch (atributo) {
            case "cliente": return um.getUsuario(p.getIdCliente()).getNome();
            case "empresa": return em.getEmpresa(p.getIdEmpresa()).getNome();
            case "estado":  return p.getEstado();
            case "valor":   return String.format(Locale.US, "%.2f", p.getValorTotal());
            case "produtos":
                StringBuilder sb = new StringBuilder("{[");
                for (int i = 0; i < p.getProdutos().size(); i++) {
                    sb.append(p.getProdutos().get(i).getNome());
                    if (i < p.getProdutos().size() - 1) sb.append(", ");
                }
                return sb.append("]}").toString();
            default: throw new PedidoException("Atributo nao existe");
        }
    }

    /**
     * Fecha um pedido (muda estado para "preparando").
     *
     * @throws PedidoNaoEncontradoException se o pedido não existir
     */
    public void fecharPedido(int numero) throws PedidoNaoEncontradoException {
        getPedido(numero).setEstado("preparando");
    }

    /**
     * Libera um pedido para entrega (muda estado para "pronto").
     *
     * @throws PedidoNaoEncontradoException se o pedido não existir
     * @throws PedidoFechadoException se o pedido não estiver em preparação ou já estiver pronto
     */
    public void liberarPedido(int numero) throws PedidoNaoEncontradoException, PedidoFechadoException {
        Pedido p = getPedido(numero);
        if (p.getEstado().equals("pronto")) throw new PedidoFechadoException("Pedido ja liberado");
        if (!p.getEstado().equals("preparando")) throw new PedidoFechadoException("Nao e possivel liberar um produto que nao esta sendo preparado");
        p.setEstado("pronto");
    }

    /**
     * Obtém o número de um pedido pronto para um entregador, priorizando farmácias.
     *
     * @throws PedidoException se o usuário não for Entregador
     * @throws EntregadorSemEmpresaException se o entregador não estiver em nenhuma empresa
     * @throws SemPedidoParaEntregaException se não houver pedido disponível
     * @throws UsuarioNaoEncontradoException se o entregador não for encontrado
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public int obterPedido(int idEntregador, EmpresaManager em, UsuarioManager um)
            throws PedidoException, EntregadorSemEmpresaException, SemPedidoParaEntregaException,
                   UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        if (!um.getUsuario(idEntregador).isEntregador())
            throw new PedidoException("Usuario nao e um entregador");
        if (!em.entregadorTemEmpresa(idEntregador))
            throw new EntregadorSemEmpresaException();
        return buscarPedidoPronto(idEntregador, em);
    }

    /**
     * Sobrecarga sem validação de tipo — uso interno.
     *
     * @throws SemPedidoParaEntregaException se não houver pedido disponível
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public int obterPedido(int idEntregador, EmpresaManager em)
            throws SemPedidoParaEntregaException, EmpresaNaoEncontradaException {
        return buscarPedidoPronto(idEntregador, em);
    }

    /**
     * Retorna o número de pedido de um cliente em uma empresa pelo índice.
     *
     * @throws PedidoNaoEncontradoException se o pedido não for encontrado
     */
    public int getNumeroPedido(int idCliente, int idEmpresa, int indice) throws PedidoNaoEncontradoException {
        int cont = 0;
        for (Pedido p : pedidos) {
            if (p.getIdCliente() == idCliente && p.getIdEmpresa() == idEmpresa) {
                if (cont == indice) return p.getNumero();
                cont++;
            }
        }
        throw new PedidoNaoEncontradoException();
    }

    /**
     * Cria uma entrega para um pedido pronto.
     *
     * @return id da entrega criada
     * @throws PedidoNaoEncontradoException se o pedido não existir
     * @throws PedidoFechadoException se o pedido não estiver pronto
     * @throws PedidoException se o usuário não for Entregador
     * @throws EntregadorOcupadoException se o entregador já estiver em uma entrega
     * @throws UsuarioNaoEncontradoException se o entregador não for encontrado
     */
    public int criarEntrega(int idPedido, int idEntregador, String destino, UsuarioManager um)
            throws PedidoNaoEncontradoException, PedidoFechadoException, PedidoException,
                   EntregadorOcupadoException, UsuarioNaoEncontradoException {
        Pedido p = getPedido(idPedido);
        if (!p.getEstado().equals("pronto")) throw new PedidoFechadoException("Pedido nao esta pronto para entrega");
        if (!um.getUsuario(idEntregador).isEntregador()) throw new PedidoException("Nao e um entregador valido");
        for (Entrega e : entregas) {
            if (e.getIdEntregador() == idEntregador && getPedido(e.getIdPedido()).getEstado().equals("entregando"))
                throw new EntregadorOcupadoException();
        }
        p.setEstado("entregando");
        String enderecoFinal = (destino == null || destino.trim().isEmpty())
                ? um.getUsuario(p.getIdCliente()).getEndereco() : destino;
        Entrega nova = new Entrega(proximoIdEntrega++, idPedido, idEntregador, enderecoFinal);
        entregas.add(nova);
        return nova.getId();
    }

    /**
     * Marca uma entrega como concluída.
     *
     * @throws EntregaNaoEncontradaException se a entrega não for encontrada
     * @throws PedidoNaoEncontradoException se o pedido associado não for encontrado
     */
    public void entregar(int idEntrega) throws EntregaNaoEncontradaException, PedidoNaoEncontradoException {
        Entrega e = getEntregaObj(idEntrega);
        getPedido(e.getIdPedido()).setEstado("entregue");
    }

    /**
     * Retorna o id de entrega associado a um pedido.
     *
     * @throws EntregaNaoEncontradaException se não existir entrega para o pedido
     */
    public int getIdEntrega(int idPedido) throws EntregaNaoEncontradaException {
        for (Entrega e : entregas) {
            if (e.getIdPedido() == idPedido) return e.getId();
        }
        throw new EntregaNaoEncontradaException("Nao existe entrega com esse id");
    }

    /**
     * Retorna o valor de um atributo de uma entrega.
     *
     * @throws EntregaNaoEncontradaException se a entrega não for encontrada
     * @throws PedidoNaoEncontradoException se o pedido associado não for encontrado
     * @throws PedidoException se o atributo for inválido
     * @throws UsuarioNaoEncontradoException se o cliente/entregador não for encontrado
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public String getEntrega(int id, String atributo, UsuarioManager um, EmpresaManager em)
            throws EntregaNaoEncontradaException, PedidoNaoEncontradoException, PedidoException,
                   UsuarioNaoEncontradoException, EmpresaNaoEncontradaException {
        if (atributo == null || atributo.trim().isEmpty()) throw new PedidoException("Atributo invalido");
        Entrega e = getEntregaObj(id);
        Pedido p = getPedido(e.getIdPedido());
        switch (atributo) {
            case "cliente":    return um.getUsuario(p.getIdCliente()).getNome();
            case "empresa":    return em.getEmpresa(p.getIdEmpresa()).getNome();
            case "pedido":     return String.valueOf(p.getNumero());
            case "entregador": return um.getUsuario(e.getIdEntregador()).getNome();
            case "destino":    return e.getDestino();
            case "produtos":   return getPedidos(p.getNumero(), "produtos", um, em);
            default: throw new PedidoException("Atributo nao existe");
        }
    }

    // -------------------------------------------------------------------------
    // Métodos privados auxiliares
    // -------------------------------------------------------------------------

    private int buscarPedidoPronto(int idEntregador, EmpresaManager em)
            throws SemPedidoParaEntregaException, EmpresaNaoEncontradaException {
        Pedido maisAntigoFarmacia = null;
        Pedido maisAntigoOutro = null;
        for (Pedido p : pedidos) {
            if (p.getEstado().equals("pronto") && em.isEntregadorDaEmpresa(p.getIdEmpresa(), idEntregador)) {
                boolean isFarmacia = em.getEmpresa(p.getIdEmpresa()).isFarmacia();
                if (isFarmacia) {
                    if (maisAntigoFarmacia == null || p.getNumero() < maisAntigoFarmacia.getNumero()) maisAntigoFarmacia = p;
                } else {
                    if (maisAntigoOutro == null || p.getNumero() < maisAntigoOutro.getNumero()) maisAntigoOutro = p;
                }
            }
        }
        if (maisAntigoFarmacia != null) return maisAntigoFarmacia.getNumero();
        if (maisAntigoOutro != null) return maisAntigoOutro.getNumero();
        throw new SemPedidoParaEntregaException();
    }

    private Pedido getPedido(int numero) throws PedidoNaoEncontradoException {
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) return p;
        }
        throw new PedidoNaoEncontradoException();
    }

    private Entrega getEntregaObj(int id) throws EntregaNaoEncontradaException {
        for (Entrega e : entregas) {
            if (e.getId() == id) return e;
        }
        throw new EntregaNaoEncontradaException();
    }
}
