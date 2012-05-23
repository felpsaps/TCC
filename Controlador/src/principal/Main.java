package principal;


import dao.Banco;
import dao.FuncionarioDao;
import java.sql.SQLException;

/**
 *
 * @author Felps
 */
public class Main {
     public static void main(String[] args) throws SQLException {
        Banco.criaConexao("root", "230898");
        new FuncionarioDao().selectLoginESenha("felipeaps");
        
    }
}
