package gui.componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class Link extends JLabel{


	public Link(String label) {
		super(label);
		this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		this.setForeground(Color.LIGHT_GRAY);
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				Link.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
				Link.this.setForeground(Color.red);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Link.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				Link.this.setForeground(Color.LIGHT_GRAY);
			}
		});		
	}
	
	public Link getLink() {
		return this;
	}

}
