package gui.telaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

import gui.componentes.BotaoPadrao;
import gui.componentes.LabelPadrao;
import gui.componentes.TxtFieldGrande;
import gui.componentes.TxtFieldMedio;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.MaskFormatter;

import utils.Criptografia;
import utils.MedidasPadroes;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import dao.FuncionarioDao;
import excessoes.FuncionarioDaoException;

public class PainelConfiguracaoConta extends JPanel {

	private TxtFieldGrande txtEmail;
	private TxtFieldGrande txtNome;
	private JFormattedTextField txtCel;
	private JPasswordField txtPw;
	private JPasswordField txtPwConf;
	private TelaPrincipal pai;
	private BotaoPadrao btnSalvar;
	private BotaoPadrao btnVoltar;

	public PainelConfiguracaoConta(TelaPrincipal t) {
		pai = t;
		init();
	}


	private void init() {
		FormLayout layout = new FormLayout("30dlu, pref",
											"80dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref");
		CellConstraints cc = new CellConstraints();
		setLayout(layout);
		setBackground(MedidasPadroes.COR_DE_FUNDO);
		add(getPnlLinha1(), cc.xy(2, 2));
		add(getPnlLinha2(), cc.xy(2, 4));
		add(getPnlSenhas(), cc.xy(2, 6));
		add(getPnlBtns(), cc.xy(2, 8));
		
		setBorder(BorderFactory.createTitledBorder("Configurações da Conta"));
	}

	private BotaoPadrao getBtnSalvar() {
		if (btnSalvar == null) {
			btnSalvar = new BotaoPadrao("Salvar");
			btnSalvar.setPreferredSize(MedidasPadroes.MEDIDA_BTN_PADRAO);
			btnSalvar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                	String[] op = {"Sim", "Não"};
                    int resp = JOptionPane.showOptionDialog(pai, "Deseja alterar os dados da conta?", "Atenção!",
                            JOptionPane.INFORMATION_MESSAGE,JOptionPane.YES_NO_OPTION, null, op, op[1]);
                    if (resp == JOptionPane.YES_OPTION) {
                    	pai.func.setCelular((String)getTxtCelular().getValue());
                    	pai.func.setNome(getTxtNome().getText());
                    	pai.func.setEmail(getTxtEmail().getText());
                    	
                    	System.out.println("SENHA: " + new String(getTxtPw().getPassword()) + "  SENHA2: " + new String(getTxtPwConf().getPassword()));
                    	
                    	if (!"".equals(new String(getTxtPw().getPassword())) || !"".equals(new String(getTxtPwConf().getPassword()))) {
                    		if (!(new String(getTxtPw().getPassword()).equals(new String(getTxtPwConf().getPassword())))) {
                    			JOptionPane.showMessageDialog(pai, "As senhas devem ser iguais!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    			return;
                    		}
                    		pai.func.setSenha(new String(getTxtPw().getPassword()));
                    	} else {
                    		pai.func.setSenha(getSenhaDescriptografada(pai.func.getSenha()));
                    	}
                    	
                    	new FuncionarioDao().atualizaConta(pai.func);
                    	try {
							pai.func = new FuncionarioDao().selectLoginESenha(getSenhaDescriptografada(pai.func.getLogin()), pai.func.getSenha());
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (FuncionarioDaoException e1) {
							e1.printStackTrace();
						}
                    	pai.setPainelPrincipal(PainelConfiguracaoConta.this);
                    }
                }
            });
		}
		return btnSalvar;
	}
	
	private BotaoPadrao getBtnVoltar() {
		if (btnVoltar == null) {
			btnVoltar = new BotaoPadrao("Voltar");
			btnVoltar.setPreferredSize(MedidasPadroes.MEDIDA_BTN_PADRAO);
			btnVoltar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                	pai.setPainelPrincipal(PainelConfiguracaoConta.this);
                }
            });
		}
		return btnVoltar;
	}
	
	private JPanel getPnlBtns() {
		FormLayout layout = new FormLayout("pref, 5dlu, pref",
											"pref");
		PanelBuilder pb = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		
		pb.add(getBtnVoltar(), cc.xy(1, 1));
		pb.add(getBtnSalvar(), cc.xy(3, 1));
		pb.setBackground(MedidasPadroes.COR_DE_FUNDO);
		
		return pb.getPanel();
	}
	
	private JPanel getPnlLinha1() {
		FormLayout layout = new FormLayout("pref, 5dlu, pref, 15dlu, pref, 5dlu, pref",
											"pref");
		PanelBuilder pb = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		
		pb.add(new LabelPadrao("Nome:"), cc.xy(1, 1));
		pb.add(getTxtNome(), cc.xy(3, 1));
		pb.add(new LabelPadrao("Celular:"), cc.xy(5, 1));
		pb.add(getTxtCelular(), cc.xy(7, 1));
		pb.setBackground(MedidasPadroes.COR_DE_FUNDO);
		
		return pb.getPanel();
	}
	
	private JPanel getPnlLinha2() {
		FormLayout layout = new FormLayout("pref, 5dlu, pref",
											"pref");
		PanelBuilder pb = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		
		pb.add(new LabelPadrao("E-Mail:"), cc.xy(1, 1));
		pb.add(getTxtEmail(), cc.xy(3, 1));
		pb.setBackground(MedidasPadroes.COR_DE_FUNDO);
		
		return pb.getPanel();
	}
	
	private JPanel getPnlSenhas() {
		FormLayout layout = new FormLayout("pref, 5dlu, pref, 45dlu, pref, 5dlu, pref",
											"pref");
		PanelBuilder pb = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		
		pb.add(new LabelPadrao("Senha:"), cc.xy(1, 1));
		pb.add(getTxtPw(), cc.xy(3, 1));
		pb.add(new LabelPadrao("Confirmar Senha:"), cc.xy(5, 1));
		pb.add(getTxtPwConf(), cc.xy(7, 1));
		pb.setBackground(MedidasPadroes.COR_DE_FUNDO);
		
		return pb.getPanel();
	}
	
	private JPasswordField getTxtPw() {
		if (txtPw == null) {
			txtPw = new JPasswordField();
			txtPw.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
		}
		return txtPw;
	}
	
	private JPasswordField getTxtPwConf() {
		if (txtPwConf == null) {
			txtPwConf = new JPasswordField();
			txtPwConf.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
		}
		return txtPwConf;
	}
	
	private TxtFieldGrande getTxtEmail() {
		if (txtEmail == null) {
			txtEmail = new TxtFieldGrande();
			txtEmail.setText(pai.func.getEmail());
		}
		return txtEmail;
	}

	private TxtFieldGrande getTxtNome() {
		if (txtNome == null) {
			txtNome = new TxtFieldGrande();
			txtNome.setText(pai.func.getNome());
		}
		return txtNome;
	}

	private JFormattedTextField getTxtCelular() {
		if (txtCel == null) {
			try {
				MaskFormatter mask = new MaskFormatter("(##)####-####");
				mask.setValueContainsLiteralCharacters(false);
				txtCel = new JFormattedTextField(mask);
				txtCel.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
				txtCel.setValue(pai.func.getCelular());
			} catch (ParseException ex) {
				return null;
			}
		}
		return txtCel;
	}
	
	private static String getSenhaCriptografada(String senha) {
        return Criptografia.criptografar(senha);
    }
    
    private static String getSenhaDescriptografada(String senha) {
        return Criptografia.descriptografar(senha);
    }

}
