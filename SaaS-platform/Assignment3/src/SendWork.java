import java.io.Serializable;

public class SendWork implements Serializable{
	int a[];
	int b[];
	int unit_num_A;
	int unit_num_B;
	int passcode;
	char operator;
	
	public SendWork(int[] a, int[] b,int unit_num_Of_A, int unit_num_Of_B,int passcode_in,char operator_in/*char*/ ) {
		super();
		this.a = a;
		this.b=b;
		this.unit_num_A = unit_num_Of_A;
		this.unit_num_B = unit_num_Of_B;
		this.passcode=passcode_in;
		this.operator=operator_in;
	} 
}