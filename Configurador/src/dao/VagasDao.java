package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import configurador.Vaga;

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
             ResultSet rs = comando.executeQuery(comandoSelect);

             while (rs.next()) {
                 vagas.add(new Vaga(rs.getInt("vg_id"), rs.getInt("vg_numero"),
                		 			rs.getString("vg_reserva_usr"), rs.getInt("vg_disp")));
             }
             fechaConexao();
             return vagas;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return null;
         }
        
    }
    
    public static void reservaVaga(int vgNum, String codigo) {
    	try {
    		StringBuilder sql = new StringBuilder();
    		PreparedStatement ps = null;
    		estabeleceConexao();
    		sql.append(" UPDATE\n")
    		   .append(" 	vaga SET ")
    		   .append(" 		vg_reserva_usr = ? ")
    		   .append(" WHERE ")
    		   .append(" 	vg_numero = ? ");

    		ps = getCon().prepareStatement(sql.toString());
    		ps.setString(1, codigo);
    		ps.setInt(2, vgNum);
    		
    		ps.executeUpdate();
            getCon().commit();
            fechaConexao();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void atualizarReservaVaga(int vgNum, String codigo) {
    	try {
    		StringBuilder sql = new StringBuilder();
    		PreparedStatement ps = null;
    		estabeleceConexao();
    		// PRIMEIRO LIMPA TODAS AS RESERVAS DESTE USUARIO;
    		sql.append(" UPDATE\n")
    		   .append(" 	vaga SET ")
    		   .append(" 		vg_reserva_usr = null ")
    		   .append(" WHERE ")
    		   .append(" 	vg_reserva_usr = ? ");

    		ps = getCon().prepareStatement(sql.toString());
    		ps.setString(1, codigo);
    		
    		ps.executeUpdate();
            getCon().commit();
            
            if (vgNum != 0) {
            	reservaVaga(vgNum, codigo);
            }
            
            fechaConexao();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
