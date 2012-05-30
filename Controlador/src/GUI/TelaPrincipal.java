/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import dao.FuncionarioDao;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author Felps
 */
public class TelaPrincipal extends JFrame{
    
    public TelaPrincipal() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(200, 200);
        init();
        setVisible(true);
    }
    
    public void init() {
        final JTextField txt = new JTextField();
        txt.requestFocus();
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    try {
                        new FuncionarioDao().selectLoginESenha(txt.getText());
                        Thread.sleep(5000);
                        txt.setText("");
                        txt.requestFocus();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        txt.setPreferredSize(new Dimension(0, 0));
        setLayout(new FlowLayout());
        add(txt);
    }
    
}
