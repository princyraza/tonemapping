package controler;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import view.MainFrame;

import model.*;

public class Controller {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//	    System.out.println("Hello, OpenCV");
//	    double alpha=7;
//	    int beta=0;
//	    double[] oldData=new double[3];
//	    double[] newData=new double[3];
//	    // Load the native library.
//	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
////	    new DetectFaceDemo().run();
//	    
//		Mat image = Highgui.imread("/Users/Princy/Documents/workspace/opencvjava/src/lena.png");
//		Mat new_image = new Mat(image.size(), image.type());
//		for( int y = 0; y < image.rows(); y++ )
//		{ 
//			for( int x = 0; x < image.cols(); x++ )
//		    { 
//				oldData=image.get(y, x);
//				newData[0]=alpha*oldData[0]+beta;
//				newData[1]=alpha*oldData[1]+beta;
//				newData[2]=alpha*oldData[2]+beta;
//				new_image.put(x, y, newData);
//		    }
//	    }
//		// Save the visualized detection.
//	    String filename = "newImage.png";
//	    System.out.println(String.format("Writing %s", filename));
//	    Highgui.imwrite(filename, new_image);
		
		EventQueue.invokeLater(new Runnable()
        {
            public void run(){
                MainFrame frame = new MainFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }
        );
	  }

}
