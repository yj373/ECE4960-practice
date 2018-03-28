package hackerPracticeWeek6;

public class Pravtice3_8 {
//The outputs of today's Hacker Practice is in the file output2
	public static void main(String[] args) {
		// The first Hacker Practice of Note 5
		//Use the bisection to solve nonlinear equation
		
		//n=1, e^x-1=0
		double x1=-5;
		double x2=10;
		double x= (x1+x2)/2.0;
		double res1= Math.exp(x1)-1;
		double res= Math.exp(x)-1;
		double res2= Math.exp(x2)-1;
		while(Math.abs(res)>Math.pow(10, -7)) {
			if(res*res1>0) {
				x1=x;
				res1=res;
			}else {
				x2=x;
				res2=res;
			}
			x= (x1+x2)/2.0;
			res= Math.exp(x)-1;
		}
		System.out.println("one dimension bisection********************************");
		System.out.println("x=: "+x+ "\tresidual: "+res);
		
		//n=2, e^x-e^y=0, e^x+e^y=2
		double secX1=-5;
		double secX2=10;
		double secY1=-5;
		double secY2=10;
		System.out.println("two dimension bisection********************************");
		twoDimenBisection(secX1, secX2, secY1, secY2);
		
		//The second Hacker Practice of Note 5
		//Use Newton method to solve the nonlinear equation
		System.out.println("Use Newton method to solve a nonlinear equation****************************");
		System.out.println("when x0=10");
		double newton_x=10;
		double newton_fx= Math.exp(50*newton_x)-1;
		double newton_dx= newton_fx/(-50*Math.exp(50*newton_x));
		System.out.println("x= "+newton_x+"\tdelta_x= "+newton_dx+"\tf(x)= "+newton_fx+"\t"+newton_dx/newton_x);
		while(Math.abs(newton_fx)>Math.pow(10, -7)) {
			newton_x= newton_x+newton_dx;
			newton_fx= Math.exp(50*newton_x)-1;
			newton_dx= newton_fx/(-50*Math.exp(50*newton_x));
			System.out.println("x= "+newton_x+"\tdelta_x= "+newton_dx+"\tf(x)= "+newton_fx+"\t"+newton_dx/newton_x);
		}
		System.out.println("when x0=1");
		double newton_x2=1;
		double newton_fx2= Math.exp(50*newton_x2)-1;
		double newton_dx2= newton_fx2/(-50*Math.exp(50*newton_x2));
		System.out.println("x= "+newton_x2+"\tdelta_x= "+newton_dx2+"\tf(x)= "+newton_fx2);
		while(Math.abs(newton_fx2)>Math.pow(10, -7)) {
			newton_x2= newton_x2+newton_dx2;
			newton_fx2= Math.exp(50*newton_x2)-1;
			newton_dx2= newton_fx2/(-50*Math.exp(50*newton_x2));
			System.out.println("x= "+newton_x2+"\tdelta_x= "+newton_dx2+"\tf(x)= "+newton_fx2);
		}
		

	}
	public static void twoDimenBisection(double x1, double x2, double y1, double y2) {
		double x0= (x1+x2)/2.0;
		double y0= (y1+y2)/2.0;
		double v11= function1(x1, y2);
		double v12= function2(x1, y2);
		double v21= function1(x0, y2);
		double v22= function2(x0, y2);
		double v31= function1(x2, y2);
		double v32= function2(x2, y2);
		double v41= function1(x1, y0);
		double v42= function2(x1, y0);
		double v51= function1(x0, y0);
		double v52= function2(x0, y0);
		double v61= function1(x2, y0);
		double v62= function2(x2, y0);
		double v71= function1(x1, y1);
		double v72= function2(x1, y1);
		double v81= function1(x0, y1);
		double v82= function2(x0, y1);
		double v91= function1(x2, y1);
		double v92= function2(x2, y1);
		if((v11>=0)&&(v31>=0)&&(v71>=0)&&(v91>=0)) return;
		if((v11<=0)&&(v31<=0)&&(v71<=0)&&(v91<=0)) return;
		if((v12<=0)&&(v32<=0)&&(v72<=0)&&(v92<=0)) return;
		if((v12>=0)&&(v32>=0)&&(v72>=0)&&(v92>=0)) return;
		if(Math.abs(v51)<Math.pow(10, -7)&&Math.abs(v52)<Math.pow(10, -7)) {
			System.out.println("x= "+x0+"\ty= "+y0);
			outputResidual(x0, y0);
			return;
		}
		if(Math.abs(v11)<Math.pow(10, -7)&&Math.abs(v12)<Math.pow(10, -7)) {
			System.out.println("x= "+x1+"\ty= "+y2);
			return;
		}
		if(Math.abs(v31)<Math.pow(10, -7)&&Math.abs(v32)<Math.pow(10, -7)) {
			System.out.println("x= "+x2+"\ty= "+y2);
			outputResidual(x2, y2);
			return;
		}
		if(Math.abs(v71)<Math.pow(10, -7)&&Math.abs(v72)<Math.pow(10, -7)) {
			System.out.println("x= "+x1+"\ty= "+y1);
			outputResidual(x2, y2);
			return;
		}
		if(Math.abs(v91)<Math.pow(10, -7)&&Math.abs(v92)<Math.pow(10, -7)) {
			System.out.println("x= "+x2+"\ty= "+y1);
			outputResidual(x2, y2);
			return;
		}
		if(!((v11>=0)&&(v21>=0)&&(v41>=0)&&(v51>=0))&&!((v11<=0)&&(v21<=0)&&(v41<=0)&&(v51<=0))&&!((v12>=0)&&(v22>=0)&&(v42>=0)&&(v52>=0))&&!((v12<=0)&&(v22<=0)&&(v42<=0)&&(v52<=0))) twoDimenBisection(x1,x0,y0,y2);
		if(!((v21>=0)&&(v31>=0)&&(v51>=0)&&(v61>=0))&&!((v21<=0)&&(v31<=0)&&(v51<=0)&&(v61<=0))&&!((v22>=0)&&(v32>=0)&&(v52>=0)&&(v62>=0))&&!((v22<=0)&&(v32<=0)&&(v52<=0)&&(v62<=0))) twoDimenBisection(x0,x2,y0,y2);
		if(!((v41>=0)&&(v51>=0)&&(v71>=0)&&(v81>=0))&&!((v41<=0)&&(v51<=0)&&(v71<=0)&&(v81<=0))&&!((v42>=0)&&(v52>=0)&&(v72>=0)&&(v82>=0))&&!((v42<=0)&&(v52<=0)&&(v72<=0)&&(v82<=0))) twoDimenBisection(x1,x0,y1,y0);
		if(!((v51>=0)&&(v61>=0)&&(v81>=0)&&(v91>=0))&&!((v51<=0)&&(v61<=0)&&(v81<=0)&&(v91<=0))&&!((v52>=0)&&(v62>=0)&&(v82>=0)&&(v92>=0))&&!((v52<=0)&&(v62<=0)&&(v82<=0)&&(v92<=0))) twoDimenBisection(x0,x2,y1,y0);
		return;
	}
	public static double function1(double x, double y) {
		return Math.exp(x)-Math.exp(y);
	}
	public static double function2(double x, double y) {
		return Math.exp(x)+Math.exp(y)-2;
	}
	public static void outputResidual(double x, double y) {
		double r1= Math.abs(Math.exp(x)-Math.exp(y));
		double r2= Math.abs(Math.exp(x)+Math.exp(y)-2);
		System.out.println("The residual of the first function: "+r1);
		System.out.println("The residual of the second function: "+r2);
	}
	

}
