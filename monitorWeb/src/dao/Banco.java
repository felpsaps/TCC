package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Felipe
 */
public class Banco {
    public static Connection con;
    /**
     * Estabelece uma conexão com o banco de dados
     *
     * @param usuario Usuário do banco
     * @param senha Senha para o usuário
     * @return Objetco de conexão
     * @throws SQLException
     */
    public static Connection connect() throws SQLException
    {
        if (con == null)
            criaConexao("postgres", "890823Fe");
        return con;
    }
    private static void criaConexao(String usuario, String senha) throws SQLException
    {
    	String driver = "org.postgresql.Driver";
        String banco = "estacionamento";
        String host = "localhost:5432";
        String strConn = "jdbc:postgresql://" + host + "/" + banco;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
             JOptionPane.showMessageDialog(null, "A biblioteca necessária para "
                     + "a conexão com o banco de dados não foi corretamente instalada!",
                    "Erro no banco de dados", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        con = DriverManager.getConnection(strConn, usuario, senha);
        setAutoCommit(false);
    }
    public static void setAutoCommit(boolean a) throws SQLException
    {
        con.setAutoCommit(a);
    }
    public static void rollBack() throws SQLException
    {
        con.rollback();
    }
    public static void commit() throws SQLException
    {
        con.commit();
    }

}
