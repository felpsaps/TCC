package gui.telaPrincipal;

import gui.componentes.BotaoPadrao;
import gui.modelos.MensagemTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import utils.MedidasPadroes;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import configurador.MensagemBean;
import dao.FuncionarioDao;

public class PainelMensagens extends JPanel {

    private TelaPrincipal telaPrincipal;
    
    private MensagemTableModel model;
    
    private JTable tabelaMensagens;   
    
    private JButton btnExcluir;

    private List<MensagemBean> mensagens;
    
	public PainelMensagens(TelaPrincipal pai) {
        telaPrincipal = pai;
        init();
    }
	
	private void init() {
        FormLayout layout = new FormLayout("30dlu, pref",
                                           "80dlu, pref, 3dlu, pref, 3dlu, pref");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);
        setBackground(MedidasPadroes.COR_DE_FUNDO);
        JScrollPane scroll = new JScrollPane(getTabelaMensagens());
        scroll.setPreferredSize(new Dimension(1000, 300));
        JPanel pnel = new JPanel(new BorderLayout());
        pnel.setBackground(MedidasPadroes.COR_DE_FUNDO);        
        pnel.add(getBtnExcluir(), BorderLayout.WEST);
        
        add(pnel, cc.xy(2, 2));
        add(scroll, cc.xy(2, 4));
    }
	
	private JButton getBtnExcluir() {
		if (btnExcluir == null) {
			btnExcluir = new BotaoPadrao("Excluir");
			btnExcluir.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					for ( MensagemBean m : model.getMensagens()) {
						if (m.getChecado()) {
							new FuncionarioDao().excluirMensagem(m, telaPrincipal);
							model.remove(m);
						}
					}
				}
			});
		}
		return btnExcluir;
	}
	
	private JTable getTabelaMensagens() {
        if (tabelaMensagens == null) {  
            tabelaMensagens = new JTable(); 
            model = new MensagemTableModel(tabelaMensagens);
            mensagens = new FuncionarioDao().getMensagens();
            model.adicionaMensagem(mensagens);
            tabelaMensagens.setModel(model);
            
            tabelaMensagens.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                	if (tabelaMensagens.getSelectedColumn() == -1) {
                		return;
                	}
                	MensagemBean msg = model.getMensagen(tabelaMensagens.getSelectedRow());
                	String m = "Atenção!\nA vaga " + msg.getVaga() + " está reservada para o funcionário " + msg.getUsrReservado().getNome()
    						+ ".\nÉ provável que o funcionário " + msg.getUsrEstacionado().getNome() + " tenha estacionado sem autorização.\n"
    						+ "Favor verificar!";
                	new JOptionPane().showMessageDialog(PainelMensagens.this, m, "Vaga Reservada", JOptionPane.INFORMATION_MESSAGE, null);
                	// Tornar Mensagem lida
                	new FuncionarioDao().lerMensagem(msg, telaPrincipal);
                	model.setValueAt("Sim", tabelaMensagens.getSelectedRow(), 3);
                }
                
                 @Override
                public void mouseReleased(MouseEvent evt) {
                }
            });          
           
            
            return tabelaMensagens;
        } else {
            return tabelaMensagens;
        }
    }
}
