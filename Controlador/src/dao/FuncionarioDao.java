package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Tiago
 */
public class FuncionarioDao extends Dao {
     //private Funcionario f;

    /**
     * Construtor
     * @param cli Objeto cliente
     */
    public FuncionarioDao(/*Funcionario func*/)
    {
        //f = func;
    }

    /**
     * Insere o funcionario na base de dados
     * @throws SQLException
     */
    public void insere() throws SQLException
    {
        String comandoInsert;

        comandoInsert = String.format("INSERT INTO `funcionario` (`Codigo`, `Nome`, "
                + "`Vaga`) VALUES ('%s', '%s', '%s');",
                "0", "Oi", "22");

        estabeleceConexao();
        comando.executeUpdate(comandoInsert);
        fechaConexao();
    }

    /**
     * Apaga o funcionario da base de dados
     * @throws SQLException
     */
    public void apaga() throws SQLException
    {
        String comandoRemove;

        comandoRemove = String.format("DELETE FROM `funcionario` "
                + "WHERE (`Codigo`= '%s')",
                "0");

        estabeleceConexao();
        comando.executeUpdate(comandoRemove);
        fechaConexao();
    }

    /**
     * Atualiza os dados do cliente na base de dados
     * @throws SQLException
     */
    public void atualiza() throws SQLException
    {
        String comandoAtualiza;

        comandoAtualiza = String.format("UPDATE `funcionario` SET `nome`='%s',"
                + "`Vaga`='%s' "
                + "WHERE Codigo = '%s'",
                "0");


        estabeleceConexao();
        comando.executeUpdate(comandoAtualiza);
        fechaConexao();
    }
    
    public void selectLoginESenha(String login) throws SQLException
    {
        String comandoSelect = String.format("select login, senha from funcionario where login='%s'",
                                              login);

        estabeleceConexao();
        ResultSet rs = comando.executeQuery(comandoSelect);
        if (rs.next()) {
            System.out.println("login: " + rs.getString(1) + rs.getString(2));
        } else {
            System.out.println("OI");
        }
        fechaConexao();
        System.out.println("OIIIIII");       
    }
    
}