package gui.telaPrincipal;

import gui.componentes.Link;
import gui.relatorios.RelatorioEntradaSaida;
import gui.relatorios.RelatorioPrevisaoUtilizacaoTotal;
import gui.relatorios.RelatorioUtilizacaoData;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import utils.MedidasPadroes;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class PainelRelatorios extends JPanel{

	private TelaPrincipal pai;
	
	public PainelRelatorios(TelaPrincipal frame) {
		pai = frame;
		init();
	}
	
	private void init() {
        FormLayout layout = new FormLayout("30dlu, pref",
                                           "80dlu, pref, 10dlu, pref, 10dlu, pref");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);
        setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        Link relEntradaSaida = new Link("- Relatório de Entrada e Saída");
        relEntradaSaida.getLink().addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		pai.setPainel(PainelRelatorios.this, 
                        new RelatorioEntradaSaida(pai), 
                        "relatorioEntradaSaida");
        	}
		});
        
        Link relUtilizacaoData = new Link("- Previsão de Utilização em Data Específica");
        relUtilizacaoData.getLink().addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		pai.setPainel(PainelRelatorios.this, 
                        new RelatorioUtilizacaoData(pai), 
                        "previsaoData");
        	}
		});
        
        Link relUtilizacaoTotal = new Link("- Previsão de Utilização Total");
        relUtilizacaoTotal.getLink().addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		pai.setPainel(PainelRelatorios.this, 
                        new RelatorioPrevisaoUtilizacaoTotal(pai), 
                        "previsaoTotal");
        	}
		});
        
        
        add(relEntradaSaida, cc.xy(2, 2));
        add(relUtilizacaoData, cc.xy(2, 4));
        add(relUtilizacaoTotal, cc.xy(2, 6));
    }

}
