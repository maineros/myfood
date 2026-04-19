package managers;

import models.*;
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

    public int criarEmpresa(int idDono, String nome, String endereco, String tipoCozinha, UsuarioManager um) {
        Usuario dono = um.getUsuario(idDono);
        if (!(dono instanceof DonoEmpresa)) {
            throw new RuntimeException("Usuario nao pode criar uma empresa");
        }

        for (Empresa e : empresas) {
            if (e.getNome().equals(nome)) {
                if (e.getIdDono() != idDono) throw new RuntimeException("Empresa com esse nome ja existe");
                if (e.getEndereco().equals(endereco)) throw new RuntimeException("Proibido cadastrar duas empresas com o mesmo nome e local");
            }
        }

        Empresa nova = new Empresa(proximoId++, idDono, nome, endereco, tipoCozinha);
        empresas.add(nova);
        return nova.getId();
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
            case "tipoCozinha": return e.getTipoCozinha();
            case "dono": return um.getUsuario(e.getIdDono()).getNome();
            default: throw new RuntimeException("Atributo invalido");
        }
    }

    public Empresa getEmpresa(int id) {
        for (Empresa e : empresas) {
            if (e.getId() == id) return e;
        }
        throw new RuntimeException("Empresa nao cadastrada");
    }
}