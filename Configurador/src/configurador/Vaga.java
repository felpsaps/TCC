package configurador;

/**
 *
 * @author Felps
 */
public class Vaga {
    
    private int id;
    private String reservadaPara;
    private int numero;
    private int disponibilidade;
    
    public static final int INDISPONIVEL = 0;
    public static final int DISPONIVEL = 1;
    
    public Vaga(int id, int n, String resPara, int disp) {
        this.id = id;
        numero = n;
        reservadaPara = resPara;
        disponibilidade = disp;        
    }

    
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}


	public int getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(int disp) {
        this.disponibilidade = disp;
    }


    public String getReservadaPara() {
        return reservadaPara;
    }

    public void setReservadaPara(String resP) {
        this.reservadaPara = resP;
    }
    
}
