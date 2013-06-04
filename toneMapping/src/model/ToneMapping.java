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
		System.out.println("beta: "+beta);
		image.convertTo(newImage, image.type(),alpha,beta);
//		Highgui.imwrite("newImage.png", newImage);
		return newImage;
	}

	public Mat setContrast(Mat image, double alpha) {
		Mat newImage = new Mat(originalImage.size(), originalImage.type());
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("alpha: "+alpha);
		image.convertTo(newImage, image.type(),alpha);
//		Highgui.imwrite("newImage.png", newImage);
		return newImage;
	}
	
	public Mat boxFilter(Mat image, Size ksize)
	{
		if(ksize.height>0 || ksize.width>0)
		{
			Mat newImage = new Mat(originalImage.size(), originalImage.type());
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("box radius:"+ksize);
			Imgproc.boxFilter(image, newImage, image.depth(), ksize);
//			Highgui.imwrite("newImage.png", newImage);
			return newImage;
		}
		return image;
	}
	
	public Mat gaussianBlur(Mat image, Size ksize)
	{
		if(ksize.height>0 || ksize.width>0 && (ksize.width > 0 && ksize.width % 2 == 1 && ksize.height > 0 && ksize.height % 2 == 1))
		{
			Mat newImage = new Mat(originalImage.size(), originalImage.type());
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("gaussian radius: "+ksize);
			Imgproc.GaussianBlur(image, newImage, ksize, 0, 0); //sigmaX and sigmaY are calculated using the kernel size
			return newImage;
		}
		return image;
	}
	
	public void applySettings(double alpha, int beta, double radius, double gaussRadius)
	{
		Mat image = setContrast(originalImage, alpha);
		image = setBrightness(image, beta);
		Size boxKsize = new Size(radius, radius);
		image = boxFilter(image, boxKsize);
		Size gaussKsize = new Size(gaussRadius, gaussRadius);
		image = gaussianBlur(image, gaussKsize);
		setChanged();
		notifyObservers(matToBufferedImage(image));
	}
	
	
	
	

}
