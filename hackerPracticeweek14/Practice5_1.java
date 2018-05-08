package hackerPracticeWeek14;

public class Practice5_1 {

	public static void main(String[] args) {
		//The fist practice in Note 7 part 2
		System.out.println("Approximate the sin(x) using 8 points");
		double delta_x = Math.PI/8.0;
		double center = Math.PI/8.0;
		double estimated_area = 0;
		while(center<Math.PI) {
			estimated_area = estimated_area+compute_v(center, delta_x);
			center = center + delta_x;
		}
		System.out.println("The estimated area is: "+estimated_area);
		System.out.println("Approximate the sin(x) using 100 points");
		delta_x = Math.PI/100;
		center = Math.PI/100;
		estimated_area = 0;
		while(center<Math.PI) {
			estimated_area = estimated_area+compute_v(center, delta_x);
			center = center + delta_x;
		}
		System.out.println("The estimated area is: "+estimated_area);

	}
	
	public static double compute_v(double center, double delta_x) {
		return Math.sin(center)*delta_x;
	}

}
