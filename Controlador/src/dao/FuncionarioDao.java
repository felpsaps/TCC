package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.Date;
import java.util.GregorianCalendar;

import principal.FuncionarioBean;
import principal.ListaUsuarios;

/**
 *
 * @author Tiago
 */
public class FuncionarioDao extends Dao {
     
    
    public void selectLoginESenha(String login) throws SQLException
    {
        String comandoSelect = String.format("select* from funcionario where usr_codigo='%s'",
                                              login);

        estabeleceConexao();
        ResultSet rs = comando.executeQuery(comandoSelect);
        if (rs.next()) {
        	FuncionarioBean f = new FuncionarioBean(rs.getString("usr_codigo"), rs.getString("usr_nome"), 
        											rs.getString("usr_email"), rs.getString("usr_celular"), rs.getInt("usr_tipo"));
        	
        	/* PRIMEIRO VERIFICA SE FUNCIONARIO ESTA ENTRANDO OU SAINDO*/
        	StringBuilder sql = new StringBuilder();
        	PreparedStatement ps = null;
        	ResultSet rs1 = null;
        	
        	// PEGA O ULTIMO REGISTRO DELE
        	sql.append(" SELECT * \n")
        	   .append(" FROM \n")
        	   .append("	registro \n")
        	   .append(" WHERE \n")
        	   .append("	reg_usr_cod = ? \n")
        	   .append(" ORDER BY \n")
        	   .append("	reg_data DESC ");
        	
        	ps = getCon().prepareStatement(sql.toString());
        	ps.setString(1, f.getCodigo());
        	
        	rs1 = ps.executeQuery();
        	
        	// ADICIONAR REGISTRO DE ENTRADA OU SAIDA
        	sql.setLength(0);
        	sql.append(" INSERT INTO \n")
     	   	   .append(" 	registro ( \n")
     	   	   .append("		reg_usr_cod, \n")
     	   	   .append("		reg_tipo, \n")
     	   	   .append("		reg_data \n")
     	   	   //.append("		reg_permanencia \n")
     	   	   .append(" 	) \n")
     	   	   .append(" VALUES ( ")
     	   	   .append(" 	?, ")
     	   	   .append(" 	?, ")
     	   	   .append(" 	now()) ");
     	   	   //.append(" 	?) ");        	

        	ps = getCon().prepareStatement(sql.toString());
        	ps.setString(1, f.getCodigo());
        	
        	if (rs1.next()) {
        		// VERIFICA SE O ULTIMO REGISTRO EH DE ENTRADA OU SAIDA
        		if ("Saída".equals(rs1.getString("reg_tipo"))) {
        			/* ADICIONA O FUNCIONARIO NA LISTA DE FUNCIONARIO QUE AINDA NAO ESTACIONARAM */
                	ListaUsuarios.getInstance().addFunc(f);
                	
                	ps.setString(2, "Entrada");
                	//ps.setNull(3, Types.NULL);
        		} else {
                	ps.setString(2, "Saída");
                	//ps.setTime(3, new Time(rs1.getDate("reg_data").getTime() - new GregorianCalendar().getTimeInMillis()));        			
        		}
        		
        	} else {
        		// AINDA NAO EXISTE NENHUM REGISTRO
        		// ENTAO ESTA ENTRANDO
        		/* ADICIONA O FUNCIONARIO NA LISTA DE FUNCIONARIO QUE AINDA NAO ESTACIONARAM */
            	ListaUsuarios.getInstance().addFunc(f);
            	
            	ps.setString(1, "Entrada");
            	ps.setNull(3, Types.NULL);
        	}        	
        	ps.executeUpdate();
        	
            System.out.println("login: " + rs.getString(1) + "  " + rs.getString(2));
            System.out.println("Autenticação realizada");
        } else {
            System.out.println("Funcionário não encontrado");
        }
        fechaConexao();      
    }
    
}