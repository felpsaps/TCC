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
    protected Connection conexao;
    /**
     * Statement, utilizado para realizar consultas inserções, etc...
     */
    protected Statement comando;
    /**
     * Constantes de usuário e senha
     */
    protected static final String USUARIO = "root", SENHA = "230898";

    /**
     * Cria uma conexão e um statement com o banco de dados
     *
     * @throws SQLException
     */
    public void estabeleceConexao() throws SQLException
    {
        conexao = Banco.connect();
        comando = conexao.createStatement();
    }
    
    public Connection getCon() {
    	return conexao;
    }

    /**
     * Fecha uma conexão e o statement cm o banco de dados
     *
     * @throws SQLException
     */
    protected void fechaConexao() throws SQLException
    {
        comando.close();
    }


}
