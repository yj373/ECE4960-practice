package A1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;

public class A1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File integerOverflow_2 = new File("src/A1/output/level2.txt");
			integerOverflow_2.createNewFile();
			BufferedWriter output_2= new BufferedWriter(new FileWriter(integerOverflow_2));
			File integerOverflow_1 = new File("src/A1/output/level1.txt");
			integerOverflow_1.createNewFile();
			BufferedWriter output_1= new BufferedWriter(new FileWriter(integerOverflow_1));
			Functions f= new Functions();
			f.integerOverflow(output_1, output_2);
			f.integerDividedBy_0(output_1, output_2);
			f.floatPointOverflow(output_1,output_2);
			f.infAndNinf(output_1,output_2);
			f.nanObservation(output_1,output_2);
			f.signedZeroObservation(output_1, output_2);
			f.floatPointUnderflow(output_1, output_2);
			f.calculatePi(output_2);
			
			output_1.close();
			output_2.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		//integerOverflow_1();
		
	}

}

