package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import principal.VagaBean;

public class VagaDAO extends Dao{

	public List<VagaBean> selectVagas() {
        try {
             List<VagaBean> vagas = new ArrayList<VagaBean>(50);
             VagaBean vg = new VagaBean();
             String comandoSelect = String.format("select * from vaga order by vg_numero");

             estabeleceConexao();
             ResultSet rs = comando.executeQuery(comandoSelect);

             while (rs.next()) {
            	 vg = new VagaBean();
            	 vg.setId(rs.getInt("vg_id"));
            	 vg.setNro(rs.getInt("vg_numero"));
            	 vg.setDisponibilidade(rs.getInt("vg_disp"));
            	 vg.setUsrReservadoId(rs.getString("vg_reserva_usr"));
            	 
            	 vagas.add(vg);
             }
             rs.close();
             fechaConexao();
             return vagas;
         } catch (SQLException ex) {
             ex.printStackTrace();
             return null;
         }        
    }
	
	/**
	 * Atualiza status da vaga
	 * 1 = vaga disponivel
	 * 0 = vaga ocupada
	 * @param vg
	 */
	public void updateDisponibilidade(VagaBean vg) {
        try {
             StringBuilder sql = new StringBuilder();
             PreparedStatement ps = null;

             estabeleceConexao();
             
             sql.append(" UPDATE \n")
                .append("    vaga \n")
                .append(" SET \n")
                .append("    vg_disp = ? \n")
                .append(" WHERE \n")
                .append("    vg_id = ?");
             
             ps = getCon().prepareStatement(sql.toString());
             ps.setInt(1, vg.getDisponibilidade());
             ps.setInt(2, vg.getId());
             
             ps.executeUpdate();

             fechaConexao();
         } catch (SQLException ex) {
             ex.printStackTrace();
         }        
    }
	

}
