package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.Criptografia;
import configurador.Funcionario;
import configurador.MensagemBean;
import excessoes.FuncionarioDaoException;
import gui.telaPrincipal.TelaPrincipal;

/**
 *
 * @author Felps
 */
public class FuncionarioDao extends Dao {

    /**
     * Insere o funcionario na base de dados
     * @throws SQLException
     */
    public static void insere(Funcionario func)
    {
    	PreparedStatement ps = null;
        try {
        	StringBuilder comandoInsert = new StringBuilder();
        	estabeleceConexao();
            comandoInsert.append(" INSERT INTO \n")
            			 .append(" 		funcionario (\n")
            			 .append(" 			usr_codigo,\n")
            			 .append(" 			usr_nome,\n")
            			 .append(" 			usr_email,\n")
            			 .append(" 			usr_celular,\n")
            			 .append(" 			usr_tipo,")
            			 .append(" 			usr_login,\n")
            			 .append(" 			usr_senha\n")
            			 .append(" 		)\n")
            			 .append(" 		VALUES(\n")
            			 .append(" 			?,?,?,?,?,?,?\n")
            			 .append(" 		)\n");
            
            ps = getCon().prepareStatement(comandoInsert.toString());
            ps.setString(1, func.getCodigo());
            ps.setString(2, func.getNome());
            ps.setString(3, func.getEmail());
            ps.setString(4, func.getCelular());
            ps.setInt(5, func.getTipo());
            ps.setString(6, getSenhaCriptografada(func.getLogin()));
            ps.setString(7, getSenhaCriptografada(func.getSenha()));

            
            ps.executeUpdate();
            getCon().commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	if (ps != null) {
        		try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
        	}
        	try {
				fechaConexao();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
    }

    /**
     * Apaga o funcionario da base de dados
     * @throws SQLException
     */
    public static void apaga(String codigo)
    {
        try {
            String comandoRemove;

            comandoRemove = String.format("DELETE FROM `funcionario` "
                    + "WHERE (`usr_codigo`= '%s')",
                    codigo);

            estabeleceConexao();
            comando.executeUpdate(comandoRemove);
            getCon().commit();
            fechaConexao();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Atualiza os dados do cliente na base de dados
     * @throws SQLException
     */
    public static void atualiza(Funcionario func)
    {
        try{
            String comandoAtualiza;

            comandoAtualiza = String.format("UPDATE funcionario SET usr_nome='%s', "
                    + "usr_email='%s', usr_celular='%s', usr_tipo='%d', usr_login='%s', "
                    + "usr_senha='%s' WHERE usr_codigo = '%s'",
                    func.getNome(), func.getEmail(), func.getCelular(), func.getTipo(), 
                    getSenhaCriptografada(func.getLogin()), getSenhaCriptografada(func.getSenha()), 
                    func.getCodigo());


            estabeleceConexao();
            comando.executeUpdate(comandoAtualiza);
            getCon().commit();
            fechaConexao();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Funcionario selectPorCodigo(String cod)  {
         try {
             Funcionario func = null;
             String comandoSelect = String.format("select * from funcionario where usr_codigo='" + cod + "'");

             estabeleceConexao();
             ResultSet rs = comando.executeQuery(comandoSelect);

             if (rs.next()) {
                 func = new Funcionario(rs.getString(1), rs.getString(2), rs.getString(3),
                         rs.getString(4), rs.getInt(5), getSenhaDescriptografada(rs.getString("usr_login") == null ? "" : rs.getString("usr_login")),
                         getSenhaDescriptografada(rs.getString("usr_senha") == null ? "" : rs.getString("usr_senha")));
             }
             fechaConexao();
             return func;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return null;
         }
     }
    
     public static List<Funcionario> selectTodosFuncionarios()  {
         try {
             List<Funcionario> func = new ArrayList<Funcionario>(50);
             String comandoSelect = String.format("select * from funcionario order by usr_tipo");

             estabeleceConexao();
             ResultSet rs = comando.executeQuery(comandoSelect);

             while (rs.next()) {
                 func.add(new Funcionario(rs.getString(1), rs.getString(2), rs.getString(3),
                         rs.getString(4), rs.getInt(5)));
             }
             fechaConexao();
             return func;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return null;
         }
     }
     
     public static List<Funcionario> selectLike(String nome) {
         try {
             List<Funcionario> func = new ArrayList<Funcionario>(50);
             String charactere = "%";
             String comandoSelect = "select * from funcionario where usr_nome ILIKE '" + nome
                     + charactere + "'";

             estabeleceConexao();
             ResultSet rs = comando.executeQuery(comandoSelect);

             while (rs.next()) {
                 func.add(new Funcionario(rs.getString(1), rs.getString(2), rs.getString(3),
                         rs.getString(4), rs.getInt(5)));
             }
             fechaConexao();
             return func;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return null;
         }

}
    
    public static Funcionario selectPorEmail(String email) throws SQLException, FuncionarioDaoException {
        Funcionario func;
        String comandoSelect = String.format("select * from funcionario where usr_email='%s'",
                                              email);
        
        estabeleceConexao();
        ResultSet rs = comando.executeQuery(comandoSelect);
        if (rs.next()) {
            func = new Funcionario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                   rs.getInt(5), rs.getString(6), rs.getString(7));
        } else {
            fechaConexao();
            throw new FuncionarioDaoException("Email Não Cadastrado!");
        }
        fechaConexao();
        return func;
    }
    
    /**
     * Seleciona um funcionário (Administrador ou Proprietário) a partir de seu login
     * @param login
     *              Login do Funcionário
     * @return
     *              Retorna um funcionário com todas suas informações
     * @throws SQLException 
     */
    public static Funcionario selectLoginESenha(String login, String senha) throws SQLException, FuncionarioDaoException
    {
        Funcionario func;
        String comandoSelect = String.format("select * from funcionario where usr_login='%s'",
                                              getSenhaCriptografada(login));

        estabeleceConexao();
        ResultSet rs = comando.executeQuery(comandoSelect);
        if (rs.next()) {
            if (!rs.getString(7).equals(getSenhaCriptografada(senha))) {
                fechaConexao();
                throw new FuncionarioDaoException("Login ou Senha Inválida!");
            }
            func = new Funcionario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                   rs.getInt(5), rs.getString(6), rs.getString(7));
        } else {
            fechaConexao();
            throw new FuncionarioDaoException("Login ou Senha Inválida!");
        } 
        
        fechaConexao();
        return func;
    }
    
    public boolean selectVagaNaoAltorizadaToaster(TelaPrincipal pai) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			estabeleceConexao();
			
			sql.append(" SELECT COUNT(*) FROM ");
			sql.append(" 	estacionamento_nao_autorizado  ");
			sql.append(" WHERE ena_lida = 'N' ");
			
			ps = getCon().prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				pai.getBtnMsgs().setText("Mensagens("+rs.getInt(1)+")");
			}
			ps.close();
			rs.close();
			
			sql.setLength(0);
			sql.append(" SELECT * FROM ");
			sql.append(" 	estacionamento_nao_autorizado  ");
			sql.append(" WHERE ena_toaster = 'N' ");
			
			ps = getCon().prepareStatement(sql.toString());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				sql.setLength(0);
				sql.append(" UPDATE ");
				sql.append(" 	estacionamento_nao_autorizado SET ");
				sql.append(" 		ena_toaster = 'S' ");
				sql.append(" WHERE ena_toaster = 'N' ");
				ps = getCon().prepareStatement(sql.toString());
				ps.executeUpdate();
				getCon().commit();
				
				return true;
			}
			
			fechaConexao();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
    
    public List<MensagemBean> getMensagens() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MensagemBean> msg = new ArrayList<MensagemBean>();
		FuncionarioDao fDao = new FuncionarioDao();
		try {

			StringBuilder sql = new StringBuilder();
			estabeleceConexao();
			
			sql.append(" SELECT estacionamento_nao_autorizado.*, to_char(ena_data, 'dd/MM/yyyy - hh24:MI:ss') as data FROM ");
			sql.append(" 	estacionamento_nao_autorizado  ");
			sql.append(" WHERE ena_excluida = 'N' ");
			
			ps = getCon().prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				MensagemBean m = new MensagemBean();
				m.setData(rs.getString("data"));
				m.setId(rs.getInt("ena_id"));
				m.setLida(rs.getString("ena_lida"));
				m.setUsrEstacionado(fDao.selectPorCodigo(rs.getString("ena_usr_estacionado")));
				m.setUsrReservado(fDao.selectPorCodigo(rs.getString("ena_usr_reservada")));
				m.setVaga(rs.getInt("ena_vaga"));
				msg.add(m);
			}
			ps.close();
			rs.close();
									
			fechaConexao();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return msg;
	}
    
    private static String getSenhaCriptografada(String senha) {
        return Criptografia.criptografar(senha);
    }
    
    private static String getSenhaDescriptografada(String senha) {
        return Criptografia.descriptografar(senha);
    }
    
}