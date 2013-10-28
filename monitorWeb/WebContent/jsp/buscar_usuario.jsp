<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="dao.VagasDao"%>
<%
int vaga = VagasDao.selectVagaByCod(request.getParameter("usr_codigo"));
if (vaga == -1) {
	out.print("Desculpe, seu veículo não foi encontrado."); 
} else {
	out.print("Seu veículo se encontra na vaga " + vaga + "!");	
}
%>