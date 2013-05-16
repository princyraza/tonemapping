package view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.JToolBar;

public class MainFrame extends JFrame implements Observer
{
	public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    
	public MainFrame() {
		
		setTitle("Intereactive local tone adjustment");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel imagePanel = new JPanel();
		getContentPane().add(imagePanel, BorderLayout.CENTER);
		
		JPanel topMenu = new JPanel();
		getContentPane().add(topMenu, BorderLayout.NORTH);
		
		JPanel leftPanel = new JPanel();
		getContentPane().add(leftPanel, BorderLayout.EAST);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		JLabel contrastLabel = new JLabel("Brightness");
		contrastLabel.setAlignmentY(Component.LEFT_ALIGNMENT);
		leftPanel.add(contrastLabel);
		
		JSlider brightSlider = new JSlider();
		brightSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftPanel.add(brightSlider);
		
		ImageIcon imgIcon = new ImageIcon("/Users/Princy/Documents/workspace/toneMapping/ressources/lena.png");
		JLabel image = new JLabel(imgIcon);
		imagePanel.add(image);
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
