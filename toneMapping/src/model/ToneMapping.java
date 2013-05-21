package model;

import java.util.Observable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class ToneMapping extends Observable {
	
	private Mat originalImage;
	
	public ToneMapping(Mat originalImage)
	{
		this.originalImage=originalImage;
	}
	
	public ToneMapping()
	{
		
	}

	public Mat getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(Mat originalImage) {
		this.originalImage = originalImage;
	}

	public void setBrightness(int beta) {
		
		double alpha=1; //contrast not changed
//	    double[] oldData=new double[3];
//	    double[] newData=new double[3];
	    // Load the native library.
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		Mat newImage = new Mat(originalImage.size(), originalImage.type());
		System.out.println(beta);
		originalImage.convertTo(newImage, originalImage.type(),alpha,beta);
		
	    String filename = "ressources/newImage.png";
	    Highgui.imwrite(filename, newImage);
	    setChanged();
	    notifyObservers();
	}
	
	

}
