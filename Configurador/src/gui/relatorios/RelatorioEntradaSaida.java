package gui.relatorios;

import gui.componentes.BotaoPadrao;
import gui.componentes.LabelPadrao;
import gui.componentes.TxtFieldMedio;
import gui.telaPrincipal.TelaPrincipal;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import utils.MedidasPadroes;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import configurador.Funcionario;

import dao.Dao;
import dao.FuncionarioDao;

public class RelatorioEntradaSaida extends JPanel{

	private TelaPrincipal pai;
	private BotaoPadrao btnGerar;
	private JComboBox comboUsuarios;
	private List<Funcionario> funcs;
	private TxtFieldMedio dataIni;
	private TxtFieldMedio dataFim;
	
	public RelatorioEntradaSaida(TelaPrincipal frame) {
		pai = frame;
		init();		
	}
	
	private void init() {
        FormLayout layout = new FormLayout("30dlu, pref",
                                           "80dlu, pref, 3dlu, pref, 3dlu, pref");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);
        setBackground(MedidasPadroes.COR_DE_FUNDO);
        JPanel pnl = new JPanel();
		pnl.setBackground(MedidasPadroes.COR_DE_FUNDO);
        pnl.add(getBtnGerar());
        add(getPanelFiltros(), cc.xy(2, 2));
        add(pnl, cc.xy(2, 4));
    }
	
	private JPanel getPanelFiltros() {
		JPanel pnl = new JPanel();
		FormLayout layout = new FormLayout("30dlu, pref, 10dlu, pref, 10 dlu, pref, 10 dlu, pref, 5dlu, pref",
											"5dlu, pref, 3dlu, pref, 3dlu, pref");
		CellConstraints cc = new CellConstraints();
		pnl.setLayout(layout);
		pnl.setBackground(MedidasPadroes.COR_DE_FUNDO);
		pnl.add(new LabelPadrao("Usuário:"), cc.xy(2, 2));
		pnl.add(getComboUsuarios(), cc.xy(4, 2));
		pnl.add(new LabelPadrao("Data:"), cc.xy(2, 4));
		pnl.add(getDataIni(), cc.xy(4, 4));
		pnl.add(new LabelPadrao("à "), cc.xy(6, 4));
		pnl.add(getDataFim(), cc.xy(8, 4));
		pnl.add(new JLabel("(dd/mm/yyyy)"), cc.xy(10, 4));
		pnl.setBorder(BorderFactory.createTitledBorder("Filtros"));
		
		return pnl;		
	}
	
	private TxtFieldMedio getDataIni() {
		if (dataIni == null) {
			dataIni = new TxtFieldMedio();
		}
		return dataIni;
	}
	
	private TxtFieldMedio getDataFim() {
		if (dataFim == null) {
			dataFim = new TxtFieldMedio();
		}
		return dataFim;
	}
	
	private JComboBox getComboUsuarios() {
		if (comboUsuarios == null) {
			comboUsuarios = new JComboBox();
			comboUsuarios.addItem("Todos");
			funcs = new FuncionarioDao().selectTodosFuncionarios();
			for (Funcionario f : funcs) {
				comboUsuarios.addItem(f);
			}
		}
		return comboUsuarios;
	}
	
	private BotaoPadrao getBtnGerar() {
		if (btnGerar == null) {
			btnGerar = new BotaoPadrao("Gerar");
			btnGerar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Map<String,Object> parametros = new HashMap<String,Object>();
						StringBuilder filtros = new StringBuilder();
						StringBuilder sql = new StringBuilder();
						StringBuilder sqlInterna = new StringBuilder();
						// usar parametros para filtros do relatorio
						if (getComboUsuarios().getSelectedIndex() != 0) {
							filtros.append(" AND r1.reg_usr_cod = '").append(((Funcionario)getComboUsuarios().getSelectedItem()).getCodigo()).append("' \n");
						}
						if (!"".equals(getDataIni().getText()) || !"".equals(getDataFim().getText())) {
							if ("".equals(getDataIni().getText()) || "".equals(getDataFim().getText())) {
								JOptionPane.showMessageDialog(pai, "As duas datas devem ser preenchidas!", "Atenção", JOptionPane.WARNING_MESSAGE);
								return;
							}
							filtros.append(" AND r1.reg_data >= to_date('").append(getDataIni().getText()).append(" 00:00:00', 'dd/MM/yyyy hh24:MI:ss') \n");
							filtros.append(" AND r1.reg_data <= to_date('").append(getDataFim().getText()).append(" 23:59:59', 'dd/MM/yyyy hh24:MI:ss') \n");
						}
						
						sqlInterna.append("(select sum(reg_permanencia) as perm from registro r2 where r1.reg_usr_cod = r2.reg_usr_cod");
						sqlInterna.append(filtros.toString().replaceAll("r1", "r2"));
						sqlInterna.append(")");
						
						sql.append(" select usr_nome, reg_tipo, reg_data, reg_usr_cod, reg_permanencia, \n");
						sql.append(" to_char(").append(sqlInterna).append(", 'dd') || ' Dias | ' || \n");
						sql.append(" to_char(").append(sqlInterna).append(", 'hh24 Horas | ') || \n");
						sql.append(" to_char(").append(sqlInterna).append(", 'MI') || ' Minutos | ' || \n");
						sql.append(" to_char(").append(sqlInterna).append(", 'ss') || ' Segundos' \n");
						sql.append(" from registro r1, funcionario \n");
						sql.append(" where usr_codigo = reg_usr_cod \n");
						sql.append(filtros.toString().replaceAll("r2", "r1"));
						sql.append(" order by lower(usr_nome), reg_data \n");			
						
						System.out.println(sql.toString());
						
						parametros.put("SQL", sql.toString());
						
						Dao d = new Dao();
						String path = getClass().getResource("/jasper/estacionamento.jasper").getPath();
						JasperPrint relatorio = JasperFillManager.fillReport(getClass().getResource("/jasper/estacionamento.jasper").getPath(), 
						        parametros, d.getCon());
						
						//abre visualizador  
			            JasperViewer jv = new JasperViewer(relatorio, false);  
			            jv.setTitle("Relatório de Entrada e Saída de Funcionários");    
			            jv.setVisible(true);
			            
					} catch (JRException e1) {
						e1.printStackTrace();
					}
					
					
				}
			});
		}
		return btnGerar;
	}

}
