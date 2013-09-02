import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import principal.VagaBean;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import dao.VagaDAO;


public class Monitor extends JFrame{

	public static final String DISPONIVEL = "_disp.png";
	public static final String OCUPADA = "_ocu.png";
	public static final String RESERVADA = "_res.png";
	
	private JLabel vaga1;
	private JLabel vaga2;
	private JLabel vaga3;
	private JLabel vaga4;
	private JLabel vaga5;
	private JLabel vaga6;
	private JLabel vaga7;
	private JLabel vaga8;
	
	private List<JLabel> vagasLbl;
	
	public Monitor() {
		super("Monitor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //setIconImage(new ImageIcon(getClass().getClassLoader().getResource("arquivos/loginIcone.png")).getImage());
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
	}
	
	public void init() {
		JPanel painel = new ImagemFundo();
		FormLayout layout = new FormLayout("145dlu, pref, 15dlu, pref, 20dlu, pref, 17dlu, pref",
                						   "120dlu, pref, 93dlu, pref");
		painel.setLayout(layout);
		CellConstraints cc = new CellConstraints();
		
		JPanel vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga1 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/1_disp.png")));
		vaga1.setName("1");
		vaga.add(vaga1);		
		painel.add(vaga, cc.xy(2, 2));
		
		vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga2 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/2_disp.png")));
		vaga2.setName("2");
		vaga.add(vaga2);
		painel.add(vaga, cc.xy(4, 2));
		
		vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga3 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/3_disp.png")));
		vaga3.setName("3");
		vaga.add(vaga3);
		painel.add(vaga, cc.xy(6, 2));
		
		vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga4 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/4_disp.png")));
		vaga4.setName("4");
		vaga.add(vaga4);
		painel.add(vaga, cc.xy(8, 2));
		
		vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga5 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/5_disp.png")));
		vaga5.setName("5");
		vaga.add(vaga5);
		painel.add(vaga, cc.xy(2, 4));
		
		vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga6 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/6_disp.png")));
		vaga6.setName("6");
		vaga.add(vaga6);
		painel.add(vaga, cc.xy(4, 4));
		
		vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga7 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/7_disp.png")));
		vaga7.setName("7");
		vaga.add(vaga7);
		painel.add(vaga, cc.xy(6, 4));
		
		vaga = new JPanel();
		vaga.setPreferredSize(new Dimension(85,90));
		vaga8 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/8_disp.png")));
		vaga8.setName("8");
		vaga.add(vaga8);
		painel.add(vaga, cc.xy(8, 4));
		
		vagasLbl = new ArrayList<JLabel>();
		vagasLbl.add(vaga1);
		vagasLbl.add(vaga2);
		vagasLbl.add(vaga3);
		vagasLbl.add(vaga4);
		vagasLbl.add(vaga5);
		vagasLbl.add(vaga6);
		vagasLbl.add(vaga7);
		vagasLbl.add(vaga8);
		
		this.add(painel);
		new MonitorProcess().start();
	}
	
	private class MonitorProcess extends Thread {
		
		@Override
		public void run() {
			
			while (true) {
				for (VagaBean vg : new VagaDAO().selectVagas()) {
					JLabel vagaLbl = vagasLbl.get(vg.getNro() -1);
					// VAGA OCUPADA
					if (vg.getDisponibilidade().equals(0)) {
						vagaLbl.setIcon(
								new ImageIcon(getClass().getClassLoader().getResource("imgs/" + vagaLbl.getName() + OCUPADA)));
						vagaLbl.repaint();
					} else {
						// VAGA DISPONIVEL
						if (vg.getUsrReservadoId() != null) {
							// VAGA RSERVADA
							vagaLbl.setIcon(
									new ImageIcon(getClass().getClassLoader().getResource("imgs/" + vagaLbl.getName() + RESERVADA)));
							vagaLbl.repaint();
						} else {
							// VAGA DISPONIVEL
							vagaLbl.setIcon(
									new ImageIcon(getClass().getClassLoader().getResource("imgs/" + vagaLbl.getName() + DISPONIVEL)));
							vagaLbl.repaint();
						}
					}
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}			
		}
	}
	
	private class ImagemFundo extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Image imagemLogin = new ImageIcon(getClass().getClassLoader().getResource("imgs/monitor.png")).getImage();

            g.drawImage(imagemLogin, 0, 0, this);
            vaga1.repaint();
            vaga2.repaint();
            vaga3.repaint();
            vaga4.repaint();
            vaga5.repaint();
            vaga6.repaint();
            vaga7.repaint();
            vaga8.repaint();
        }
    }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Monitor();
			}
		});
	}

}
