package hackerPracticeWeek13;
import A2.Iterative_solver;
import homework_4.*;
public class Practice4_24 {

	public static void main(String[] args) {
		// The third hacker practice of Note 7 
		// 1-D Parabolic PDE
		double h = 1;
		double delta_t = 1;
		double D = 1;
		double term1 = 1.0/delta_t+2*D/(h*h);
		double term2 = -D/(h*h);
		double term3 = 1.0/delta_t;
		System.out.println("Zero Dirichlet boundary condition*******************************************");
		double[] v1 = {term1, term2, term2, term1, term2, term2, term1};
		int[] rP1 = {0, 2, 5, 7};
		int[] cI1 = {0, 1, 0, 1, 2, 1, 2};
		SparseMatrix A = new SparseMatrix(v1, rP1, cI1);
		double[] v2 = {term3, term3, term3};
		int[] rP2 = {0, 1, 2, 3};
		int[] cI2 = {0, 1, 2};
		SparseMatrix B = new SparseMatrix(v2, rP2, cI2);
		double[] n_v = {0, 10, 0};
		Vector n = new Vector(n_v);
		Vector b = B.productAx(n);
		Iterative_solver is = new Iterative_solver(A, b);
		n = is.iterate();
		for(int i=2; i<=5;i++) {
			b = B.productAx(n);
			is = new Iterative_solver(A, b);
			n = is.iterate();
			double[] cur_n = n.getValue();
			System.out.println("t = "+i+"\tn1 = "+cur_n[0]+"\tn2 = "+cur_n[1]+"\tn3 = "+cur_n[2]);
		}
		System.out.println("Zero-slope Neumann boundary condition*******************************************");
		double[] v3 = {term1-1, term2, term2, term1, term2, term2, term1-1};
		int[] rP3 = {0, 2, 5, 7};
		int[] cI3 = {0, 1, 0, 1, 2, 1, 2};
		SparseMatrix A_ = new SparseMatrix(v3, rP3, cI3);
		double[] n2_v = {0, 10, 0};
		Vector n2 = new Vector(n2_v);
		b = B.productAx(n2);
		Iterative_solver is2 = new Iterative_solver(A_, b);
		n2 = is2.iterate();
		for(int i=2; i<=5;i++) {
			b = B.productAx(n2);
			is2 = new Iterative_solver(A_, b);
			n2 = is2.iterate();
			double[] cur_n = n2.getValue();
			System.out.println("t = "+i+"\tn1 = "+cur_n[0]+"\tn2 = "+cur_n[1]+"\tn3 = "+cur_n[2]);
		}


	}

}
