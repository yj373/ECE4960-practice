package homeworkAndPractice;

public class Practice2_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Generate NaN and INF in double
		//The outputs are: res1=Infinity and res2=NaN. 
		double x= 0.0;
		double y= 0.0;
		double res1= 1/x;
		double res2= y/x;
		System.out.println("res1="+res1);
		System.out.println("res2="+res2);
		
		
		//Observe NaN and INF handling in integers 
		//An ArithmeticExcepyion (/ by zero) is thrown. 
		try {
			long m= 0;
			long n= 0;
			long intRes1= 1/m;
			long intRes2= m/n;
			System.out.println("intRes1="+intRes1);
			System.out.println("intRes2="+intRes2);
		}catch(ArithmeticException e) {
			System.out.println(e.getMessage());
			System.out.println("An ArithmeticException is thrown!");
		}
		
		// Observe overflow handling in integers
		// The overflow happens when we compute 20!
		long intFactorial=1;
		for(long i=2; i<20; i++) {
			intFactorial*=i;
			System.out.print(i+" ");
			System.out.println(intFactorial);
		}
		for(long i=20; i>1; i--) {
			intFactorial/=i;
			System.out.print(i+" ");
			System.out.println(intFactorial);
		}
		
		//Since log(+0)=NINF, log(-0)=NaN, we can use this property to write the functions:
		//isPositiveZero(x) and isNegativeZero(x).
		//These two functions can distinguish +0 and -0 from +1.0, -1.0, DBL_MAX, -1.0*DBL_MAX, INF, NINF and NAN.
		double k= Double.NaN;
		System.out.println(isPositiveZero(k));
		System.out.println(isNegativeZero(k));
		
		//Observe the loss of precision, caused by underflow
		//Make x3 with easily observable precision
		double x3= 1.234567890123456;
		//The normalized number is above 4.9407*10^(-324)
		x3*= Math.pow(10, -307);
		//Decrease the normalized number to the range of denormals
		for (int j=1; j<20; j++) {
			x3/=10.0;
			System.out.println(x3);
		}
		
	}
	private static boolean isPositiveZero(double x) {
		return Math.log(x)==Double.NEGATIVE_INFINITY;
	}
	private static boolean isNegativeZero(double x) {
		return Math.log(x)==Double.NaN;
	}

}