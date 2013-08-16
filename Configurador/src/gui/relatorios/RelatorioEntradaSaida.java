package gui.relatorios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import gui.componentes.BotaoPadrao;
import gui.componentes.Link;
import gui.telaPrincipal.PainelRelatorios;
import gui.telaPrincipal.TelaPrincipal;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import utils.MedidasPadroes;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import dao.Dao;

public class RelatorioEntradaSaida extends JPanel{

	private TelaPrincipal pai;
	private BotaoPadrao btnGerar;
	
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
        
        
        
        add(getBtnGerar(), cc.xy(2, 2));
    }
	
	private BotaoPadrao getBtnGerar() {
		if (btnGerar == null) {
			btnGerar = new BotaoPadrao("Gerar");
			btnGerar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Map<String,Object> parametros = new HashMap<String,Object>();
						// TODO usar parametros para filtros do relatorio
						
						Dao d = new Dao();
						String path = getClass().getResource("/jasper/estacionamento.jasper").getPath();
						System.out.println(path);
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
