package dao;

import configurador.Funcionario;
import excessoes.FuncionarioDaoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.Criptografia;

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
        try {
            String comandoInsert;

            comandoInsert = String.format("INSERT INTO `funcionario` (`codigo`, `nome`, "
                    + "`email`, `celular`, `tipo`, `login`, `senha`) "
                    + "VALUES ('%s', '%s', '%s', '%s', '%d', '%s', '%s');",
                    func.getCodigo(), func.getNome(), func.getEmail(), func.getCelular(), func.getTipo(),
                    getSenhaCriptografada(func.getLogin()), getSenhaCriptografada(func.getSenha()));

            estabeleceConexao();
            comando.executeUpdate(comandoInsert);
            fechaConexao();
        } catch (SQLException ex) {
            ex.printStackTrace();
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
                    + "WHERE (`codigo`= '%s')",
                    codigo);

            estabeleceConexao();
            comando.executeUpdate(comandoRemove);
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

            comandoAtualiza = String.format("UPDATE `funcionario` SET nome='%s', "
                    + "email='%s', celular='%s', tipo='%d', login='%s', "
                    + "senha='%s' WHERE codigo = '%s'",
                    func.getNome(), func.getEmail(), func.getCelular(), func.getTipo(), 
                    getSenhaCriptografada(func.getLogin()), getSenhaCriptografada(func.getSenha()), 
                    func.getCodigo());


            estabeleceConexao();
            comando.executeUpdate(comandoAtualiza);
            fechaConexao();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Funcionario selectPorCodigo(String cod)  {
         try {
             Funcionario func = null;
             String comandoSelect = String.format("select * from funcionario where codigo='" + cod + "'");

             estabeleceConexao();
             ResultSet rs = comando.executeQuery(comandoSelect);

             if (rs.next()) {
                 func = new Funcionario(rs.getString(1), rs.getString(2), rs.getString(3),
                         rs.getString(4), rs.getInt(5), getSenhaDescriptografada(rs.getString(6)),
                         getSenhaDescriptografada(rs.getString(7)));
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
             String comandoSelect = String.format("select * from funcionario order by tipo");

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
             String comandoSelect = "select * from funcionario where nome LIKE '" + nome
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
        String comandoSelect = String.format("select * from funcionario where email='%s'",
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
        String comandoSelect = String.format("select * from funcionario where login='%s'",
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
    
    private static String getSenhaCriptografada(String senha) {
        return Criptografia.criptografar(senha);
    }
    
    private static String getSenhaDescriptografada(String senha) {
        return Criptografia.descriptografar(senha);
    }
    
}