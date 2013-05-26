package model;

import java.awt.image.BufferedImage;
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
	
	/**
	 * Converts/writes a Mat into a BufferedImage.
	 * 
	 * @param matrix Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */
	public static BufferedImage matToBufferedImage(Mat matrix) {
	    int cols = matrix.cols();
	    int rows = matrix.rows();
	    int elemSize = (int)matrix.elemSize();
	    byte[] data = new byte[cols * rows * elemSize];
	    int type;

	    matrix.get(0, 0, data);

	    switch (matrix.channels()) {
	        case 1:
	            type = BufferedImage.TYPE_BYTE_GRAY;
	            break;

	        case 3: 
	            type = BufferedImage.TYPE_3BYTE_BGR;

	            // bgr to rgb
	            byte b;
	            for(int i=0; i<data.length; i=i+3) {
	                b = data[i];
	                data[i] = data[i+2];
	                data[i+2] = b;
	            }
	            break;

	        default:
	            return null;
	    }

	    BufferedImage image = new BufferedImage(cols, rows, type);
	    image.getRaster().setDataElements(0, 0, cols, rows, data);

	    return image;
	}

	public void setBrightness(int beta) {
		
		double alpha=1;
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		Mat newImage = new Mat(originalImage.size(), originalImage.type());
		System.out.println(beta);
		originalImage.convertTo(newImage, originalImage.type(),alpha,beta);
	    setChanged();
	    notifyObservers(matToBufferedImage(newImage));
	}

	public void setContrast(double alpha) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		Mat newImage = new Mat(originalImage.size(), originalImage.type());
		System.out.println(alpha);
		originalImage.convertTo(newImage, originalImage.type(),alpha);
	    setChanged();
	    notifyObservers(matToBufferedImage(newImage));
		
	}
	
	

}
