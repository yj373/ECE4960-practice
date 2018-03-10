package homework_4;
import java.io.*;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// Part 1
		double[] value= {1,2,3,4,5,6,7,8,9,10,11,12};
		int[] rowPtr= {0,3,6,9,10,12};
		int[] colInd= {0,1,4,0,1,2,1,2,4,3,0,4};
		double[][] value_= {{1,2,0,0,3},{4,5,6,0,0},{0,7,8,0,9},{0,0,0,10,0},{11,0,0,0,12}};
		double[] vec= {5,4,3,2,1};
		Vector vector= new Vector(vec);
		SparseMatrix sm= new SparseMatrix(value, rowPtr, colInd, 5);
		FullMatrix fm= new FullMatrix(value_);
		permutationTest1(sm, fm);
		rowScalingTest1(sm, fm);
		productTest1(sm, fm, vector);
		
		// part 2
		//load the memplus matrix
		String[] parts;
		ArrayList<Double> v= new ArrayList<Double>();
		ArrayList<Integer> rP= new ArrayList<Integer>();
		ArrayList<Integer> cI= new ArrayList<Integer>();
		try {
			File memplus= new File("src/homework_4/memplus.txt");
			BufferedReader br= new BufferedReader(new FileReader(memplus));
			String line="";
			line= br.readLine();
			line= br.readLine();
			parts= line.split(" ");
			rP.add(0);
			int rowPos=1;
			int nonZero=0;
			while(line!=null) {
				line= br.readLine();
				parts= line.split(" ");
				int col= Integer.valueOf(parts[0]);
				int row= Integer.valueOf(parts[1]);
				double val= Double.valueOf(parts[2]);
				if(row==rowPos) nonZero++;
				else {
					rP.add(rP.get(rP.size()-1)+nonZero);
					nonZero=1;
					rowPos= row;
				}
				v.add(val);
				cI.add(col);
			}
			br.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		SparseMatrix m= new SparseMatrix(v, rP, cI).traverse();
		permutationTest2(m);
		rowScalingTest2(m);

	}
	
	//part 1 test functions
	public static double computeSecondNorm(SparseMatrix sm, FullMatrix fm) {
		double norm=0;
		for(int i=0; i<fm.rowNum; i++) {
			for(int j=0; j<fm.colNum; j++) {
				norm= norm+ Math.pow(fm.retrieveElement(i+1, j+1)-sm.retrieveElementRC(i+1, j+1), 2);
			}
		}
		return norm;
	}
	public static double computeSecondNorm(Vector sv, Vector fv) {
		double norm=0;
		for(int i=0; i<sv.length; i++) {
			norm= norm+ Math.pow(fv.value[i]-sv.value[i], 2);
		}
		norm= Math.sqrt(norm);
		return norm;
	}
	public static void permutationTest1(SparseMatrix sm, FullMatrix fm) {
		sm.rowPermute(1, 3);
		sm.rowPermute(1, 5);
		fm.rowPermute(1, 3);
		fm.rowPermute(1, 5);
		double norm= computeSecondNorm(sm, fm);
		System.out.println("Permutation of (1,3) and then(1,5), the secondon norm is: "+norm);
		
	}
	public static void rowScalingTest1(SparseMatrix sm, FullMatrix fm) {
		sm.rowScale(1, 4, 3.0);
		fm.rowScale(1, 4, 3.0);
		double norm1= computeSecondNorm(sm, fm);
		System.out.println("Test 3.0*row[1]+row[4], the second norm is: "+norm1);
		sm.rowScale(5, 2, -4.4);
		fm.rowScale(5, 2, -4.4);
		double norm2= computeSecondNorm(sm, fm);
		System.out.println("Test -4.4*row[5]+row[2], the second norm is: "+norm2);
	}
	public static void productTest1(SparseMatrix sm, FullMatrix fm, Vector x) {
		Vector vectorFull= fm.productAx(x);
		Vector vectorSparse= sm.productAx(x);
		double norm= computeSecondNorm(vectorSparse, vectorFull);
		System.out.println("Test Ax=b, the second norm is: "+norm);
	}
	
	//part 2 test functions
	public static void permutationTest2(SparseMatrix sm) {
		//sm is the original matrix, while sm2 is the matrix permuted sm
		SparseMatrix sm2= sm;
		//record the time used for these permutations
		long startTime= System.nanoTime();
		sm2.rowPermute(1, 3);
		sm2.rowPermute(1, 5);
		sm2.rowPermute(10, 3000);
		sm2.rowPermute(5000, 10000);
		sm2.rowPermute(6, 15000);
		long endTime= System.nanoTime();
		Runtime runtime = Runtime.getRuntime();
		System.out.println("The time used for permutations: "+(endTime-startTime)+"ns");
		System.out.println("The used memory after permutatons: "+(runtime.totalMemory()-runtime.freeMemory())+" bytes");
		System.out.println(runtime.freeMemory());
		//check each element of the rows that are permuted to make sure the answer is right
		
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(1, i+1)!=sm2.retrieveElementRC(3, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(3, i+1)!=sm2.retrieveElementRC(5, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(5, i+1)!=sm2.retrieveElementRC(1, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(10, i+1)!=sm2.retrieveElementRC(3000, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(3000, i+1)!=sm2.retrieveElementRC(10, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(5000, i+1)!=sm2.retrieveElementRC(10000, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(10000, i+1)!=sm2.retrieveElementRC(5000, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(6, i+1)!=sm2.retrieveElementRC(15000, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		for(int i=0; i<sm.colNum; i++) {
			if(sm.retrieveElementRC(15000, i+1)!=sm2.retrieveElementRC(6, i+1)) {
				System.out.println("The permutation function is wrong!");
				return;
			}
		}
		System.out.println("The permutation funtion is right!");
		return;
		
	}
	public static void rowScalingTest2(SparseMatrix sm) {
		SparseMatrix sm2= sm;
		long startTime= System.nanoTime();
		sm2.rowScale(2, 4, 3.0);
		sm2.rowPermute(2, 5);
		sm2.rowScale(5, 4, -3.0);
		long endTime= System.nanoTime();
		Runtime runtime = Runtime.getRuntime();
		
		System.out.println("The time used for row scaling: "+(endTime-startTime)+"ns");
		System.out.println("The used memory after row scaling: "+(runtime.totalMemory()-runtime.freeMemory())+" bytes"); 
		double total=0.0;
		for(int i=0; i<sm.value.size(); i++) {
			total+=sm.value.get(i);
		}
		System.out.println("Doing checksum test for question 2:");
		checkSums(sm2, total);
	}
	
	public static void checkSums(SparseMatrix sm, double check2) {
		double[] v= new double[sm.colNum];
		for(int i=0; i<v.length; i++) {
			v[i]=1.0;
		}
		Vector x= new Vector(v);
		Vector res= sm.productAx(x);
		double check1=0;
		for(int i=0; i<res.length; i++) {
			check1+=res.value[i];
		}
		if(Math.abs(check1-check2)<Math.pow(10, -7)) {
			System.out.println("Pass checkSum test!");
		}else {
			System.out.println("Not pass checkSum test!");
		}
	}

}
