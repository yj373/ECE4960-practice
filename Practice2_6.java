
public class Practice2_6 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("for the function f(x)=x^2: ");
		for(double h=0.1; h>=Math.pow(10, -18); h=h/10) {
			double d_1= derivative_1(1,0,0,1,h);
			double e_1= relativeErr(d_1, 2);

			double d_2= derivative_2(1,0,0,1,h);
			double e_2= relativeErr(d_2, 2);
			
			System.out.println("h= "+h+"\tfirst-order derivative: "+d_1+"\trelative error: "+e_1+
								"\tsecond-order derivative: "+d_2+"\trelative error: "+e_2);
		}

	}
	public static double function(double a, double b, double c, double x) {
		return a*x*x+b*x+c;
	}
	public static double derivative_1(double a, double b, double c, double x, double h) {
		double f_1= function(a,b,c,x);
		double f_2= function(a,b,c,x+h);
		return (f_2-f_1)/h;
	}
	public static double derivative_2(double a, double b, double c, double x, double h) {
		double f_1= function(a,b,c,x-h);
		double f_2= function(a,b,c,x+h);
		return (f_2-f_1)/(2*h);
	}
	public static double relativeErr(double cal, double stad) {
		return Math.abs(cal-stad)/stad;
	}

}
