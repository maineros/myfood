package models;

public class Mercado extends Empresa {
    private String abre;
    private String fecha;
    private String tipoMercado;

    public Mercado() {}

    public Mercado(int id, int idDono, String nome, String endereco, String abre, String fecha, String tipoMercado) {
        super(id, idDono, nome, endereco);
        this.abre = abre;
        this.fecha = fecha;
        this.tipoMercado = tipoMercado;
    }

    public String getAbre() { return abre; }
    public void setAbre(String abre) { this.abre = abre; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getTipoMercado() { return tipoMercado; }
    public void setTipoMercado(String tipoMercado) { this.tipoMercado = tipoMercado; }
}
