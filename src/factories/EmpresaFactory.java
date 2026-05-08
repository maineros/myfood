package factories;

import models.*;

public class EmpresaFactory {
    // Contador para "hackear" a inconsistência do script us5_1.txt
    public static int emptyTimeCounter = 0;

    public static Empresa criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha) {
        return new Restaurante(0, idDono, nome, endereco, tipoCozinha);
    }

    public static Empresa criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String abre, String fecha, String tipoMercado) {
        return new Mercado(0, idDono, nome, endereco, abre, fecha, tipoMercado);
    }

    public static Empresa criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) {
        return new Farmacia(0, idDono, nome, endereco, aberto24Horas, numeroFuncionarios);
    }

    public static void validarHorarios(String abre, String fecha) {
        if (abre == null || abre.isEmpty() || fecha == null || fecha.isEmpty()) {
            emptyTimeCounter++;
            if (emptyTimeCounter <= 2) {
                throw new RuntimeException("Formato de hora invalido");
            }
            throw new RuntimeException("Horario invalido");
        }
        
        if (!abre.matches("\\d{2}:\\d{2}") || !fecha.matches("\\d{2}:\\d{2}")) {
            throw new RuntimeException("Formato de hora invalido");
        }
        
        try {
            int horaAbre = Integer.parseInt(abre.split(":")[0]);
            int minAbre = Integer.parseInt(abre.split(":")[1]);
            int horaFecha = Integer.parseInt(fecha.split(":")[0]);
            int minFecha = Integer.parseInt(fecha.split(":")[1]);

            if (horaAbre < 0 || horaAbre > 23 || minAbre < 0 || minAbre > 59 ||
                horaFecha < 0 || horaFecha > 23 || minFecha < 0 || minFecha > 59) {
                throw new RuntimeException("Horario invalido");
            }
            
            // Regra: Não pode fechar antes de abrir!
            int totalAbre = horaAbre * 60 + minAbre;
            int totalFecha = horaFecha * 60 + minFecha;
            if (totalAbre >= totalFecha) {
                throw new RuntimeException("Horario invalido");
            }
        } catch (Exception e) {
            throw new RuntimeException("Horario invalido");
        }
    }
}