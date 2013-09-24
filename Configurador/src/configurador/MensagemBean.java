package configurador;

public class MensagemBean {
	  
	private Integer id;
	private String toaster;
	private String lida;
	private String excluida;
	private Integer vaga;
	private Funcionario usrReservado;
	private Funcionario usrEstacionado;
	private String data;
	private boolean checado = false;
	
	public void setChecado() {
		if (checado) {
			checado = false;
			return;
		}
		checado = true;
	}
	public boolean getChecado() {
		return checado;
	}
	public Integer getId() {
		return id;
	}
	public String getToaster() {
		return toaster;
	}
	public String getLida() {
		return lida;
	}
	public String getExcluida() {
		return excluida;
	}
	public Integer getVaga() {
		return vaga;
	}
	public Funcionario getUsrReservado() {
		return usrReservado;
	}
	public Funcionario getUsrEstacionado() {
		return usrEstacionado;
	}
	public String getData() {
		return data;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setToaster(String toaster) {
		this.toaster = toaster;
	}
	public void setLida(String lida) {
		this.lida = lida;
	}
	public void setExcluida(String excluida) {
		this.excluida = excluida;
	}
	public void setVaga(Integer vaga) {
		this.vaga = vaga;
	}
	public void setUsrReservado(Funcionario usrReservado) {
		this.usrReservado = usrReservado;
	}
	public void setUsrEstacionado(Funcionario usrEstacionado) {
		this.usrEstacionado = usrEstacionado;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public boolean equals(Object msg) {
		if (((MensagemBean)msg).getId().equals(getId())) {
			return true;
		}
		return false;
	}

}
