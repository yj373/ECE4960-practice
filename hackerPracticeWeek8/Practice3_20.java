package hackerPracticeWeek8;

import homework_4.*;

public class Practice3_20 {

	public static void main(String[] args) {
		//The third hacker practice of Note 5
		//Newton method with line search
		System.out.println("The third hacker practice of Note5:**************************************************************");
		newtonWithLineSearch(10,1);
		
		//The forth hacker practice of Note 5
		//Use quasi-Newton method with line search to the nonlinear equation
		System.out.println("The fourth hacker practice of Note5:**************************************************************");
		newtonWithLineSearch(1,2);
		
		//The fifth hacker practice of Note 5
		//quisi-Newton + line search to solve nonlinear optimization
		//initial guess: (-2,-2) we get (-1, 1)
		//initial guess: (2, 2) we get (1, 1)
		System.out.println("The fifth hacker practice of Note 5:****************************************************************");
		double[] value1= {2,2};
		Vector x1= new Vector(value1);
		newtonWithLineSearch(x1, 1);
		
		//The sixth hacker practice of Note 5
		//steepest descent method
		//initial guess: (-2,-2) we get (-1, 1)
		//initial guess: (2, 2) we get (1, 1)
		//No quadratic convergence
		System.out.println("The sith hacker practice of Note 5: ******************************************************************");
		double[] value2= {2,2};
		Vector x2= new Vector(value2);
		newtonWithLineSearch(x2,2);

	}
	//Single variable Newton method
	//Module1: compute the function value
	public static double fx(double x, int i) {
		switch(i) {
			case 1: return Math.exp(x*50)-1;
			case 2: return Math.exp(x*100)-1;
			default: return 0;
		
		}

	}
	//Module2: compute delta_x according to different f(x)
	public static double computeDeltaX_fx(double x, int i) {
		switch(i) {
			case 1:{
				double y=fx(x,1);
				return -y/(50*Math.exp(x*50));
			}
			//Quasi-Newton method
			case 2:{
				if(x!=0) {
					double y1=fx(1.0001*x,2);
					double y0=fx(x,2);
					return -y0/((y1-y0)/(0.0001*x));
				}else {
					double y1=fx(x+0.0001,2);
					double y0=fx(x,2);
					return -y0/((y1-y0)/(0.0001));
				}
				
			}
			default: return 0;
		}
		
	}
	//Module3: Newton+line search
	public static void newtonWithLineSearch(double x, int i) {
		double delta_x= computeDeltaX_fx(x,i);
		double t= 1;
		double norm0= computeNorm(x, 0, delta_x, i);
		while(norm0>Math.pow(10, -7)) {
			double norm1= computeNorm(x, t, delta_x, i);
			double norm2= computeNorm(x, t/2.0, delta_x, i);
			while (norm2<norm1 && norm1>Math.pow(10, -7)) {
				t=t/2.0;
				norm1= norm2;
				norm2= computeNorm(x, t/2.0, delta_x, i); 
			}
			delta_x= t*delta_x;
			System.out.println("x= "+x+"\tdelta_x= "+delta_x+"\tf(x)= "+fx(x,i));
			x= x+delta_x;
			delta_x= computeDeltaX_fx(x,i);
			t=1;
			norm0= computeNorm(x, 0, delta_x,i);
		}
		System.out.println("x= "+x+"\tdelta_x= "+delta_x+"\tf(x)= "+fx(x,i));
	}
	//Module4: Compute the norm
	public static double computeNorm(double x, double t, double delta_x, int i) {
		double res= fx(x+t*delta_x,i);
		return Math.abs(res);
	}
	
	
	//Multi-variable Newton optimization
	//Module1:Compute the value of objective function
	public static double vx(Vector x, int i) {
		switch(i) {
			case 1: {
				if(x.getLength()!=2) return 0;
				double x1= x.getValue()[0];
				double x2= x.getValue()[1];
				double v= Math.pow(3*x1*x1+x2-4, 2)+Math.pow(x1*x1-3*x2+2, 2);
				return v;
			}
			case 2: {
				if(x.getLength()!=2) return 0;
				double x1= x.getValue()[0];
				double x2= x.getValue()[1];
				double v= Math.pow(3*x1*x1+x2-4, 2)+Math.pow(x1*x1-3*x2+2, 2);
				return v;
			}
			default: return 0;
	
		}
	}
	//Module2: Local analysis of the Jacobian Matrix
	public static double jaco_vx(Vector x, int i, int j) {
		switch(i) {
			case 1: {
				if(x.getLength()!=2) return 0;
				//partial derivative with respect to x1
				if(j==1) {
					if(x.getValue()[0]!=0) {
						double[] perturbX_v= {x.getValue()[0]*1.0001, x.getValue()[1]};
						Vector perturbX= new Vector(perturbX_v);
						return (vx(perturbX,1)-vx(x,1))/(perturbX.getValue()[0]-x.getValue()[0]);
					}else {
						double[] perturbX_v= {x.getValue()[0]+0.0001, x.getValue()[1]};
						Vector perturbX= new Vector(perturbX_v);
						return (vx(perturbX,1)-vx(x,1))/(perturbX.getValue()[0]-x.getValue()[0]);
					}
					
				//partial derivative with respect to x2
				}else if(j==2) {
					if(x.getValue()[1]!=0) {
						double[] perturbX_v= {x.getValue()[0], x.getValue()[1]*1.0001};
						Vector perturbX= new Vector(perturbX_v);
						return (vx(perturbX,1)-vx(x,1))/(perturbX.getValue()[1]-x.getValue()[1]);
					}else {
						double[] perturbX_v= {x.getValue()[0], x.getValue()[1]+0.0001};
						Vector perturbX= new Vector(perturbX_v);
						return (vx(perturbX,1)-vx(x,1))/(perturbX.getValue()[1]-x.getValue()[1]);
					}
					
				}
			}
			case 2: {
		
			}
			default: return 0;

		}
	}
	
	//Module3: Compute delta_x
	public static Vector computeDeltaX_vx(Vector x, int i) {
		switch(i) {
			case 1:{
				if(x.getLength()!=2) return new Vector();
				double[] perturbX1_v= {x.getValue()[0]*1.0001, x.getValue()[1]};
				double[] perturbX2_v= {x.getValue()[0], x.getValue()[1]+0.0001};
				Vector perturbX1= new Vector(perturbX1_v);
				Vector perturbX2= new Vector(perturbX2_v);
				//Compute the four elements in Heissian matrix
				double[] jacobianMatrix_v= {jaco_vx(x, 1, 1), jaco_vx(x, 1, 2)};
				Vector jacobianMatrix= new Vector(jacobianMatrix_v);
				double a= (jaco_vx(perturbX1,1,1)-jaco_vx(x, 1, 1))/(perturbX1.getValue()[0]-x.getValue()[0]);
				double b= (jaco_vx(perturbX2,1,1)-jaco_vx(x, 1, 1))/(perturbX2.getValue()[1]-x.getValue()[1]);
				double c= (jaco_vx(perturbX1,1,2)-jaco_vx(x, 1, 2))/(perturbX1.getValue()[0]-x.getValue()[0]);
				double d= (jaco_vx(perturbX2,1,2)-jaco_vx(x, 1, 2))/(perturbX2.getValue()[1]-x.getValue()[1]);
				System.out.println(d);
				double determinant= a*d-b*c;
				double[][] NegHeissianMatrixInv_v= {{-d/determinant, b/determinant},{c/determinant, -a/determinant}};
				FullMatrix NegHessianMatrixInv= new FullMatrix(NegHeissianMatrixInv_v);
				return NegHessianMatrixInv.productAx(jacobianMatrix);
			}
			//Quasi-Newton method
			case 2:{
				double[] deltaX_v= new double[2];
				deltaX_v[0]= -jaco_vx(x,1,1);
				deltaX_v[1]= -jaco_vx(x,1,2);
				return new Vector(deltaX_v);
				
			}
			default: return new Vector();
		}
	}
	//Module4:Newton+line search
	public static void newtonWithLineSearch(Vector x, int i) {
		Vector delta_x= computeDeltaX_vx(x,i);
		double t=1;
		double norm_x= x.computeSecondNorm();
		double norm_deltaX= delta_x.computeSecondNorm();
		double norm1= computeNorm(x, t, delta_x, i);
		double norm2= computeNorm(x, t/2.0, delta_x, i);
		while (norm2<norm1 && norm1>Math.pow(10, -7)) {
			t=t/2.0;
			norm1= norm2;
			norm2= computeNorm(x, t/2.0, delta_x, i); 
		}
		delta_x= delta_x.scaling(t);
		norm_deltaX= delta_x.computeSecondNorm();
		System.out.println("x1= "+x.getValue()[0]+"\tx2= "+x.getValue()[1]+"The second norm of x: "+norm_x+"\tThe second norm of delta_x: "+norm_deltaX+"\tt= "+t+"\tV(x)= "+vx(x,i));
		double norm0= computeNorm(x, 0, delta_x, i);
		while(norm0>Math.pow(10, -7)&&norm_deltaX>Math.pow(10, -7)) {
			x= x.add(delta_x);
			delta_x= computeDeltaX_vx(x,i);
			t=1;
			norm1= computeNorm(x, t, delta_x, i);
			norm2= computeNorm(x, t/2.0, delta_x, i);
			while (norm2<norm1 && norm1>Math.pow(10, -7)) {
				t=t/2.0;
				norm1= norm2;
				norm2= computeNorm(x, t/2.0, delta_x, i); 
			}
			norm_x= x.computeSecondNorm();
			delta_x= delta_x.scaling(t);
			norm_deltaX= delta_x.computeSecondNorm();
			System.out.println("x1= "+x.getValue()[0]+"\tx2= "+x.getValue()[1]+"The second norm of x: "+norm_x+"\tThe second norm of delta_x: "+norm_deltaX+"\tt= "+t+"\tV(x)= "+vx(x,i));
			norm0= computeNorm(x, 0, delta_x,i);
		}

		
	}
	//Module5: Compute the norm
	public static double computeNorm(Vector x, double t, Vector delta_x, int i) {
			Vector delta_x2= delta_x.scaling(t);
			double res= vx(x.add(delta_x2),i);
			return Math.abs(res);
		}
	

}
