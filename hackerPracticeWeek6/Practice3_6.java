package hackerPracticeWeek6;

import java.util.ArrayList;
import java.util.Arrays;

import homeworkAndPractice.Practice3_1;
import homework_4.*;
public class Practice3_6 {
//The outputs of the Hacker Practices are in the file output1
	public static void main(String[] args) {
		// The sixth hacker practice of Note4
		//Use the Jacobi iterations to solve the problem
		
		//Construct the matrix A and the vector b
		System.out.println("Use the Jacobi iterations to solve the problem***********************");
		double[] value_A= {-4, 1, 1, 4, -4, 1, 1, -4, 1, 1, -4, 1, 1, 1, -4};
		int[] rowptr_A= {0,3,6,9,12,15};
		int[] colInd_A= {0,1,4,0,1,2,1,2,3,2,3,4,0,3,4};
		SparseMatrix A= new SparseMatrix(value_A, rowptr_A, colInd_A);
		double[] value_b= {1,0,0,0,0};
		Vector b= new Vector(value_b);
		
		//Get the diagonal matrix of A
		int rowN= A.getRowNum();
		ArrayList<Double> value_D= new ArrayList<Double>();
		ArrayList<Integer> rowPtr_D= new ArrayList<Integer>();
		ArrayList<Integer> colInd_D= new ArrayList<Integer>();
		rowPtr_D.add(0);
		for(int i=1;i<=rowN; i++) {
			value_D.add(A.retrieveElementRC(i, i));
			rowPtr_D.add(rowPtr_D.get(i-1)+1);
			colInd_D.add(i-1);
		}
		SparseMatrix D = new SparseMatrix(value_D, rowPtr_D, colInd_D);
		
		//Get the inverse of D
		ArrayList<Double> value_DI= new ArrayList<Double>();
		for(Double d: D.getValue()) {
			value_DI.add(1.0/d);
		}
		SparseMatrix DI = new SparseMatrix(value_DI, rowPtr_D, colInd_D);
		
		//Get the initial guess x0 and do the iterations
		Vector x0= DI.productAx(b);
		SparseMatrix step1= D.subtract(A);
		SparseMatrix para= DI.specialProductAB(DI, step1);
		Vector x= para.productAx(x0).add(DI.productAx(b));
		int count=1;
		double norm= residualNormTest(A, x, b);
		System.out.println("The number of iteration: "+ count+ "\tThe second norm of residual vector: "+norm);
		while(x.computeSecondNorm(x0)>Math.pow(10, -7)) {
			x0=x;
			x= para.productAx(x0).add(DI.productAx(b));
			count++;
			norm= residualNormTest(A, x, b);
			System.out.println("The number of iteration: "+ count+ "\tThe second norm of residual vector: "+norm);
		}
		
		//Use direct solver to solve the equation
		System.out.println("Use LUMatrixSolver to solve the same problem**********************");
		Practice3_1 s= new Practice3_1();
		double[][] value_full= {{-4,1,0,0,1},{4,-4,1,0,0},{0,1,-4,1,0},{0,0,1,-4,1},{1,0,0,1,-4}};
		FullMatrix A_f= new FullMatrix(value_full);
		Vector x_=s.LUMatrixSolver(A_f, b);
		double norm_= residualNormTest(A, x_, b);
		System.out.println("The second norm of of residual vector: "+norm_);
		
		//Check the first and infinite norms of DI*(L+U)
		SparseMatrix re= DI.specialProductAB(DI, D.subtract(A));
		double first= s.upBoundFirNormSM(re);
		double inf= s.upBoundInfNormSM(re);
		System.out.println("The upper bound of the first norm of DI*(L+U): "+first);
		System.out.println("The upper bound of the infinite norm of DI*(L+U): "+inf);
		
		
		//The seventh hacker practice of Note4
		//Use the SOR iterations to solve the problem, w=2
		System.out.println("Use the SOR iterations to solve the problem***********************");
		Vector r0= b.subtract(A.productAx(x0));
		double[] empty= new double[r0.getLength()];
		Vector v0= new Vector(empty);
		double w=2;
		Vector x_sor= x0.add(DI.productAx(r0).scaling(w));
		int count_sor=1;
		double norm_sor= residualNormTest(A, x_sor, b);
		double norm_x= x_sor.computeSecondNorm(x0);
		double norm_r= r0.computeSecondNorm(v0);
		System.out.println("The number of iteration: "+ count_sor+ "\tx(k): "+Arrays.toString(x0.getValue())+"\tx(k+1): "+Arrays.toString(x_sor.getValue())+"\tThe second norm of residual vector: "+norm_sor
				+"\tThe second norm of the difference of x: "+norm_x+ "The second norm of r"+norm_r);
		while(norm_x>Math.pow(10, -7)) {
			x0=x_sor;
			r0= b.subtract(A.productAx(x0));
			x_sor= x0.add(DI.productAx(r0).scaling(w));
			count_sor++;
			norm_sor= residualNormTest(A, x_sor, b);
			norm_x=x_sor.computeSecondNorm(x0);
			norm_r= r0.computeSecondNorm(v0);
			System.out.println("The number of iteration: "+ count_sor+ "\tx(k): "+Arrays.toString(x0.getValue())+"\tx(k+1): "+Arrays.toString(x_sor.getValue())+"\tThe second norm of residual vector: "+norm_sor
					+"\tThe second norm of the difference of x: "+norm_x+ "The second norm of r"+norm_r);
		}
		

	}
	public static double residualNormTest(SparseMatrix A, Vector x, Vector b) {
		Vector test= A.productAx(x);
		if(test.getLength()!=b.getLength()) return -1;
		double[] value1= test.getValue();
		double[] value2= b.getValue();
		double norm=0;
		double div=0;
		for(int i=0; i<value1.length; i++) {
			norm=norm+Math.pow(value1[i]-value2[i], 2);
			div=div+Math.pow(value2[i], 2);
		}
		return Math.sqrt(norm)/Math.sqrt(div);
		
	}

}
