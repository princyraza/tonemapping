package controler;

import java.awt.EventQueue;

import javax.swing.JFrame;

import model.ToneMapping;

import view.MainFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable()
        {
            public void run(){
            	ToneMapping toneMap = new ToneMapping();
            	Controller ctrl = new Controller();
            	ctrl.setToneMapping(toneMap);
                MainFrame frame = new MainFrame();
                toneMap.addObserver(frame);
                
                frame.setCtrl(ctrl);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }
        );
	}

}
