package principal;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuarios {

	private static ListaUsuarios instance;
	private List<FuncionarioBean> funcLista;
	
	public static ListaUsuarios getInstance() {
		if (instance == null) {
			instance = new ListaUsuarios();
		}
		return instance;
	}
	
	private ListaUsuarios() {
		funcLista = new ArrayList<FuncionarioBean>();
	}
	
	public List<FuncionarioBean> getLista() {
		return funcLista;
	}
	
	public FuncionarioBean getPrimeiroFunc() {
		if (!funcLista.isEmpty()) {
			return funcLista.get(0);
		}
		return null;
	}
	
	public void removePrimeiroFunc() {
		if (!funcLista.isEmpty()) {
			funcLista.remove(0);
		}
	}
	
	public void addFunc(FuncionarioBean f) {
		if (funcLista != null && !funcLista.contains(f)) {
			funcLista.add(f);
		}
	}

}
