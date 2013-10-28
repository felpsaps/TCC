package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Vaga;

/**
 *
 * @author Felps
 */
public class VagasDao extends Dao{
    
    public static List<Vaga> selectVagas() {
        try {
             List<Vaga> vagas = new ArrayList<Vaga>(50);
             String comandoSelect = String.format("select * from vaga order by vg_numero");

             estabeleceConexao();
             PreparedStatement ps = getCon().prepareStatement(comandoSelect);
             ResultSet rs = ps.executeQuery();

             while (rs.next()) {
                 vagas.add(new Vaga(rs.getInt("vg_id"), rs.getInt("vg_numero"),
                		 			rs.getString("vg_reserva_usr"), rs.getInt("vg_disp")));
             }
             fechaConexao();
             rs.close();
             return vagas;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return null;
         }
        
    }
    
    public static Integer selectVagaByCod(String cod) {
        try {
             String comandoSelect = String.format("select * from usuario_estacionado where usr_codigo = '" + cod + "'");

             estabeleceConexao();
             PreparedStatement ps = getCon().prepareStatement(comandoSelect);
             ResultSet rs = ps.executeQuery();

             if (rs.next()) {
            	 return rs.getInt("vaga");
             }
             return -1;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return -1;
         } finally {
        	 try {
				fechaConexao();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
        
    }
    
    
    
}
