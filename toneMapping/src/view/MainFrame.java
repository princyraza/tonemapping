package view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Controller;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseMotionAdapter;

public class MainFrame extends JFrame implements Observer
{
	Controller ctrl;
	
	public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    
    private JPanel imagePanel;
    private JPanel topMenu;
    private JPanel rightPanel;
    private JLabel brightnessLabel;
    private JPanel buttonPanel;
    private JButton resetButton;
    private JSlider brightSlider;
    private ImageIcon imgIcon;
    private JLabel image;
    private JMenuBar menuBar;
    private JMenu menu;
    private JLabel lblContrast;
    private JSlider contrastSlider;
    private JLabel boxFliterlabel;
    private JSlider boxFilterSlider;
    private JLabel gaussianLabel;
    private JSlider gaussianSlider;
    private JLabel medianLabel;
    private JSlider medianSlider;
    private JLabel bilateralLabel;
    private JSlider bilateralSlider;
    Graphics2D graphSettings;
    private JLabel brushSizeLabel;
    private JSlider brushSizeSlider;
    private int brushSize = 30; //default size of the brush
    
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
		
		resetButton = new JButton();
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.reset();
				resetSliders();
			}
		});
		resetButton.setText("Reset");
		rightPanel.add(resetButton);
		
		brushSizeLabel = new JLabel("Size of the brush");
		brushSizeLabel.setAlignmentY(0.0f);
		rightPanel.add(brushSizeLabel);
		
		brushSizeSlider = new JSlider();
		brushSizeSlider.setValue(30);
		brushSizeSlider.setMinimum(1);
		brushSizeSlider.setEnabled(false);
		brushSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    brushSize = source.getValue();
			}
		});
		brushSizeSlider.setAlignmentX(0.0f);
		rightPanel.add(brushSizeSlider);
		
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
			    ctrl.setBeta(beta);
			    ctrl.applySettings();
			}
		});
		brightSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(brightSlider);
		
		lblContrast = new JLabel("Contrast");
		rightPanel.add(lblContrast);
		
		contrastSlider = new JSlider();
		contrastSlider.setMinimum(0);
		contrastSlider.setMaximum(200);
		contrastSlider.setValue(100);
		contrastSlider.setEnabled(false);
		contrastSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    double alpha = (double)source.getValue()/100;
			    ctrl.setAlpha(alpha);
			    ctrl.applySettings();
			}
		});
		contrastSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(contrastSlider);
		
		boxFliterlabel = new JLabel("Box Filter");
		rightPanel.add(boxFliterlabel);
		
		boxFilterSlider = new JSlider();
		boxFilterSlider.setValue(0);
		boxFilterSlider.setMinimum(0);
		boxFilterSlider.setMaximum(200);
		boxFilterSlider.setEnabled(false);
		boxFilterSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				double radius = (double)source.getValue();
				ctrl.setBoxRadius(radius);
				ctrl.applySettings();
			}
		});
		boxFilterSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(boxFilterSlider);
		
		gaussianLabel = new JLabel("Gaussian Filter");
		rightPanel.add(gaussianLabel);
		
		gaussianSlider = new JSlider();
		gaussianSlider.setValue(0);
		gaussianSlider.setMinimum(0);
		gaussianSlider.setMaximum(100);
		gaussianSlider.setEnabled(false);
		gaussianSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				int gaussRadius = source.getValue();
				ctrl.setGaussRadius(gaussRadius);
				ctrl.applySettings();
			}
		});
		gaussianSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(gaussianSlider);
		
		medianLabel = new JLabel("Median Filter");
		rightPanel.add(medianLabel);
		
		medianSlider = new JSlider();
		medianSlider.setValue(0);
		medianSlider.setMinimum(0);
		medianSlider.setMaximum(100);
		medianSlider.setEnabled(false);
		medianSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				int ksize = source.getValue();
				ctrl.setMedianKSize(ksize);
				ctrl.applySettings();
			}
		});
		medianSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(medianSlider);
		
		bilateralLabel = new JLabel("Bilateral Filter");
		rightPanel.add(bilateralLabel);
		
		bilateralSlider = new JSlider();
		bilateralSlider.setValue(0);
		bilateralSlider.setMinimum(0);
		bilateralSlider.setMaximum(100);
		bilateralSlider.setEnabled(false);
		bilateralSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				int sigma = source.getValue();
				ctrl.setBilateralDiameter(sigma);
				ctrl.applySettings();
			}
		});
		bilateralSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(bilateralSlider);
		
		imgIcon = new ImageIcon();
		image = new JLabel(imgIcon);
		image.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point coord = e.getPoint();
				System.out.println(coord);
				ctrl.brushStroke(coord.x, coord.y,brushSize,brushSize);
			}
		});
		image.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point coord = e.getPoint();
				System.out.println(coord);
				ctrl.brushStroke(coord.x, coord.y,brushSize,brushSize);
			}
		});
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
		            resetSliders();
		            try {
						image.setIcon(new ImageIcon(ImageIO.read(file)));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this,
							    "Error",
							    "error",
							    JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
		        }
			}
		});
		menu.add(openImage);
		//to be implemented later
//		JMenuItem save = new JMenuItem("save");
	}
	
	

	public Controller getCtrl() {
		return ctrl;
	}



	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
	}
	
	private void resetSliders()
	{
		brightSlider.setEnabled(true);
		brightSlider.setValue(0);
        contrastSlider.setEnabled(true);
        contrastSlider.setValue(100);
        boxFilterSlider.setEnabled(true);
        gaussianSlider.setEnabled(true);
        gaussianSlider.setValue(0);
        medianSlider.setEnabled(true);
        medianSlider.setValue(0);
        bilateralSlider.setEnabled(true);
        bilateralSlider.setValue(0);
        brushSizeSlider.setEnabled(true);
        brushSizeSlider.setValue(30);
	}



	@Override
	public void update(Observable observable, Object arg1) {
		BufferedImage newImage = (BufferedImage) arg1;
		image.setIcon(new ImageIcon(newImage));
//		Point coord = image.getLocation();
//		System.out.println(image.getLocationOnScreen());
//		System.out.println(coord.x+","+coord.y);
	}
	

}
