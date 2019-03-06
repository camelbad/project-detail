import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MatrixServer {

	public static void main(String args[]) throws InterruptedException{
		 LinkedBlockingQueue<SendWork> taskqueue = new LinkedBlockingQueue<SendWork>();//Generate a task queue
		 LinkedBlockingQueue<String> endqueue = new LinkedBlockingQueue<String>();
		 Hashtable<Integer,Request> requesttable = new Hashtable<>();//generate a request table
		 Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable = new Hashtable<>();//generate a processing request table
		 
		 ServeOneClient client = new ServeOneClient(taskqueue,requesttable,processingrequesttable);
		 client.start();
		 ServeOneWorker worker = new ServeOneWorker(taskqueue,requesttable,processingrequesttable);
		 worker.start();
		 MonitorServer monitor = new MonitorServer();
		 monitor.start();
		 ScanHeart scan = new ScanHeart(taskqueue,processingrequesttable);
			scan.start();


}
}
class ServeOneClient extends Thread{
	 private LinkedBlockingQueue<SendWork> taskqueue;
	 private Hashtable<Integer,Request> requesttable;
	 private Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable = new Hashtable<>();
	 private ServerSocket clientsock;

	 public int[] getRow(int[][] matrixT,int rowNum )
	 {
	 	int[] temRow=new int[matrixT.length];
	 	for(int k=0;k<matrixT[0].length;k++)
	 	{
	 		temRow[k]=matrixT[rowNum][k];
	 
	 		
	 	}
	 
	     return temRow;	
	 }

	 public int[] getLine(int[][] matrixT, int lineNum) {
	     int[] temLine=new int[matrixT[0].length];
	     for(int k=0;k<matrixT.length;k++)
	     {
	     	temLine[k]=matrixT[k][lineNum];
	     }
	 
	 	return temLine;
	 	
	 }
	 public ServeOneClient(LinkedBlockingQueue<SendWork> taskqueue,Hashtable<Integer,Request> requesttable,Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable) {
			this.requesttable = requesttable;
			this.taskqueue = taskqueue;
			this.processingrequesttable = processingrequesttable;
			try {
				clientsock = new ServerSocket(1235);
				System.out.println("Serve one client Server running ...");
			}catch (IOException e)
			{
				System.out.println("Error: couldn't create socket.");
			}
	 }
			public void run() {
				Socket client = null;
				while(true) {
					if(clientsock == null)
						return;
					 try {
						 client = clientsock.accept();
							ObjectOutputStream out = 
						            new ObjectOutputStream (         
						            		client.getOutputStream());
						    ObjectInputStream in = 
						            new ObjectInputStream (
						            		client.getInputStream());
					
					send received1 =(send)in.readObject();//reveive request
					boolean check_status=received1.check_status;
		  	        boolean stop=received1.stop;
					  if(!check_status&&!stop) {
						    double d = Math.random();
							int passcode = (int)(d*10000);//If not check or stop it's a new request, Generate a unique passcode
							while(requesttable.containsKey(passcode)) {
								passcode = (int)(d*10000);
							}
							
						  int size =received1.a.length;
				            char operator=received1.op.charAt(0);
				            int[][] A = new int[size][size];
							int[][] B = new int[size][size];
							A=received1.a; //Get matrix
				            B=received1.b;							
							send sendpasscode = new send(passcode);
							out.writeObject(sendpasscode);
						if(operator=='*') {		
							requesttable.put(passcode, new Request(A,B,passcode,operator,size*size)); //put request into request table
								for(int k=0;k<size;k++)
						         {									 
						        	 int[] a_row=new int[size];
						        	 //get  one row
						        	 a_row =getRow(A, k);
						        	 for(int j=0;j<size;j++)
						        	 {
						        		 
						        		 int[] b_line=new int[size];
						        		 //get one line
						        		 b_line=getLine(B,j);
						        		 //construct a new task
						        		 SendWork task=new SendWork(a_row,b_line,k,j,passcode,operator);					     
						        		 //add new task into the queue
						        		 try {
											taskqueue.put(task);
											
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										};
						        		
						        	 }
						         }
						}else if(operator=='+'||operator=='-'){
							requesttable.put(passcode, new Request(A,B,passcode,operator,size)); //put request into request table
							for(int h = 0;h<size;h++) {
								int[] a_row=new int[size];
								int[] b_row=new int[size];
								a_row = getRow(A,h);
								b_row=getRow(B,h);
								SendWork task=new SendWork(a_row,b_row,h,h,passcode,operator);
								 try {
										taskqueue.put(task);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									};
						}
					  }
						out.flush();
						in.close();
						out.close();
						client.close();
					}else if(check_status) {
						int checkpasscode = received1.passcode;
						if(requesttable.containsKey(checkpasscode)) {
							System.out.println(requesttable.get(checkpasscode).getFinishstatus());
							if(requesttable.get(checkpasscode).getFinished_task_numbers()==requesttable.get(checkpasscode).getTask_numbers()) {
								send sendresult = new send(requesttable.get(checkpasscode).getMatrix_result(),"finish",requesttable.get(checkpasscode).getBill());
								out.writeObject(sendresult);
							}else {
								send sendstatus = new send("processing");
										out.writeObject(sendstatus);
							}
						}else {
							send sendstatus = new send("Wrong passcode");
							out.writeObject(sendstatus);
						}
						out.flush();
						in.close();
						out.close();
						client.close();
					}else if(stop) {
						int stoppasscode = received1.passcode;
						if(requesttable.containsKey(stoppasscode)) {							
							if(requesttable.get(stoppasscode).getFinishstatus()) {
								send sendresult = new send(requesttable.get(stoppasscode).getMatrix_result(),"finish",requesttable.get(stoppasscode).getBill());
								out.writeObject(sendresult);
							}else {
								System.out.println("Create a new endrequest thread to end:"+stoppasscode);
								new EndRequest(stoppasscode,taskqueue,requesttable,processingrequesttable);
								send sendstop = new send(requesttable.get(stoppasscode).getMatrix_result(),"stop",requesttable.get(stoppasscode).getBill());
								out.writeObject(sendstop);
								out.flush();
								in.close();
								out.close();
								client.close();
					    	}
						}else {
							send error = new send("Wrong passcode");
							out.writeObject(error);
						}
					}	
				}catch (IOException | ClassNotFoundException e){ }
			}	
		}
	
}

class EndRequest extends Thread{
	 private int stoppasscode;
	 private LinkedBlockingQueue<SendWork> taskqueue;
	 private Hashtable<Integer,Request> requesttable;
	 private Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable = new Hashtable<>();
	 public EndRequest(int passcode,LinkedBlockingQueue<SendWork> taskqueue,Hashtable<Integer,Request> requesttable,Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable){
		this.stoppasscode = passcode;
		this.taskqueue = taskqueue;
		this.requesttable = requesttable;
		this.processingrequesttable = processingrequesttable;
		start();
	}
	public void run() {
		System.out.println("Start to stop request of "+stoppasscode);
		for(SendWork task:taskqueue) {
			if(task.passcode == stoppasscode) {
				taskqueue.remove(task); //Remove tasks in task queue
			}
		}
		processingrequesttable.remove(stoppasscode); //remove processing task
		requesttable.remove(stoppasscode);// remove request
	}
}

class ServeOneWorker extends Thread{
	 private LinkedBlockingQueue<SendWork> taskqueue;
	 private Hashtable<Integer,Request> requesttable;
	 private Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable = new Hashtable<>();
	 private ServerSocket sock;
	 public ServeOneWorker(LinkedBlockingQueue<SendWork> taskqueue,Hashtable<Integer,Request> requesttable,Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable) {
			this.requesttable = requesttable;
			this.taskqueue = taskqueue;
			this.processingrequesttable = processingrequesttable;
			try {
				sock = new ServerSocket(1234);
				System.out.println("Serve one worker Server running ...");
			}catch (IOException e)
			{
				System.out.println("Error: couldn't create socket.");
			}

	 }
	public void run() {
		Socket worker = null;
	     while(true) {
			
                if( sock == null)
                	return;
                try {
					worker = sock.accept();
					ObjectOutputStream out = 
				            new ObjectOutputStream (         
				                     worker.getOutputStream());
				    ObjectInputStream in = 
				            new ObjectInputStream (
				                    worker.getInputStream());
				    UnitResult temp = (UnitResult) in.readObject();
				    System.out.println("worker:"+temp.worker_id+" needs "+temp.request_type);
				    if(temp.request_type=='t') {
				    	SendWork task = taskqueue.poll(2, TimeUnit.SECONDS);//Check task queue
						if (task!=null) {
				    	out.writeObject(task);//If there are tasks in queue write one to the worker
				    	//Generate a string processingtask in style of passcode:unit_num_A:unit_num_B:worker_id, passcode:unit_num_A:unit_num_B represents a unique task id;
				   		String processingtaskid = String.valueOf(task.passcode)+":"+String.valueOf(task.unit_num_A)+":"+String.valueOf(task.unit_num_B)+":"+String.valueOf(temp.worker_id);
				   		
				     	//Backup processing tasks
				   		if(processingrequesttable.containsKey(task.passcode)) {
				   			processingrequesttable.get(task.passcode).put(processingtaskid, task);
				   			System.out.println("Add "+processingtaskid+" into processing table");
				   		}else {
				   			Hashtable<String,SendWork> processingtask = new Hashtable<>();
				   			processingtask.put(processingtaskid, task);
				   			processingrequesttable.put(task.passcode, processingtask);
				   			System.out.println("Add "+processingtaskid+" into processing table");
				   		}
				   		 
						   		
				    	System.out.println("giving task"+task.passcode);						
						out.flush();
						in.close();
						out.close();
						worker.close();
						}else {
							in.close();
							out.close();
							worker.close();
						}
				    }else if(temp.request_type=='r') {
				    	//Generate finished task's key (passcode:unit_num_A:unit_num_B:worker_id)
				    	String donetask= String.valueOf(temp.passcode)+":"+String.valueOf(temp.unit_num_A)+":"+String.valueOf(temp.unit_num_B)+":"+String.valueOf(temp.worker_id);
						requesttable.get(temp.passcode).setMatrix_result(temp.unit_num_A, temp.unit_num_B, temp.a,temp.operator); //Find this request by key(passcode), then write unit result into result;
						requesttable.get(temp.passcode).setTotalcosttime(temp.costtime); // Add this task's time to the total cost time
						requesttable.get(temp.passcode).setFinished_task_numbers();// Update Finished_task_numbers		
						processingrequesttable.get(temp.passcode).remove(donetask); //Remove finished task from the processingtasktable in request
						System.out.println("Remove finished task:"+donetask);
						    out.flush();
							in.close();
							out.close();
							worker.close();
							
					}
                } catch (IOException | ClassNotFoundException | InterruptedException e)
    			{
    				
    			}
			
		}
	}
	}

class MonitorServer extends Thread{
	static Hashtable<String,Integer> workerstatus = new Hashtable<String,Integer>();
	private ServerSocket heartserverSocket;
	private int heartport = 1237;
	private int workerload;
	public MonitorServer(){
		
		try {
			heartserverSocket = new ServerSocket(heartport);
			System.out.println("Heart Server 1237 running ...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {
		
		Socket heart = null;
		while(true) {
			System.out.println("Recieve heart beat");
			if( heartserverSocket == null)
            	return;
            try {
            	heart = heartserverSocket.accept();
            	BufferedReader in = new BufferedReader(
					     new InputStreamReader(
					    		 heart.getInputStream()));
		
				     PrintWriter out = new PrintWriter(
					     new BufferedWriter(
					         new OutputStreamWriter(
					        		 heart.getOutputStream())));
				     String worker_id = in.readLine();
				     String loadstr = in.readLine();				    
				     workerload = Integer.parseInt(loadstr);
				     System.out.println("This worker:"+worker_id+" is alive!"+" its load is:" + workerload);
				     int flag = 1;
				     workerstatus.put(worker_id, flag);
	
				      if(workerload>1000) {
				    	 //create a new worker
				    	 System.out.println("This worker:"+worker_id+" is under full load");
				     } 
				     in.close();
				     out.close();
				     heart.close();
            }catch(IOException e){}
		}
	}
}
class ScanHeart extends Thread{
	private int scanTime = 2500;
	private LinkedBlockingQueue<SendWork> taskqueue;
	private Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable;
	public ScanHeart(LinkedBlockingQueue<SendWork> taskqueue,Hashtable<Integer,Hashtable<String,SendWork>> processingrequesttable) {
		this.taskqueue = taskqueue;
		this.processingrequesttable = processingrequesttable;	
	}
	public void run() {
		while (true) {
			if(!MonitorServer.workerstatus.isEmpty()) {
				for(Iterator itr = MonitorServer.workerstatus.keySet().iterator();itr.hasNext();) {
					String thisworker = (String) itr.next();
					if(MonitorServer.workerstatus.get(thisworker) == 0) {
						MonitorServer.workerstatus.remove(thisworker);
						//Do backup;********************************************
						Set<Integer> outer = processingrequesttable.keySet();
						 for(int i : outer) {
							 Hashtable<String,SendWork> inner = processingrequesttable.get(i);
							 Set<String> processingtaskid = inner.keySet();
							 for(String prt : processingtaskid) {
								 String[] tmp = prt.split(":");
								 if(tmp[tmp.length-1] == String.valueOf(thisworker)) {
									 try {
										taskqueue.put(processingrequesttable.get(i).get(prt));
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									 processingrequesttable.get(i).remove(prt);
								 }
							 }
						 }
					}else {
						MonitorServer.workerstatus.put(thisworker, 0);
					}
					
				}
				System.out.println("Scanning");
				try {
					Thread.currentThread().sleep(scanTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				

			}
		}
	}
}



