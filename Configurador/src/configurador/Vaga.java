package configurador;

/**
 *
 * @author Felps
 */
public class Vaga {
    
    private String codigo;
    private String reservadaPara;
    private String nivel;
    private int disponibilidade;
    
    public static final int INDISPONIVEL = 0;
    public static final int DISPONIVEL = 1;
    
    public Vaga(String cod, String n, String resPara, int disp) {
        codigo = cod;
        nivel = n;
        reservadaPara = resPara;
        disponibilidade = disp;        
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String cod) {
        codigo = cod;
    }

    public int getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(int disp) {
        this.disponibilidade = disp;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String n) {
        this.nivel = n;
    }

    public String getReservadaPara() {
        return reservadaPara;
    }

    public void setReservadaPara(String resP) {
        this.reservadaPara = resP;
    }
    
}
