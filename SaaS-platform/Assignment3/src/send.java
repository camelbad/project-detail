import java.io.Serializable;

public class send implements Serializable {
	
		public int a[][];
     	public int b[][];
     	public int result[][];
		public String op;
		public int passcode;
		
		public boolean stop;
		public boolean check_status;
		public int task_numbers;
		public int processing_task_numbers;
		public int finished_task_numbers;
		
		
		public boolean answer_fist_request;
		public boolean answer_check_status;
		
		public double bill;
		public String request_status;//finished or processing
		
		public send(int[][] a, int[][] b, String op, int passcode, boolean stop, boolean check_status,
				int task_numbers, int processing_task_numbers, int finished_task_numbers, boolean answer_fist_request,
				boolean answer_check_status) {
			super();
			this.a = a;
			this.b = b;
			this.op = op;
			this.passcode = passcode;
			this.stop = stop;
			this.check_status = check_status;
			this.task_numbers = task_numbers;
			this.processing_task_numbers = processing_task_numbers;
			this.finished_task_numbers = finished_task_numbers;
			this.answer_fist_request = answer_fist_request;
			this.answer_check_status = answer_check_status;
		}
		//for client side generate a new send package
		public send(int[][] a, int[][] b, String op) {
			super();
			this.a = a;
			this.b = b;
			this.op = op;
			this.stop=false;
			this.check_status=false;
			task_numbers=0;
			processing_task_numbers=0;
			finished_task_numbers=0;
			passcode=-1;	
			answer_fist_request=false;
			answer_check_status=false;
		} 
		//for answer new request
		public send(int[][] a, int[][] b, String op,  int passcode_in, boolean answer_fist_request_in) {
			super();
			this.a = a;
			this.b = b;
			this.op = op;
			this.passcode = passcode_in;
			this.stop = false;
			this.check_status = false;
			this.task_numbers = 0;
			this.processing_task_numbers = 0;
			this.finished_task_numbers = 0;
			this.answer_fist_request = true;
			this.answer_check_status = false;
		}

		//for answer check status(fiished)
		public send(int[][] a, int[][] b, int[][] c, String op, int passcode, boolean check_status,
				int task_numbers, int processing_task_numbers, int finished_task_numbers, 
				boolean answer_check_status,double bill_in, String request_status_in) {
			super();
			this.a = a;
			this.b = b;
			this.result=c;
			this.op = op;
			this.passcode = passcode;
			this.stop = false;
			this.check_status = check_status;
			this.task_numbers = task_numbers;
			this.processing_task_numbers = processing_task_numbers;
			this.finished_task_numbers = finished_task_numbers;
			this.answer_fist_request = false;
			this.answer_check_status = answer_check_status;
			this.bill=bill_in;
			this.request_status=request_status_in;
		}
	////////////////////////////////////////
		public send(int[][] a, int[][] b, String op, int passcode, boolean check_status,
				int task_numbers, int processing_task_numbers, int finished_task_numbers, 
				boolean answer_check_status,String request_status_in) {
			super();
			this.a = a;
			this.b = b;
			
			this.op = op;
			this.passcode = passcode;
			this.stop = false;
			this.check_status = check_status;
			this.task_numbers = task_numbers;
			this.processing_task_numbers = processing_task_numbers;
			this.finished_task_numbers = finished_task_numbers;
			this.answer_fist_request = false;
			this.answer_check_status = answer_check_status;
			this.request_status=request_status_in;
		}
		
		
		
		
		
		public send(boolean stop, boolean check_status) {
			super();
			this.stop = stop;
			this.check_status = check_status;
		}

		
		
		public send(int passcode) {
			super();
			this.passcode = passcode;
		}
		
		public send(int passcode, boolean stop, boolean check_status) {
			super();
			this.passcode = passcode;
			this.stop = stop;
			this.check_status = check_status;
		}
		
		public send(double bill) {
			super();
			this.bill = bill;
		}

		public send(String status) {
			super();
			this.request_status = status;
		}
		
		public send(int[][]c, String status, double bill) {
			super();
			this.request_status = status;
			this.result = c;
			this.bill = bill;
		}
		
}
