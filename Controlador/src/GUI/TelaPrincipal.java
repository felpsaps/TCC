/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dao.FuncionarioDao;
import dao.VagaDAO;

/**
 *
 * @author Felps
 */
public class TelaPrincipal extends JFrame{
    
	public final JLabel lbl = new JLabel();
    final JTextField txt = new JTextField();
	
    public TelaPrincipal() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        init();
        setVisible(true);
        new EstatisticaDiaria().start();
    }
    
    public void init() {
        txt.requestFocus();
        txt.addKeyListener(new Controle());
        txt.setPreferredSize(new Dimension(0, 0));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 300));
        add(lbl);
        add(txt);
    }
    
    private class Controle extends KeyAdapter {
    	@Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                try {
                    new FuncionarioDao().selectLoginESenha(txt.getText(), TelaPrincipal.this);
                    Thread t = new Thread(new Runnable() {
            			
            			@Override
            			public void run() {				
            				try {
            		            txt.setText("");
            		            txt.requestFocus();
            		    		Thread.sleep(5000);
                                lbl.setText("");
            	    		} catch (InterruptedException ex) {
            	    			ex.printStackTrace();
            	    		}
            			}
            		});
                    t.start();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }    	
    }
    
    private class EstatisticaDiaria extends Thread {
    	@Override
    	public void run() {
    		//Calendar data = new GregorianCalendar().getInstance(new Locale("pt", "BR"));
    		
    		while (true) {
    			//if (data.get(Calendar.HOUR_OF_DAY) > 23) {
    				new VagaDAO().insertEstatisticaDiaria();
    				
    			//}
    			try {
					Thread.sleep(1000 * 60 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
}
