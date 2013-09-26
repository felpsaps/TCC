package gui.relatorios;

import gui.componentes.BotaoPadrao;
import gui.componentes.LabelPadrao;
import gui.componentes.TxtFieldMedio;
import gui.telaPrincipal.TelaPrincipal;

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
import dao.VagasDao;

public class RelatorioUtilizacaoData extends JPanel{

	private TelaPrincipal pai;
	private BotaoPadrao btnGerar;
	private List<Funcionario> funcs;
	private TxtFieldMedio dataIni;
	private TxtFieldMedio dataFim;
	
	public RelatorioUtilizacaoData(TelaPrincipal frame) {
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
		pnl.add(new LabelPadrao("Data:"), cc.xy(2, 4));
		pnl.add(getDataIni(), cc.xy(4, 4));
		pnl.add(new JLabel("(dd/mm/yyyy)"), cc.xy(6, 4));
		pnl.setBorder(BorderFactory.createTitledBorder("Prever a porcentagem de utilização na data escolhida."));
		
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
	
	private BotaoPadrao getBtnGerar() {
		if (btnGerar == null) {
			btnGerar = new BotaoPadrao("Gerar");
			btnGerar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if ("".equals(getDataIni().getText())) {
						JOptionPane.showMessageDialog(pai, "Digite a data!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					String msg = "A previsão da procentagem de utilização para " + getDataIni().getText() + " é de:\n" + 
									new VagasDao().preverUtilizacaoData(getDataIni().getText()) + "%";
					JOptionPane.showMessageDialog(pai, msg, "Previsão", JOptionPane.INFORMATION_MESSAGE);
				}
					
			});
		}
		return btnGerar;
	}

}
