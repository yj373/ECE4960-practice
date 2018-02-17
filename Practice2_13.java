package homeworkAndPractice;

public class Practice2_13 {

	public static void main(String[] args) {
		// The forth Hacker Practice of Note 3
		double res1=rectangle(-1,1,0.1);
		double reErr1= relativeError(res1, 2.3504);
		System.out.println("Schemes: Rectangle"+"\tApproximation: "+res1+"\tRelative Error: "+ reErr1+"\n");
		double res2=trapezoid(-1,1,0.1);
		double reErr2= relativeError(res2, 2.3504);
		System.out.println("Schemes: Trapezoid"+"\tApproximation: "+res2+"\t\tRelative Error: "+ reErr2+"\n");
		double res3=midPoint(-1,1,0.1);
		double reErr3= relativeError(res3, 2.3504);
		System.out.println("Schemes: Mid-point"+"\tApproximation: "+res3+"\tRelative Error: "+ reErr3+"\n");
		double res4=simpson(-1,1,0.1);
		double reErr4= relativeError(res4, 2.3504);
		System.out.println("Schemes: Simpson"+"\t\tApproximation: "+res4+"\tRelative Error: "+ reErr4+"\n");
		double res5=twoPointGaussian(-1,1,0.1);
		double reErr5= relativeError(res5, 2.3504);
		System.out.println("Schemes: 2-point Daussian"+"\tApproximation: "+res5+"\t\tRelative Error: "+ reErr5+"\n");
		

	}
	//functions for the forth Hacker Practice of Note 3
	public static double function(double x) {
		return Math.exp(x);
	}
	public static double relativeError(double y, double truth) {
		return Math.abs(y-truth)/truth;
	}
	public static double rectangle(double x1, double x2, double h) {
		double res=0;
		for(double i= x1; i<x2; i=i+h) {
			res= res+function(i)*h;
		}
		return res;
	}
	public static double trapezoid(double x1, double x2, double h) {
		double res=0;
		for(double i= x1; i+h<=x2; i=i+h) {
			res= res+(function(i)+function(i+h))*h/2.0;
		}
		return res;
	}
	public static double midPoint(double x1, double x2, double h) {
		double res=0;
		for(double i= x1; i+h<=x2; i=i+h) {
			res= res+(function(i+h/2.0))*h;
		}
		return res;
	}
	public static double simpson(double x1, double x2, double h) {
		double res=0;
		for(double i= x1; i+h<=x2; i=i+h) {
			res= res+h*(function(i)/6.0+4*function(i+h/2.0)/6.0+function(i+h)/6.0);
		}
		return res;
	}
	public static double twoPointGaussian(double x1, double x2, double h) {
		double res=0;
		for(double i= x1; i+h<=x2; i=i+h) {
			res= res+h*(function(i+h/2.0-h/(2*Math.sqrt(3)))+function(i+h/2.0+h/(2*Math.sqrt(3))))/2.0;
		}
		return res;
	}
	
}
