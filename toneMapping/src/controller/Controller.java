package controller;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import view.MainFrame;

import model.*;

public class Controller {
	private ToneMapping toneMapping;
	private int beta = 0;
	private double alpha = 1;
	private double radius = 0;
	
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


	public double getRadius() {
		return radius;
	}


	public void setRadius(double radius) {
		this.radius = radius;
	}


//	public void setBrightness(int beta)
//	{
//		toneMapping.setBrightness(beta);
//	}
//
//	public void setContrast(double alpha) {
//		toneMapping.setContrast(alpha);
//	}
//	
//	public void boxFilter(double radius)
//	{
//		Size ksize = new Size(radius, radius);
//		toneMapping.boxFilter(ksize);
//	}
//	
	public void applySettings()
	{
		toneMapping.applySettings(alpha, beta, radius);
	}

}
