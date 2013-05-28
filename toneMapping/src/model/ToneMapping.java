package model;

import java.awt.image.BufferedImage;
import java.util.Observable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

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

	public Mat setBrightness(Mat image, int beta) {
		Mat newImage = new Mat(originalImage.size(), originalImage.type());
		double alpha=1;
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println(beta);
		image.convertTo(newImage, image.type(),alpha,beta);
		Highgui.imwrite("newImage.png", newImage);
		return newImage;
//	    setChanged();
//	    notifyObservers(matToBufferedImage(newImage));
	}

	public Mat setContrast(Mat image, double alpha) {
		Mat newImage = new Mat(originalImage.size(), originalImage.type());
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println(alpha);
		image.convertTo(newImage, image.type(),alpha);
		Highgui.imwrite("newImage.png", newImage);
		return newImage;
//	    setChanged();
//	    notifyObservers(matToBufferedImage(newImage));
	}
	
	public Mat boxFilter(Mat image, Size ksize)
	{
		if(ksize.height>0 || ksize.width>0)
		{
			Mat newImage = new Mat(originalImage.size(), originalImage.type());
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			Imgproc.boxFilter(image, newImage, image.depth(), ksize);
			Highgui.imwrite("newImage.png", newImage);
			return newImage;
		}
		return image;
//		setChanged();
//		notifyObservers(matToBufferedImage(newImage));
	}
	
	public void applySettings(double alpha, int beta, double radius)
	{
		Mat image = setContrast(originalImage, alpha);
		image = setBrightness(image, beta);
		Size ksize = new Size(radius, radius);
		image = boxFilter(image, ksize);
		setChanged();
		notifyObservers(matToBufferedImage(image));
	}
	
	
	
	

}
