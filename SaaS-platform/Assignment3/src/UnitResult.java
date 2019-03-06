import java.io.Serializable;

public class UnitResult implements Serializable {

	int a[];
	int unit_num_A;
	int unit_num_B;
	int passcode;
	char operator;
	char request_type;
	long costtime;
	int worker_id;
	public UnitResult(int[] a,int unit_num_Of_A, int unit_num_Of_B,int passcode_in,char operator_in,char request_type ,long costtime,int worker_id) {
		super();
		this.a = a;
		this.unit_num_A = unit_num_Of_A;
		this.unit_num_B = unit_num_Of_B;
		this.passcode=passcode_in;
		this.operator=operator_in;
		this.request_type=request_type;
		this.costtime = costtime;
		this.worker_id = worker_id;
	}
}
