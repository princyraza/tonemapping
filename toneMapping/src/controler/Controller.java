package controler;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import view.MainFrame;

import model.*;

public class Controller {
	private ToneMapping toneMapping;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    System.out.println("Hello, OpenCV");
	    double alpha=2;
	    int beta=0;
	    double[] oldData=new double[3];
	    double[] newData=new double[3];
	    // Load the native library.
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		Mat image = Highgui.imread("/Users/Princy/Documents/workspace/opencvjava/src/lena.png");
		Mat newImage = new Mat(image.size(), image.type());
		image.convertTo(newImage, image.type(),alpha,beta);
		// Save the visualized detection.
	    String filename = "newImage.png";
	    System.out.println(String.format("Writing %s", filename));
	    Highgui.imwrite(filename, newImage);
	}
	
	public Controller()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	
	public ToneMapping getToneMapping() {
		return toneMapping;
	}


	public void setToneMapping(ToneMapping toneMapping) {
		this.toneMapping = toneMapping;
	}


	public void setOriginalImage(String filePath)
	{
		System.out.println(filePath);
		Mat image = Highgui.imread(filePath);
		toneMapping.setOriginalImage(image);
	}
	
	public void setBrightness(int beta)
	{
		toneMapping.setBrightness(beta);
	}

	public void setContrast(double alpha) {
		toneMapping.setContrast(alpha);
		
	}

}
