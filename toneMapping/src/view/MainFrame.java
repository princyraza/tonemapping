package view;

import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.MenuItem;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import controler.Controller;

public class MainFrame extends JFrame implements Observer
{
	Controller ctrl;
	
	public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    
    private JPanel imagePanel;
    private JPanel topMenu;
    private JPanel rightPanel;
    private JLabel brightnessLabel;
    private JSlider brightSlider;
    private ImageIcon imgIcon;
    private JLabel image;
    private JMenuBar menuBar;
    private JMenu menu;
    private JLabel lblContrast;
    private JSlider sliderContrast;
    
	public MainFrame() {
		
		setTitle("Intereactive local tone adjustment");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		imagePanel = new JPanel();
		getContentPane().add(imagePanel, BorderLayout.CENTER);
		
		topMenu = new JPanel();
		getContentPane().add(topMenu, BorderLayout.NORTH);
		
		rightPanel = new JPanel();
		getContentPane().add(rightPanel, BorderLayout.EAST);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		brightnessLabel = new JLabel("Brightness");
		brightnessLabel.setAlignmentY(Component.LEFT_ALIGNMENT);
		rightPanel.add(brightnessLabel);
		
		brightSlider = new JSlider();
		brightSlider.setMaximum(100);
		brightSlider.setMinimum(-100);
		brightSlider.setValue(0);
		brightSlider.setEnabled(false);
		brightSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    int beta = (int)source.getValue();
			    ctrl.setBrightness(beta);
			}
		});
		brightSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(brightSlider);
		
		lblContrast = new JLabel("Contrast");
		rightPanel.add(lblContrast);
		
		sliderContrast = new JSlider();
		sliderContrast.setMinimum(0);
		sliderContrast.setMaximum(200);
		sliderContrast.setValue(100);
		sliderContrast.setEnabled(false);
		sliderContrast.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    double alpha = (double)source.getValue()/100;
			    ctrl.setContrast(alpha);
			}
		});
		sliderContrast.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(sliderContrast);
		
		imgIcon = new ImageIcon();
		image = new JLabel(imgIcon);
		imagePanel.add(image);
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menu);
		
		JMenuItem openImage = new JMenuItem("open");
		openImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            ctrl.setOriginalImage(file.getAbsolutePath());
		            brightSlider.setEnabled(true);
		            brightSlider.setValue(0);
		            sliderContrast.setEnabled(true);
		            sliderContrast.setValue(100);
		            try {
						image.setIcon(new ImageIcon(ImageIO.read(file)));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this,
							    "Eggs are not supposed to be green.",
							    "Inane error",
							    JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
		        }
			}
		});
		menu.add(openImage);
	}
	
	

	public Controller getCtrl() {
		return ctrl;
	}



	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
	}



	@Override
	public void update(Observable observable, Object arg1) {
		BufferedImage newImage = (BufferedImage) arg1;
		image.setIcon(new ImageIcon(newImage));
	}
	

}
