package model;

import java.awt.image.BufferedImage;
import java.util.Observable;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ToneMapping extends Observable {
	/**
	 * The original image inputed by the user
	 */
	private Mat originalImage;
	
	/**
	 * The image modified by the user
	 */
	private Mat workingImage;
	
	private Mat weight;
	
	public ToneMapping(Mat originalImage)
	{
		this.originalImage=originalImage;
		this.workingImage = originalImage.clone();
		//weight = new Mat(originalImage.size(), originalImage.type());
		weight = Mat.zeros(originalImage.size(), originalImage.type());
	}
	
	/**
	 * Constructor
	 */
	public ToneMapping()
	{
		
	}

	public Mat getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(Mat originalImage) {
		this.originalImage = originalImage;
		this.workingImage = originalImage.clone();
		weight = Mat.zeros(originalImage.size(), CvType.CV_32FC1);
	}
	
	public Mat getWorkingImage() {
		return workingImage;
	}

	public void setWorkingImage(Mat workingImage) {
		this.workingImage = workingImage;
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
	
	/**
	 * Change the brightness of an image with a user inputed beta (brightness) value.
	 * @param image the original image
	 * @param beta the brightness value to be applied
	 * @return the edited image
	 */
	public Mat setBrightness(Mat image, int beta) {
		Mat newImage = new Mat(workingImage.size(), workingImage.type());
		double alpha=1;
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("beta: "+beta);
		image.convertTo(newImage, image.type(),alpha,beta);
		return newImage;
	}
	
	/**
	 * Change the brightness of an image with a user inputed alpha (contrast) value.
	 * @param image the original image
	 * @param alpha the contrast value to be applied
	 * @return the edited image
	 */
	public Mat setContrast(Mat image, double alpha) {
		Mat newImage = new Mat(workingImage.size(), workingImage.type());
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("alpha: "+alpha);
		image.convertTo(newImage, image.type(),alpha);
		return newImage;
	}
	
	
	/**
	 * Apply the box filter (blur) to an inputed image.
	 * @param image the inputed image
	 * @param ksize the size of the box kernel
	 * @return the edited image
	 */
	public Mat boxFilter(Mat image, Size ksize)
	{
		if(ksize.height>0 || ksize.width>0)
		{
			Mat newImage = new Mat(workingImage.size(), workingImage.type());
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("box radius:"+ksize);
			Imgproc.boxFilter(image, newImage, image.depth(), ksize);
//			Highgui.imwrite("newImage.png", newImage);
			return newImage;
		}
		return image;
	}
	
	/**
	 * Apply the gaussian filter (blur) to an inputed image. The user input the kernel size.
	 * sigmaX and sigmaY are calculated using the kernel.
	 * @param image the inputed image
	 * @param ksize the size of the gaussian kernel
	 * @return the edited image
	 */
	public Mat gaussianBlur(Mat image, Size ksize)
	{
		if(ksize.height>0 || ksize.width>0 && (ksize.width > 0 && ksize.width % 2 == 1 && ksize.height > 0 && ksize.height % 2 == 1))
		{
			Mat newImage = new Mat(workingImage.size(), workingImage.type());
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("gaussian radius: "+ksize);
			Imgproc.GaussianBlur(image, newImage, ksize, 0, 0); //sigmaX and sigmaY are calculated using the kernel size
			return newImage;
		}
		return image;
	}
	
	/**
	 * Apply the median filter (blur) to an inputed image.
	 * @param image the inputed image
	 * @param ksize the size of the median kernel
	 * @return the edited image
	 */
	public Mat medianBlur(Mat image, int ksize)
	{
		if(ksize % 2 == 1)
		{
			Mat newImage = new Mat(workingImage.size(), workingImage.type());
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("median radius: "+ksize);
			Imgproc.medianBlur(image, newImage, ksize);
			return newImage;
		}
		return image;
	}
	
	/**
	 * Apply the bilateral filter to an inputed image. 
	 * The user specifies a sigma value used both for sigmaColor (Filter sigma in the color space) and 
	 * sigmaSpace (Filter sigma in the coordinate space) .
	 * The size of the filter is set to 9 as recommended by the javadoc for offline applications.
	 * @param image the inputed image
	 * @param sigma the sigma inputed by the user
	 * @return the edited image
	 */
	public Mat bilateralFilter(Mat image, int sigma)
	{
		if(sigma > 0)
		{
			Mat newImage = new Mat(workingImage.size(), workingImage.type());
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("bilateral sigma: "+sigma);
			int sigmaColor = sigma;
			int sigmaSpace = sigma;
			Imgproc.bilateralFilter(image, newImage, 9, sigmaColor, sigmaSpace); //diameter = 9 as recommended for offline application, sigmas are defined by the user 
			return newImage;
		}
		return image;
	}
	
	/**
	 * Apply brightness, contrast, box filter, gaussian filter, median filter and bilateral filter to the original image inputed by the user.
	 * @param alpha
	 * @param beta
	 * @param radius
	 * @param gaussRadius
	 * @param ksize
	 * @param sigma
	 */
	public void applySettings(double alpha, int beta, double radius, double gaussRadius, int ksize, int sigma)
	{
		Mat image = workingImage;
		image=setContrast(workingImage, alpha);
		image = setBrightness(image, beta);
		if(radius>0)
		{
			Size boxKsize = new Size(radius, radius);
			image = boxFilter(image, boxKsize);
		}
		if(gaussRadius>0)
		{
			Size gaussKsize = new Size(gaussRadius, gaussRadius);
			image = gaussianBlur(image, gaussKsize);
		}
		if(ksize>0)
		{
			image = medianBlur(image,ksize);
		}
		if(sigma>0)
		{
			image = bilateralFilter(image, sigma);
		}
		
		setChanged();
		notifyObservers(matToBufferedImage(image));
	}
	
	/**
	 * Draw a brush stroke on the image.
	 * @param x x-coordinate of the stroke
	 * @param y y-coordinate of the stroke
	 * @param width width of the stroke (radius for a circle)
	 * @param height height of the stroke (radius for a circle)
	 */
	public void brushStroke(int x, int y, int width, int height)
	{
//		Rect roi = new Rect(x, y, width, height);
//		Mat submat = originalImage.submat(roi);
		Core.circle(weight, new Point(x, y), width, new Scalar(1),Core.FILLED); //fill out the weight matrix
//		System.out.println(weight.dump());
		Core.circle(workingImage, new Point(x, y), width, new Scalar(0, 255, 0),Core.FILLED);
		//Core.rectangle(workingImage, new Point(roi.x, roi.y), new Point(roi.x + roi.width, roi.y + roi.height), new Scalar(0, 255, 0),Core.FILLED);
		setChanged();
		notifyObservers(matToBufferedImage(workingImage));
	}
	
	public void reset()
	{
		workingImage = originalImage.clone();
		setChanged();
		notifyObservers(matToBufferedImage(workingImage));
	}

}