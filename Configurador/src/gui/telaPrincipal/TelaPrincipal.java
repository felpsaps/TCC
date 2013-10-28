package gui.telaPrincipal;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.nitido.utils.toaster.Toaster;

import configurador.Funcionario;
import dao.FuncionarioDao;
import dao.VagasDao;
import gui.TelaLogin;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.*;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;

import utils.MedidasPadroes;


public class TelaPrincipal extends JFrame {
    
    public static Funcionario func;

    private JButton btnCadastrar;
    private JButton btnMsg;
    private JButton btnRelatorios;
    private JButton btnConfiguracoes;

    private JLabel lblSair;
    private JLabel lblConfigurarConta;

    private JPanel painelBotoes;
    private JPanel painelPrincipal;
    private JPanel painelCardLayout;

    private JMenuBar menuBar;
    private JMenu arquivoItem;
    
    private static CardLayout cardLayout = null;
    
    private boolean continuaToaster;

    public TelaPrincipal(Funcionario f) {
        func = f;
        init();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        continuaToaster = true;
        new MostraToaster().start();
    }
    
    

    private void init() {
        menuBar = new JMenuBar();
        arquivoItem = new JMenu("Arquivo");
        JMenuItem conf = new JMenuItem("Configurar Conta");
		conf.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
	                	getPainelCardLayout().add(new PainelConfiguracaoConta(TelaPrincipal.this), "confConta");
	                    cardLayout.show(painelCardLayout, "confConta"); 
					}
				});
        JMenuItem sair = new JMenuItem("Sair");
        sair.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] op = {"Sim", "Não"};
                int resp = JOptionPane.showOptionDialog(TelaPrincipal.this, "Deseja mesmo sair?", "Atenção!",
                        JOptionPane.INFORMATION_MESSAGE,JOptionPane.YES_NO_OPTION, null, op, op[1]);
                if (resp == JOptionPane.YES_OPTION) {
                    TelaPrincipal.this.dispose();
                    func = null;
                    continuaToaster = false;
                    new TelaLogin();
                } else {
                    return;
                }
			}
		});
        arquivoItem.add(conf);
        arquivoItem.add(sair);
        menuBar.add(arquivoItem);
        setJMenuBar(menuBar);
        FormLayout layout = new FormLayout("pref, pref, pref",
                                           "pref");
        setLayout(layout);

        CellConstraints cc = new CellConstraints();

        add(getPainelBotoes(), cc.xy(1, 1));
        add(getPainelSeparador(), cc.xy(2, 1));
        add(getPainelCardLayout(), cc.xy(3, 1));      
        

    }

    private JPanel getPainelCardLayout() {
        if (painelCardLayout == null) {
            cardLayout = new CardLayout();
            painelCardLayout = new JPanel();
            painelCardLayout.setLayout(cardLayout);
            painelCardLayout.add(getPainelPrincipal(), "principal");
            return painelCardLayout;
        } else {
            return painelCardLayout;
        }

    }

    private JPanel getPainelSeparador() {
        JPanel painel = new JPanel();
        painel.setPreferredSize(new Dimension(2, Toolkit.getDefaultToolkit().getScreenSize().height));
        painel.setBackground(Color.BLACK);
        painel.setBorder(BorderFactory.createEmptyBorder());
        return painel;
    }
    
    private JPanel getPainelBotoes() {
        if (painelBotoes == null) {
            painelBotoes = new JPanel();
            painelBotoes.setPreferredSize(new Dimension(200, Toolkit.getDefaultToolkit().getScreenSize().height));
            FormLayout layout = new FormLayout("5dlu, pref",
                                               "200dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref");
            CellConstraints cc = new CellConstraints();
            painelBotoes.setLayout(layout);
            painelBotoes.add(getBtnCadastrar(), cc.xy(2, 2));
            painelBotoes.add(getBtnMsgs(), cc.xy(2, 4));
            painelBotoes.add(getBtnRemover(), cc.xy(2, 6));
            painelBotoes.add(getBtnConfigurar(), cc.xy(2, 8));
            painelBotoes.setBackground(Color.red);
            
            if (func.getTipo() != Funcionario.TIPO_PROPRIETARIO) {
            	getBtnRemover().setVisible(false);
            	getBtnConfigurar().setVisible(false);
            }
            
            return painelBotoes;
        } else {
            return painelBotoes;
        }
    }
    
    private JButton getBtnRemover() {
        if (btnRelatorios == null) {
            btnRelatorios = new JButton("Relatórios");
            btnRelatorios.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                	getPainelCardLayout().add(new PainelRelatorios(TelaPrincipal.this), "relatorios");
                    cardLayout.show(painelCardLayout, "relatorios"); 
                }
            });
            return btnRelatorios;
        } else {
            return btnRelatorios;
        }
    }

    private JButton getBtnConfigurar() {
        if (btnConfiguracoes == null) {
            btnConfiguracoes = new JButton("Configurar Sistema");
            //btnConfiguracoes.setVisible(false);
            return btnConfiguracoes;
        } else {
            return btnConfiguracoes;
        }
    }

    private JButton getBtnCadastrar() {
        if (btnCadastrar == null) {
            btnCadastrar = new JButton("Manter Funcionário");
            btnCadastrar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getPainelCardLayout().add(new ConsultarFuncionario(TelaPrincipal.this), "consultar");
                    cardLayout.show(painelCardLayout, "consultar");                    
                }
            });
            return btnCadastrar;
        } else {
            return btnCadastrar;
        }
    }
    
    public JButton getBtnMsgs() {
        if (btnMsg == null) {
        	btnMsg = new JButton("Mensagens(0)");
        	btnMsg.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getPainelCardLayout().add(new PainelMensagens(TelaPrincipal.this), "mensagem");
                    cardLayout.show(painelCardLayout, "mensagem");                    
                }
            });
            return btnMsg;
        } else {
            return btnMsg;
        }
    }

    private JPanel getPainelPrincipal() {
        if (painelPrincipal == null) {
            painelPrincipal = new JPanel();
            painelPrincipal.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width-200,
                    Toolkit.getDefaultToolkit().getScreenSize().height));
            painelPrincipal.setBackground(MedidasPadroes.COR_DE_FUNDO);
            FormLayout layout = new FormLayout("530dlu, pref, 5dlu, pref",
                                               "3dlu, pref");
            painelPrincipal.setLayout(layout);
            CellConstraints cc = new CellConstraints();

            painelPrincipal.add(getLblSair(), cc.xy(2, 2));
            painelPrincipal.add(getLblConfigurarConta(), cc.xy(4, 2));
            return painelPrincipal;
        } else {
            return painelPrincipal;
        }
    }
    
    public void setPainelPrincipal(Component painel) {
        cardLayout.removeLayoutComponent(painel);
        cardLayout.show(painelCardLayout, "principal");
    }
    
    public void setPainel(Component painelRemover, Component painelIncluir, String nomePainel) {
        cardLayout.removeLayoutComponent(painelRemover);
        painelCardLayout.add(nomePainel, painelIncluir);
        cardLayout.show(painelCardLayout, nomePainel);
    }

    private JLabel getLblSair() {
        if (lblSair == null) {
            lblSair = new JLabel("Sair");
            lblSair.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            lblSair.setForeground(Color.LIGHT_GRAY);
            lblSair.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String[] op = {"Sim", "Não"};
                    int resp = JOptionPane.showOptionDialog(TelaPrincipal.this, "Deseja mesmo sair?", "Atenção!",
                            JOptionPane.INFORMATION_MESSAGE,JOptionPane.YES_NO_OPTION, null, op, op[1]);
                    if (resp == JOptionPane.YES_OPTION) {
                        TelaPrincipal.this.dispose();
                        func = null;
                        continuaToaster = false;
                        new TelaLogin();
                    } else {
                        return;
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    lblSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    lblSair.setForeground(Color.red);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    lblSair.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    lblSair.setForeground(Color.LIGHT_GRAY);
                }
            });
            return lblSair;
        } else {
            return lblSair;
        }
    }

    private JLabel getLblConfigurarConta() {
        if (lblConfigurarConta == null) {
            lblConfigurarConta = new JLabel("Configurar Conta");
            lblConfigurarConta.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            lblConfigurarConta.setForeground(Color.LIGHT_GRAY);
            lblConfigurarConta.addMouseListener(new MouseAdapter() {
            	@Override
                public void mouseEntered(MouseEvent e) {
                    lblConfigurarConta.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    lblConfigurarConta.setForeground(Color.red);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    lblConfigurarConta.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    lblConfigurarConta.setForeground(Color.LIGHT_GRAY);
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                	getPainelCardLayout().add(new PainelConfiguracaoConta(TelaPrincipal.this), "confConta");
                    cardLayout.show(painelCardLayout, "confConta"); 
                }
            });
            return lblConfigurarConta;
        } else {
            return lblConfigurarConta;
        }
    }
    
    /**
     * Mostra um alerta para avisar que há novas mensagens
     * @author felps
     *
     */
    private class MostraToaster extends Thread {
    	public void run() {
    		Toaster t = new Toaster();
            Icon ic = new ImageIcon(getClass().getClassLoader().getResource("arquivos/alert1.png"));
            t.setToasterHeight(100);
            t.setToasterWidth(410);
            t.setStep(2);
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            while (continuaToaster) {
        		/* VERIFICA SE HA NOVAS MENSAGENS 
        		 * SE TIVER MOSTRA O TOASTER */
            	if (new FuncionarioDao().selectVagaNaoAltorizadaToaster(TelaPrincipal.this)) {
            		t.showToaster(ic, "a");
            	}
                
	    		try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
    	}
    }
    
    
}
