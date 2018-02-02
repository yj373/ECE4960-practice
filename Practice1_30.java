public class Practice1_30 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Declare three constants for the quadratic equation
		final double a=1E-20;
		final double b=1E3;
		final double c=1E3;
		
		//This is the first way to solve the equation, and the outputs are:
		//x11= 0.0, x12= -1.0000000000000001E23
		double x11= (-b+Math.sqrt(b*b-4*a*c))/(2*a);
		double x12= (-b-Math.sqrt(b*b-4*a*c))/(2*a);
		System.out.println("x11="+x11);
		System.out.println("x12="+x12);
		
		//This the second way to solve the equation, and the outputs are:
		//x21= Infinity, x22= -1.0
		double x21= (2*c)/(-b+Math.sqrt(b*b-4*a*c));
		double x22= (2*c)/(-b-Math.sqrt(b*b-4*a*c));
		System.out.println("x21="+x21);
		System.out.println("x22="+x22);
		
		//This the second way to solve the equation, and the outputs are:
		//x31= -1.0, x32= -1.0000000000000001E23
		double x31= -c/b;
		double x32= -b/a+c/b;
		System.out.println("x31="+x31);
		System.out.println("x32="+x32);
	}

}