<%@page import="dao.VagasDao"%>
<%@page import="bean.Vaga"%>
<%
	for (Vaga v : new VagasDao().selectVagas()) {
		out.print(v.getDisponibilidade() + "-" + (v.getReservadaPara() == null ? "N" : "S"));
		if (v.getNumero() != 8) {
			out.print(";");
		}
	}
%>