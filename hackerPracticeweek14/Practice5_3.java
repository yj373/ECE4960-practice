package hackerPracticeWeek14;
import A2.Iterative_solver;
import homework_4.*;
public class Practice5_3 {

	public static void main(String[] args) {
		//The second practice in Note 7 part 2
		double[] val = {-2, 1, 1, -2, 1, 1, -2, 1, 1, -2, 1, 1, -2, 1, 1, -2, 1, 1, -2, 1, 1, -2, 1, 1, -2};
		int[] rp = {0, 2, 5, 8, 11, 14, 17, 20, 23, 25};
		int[] ci = {0, 1, 0, 1, 2, 1, 2, 3, 2, 3, 4, 3, 4, 5, 4, 5, 6, 5, 6, 7, 6, 7, 8, 7, 8};
		SparseMatrix A = new SparseMatrix(val, rp, ci);
		double h = 1.0/10.0;
		double[] f_v = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
		Vector f = new Vector(f_v);
		Vector b = f.scaling(h*h);
		Iterative_solver is = new Iterative_solver(A, b);
		Vector res = is.iterate();
		double[] res_v = res.getValue();
		for(int i=1; i<10; i++) {
			System.out.println("x = "+i*h+", psi_x"+i+" = "+res_v[i-1]);
		}
		
		
	}
	
}
