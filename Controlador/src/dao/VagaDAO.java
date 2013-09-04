package dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import principal.FuncionarioBean;
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
	 * Insere informacao de funcionario estacionado em 
	 * vaga nao autorizada
	 * @param f
	 * @param vg
	 */
	public void vagaNaoAltorizada(FuncionarioBean f, VagaBean vg) {
		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			estabeleceConexao();
			
			sql.append(" INSERT INTO ");
			sql.append(" 	estacionamento_nao_autorizado ( ");
			sql.append(" 		ena_vaga, ");
			sql.append(" 		ena_usr_reservada, ");
			sql.append(" 		ena_usr_estacionado ");
			sql.append(" 		ena_data )");
			sql.append(" 	VALUES ( ");
			sql.append(" 		?, ?, ?, now()) ");
			
			ps = getCon().prepareStatement(sql.toString());
			ps.setInt(1, vg.getId());
			ps.setString(2, vg.getUsrReservadoId());
			ps.setString(3, f.getCodigo());
			
			ps.executeUpdate();
			
			fechaConexao();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Grava a porcentagem total usada do estacionamento no momento
	 * @param lista
	 */
	public void insertEstatistica(List<VagaBean> lista) {
		PreparedStatement ps = null;
		try {

			Double pct = 0.0;
			int vagasOcupadas = 0;
			for (VagaBean v : lista) {
				if (v.getDisponibilidade().equals(0)) {
					vagasOcupadas ++;
				}
			}
			
			pct = (vagasOcupadas * 100.0) / lista.size();
			
			StringBuilder sql = new StringBuilder();
			estabeleceConexao();
			
			sql.append(" INSERT INTO ");
			sql.append(" 	estatistica ( ");
			sql.append(" 		est_pct, ");
			sql.append(" 		est_data) ");
			sql.append(" 	VALUES ( ");
			sql.append(" 		?, now()) ");
			
			ps = getCon().prepareStatement(sql.toString());
			ps.setBigDecimal(1, new BigDecimal(pct));
			
			ps.executeUpdate();
			
			fechaConexao();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
