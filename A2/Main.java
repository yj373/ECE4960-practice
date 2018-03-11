package A2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import homework_4.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Doing Tests before solving A*x=b for large matrix A
		Tests t= new Tests();
		Runtime r= Runtime.getRuntime();
		long m0= r.totalMemory()-r.freeMemory();
		System.out.println("Before loading matrix, used memory:"+m0+" bytes");
		long start= System.currentTimeMillis();
		ArrayList<Double> value= new ArrayList<Double>();
		ArrayList<Integer> rowPtr= new ArrayList<Integer>();
		ArrayList<Integer> colInd= new ArrayList<Integer>();
		try {
			File val= new File("src/A2/value.csv");
			File cI= new File("src/A2/colInd.csv");
			File rP= new File("src/A2/rowPtr.csv");
			BufferedReader br_val= new BufferedReader(new FileReader(val));
			BufferedReader br_cI= new BufferedReader(new FileReader(cI));
			BufferedReader br_rP= new BufferedReader(new FileReader(rP));
			String line_val= br_val.readLine();
			String line_cI= br_cI.readLine();
			String line_rP= br_rP.readLine();
			while(line_val!=null) {
				double val_ele= Double.valueOf(line_val);
				value.add(val_ele);
				line_val= br_val.readLine();
			}
			while(line_cI!=null) {
				int cI_ele= Integer.valueOf(line_cI)-1;
				colInd.add(cI_ele);
				line_cI= br_cI.readLine();
			}
			while(line_rP!=null) {
				int rP_ele= Integer.valueOf(line_rP)-1;
				rowPtr.add(rP_ele);
				line_rP= br_rP.readLine();
			}
			br_val.close();
			br_cI.close();
			br_rP.close();
			
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		SparseMatrix A= new SparseMatrix(value, rowPtr, colInd);
		long m1= r.totalMemory()-r.freeMemory();
		System.out.println("After loading matrix, used memory: "+m1+" bytes");
		long end= System.currentTimeMillis();
		System.out.println("Time used for loading matrix:"+(end-start)+"ms");
		double[] val_b1= new double[5000];
		double[] val_b2= new double[5000];
		double[] val_b3= new double[5000];
		val_b1[0]=1.0;
		val_b2[4]=1.0;
		for(int i=0; i<5000; i++) {
			val_b3[i]=1.0;
		}
		
		long start2= System.currentTimeMillis();
		Vector b1= new Vector(val_b1);
		Iterative_solver is1= new Iterative_solver(A, b1);
		testing(is1, t);
		System.out.println("When b=(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0...), use iterative solver to solve A*x=b");
		Vector x1= is1.iterate();
		System.out.println("The second norm of residual vector: "+(t.residuleNormTest(A, x1, b1)));
		long end2= System.currentTimeMillis();
		System.out.println("Total time used of solving A*x=b: "+(end2-start2)+"ms");
		
		long start3= System.currentTimeMillis();
		Vector b2= new Vector(val_b2);
		Iterative_solver is2= new Iterative_solver(A, b2);
		System.out.println("When b=(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0...), use iterative solver to solve A*x=b");
		Vector x2= is2.iterate();
		System.out.println("The second norm of residual vector: "+(t.residuleNormTest(A, x2, b2)));
		long end3= System.currentTimeMillis();
		System.out.println("Total time used of solving A*x=b: "+(end3-start3)+"ms");
		
		long start4= System.currentTimeMillis();
		Vector b3= new Vector(val_b3);
		Iterative_solver is3= new Iterative_solver(A, b3);
		System.out.println("When b=(1.0, 1.0, 1.0...), use iterative solver to solve A*x=b");
		Vector x3= is3.iterate();
		System.out.println("The second norm of residual vector: "+(t.residuleNormTest(A, x3, b3)));
		long end4= System.currentTimeMillis();
		System.out.println("Total time used of solving A*x=b: "+(end4-start4)+"ms");
		

	}
	
	public static void testing(Iterative_solver is, Tests t) {
		System.out.println("Doing test*****************************************");
		String res_1= t.knownMatrixTest();
		System.out.println("The result of known matrix test: "+ res_1);
		double[] value= new double[1000];
		int[] colInd= new int[1000];
		int[] rowPtr= new int[1001];
		for (int i=0; i<1000; i++) {
			value[i]=i;
			colInd[i]=i;
			rowPtr[i]=i;
		}
		rowPtr[1000]= 1000;
		SparseMatrix testDiagonal= new SparseMatrix(value, rowPtr, colInd);
		String res_2= t.specialProductAX_Test(testDiagonal);
		System.out.println("The result of specialProductAX test: "+res_2);
		String res_3= t.specialProductAX_Test(testDiagonal);
		System.out.println("The result of specialProductAB test: "+res_3);
		SparseMatrix D= is.getDiagonal();
		String res_4= t.getDiagonalTest(is.A, D);
		System.out.println("The result of getDiagonal test: "+res_4);
		SparseMatrix DI= is.inverseD(D);
		String res_5= t.inverseDTest(D, DI);
		System.out.println("The result of inversD test: "+res_5);
		System.out.println("All tests done!***********************************");
	}

}
