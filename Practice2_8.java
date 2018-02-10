
public class Practice2_8 {
	public static void main (String[] args) {
		//The first Hacker practice on Note3
		for(double delta_t=0.5; delta_t<=2; delta_t*=2) {
			System.out.println("when delta_t= "+delta_t);
			for(double t=0; t<=20; t++) {
				double gt= groundTruth(t);
				double fe= forwardEuler(t,delta_t);
				double be= backwardEuler(t,delta_t);
				System.out.println("t= "+t+"\tground truth: "+gt+"\tFoward Euler:"+fe+"\tBack Euler:"+be);
			}
		}
		
		//The second Hacker practice on Note3
		for(double i=-4; i>=-40;i--) {
			double h= Math.pow(2, i);
			
			double d_1=derivative_1(h, 1);
			double d_2=derivative_2(h, 1);
			double d_3=derivative_3(h, 1);
			
			double re_1=relativeError(d_1);
			double re_2=relativeError(d_2);
			double re_3=relativeError(d_3);
			
			double n11=RCoefficient_1(d_1, derivative_1(2*h, 1));
			double n12=RCoefficient_1(d_2, derivative_2(2*h, 1));
			double n13=RCoefficient_1(d_3, derivative_3(2*h, 1));
			
			double n21=RCoefficient_2(derivative_1(4*h, 1), derivative_1(2*h, 1), derivative_1(h, 1));
			double n22=RCoefficient_2(derivative_2(4*h, 1), derivative_2(2*h, 1), derivative_2(h, 1));
			double n23=RCoefficient_2(derivative_3(4*h, 1), derivative_3(2*h, 1), derivative_3(h, 1));
			
			System.out.println("h= "+h);
			System.out.println("first approximation: "+d_1+"\tsecond approximation: "+d_2+"\tthird approximation: "+d_3);
			System.out.println("first relative error: "+re_1+"\tsecond relative error: "+re_2+"\tthird relative error: "+re_3);
			System.out.println("first approximation n1: "+n11+"\tsecond approximation n1: "+n12+"\tthird approximation n1:"+n13);
			System.out.println("first approximation n2: "+n21+"\tsecond approximation n2: "+n22+"\tthird approximation n2:"+n23);
		}
		
		
	}
	public static double groundTruth(double t) {
		return Math.pow(Math.E, -t);
	}
	public static double forwardEuler(double t, double delta_t) {
		double res=1;
		for(double i=0; i<t; i=i+delta_t) {
			res= (1-delta_t)*res;
		}
		return res;
		
	}
	public static double backwardEuler(double t, double delta_t) {
		double res=1;
		for(double i=0; i<t; i=i+delta_t) {
			res= res/(1+delta_t);
		}
		return res;
		
	}
	
	
	public static double function(double x) {
		return Math.pow(x, 3);
	}
	public static double derivative_1(double h, double x) {
		return (function(x+h)-function(x))/h;
	}
	public static double derivative_2(double h, double x) {
		return (function(x+2*h)-function(x))/(2*h);
	}
	public static double derivative_3(double h, double x) {
		return -function(x+2*h)/(2*h)-3*function(x)/(2*h)+2*function(x+h)/h;
	}
	public static double relativeError(double d) {
		return Math.abs(d-3)/3;
	}
	public static double RCoefficient_1(double d_h, double d_2h) {
		return (d_2h-3)/(d_h-3);
	}
	public static double RCoefficient_2(double a_4h, double a_2h, double a_h) {
		return (a_4h-a_2h)/(a_2h-a_h);
	}
	

}
