package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import GUI.TelaPrincipal;

import principal.Criptografia;
import principal.FuncionarioBean;
import principal.ListaUsuarios;
import principal.ServidorSMTP;

/**
 *
 * @author Tiago
 */
public class FuncionarioDao extends Dao {
     
    
	public ServidorSMTP getServidor() throws SQLException {
        ServidorSMTP servidor = null;
        String comandoSelect = "select * from servidorsmtp";

        estabeleceConexao();
        ResultSet rs = comando.executeQuery(comandoSelect);
         
        if (rs.next()) {
            servidor = new ServidorSMTP(rs.getString("nome"), rs.getString("endereco"), 
                    getSenhaDescriptografada(rs.getString("senha")), rs.getString("porta"), rs.getString("email"));
        }
        fechaConexao();
        return servidor;
    }
	
	public FuncionarioBean buscarPorCodigo(String cod) throws SQLException {
		FuncionarioBean f = null;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		estabeleceConexao();
		
		sql.append(" SELECT * from funcionario where usr_codigo = ?");
		ps = getCon().prepareStatement(sql.toString());
		ps.setString(1, cod);
		
		rs = ps.executeQuery();
		
		if (rs.next()) {
			f = new FuncionarioBean(rs.getString("usr_codigo"), rs.getString("usr_nome"), 
							rs.getString("usr_email"), rs.getString("usr_celular"), rs.getInt("usr_tipo"));
		}	
		
		if (ps != null) {
			ps.close();
		}
		if (rs != null) {
			rs.close();
		}
        fechaConexao();
        
        return f;
	}
	
    public void selectLoginESenha(String login, TelaPrincipal pai) throws SQLException
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
                	
                	pai.lbl.setText("Bem vindo " + f.getNome() + "!");
        		} else {
                	ps.setString(2, "Saída");
                	/* VERIFICA SE QUEM ESTA SAINDO AINDA ESTA NA LISTA DE FUNCIONARIOS 
                	 * NAO ESTACIONADOS. SE ESTIVER REMOVE*/
                	if (ListaUsuarios.getInstance().getLista().contains(f)) {
                		ListaUsuarios.getInstance().getLista().remove(f);
                	}
                	pai.lbl.setText("Volte Sempre!");

        			new VagaDAO().insertEstatistica(new VagaDAO().selectVagas());
        		}
        		
        	} else {
        		// AINDA NAO EXISTE NENHUM REGISTRO
        		// ENTAO ESTA ENTRANDO
        		/* ADICIONA O FUNCIONARIO NA LISTA DE FUNCIONARIO QUE AINDA NAO ESTACIONARAM */
            	ListaUsuarios.getInstance().addFunc(f);
            	
            	// ADICIONAR REGISTRO DE ENTRADA OU SAIDA
            	sql.setLength(0);
            	sql.append(" INSERT INTO \n")
         	   	   .append(" 	registro ( \n")
         	   	   .append("		reg_usr_cod, \n")
         	   	   .append("		reg_tipo, \n")
         	   	   .append("		reg_data \n");
         	   	sql.append(" 	) \n")
         	   	   .append(" VALUES ( \n")
         	   	   .append(" 	?, \n")
         	   	   .append(" 	?, \n")
         	   	   .append(" 	now() \n");
      	   	   	sql.append(" 	) \n");        	

            	ps = getCon().prepareStatement(sql.toString());
            	ps.setString(1, f.getCodigo());
            	
            	ps.setString(2, "Entrada");
            	pai.lbl.setText("Bem vindo " + f.getNome() + "!");
        	}     
        	ps.executeUpdate();
        	
            System.out.println("login: " + rs.getString(1) + "  " + rs.getString(2));
            System.out.println("Autenticação realizada");
        } else {
            System.out.println("Funcionário não encontrado");
        	pai.lbl.setText("Funcionário não encontrado!");
        }
        pai.lbl.repaint();
        pai.lbl.repaint(100);
        fechaConexao();      
    }
    
    private static String getSenhaDescriptografada(String senha) {
        return Criptografia.descriptografar(senha);
    }
    
}