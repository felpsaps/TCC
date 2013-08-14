package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.Criptografia;
import configurador.ServidorSMTP;
import excessoes.ServidorSMTPDaoException;

/**
 *
 * @author Felps
 */
public class ServidorSMTPDao extends Dao{
    
    
    public static void insere(ServidorSMTP sv) throws SQLException
    {
        String comandoInsert;
        
        estabeleceConexao();
        comandoInsert = String.format("INSERT INTO servidorSMTP (nome, "
                + "endereco, senha, porta, email) "
                + "VALUES ('%s', '%s', '%s', '%s', '%s');",
                sv.getNomeServidor(), sv.getEnderecoServidor(), getSenhaCriptografada(sv.getSenha()),
                sv.getPorta(), sv.getEmail());

        comando.executeUpdate(comandoInsert);
        getCon().commit();
        fechaConexao();
    }
    
    public static void apaga(ServidorSMTP sv) throws SQLException
    {
        String comandoRemove;

        comandoRemove = String.format("DELETE FROM `servidorSMTP` "
                + "WHERE (`nome`= '%s')",
                sv.getNomeServidor());

        estabeleceConexao();
        comando.executeUpdate(comandoRemove);
        getCon().commit();
        fechaConexao();
    }
    
    public static void atualiza(ServidorSMTP sv) throws SQLException
    {
        String comandoAtualiza;

        comandoAtualiza = String.format("UPDATE `servidorSMT` SET "
                + "`endereco='%s'`, `senha='%s'`, `porta='%s'`, `email='%s'`' "
                + "WHERE codigo = '%s'",
                sv.getEnderecoServidor(), getSenhaCriptografada(sv.getSenha()), sv.getPorta(), 
                sv.getEmail());


        estabeleceConexao();
        comando.executeUpdate(comandoAtualiza);
        getCon().commit();
        fechaConexao();
    }    
    
    public static ServidorSMTP select(String pass) throws SQLException {
        ServidorSMTP servidor = null;
        String comandoSelect = String.format("select * from servidorsmtp where senha='%s'",
                                              pass);

        estabeleceConexao();
        ResultSet rs = comando.executeQuery(comandoSelect);
         
        if (rs.next()) {
            servidor = new ServidorSMTP(rs.getString("nome"), rs.getString("endereco"), 
                    getSenhaDescriptografada(rs.getString("senha")), rs.getString("porta"), rs.getString("email"));             
        }        
        fechaConexao();
        return servidor;        
    }
    
    public static ServidorSMTP getServidor() throws SQLException, ServidorSMTPDaoException {
        ServidorSMTP servidor = null;
        String comandoSelect = "select * from servidorsmtp";

        estabeleceConexao();
        ResultSet rs = comando.executeQuery(comandoSelect);
         
        if (rs.next()) {
            servidor = new ServidorSMTP(rs.getString("nome"), rs.getString("endereco"), 
                    getSenhaDescriptografada(rs.getString("senha")), rs.getString("porta"), rs.getString("email"));
        } else {
            throw new ServidorSMTPDaoException("Erro ao Tentar Recuperar o Servidor SMTP!");
        }
        fechaConexao();
        return servidor;
    }
    
    private static String getSenhaCriptografada(String senha) {
        return Criptografia.criptografar(senha);
    }
    
    private static String getSenhaDescriptografada(String senha) {
        return Criptografia.descriptografar(senha);
    }
}
