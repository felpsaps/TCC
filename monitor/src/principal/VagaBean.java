package principal;


public class VagaBean {
	
	private Integer id;
	private Integer nro;
	private Integer disponibilidade;
	private String usrReservadoId;
	
	public Integer getId() {
		return id;
	}
	public Integer getNro() {
		return nro;
	}
	public Integer getDisponibilidade() {
		return disponibilidade;
	}
	public String getUsrReservadoId() {
		return usrReservadoId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNro(Integer nro) {
		this.nro = nro;
	}
	public void setDisponibilidade(Integer disponibilidade) {
		this.disponibilidade = disponibilidade;
	}
	public void setUsrReservadoId(String usrReservadoId) {
		this.usrReservadoId = usrReservadoId;
	}
}
