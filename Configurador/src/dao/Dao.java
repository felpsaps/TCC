package dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Felipe
 */
public class Dao {

    /**
     * Conexão com o banco
     */
    protected static Connection conexao;
    /**
     * Statement, utilizado para realizar consultas inserções, etc...
     */
    protected static Statement comando;
    /**
     * Constantes de usuário e senha
     */
    protected static final String USUARIO = "root", SENHA = "230898";

    /**
     * Cria uma conexão e um statement com o banco de dados
     *
     * @throws SQLException
     */
    protected static void estabeleceConexao() throws SQLException
    {
        conexao = Banco.connect();
        comando = conexao.createStatement();
    }
    
    public static Connection getCon() {
    	if (conexao == null) {
    		try {
				estabeleceConexao();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	return conexao;
    }

    /**
     * Fecha uma conexão e o statement cm o banco de dados
     *
     * @throws SQLException
     */
    protected static void fechaConexao() throws SQLException
    {
    	if (comando != null) {
    		comando.close();
    	}
    }


}
