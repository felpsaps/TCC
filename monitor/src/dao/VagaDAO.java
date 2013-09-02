package dao;

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

}
