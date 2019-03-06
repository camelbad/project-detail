
	// SimpleWoker.java: a simple worker program
import java.net.*;
import java.io.*;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
//GetTask is a class of thread that connect to master and ask for tasks

public class MatrixWorkerSide {
    
	public static void main(String args[]){
		final int worker_id = 5;// Integer.parseInt(args[0]);
		LinkedBlockingQueue<SendWork> taskblockingqueue = new LinkedBlockingQueue<SendWork>();//Generate a task queue
		LinkedBlockingQueue<UnitResult> resultblockingqueue = new LinkedBlockingQueue<UnitResult>();//Generate a result queue
		MonitorStates monitorstates = new MonitorStates(taskblockingqueue,resultblockingqueue,worker_id);
		monitorstates.start();
		GetTask gettask = new GetTask(taskblockingqueue,worker_id);
		gettask.start();
		Calculate calculate = new Calculate(taskblockingqueue,resultblockingqueue,worker_id);
		calculate.start();

	  }
}
class MonitorStates extends Thread{
	 private String serverIP = "127.0.0.1";
	 private int serverPort = 1236;
	 private int worker_id;
	 private boolean isRunning = true;
	 private long heartBeatInterval = 1000; //Set heartbeat sending duration time
	 private LinkedBlockingQueue<SendWork> taskblockingqueue;
	 private LinkedBlockingQueue<UnitResult> resultblockingqueue;
	 public MonitorStates(LinkedBlockingQueue<SendWork> taskblockingqueue,LinkedBlockingQueue<UnitResult> resultblockingqueue,int worker_id) {
		 this.worker_id = worker_id;
		 this.taskblockingqueue = taskblockingqueue;
		 this.resultblockingqueue = resultblockingqueue;

	 }
	 public void run() {
		 while(isRunning) {
                 try {
					Socket s3 = new Socket(serverIP,1237);
					     BufferedReader in = new BufferedReader(
						     new InputStreamReader(
						         s3.getInputStream()));
					     // Enable auto-flush:
					     PrintWriter out = new PrintWriter(
						     new BufferedWriter(
						         new OutputStreamWriter(
						             s3.getOutputStream())), true);
					     int loadstr = taskblockingqueue.size();


					     out.println(String.valueOf(worker_id));
					     out.println(String.valueOf(loadstr));
				    
					     out.close();
					     in.close();
					     s3.close();
					System.out.println("send a heart beat, this worker is"+worker_id+"Now the load is"+loadstr);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 try {
					Thread.currentThread().sleep(heartBeatInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
	 }
	 }
	 


class GetTask extends Thread{
	 private LinkedBlockingQueue<SendWork> taskblockingqueue;
	 private int[] zero = new int[0];
	 private int worker_id;
	 public UnitResult asktask;
	 public GetTask(LinkedBlockingQueue<SendWork> taskblockingqueue,int worker_id) {
		this.taskblockingqueue = taskblockingqueue;
		this.worker_id = worker_id;
	
	}

	public void run() {
		 while(true) {
			 
			 try {
				 asktask = new UnitResult(zero,0,0,0,' ','t',0,worker_id); //When connect to master, send operation 't', it means 'I'm free give me task'
				 Socket s1 = new Socket("43.240.99.156",1234);
				 ObjectOutputStream out = 
				            new ObjectOutputStream (         
				                     s1.getOutputStream());
				 ObjectInputStream in = 
				            new ObjectInputStream (
				                    s1.getInputStream());
				 out.writeObject(asktask);
				 SendWork temp = (SendWork) in.readObject();
				 for(int i = 0;i<temp.a.length;i++) {
					 System.out.println("This Worker:"+asktask.worker_id+" received "+temp.a[i]+" "+temp.b[i]);
				 }
				 taskblockingqueue.put(temp);
				 out.flush();
				 in.close();
				 out.close();
				 s1.close();
			} catch (IOException | ClassNotFoundException | InterruptedException e) {}
		         
	}

}
}
//Calculate is a class of thread that take tasks from task queue and calculate result then put result into result queue
class Calculate extends Thread{
	private LinkedBlockingQueue<SendWork> taskblockingqueue;
	private  int worker_id;
	public UnitResult cal(SendWork sw) {
		long startTime = System.nanoTime();
		int unitresult[] = new int[sw.a.length];
	  	  if(sw.operator=='+') {
	  		  for(int k=0;k<sw.a.length;k++)
	  		  {
	  			  unitresult[k]=sw.a[k]+sw.b[k];
	  		  }
	  		 
	  	  	}
	  	  else if(sw.operator == '-') {
	  	  	  for(int k=0;k<sw.a.length;k++)
	  	  	  {
	 			  unitresult[k]=sw.a[k]-sw.b[k];
	  	  	  }
	  
	  	  	}
	  	  else if(sw.operator == '*') {
	  		  for(int k=0;k<sw.a.length;k++)
		  	  {
				  unitresult[0]+=sw.a[k]*sw.b[k];
		  	  }
	  		  
	  	  }
	  	long endTime = System.nanoTime();		
	  	System.out.println("Cost time is "+ (endTime-startTime));
	  	  return new UnitResult(unitresult,sw.unit_num_A,sw.unit_num_B,sw.passcode,sw.operator,'r',(endTime-startTime),worker_id);
	}
	public Calculate(LinkedBlockingQueue<SendWork> taskblockingqueue,LinkedBlockingQueue<UnitResult> resultblockingqueue,int worker_id) {
		this.taskblockingqueue = taskblockingqueue;
	    this.worker_id = worker_id;
	}
	public void run() {
		try {
			while(true) {
				SendWork task = taskblockingqueue.poll(2, TimeUnit.SECONDS);//Check task queue, if null then wait 2 seconds
				if (task!=null) {
					UnitResult tmp = cal(task);
					Socket s2 = new Socket("43.240.99.156",1234);
					 ObjectOutputStream out = 
					            new ObjectOutputStream (         
					                     s2.getOutputStream());
					 ObjectInputStream in = 
					            new ObjectInputStream (
					                    s2.getInputStream());

					 out.writeObject(tmp);	
					 System.out.println("Send result");
					 out.flush();
					 in.close();
					 out.close();
				     s2.close();
				}
			}
				
			
		}catch (InterruptedException | IOException e){
            System.out.println("consume queue InterruptedException");
	}
}
}





