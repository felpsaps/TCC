package dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;

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

	public static BigDecimal preverUtilizacaoData(String data) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		// Lista com as observacoes da tabela estatistica diaria
		List<Observation> observacoes = new ArrayList<Observation>();
		try {        	
			estabeleceConexao();
			sql.append(" SELECT * ");
			sql.append(" FROM ");
			sql.append(" 	estatistica_diaria ");
			sql.append(" ORDER BY ");
			sql.append(" 	estd_data ");
			ps = getCon().prepareStatement(sql.toString());
			rs = ps.executeQuery();       

			Integer nroRegistros = 1;
			while (rs.next()) {
				/* ADICIONA TODAS AS MEDIAS DE PORCENTAGENS NA CLASSE DE OBSERVACAO */
				Observation obs = new Observation(rs.getBigDecimal("estd_pct").doubleValue());
				obs.setIndependentValue ("serie",nroRegistros);
				observacoes.add(obs);
				nroRegistros++;
			}
			ps.close();
			rs.close();
			fechaConexao();

			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			int dias = 0;
			try {
				dias = Days.daysBetween(new DateTime(System.currentTimeMillis()), new DateTime(fmt.parse(data).getTime())).getDays() + 1;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.out.println(dias + "   " + observacoes.size());
			while (dias != 0) {
				DataSet dataSet = new DataSet ();
				for (Observation obs : observacoes) {
					dataSet.add(obs);
				}

				// Criar o modelo de previsão
				ForecastingModel modelo = Forecaster.getBestForecast(dataSet);
				modelo.init(dataSet);

				// Criar próximo ponto de previsão, o qual será previsto
				DataPoint fcDataPoint = new Observation(0.0);
				fcDataPoint.setIndependentValue ("serie", nroRegistros);

				// Criar conjunto de dados de previsão e adicionar esses pontos de dados
				DataSet fcDataSet = new DataSet();
				fcDataSet.add (fcDataPoint);

				// Gerar a previsão
				modelo.forecast(fcDataPoint);

				// adiciono a previsao na lista de observacao para refazer o loop ateh chegar no dia desejado
				Observation o = new Observation(fcDataPoint.getDependentValue());
				o.setIndependentValue ("serie", nroRegistros);
				observacoes.add(o);
				nroRegistros++;
				dias--;
			}

			return new BigDecimal (observacoes.get(observacoes.size()-1).getDependentValue()).setScale(2, BigDecimal.ROUND_HALF_DOWN);

		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}        
	}
	
	public static String preverUtilizacaoTotal() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		// Lista com as observacoes da tabela estatistica diaria
		List<Observation> observacoes = new ArrayList<Observation>();
		try {        	
			estabeleceConexao();
			sql.append(" SELECT * ");
			sql.append(" FROM ");
			sql.append(" 	estatistica_diaria ");
			sql.append(" ORDER BY ");
			sql.append(" 	estd_data ");
			ps = getCon().prepareStatement(sql.toString());
			rs = ps.executeQuery();       

			Integer nroRegistros = 1;
			while (rs.next()) {
				/* ADICIONA TODAS AS MEDIAS DE PORCENTAGENS NA CLASSE DE OBSERVACAO */
				Observation obs = new Observation(rs.getBigDecimal("estd_pct").doubleValue());
				obs.setIndependentValue ("serie",nroRegistros);
				observacoes.add(obs);
				nroRegistros++;
			}
			ps.close();
			rs.close();
			fechaConexao();

			double pct = 0.0;
			int dias = 0;
			while (pct >= 0 && pct < 100.00) {
				DataSet dataSet = new DataSet ();
				for (Observation obs : observacoes) {
					dataSet.add(obs);
				}

				// Criar o modelo de previsão
				ForecastingModel modelo = Forecaster.getBestForecast(dataSet);
				modelo.init(dataSet);

				// Criar próximo ponto de previsão, o qual será previsto
				DataPoint fcDataPoint = new Observation(0.0);
				fcDataPoint.setIndependentValue ("serie", nroRegistros);

				// Criar conjunto de dados de previsão e adicionar esses pontos de dados
				DataSet fcDataSet = new DataSet();
				fcDataSet.add (fcDataPoint);

				// Gerar a previsão
				modelo.forecast(fcDataPoint);

				// adiciono a previsao na lista de observacao para refazer o loop ateh chegar no dia desejado
				Observation o = new Observation(fcDataPoint.getDependentValue());
				o.setIndependentValue ("serie", nroRegistros);
				observacoes.add(o);
				nroRegistros++;
				pct = observacoes.get(observacoes.size()-1).getDependentValue();
				dias++;
			}
			LocalDate data = new LocalDate().plusDays(dias);
			return data.toString("dd/MM/yyyy");

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
