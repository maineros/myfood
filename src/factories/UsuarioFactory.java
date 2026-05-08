package factories;

import models.*;

public class UsuarioFactory {

    public static Usuario criarCliente(String nome, String email, String senha, String endereco) {
        return new Cliente(0, nome, email, senha, endereco);
    }

    public static Usuario criarDonoEmpresa(String nome, String email, String senha, String endereco, String cpf) {
        return new DonoEmpresa(0, nome, email, senha, endereco, cpf);
    }

    public static Usuario criarEntregador(String nome, String email, String senha, String endereco, String veiculo, String placa) {
        if (veiculo == null || veiculo.trim().isEmpty()) throw new RuntimeException("Veiculo invalido");
        if (placa == null || placa.trim().isEmpty()) throw new RuntimeException("Placa invalido"); // Teste exige "invalido" no masculino mesmo
        return new Entregador(0, nome, email, senha, endereco, veiculo, placa);
    }
}
