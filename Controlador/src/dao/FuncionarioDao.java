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
        	sql.append(" SELECT to_char(reg_data, 'dd/MM/yy hh24:MI:ss') as data, registro.* \n")
        	   .append(" FROM \n")
        	   .append("	registro \n")
        	   .append(" WHERE \n")
        	   .append("	reg_usr_cod = ? \n")
        	   .append(" ORDER BY \n")
        	   .append("	reg_data DESC ");
        	
        	ps = getCon().prepareStatement(sql.toString());
        	ps.setString(1, f.getCodigo());
        	
        	rs1 = ps.executeQuery();
        	
        	
        	
        	if (rs1.next()) {
        		// ADICIONAR REGISTRO DE ENTRADA OU SAIDA
            	sql.setLength(0);
            	sql.append(" INSERT INTO \n")
         	   	   .append(" 	registro ( \n")
         	   	   .append("		reg_usr_cod, \n")
         	   	   .append("		reg_tipo, \n")
         	   	   .append("		reg_data \n");
         	   	   if ("Entrada".equals(rs1.getString("reg_tipo"))) {
         	   		sql.append("		,reg_permanencia \n");
         	   	   }
         	   	sql.append(" 	) \n")
         	   	   .append(" VALUES ( \n")
         	   	   .append(" 	?, \n")
         	   	   .append(" 	?, \n")
         	   	   .append(" 	now() \n");
         	   	   if ("Entrada".equals(rs1.getString("reg_tipo"))) {
         	   		   sql.append(" 	,now() - to_timestamp('" + rs1.getString("data") +"', 'dd/MM/yy hh24:mi:ss') \n");
         	   	   }
      	   	   	   sql.append(" 	) \n");        	

            	ps = getCon().prepareStatement(sql.toString());
            	ps.setString(1, f.getCodigo());
        		
        		// VERIFICA SE O ULTIMO REGISTRO EH DE ENTRADA OU SAIDA
        		if ("Saída".equals(rs1.getString("reg_tipo"))) {
        			/* ADICIONA O FUNCIONARIO NA LISTA DE FUNCIONARIO QUE AINDA NAO ESTACIONARAM */
                	ListaUsuarios.getInstance().addFunc(f);
                	
                	ps.setString(2, "Entrada");
        		} else {
                	ps.setString(2, "Saída");
        		}
        		
        	} else {
        		// AINDA NAO EXISTE NENHUM REGISTRO
        		// ENTAO ESTA ENTRANDO
        		/* ADICIONA O FUNCIONARIO NA LISTA DE FUNCIONARIO QUE AINDA NAO ESTACIONARAM */
            	ListaUsuarios.getInstance().addFunc(f);
            	
            	ps.setString(1, "Entrada");
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