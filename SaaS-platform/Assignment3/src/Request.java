

import java.util.Hashtable;
import java.util.Random;

public class Request {
	private int [][] Matrix_A;
	private int [][] Matrix_B;
	private int [][] Matrix_result;
	private int passcode;
	private char operation;	
	private int task_numbers;
	private int processing_task_numbers;
	private int finished_task_numbers;	
	private long totalcosttime;
	private double bill;

	
	
	

	public Request() {
		Matrix_A=null;
		Matrix_B=null;
		Matrix_result=null;
		passcode=0;
		operation=' ';
		processing_task_numbers=0;
		int finished_task_numbers=0;
		long totalcosttime = 0;
		

	}
	
	public Request(int[][] Matrix1,int[][] Matrix2,int passcode_in,char operation_in, int task_numbers){
		Matrix_A=Matrix1;
		Matrix_B=Matrix2;
		Matrix_result=new int[Matrix_A.length][Matrix_B.length];
		passcode=passcode_in;
		operation=operation_in;
		this.task_numbers = task_numbers;
		processing_task_numbers=0;
		finished_task_numbers=0;
		bill = 0;
		
	}

public int getTask_numbers() {
		return task_numbers;
	}
public double getBill() {
	bill = totalcosttime*0.000001;
	return bill;
}
	public void setTask_numbers(int task_numbers) {
		this.task_numbers = task_numbers;
	}
    public boolean getFinishstatus() {
    	if(finished_task_numbers==task_numbers) {
    		return true;
    	}else {
		return false;
    	}
    }
	public int getProcessing_task_numbers() {
		return processing_task_numbers;
	}

	public void setProcessing_task_numbers(int processing_task_numbers) {
		this.processing_task_numbers = processing_task_numbers;
	}

	public int getFinished_task_numbers() {
		return finished_task_numbers;
	}

	public void setFinished_task_numbers() {
		this.finished_task_numbers = this.finished_task_numbers + 1;
	}

public int[][] getMatrix_result() {
		return Matrix_result;
	}

	public void setMatrix_result(int row_num,int line_num,int[] unit_result,char op) {
		if(op=='*') {
		Matrix_result[row_num][line_num] = unit_result[0];
		}else if(op=='+'||op=='-') {
			for(int i = 0;i<unit_result.length;i++) {
				Matrix_result[row_num][i] = unit_result[i];
			}
		}
	}

	//getter and setter
	public int[][] getMatrix_A() {
		return Matrix_A;
	}


	public void setMatrix_A(int[][] matrix_A) {
		Matrix_A = matrix_A;
	}


	public int[][] getMatrix_B() {
		return Matrix_B;
	}


	public void setMatrix_B(int[][] matrix_B) {
		Matrix_B = matrix_B;
	}


	public int getPasscode() {
		return passcode;
	}


	public void setPasscode(int passcode) {
		this.passcode = passcode;
	}


	public char getOperation() {
		return operation;
	}


	public void setOperation(char operation) {
		this.operation = operation;
	}

	
	public long getTotalcosttime() {
		return totalcosttime;
	}

	public void setTotalcosttime(long costtime) {
		this.totalcosttime = this.totalcosttime + costtime;
	}


}
