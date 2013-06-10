package controller;

import model.ToneMapping;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Controller {
	private ToneMapping toneMapping;
	private int beta = 0;
	private double alpha = 1;
	private double boxRadius = 0;
	private double gaussRadius = 0;
	private int medianKSize = 0;
	private int bilateralDiameter;
	
	public Controller()
	{
		//can't read an image without this
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
	
	public int getBeta() {
		return beta;
	}


	public void setBeta(int beta) {
		this.beta = beta;
	}


	public double getAlpha() {
		return alpha;
	}


	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}


	public double getBoxRadius() {
		return boxRadius;
	}


	public void setBoxRadius(double boxRadius) {
		this.boxRadius = boxRadius;
	}
	
	public void setGaussRadius(int gaussRadius) {
		if(gaussRadius % 2 == 0)
		{
			gaussRadius++;
		}
		this.gaussRadius=gaussRadius;
		
	}
	
	public int getMedianKSize() {
		return medianKSize;
	}


	public void setMedianKSize(int medianKSize) {
		if(medianKSize % 2==0 && medianKSize!=0)
		{
			medianKSize++; //to get an odd number
		}
		this.medianKSize = medianKSize;
	}


	public double getGaussRadius() {
		return gaussRadius;
	}


	public void applySettings()
	{
		toneMapping.applySettings(alpha, beta, boxRadius, gaussRadius, medianKSize, bilateralDiameter);
	}
	
	public void brushStroke(int x, int y, int width, int height)
	{
		toneMapping.brushStroke(x,y,width,height);
	}


	public void setBilateralDiameter(int bilateralDiameter) {
		this.bilateralDiameter = bilateralDiameter;
		
	}
	
}
