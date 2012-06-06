package dao;

import configurador.Funcionario;
import configurador.Vaga;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felps
 */
public class VagasDao extends Dao{
    
    public static List<Vaga> selectVagas() {
        try {
             List<Vaga> vagas = new ArrayList<Vaga>(50);
             String comandoSelect = String.format("select * from vaga order by codigo");

             estabeleceConexao();
             ResultSet rs = comando.executeQuery(comandoSelect);

             while (rs.next()) {
                 vagas.add(new Vaga(rs.getString(1), rs.getString(2), 
                         rs.getString(3), rs.getInt(4)));
             }
             fechaConexao();
             return vagas;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return null;
         }
        
    }
    
}
