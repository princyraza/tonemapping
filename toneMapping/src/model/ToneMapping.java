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
	
	public void brightness(int beta)
	{
	    double alpha=0; //contrast not changed
	    double[] oldData=new double[3];
	    double[] newData=new double[3];
	    // Load the native library.
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		Mat newImage = new Mat(originalImage.size(), originalImage.type());
		for( int y = 0; y < originalImage.rows(); y++ )
		{ 
			for( int x = 0; x < originalImage.cols(); x++ )
		    { 
				oldData=originalImage.get(y, x);
				
				newData[0]=alpha*oldData[0]+beta;
				newData[1]=alpha*oldData[1]+beta;
				newData[2]=alpha*oldData[2]+beta;
				originalImage.put(x, y, newData);
		    }
	    }
		originalImage.convertTo(newImage, originalImage.type());
		
	    String filename = "ressources/newImage.png";
	    System.out.println(String.format("Writing %s", filename));
	    Highgui.imwrite(filename, newImage);
	}

}
