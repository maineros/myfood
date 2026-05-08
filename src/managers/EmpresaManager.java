package managers;

import models.*;
import factories.EmpresaFactory;
import java.util.ArrayList;
import java.util.List;

public class EmpresaManager {
    private List<Empresa> empresas = new ArrayList<>();
    private int proximoId = 1;
    private final String ARQUIVO = "data/empresas.xml";

    @SuppressWarnings("unchecked")
    public EmpresaManager() {
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

    private void validarDadosBasicos(String tipo, String nome, String endereco) {
        if (tipo == null || tipo.trim().isEmpty()) throw new RuntimeException("Tipo de empresa invalido");
        if (nome == null || nome.trim().isEmpty()) throw new RuntimeException("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new RuntimeException("Endereco da empresa invalido");
    }

    // user story 2
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha, UsuarioManager um) {
        validarDadosBasicos(tipoEmpresa, nome, endereco);
        validarRegrasComuns(idDono, nome, endereco, um);
        Empresa e = EmpresaFactory.criarEmpresa(tipoEmpresa, idDono, nome, endereco, tipoCozinha);
        return persistirEmpresa(e);
    }

    // user story 5
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String abre, String fecha, String tipoMercado, UsuarioManager um) {
        validarDadosBasicos(tipoEmpresa, nome, endereco);
        EmpresaFactory.validarHorarios(abre, fecha); // Atualizado, sem o true!
        if (tipoMercado == null || tipoMercado.trim().isEmpty()) throw new RuntimeException("Tipo de mercado invalido");
        validarRegrasComuns(idDono, nome, endereco, um);
        Empresa e = EmpresaFactory.criarEmpresa(tipoEmpresa, idDono, nome, endereco, abre, fecha, tipoMercado);
        return persistirEmpresa(e);
    }

    // US 6: Criar Farmácia
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios, UsuarioManager um) {
        validarDadosBasicos(tipoEmpresa, nome, endereco);
        validarRegrasComuns(idDono, nome, endereco, um);
        Empresa e = EmpresaFactory.criarEmpresa(tipoEmpresa, idDono, nome, endereco, aberto24Horas, numeroFuncionarios);
        return persistirEmpresa(e);
    }

    public void alterarFuncionamento(int mercadoId, String abre, String fecha) {
        Empresa e = getEmpresa(mercadoId);
        if (!(e instanceof Mercado)) throw new RuntimeException("Nao e um mercado valido");
        EmpresaFactory.validarHorarios(abre, fecha); // Atualizado, sem o false!
        Mercado m = (Mercado) e;
        m.setAbre(abre);
        m.setFecha(fecha);
    }

    public String getEmpresasDoUsuario(int idDono, UsuarioManager um) {
        Usuario dono = um.getUsuario(idDono);
        if (!(dono instanceof DonoEmpresa)) throw new RuntimeException("Usuario nao pode criar uma empresa");

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

    public void cadastrarEntregador(int idEmpresa, int idEntregador, UsuarioManager um) {
        Usuario u = um.getUsuario(idEntregador);
        if (!(u instanceof Entregador)) throw new RuntimeException("Usuario nao e um entregador");
        
        Empresa e = getEmpresa(idEmpresa);
        if (e.getEntregadores() == null) e.setEntregadores(new ArrayList<>());
        if (!e.getEntregadores().contains(idEntregador)) e.getEntregadores().add(idEntregador);
    }

    public String getEntregadores(int idEmpresa, UsuarioManager um) {
        Empresa e = getEmpresa(idEmpresa);
        StringBuilder sb = new StringBuilder("{[");
        boolean primeiro = true;
        if (e.getEntregadores() != null) {
            for (int idEntregador : e.getEntregadores()) {
                if (!primeiro) sb.append(", ");
                sb.append(um.getUsuario(idEntregador).getEmail());
                primeiro = false;
            }
        }
        return sb.append("]}").toString();
    }

    public String getEmpresasDoEntregador(int idEntregador, UsuarioManager um) {
        Usuario u = um.getUsuario(idEntregador);
        if (!(u instanceof Entregador)) throw new RuntimeException("Usuario nao e um entregador");

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

    public boolean isEntregadorDaEmpresa(int idEmpresa, int idEntregador) {
        Empresa e = getEmpresa(idEmpresa);
        return e.getEntregadores() != null && e.getEntregadores().contains(idEntregador);
    }

    public boolean entregadorTemEmpresa(int idEntregador) {
        for (Empresa e : empresas) {
            if (e.getEntregadores() != null && e.getEntregadores().contains(idEntregador)) return true;
        }
        return false;
    }

    public int getIdEmpresa(int idDono, String nome, int indice) {
        if (nome == null || nome.trim().isEmpty()) throw new RuntimeException("Nome invalido");
        if (indice < 0) throw new RuntimeException("Indice invalido");
        int cont = 0;
        for (Empresa e : empresas) {
            if (e.getIdDono() == idDono && e.getNome().equals(nome)) {
                if (cont == indice) return e.getId();
                cont++;
            }
        }
        if (cont == 0) throw new RuntimeException("Nao existe empresa com esse nome");
        throw new RuntimeException("Indice maior que o esperado");
    }

    public String getAtributoEmpresa(int id, String atributo, UsuarioManager um) {
        Empresa e = getEmpresa(id);
        if (atributo == null || atributo.trim().isEmpty()) throw new RuntimeException("Atributo invalido");
        
        switch (atributo) {
            case "nome": return e.getNome();
            case "endereco": return e.getEndereco();
            case "dono": return um.getUsuario(e.getIdDono()).getNome();
            case "tipoCozinha": 
                if (e instanceof Restaurante) return ((Restaurante)e).getTipoCozinha();
                throw new RuntimeException("Atributo invalido");
            case "abre":
                if (e instanceof Mercado) return ((Mercado)e).getAbre();
                throw new RuntimeException("Atributo invalido");
            case "fecha":
                if (e instanceof Mercado) return ((Mercado)e).getFecha();
                throw new RuntimeException("Atributo invalido");
            case "tipoMercado":
                if (e instanceof Mercado) return ((Mercado)e).getTipoMercado();
                throw new RuntimeException("Atributo invalido");
            case "aberto24Horas":
                if (e instanceof Farmacia) return String.valueOf(((Farmacia)e).isAberto24Horas());
                throw new RuntimeException("Atributo invalido");
            case "numeroFuncionarios":
                if (e instanceof Farmacia) return String.valueOf(((Farmacia)e).getNumeroFuncionarios());
                throw new RuntimeException("Atributo invalido");
            default: throw new RuntimeException("Atributo invalido");
        }
    }

    public Empresa getEmpresa(int id) {
        for (Empresa e : empresas) {
            if (e.getId() == id) return e;
        }
        throw new RuntimeException("Empresa nao cadastrada");
    }

    private void validarRegrasComuns(int idDono, String nome, String endereco, UsuarioManager um) {
        Usuario dono = um.getUsuario(idDono);
        if (!(dono instanceof DonoEmpresa)) throw new RuntimeException("Usuario nao pode criar uma empresa");
        for (Empresa e : empresas) {
            if (e.getNome().equals(nome)) {
                if (e.getIdDono() != idDono) throw new RuntimeException("Empresa com esse nome ja existe");
                if (e.getEndereco().equals(endereco)) throw new RuntimeException("Proibido cadastrar duas empresas com o mesmo nome e local");
            }
        }
    }

    private int persistirEmpresa(Empresa e) {
        e.setId(proximoId++);
        empresas.add(e);
        return e.getId();
    }
}