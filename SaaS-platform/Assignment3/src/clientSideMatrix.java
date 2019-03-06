import java.net.*;
import java.util.Scanner;
import java.io.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;



public class clientSideMatrix {
	static void printMatrix(int[][] x)
    {
        for (int i=0; i<x.length; i++)
        {
            for(int j=0; j<x[i].length; j++)
            {
                System.out.print(x[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
	 public static int[][] GenerateMatrix(int size) {
		 int matrix[][] = new int[size][size];
		 for(int i=0;i<size;i++) {
			 for(int j=0;j<size;j++) {
				 matrix[i][j] = 1;
			 }
		 }
		 return matrix;
	 }
	public static void main(String[] args) 
	         throws IOException {
/*		
//read matrixA from matrixA.txt
			 String pathname = "C:\\Users\\louxinbo\\Desktop\\A.txt";
			 File filename = new File(pathname);
			 InputStreamReader reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
			 BufferedReader br = new BufferedReader(reader);
			 String line = "";			 
//get the size of matrixA
			 int size = 0;
			 while(line != null) {
				 line = br.readLine();
				 size++;
				 if(line != null && line.equals("")) {
					 break;
				 }
			 }
			 br.close();	 
//read matrixA.txt and store it in matrixA
			 String pathname1 = "C:\\Users\\louxinbo\\Desktop\\A.txt";
			 File filename1 = new File(pathname1);
			 InputStreamReader reader1 = new InputStreamReader(new FileInputStream(filename1), "UTF-8");
			 BufferedReader br1 = new BufferedReader(reader1);
			 String line1 = "";
			 	 
			 int[][] matrixA = new int[size-1][size-1];
			 System.out.print(size-1);
			 for(int i = 0; i<(size-1); i++)
			 {
				 line1 = br1.readLine();
				 String[] token = line1.split(" ");
				 for(int j = 0; j<(size-1); j++) {
					 matrixA[i][j]=Integer.parseInt(token[j]);
				 }
			 }
			 br1.close();

//read matrixB from matrixB.txt
			 String pathnameB = "C:\\Users\\louxinbo\\Desktop\\B.txt";
			 File filenameB = new File(pathnameB);
			 InputStreamReader readerB = new InputStreamReader(new FileInputStream(filenameB), "UTF-8");
			 BufferedReader brB = new BufferedReader(readerB);
			 String lineB = "";			 
//get the size of matrixB
			 int sizeB = 0;
			 while(lineB != null) {
				 lineB = brB.readLine();
				 sizeB++;
				 if(lineB != null && lineB.equals("")) {
					 break;
				 }
			 }
			 brB.close();	 
//read matrixB.txt and store it in matrixB
			 String pathnameB1 = "C:\\Users\\louxinbo\\Desktop\\B.txt";
			 File filenameB1 = new File(pathnameB1);
			 InputStreamReader readerB1 = new InputStreamReader(new FileInputStream(filenameB1), "UTF-8");
			 BufferedReader brB1 = new BufferedReader(readerB1);
			 String lineB1 = "";
			 	 
			 int[][] matrixB = new int[size-1][size-1];
			 System.out.print(size-1);
			 for(int i = 0; i<(size-1); i++)
			 {
				 lineB1 = brB1.readLine();
				 String[] tokenB = lineB1.split(" ");
				 for(int j = 0; j<(size-1); j++) {
					 matrixB[i][j]=Integer.parseInt(tokenB[j]);
				 }
			 }
			 brB1.close();
*/			
		int[][] matrixA = GenerateMatrix(100);
		int[][] matrixB = GenerateMatrix(100);
	 	    while(true) {
	 	    	 System.out.println("Please enter the operation: +/-/*; or Stop: S; or Check Status: CS");		 	 
	        	 Scanner scanner = new Scanner(System.in);         
	        	 String input = scanner. nextLine();
	        	 
	        	 switch (input) {
	        	 	case "+": 
	            	case "-": 
	            	case "*":  
	            		 
	        				 Socket socket = new Socket("43.240.99.156", 1235);
	        			    
	        			         System.out.println("socket = " + socket);
	        			         ObjectOutputStream out = 
	        			 	            new ObjectOutputStream (         
	        			 	                        socket.getOutputStream());
	        			 	    ObjectInputStream in = 
	        			 	            new ObjectInputStream (
	        			 	                    socket.getInputStream());
	        			 
	            		send sw=new send(matrixA, matrixB, input);
	        		    out.writeObject(sw);
	        		    try {
	    					send received = (send)in.readObject();
	    					System.out.println("Your passcode is: " + received.passcode);
	    					in.close();
	    					out.close();
	    					socket.close();
	    				} catch (ClassNotFoundException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	        		    break;
	            		 
	            	case "S":
	            		 Socket socket1 = new Socket("43.240.99.156", 1235);
	        			    
    			         System.out.println("socket = " + socket1);
    			         ObjectOutputStream out1 = 
    			 	            new ObjectOutputStream (         
    			 	                        socket1.getOutputStream());
    			 	    ObjectInputStream in1 = 
    			 	            new ObjectInputStream (
    			 	                    socket1.getInputStream());
	            		System.out.println("Please enter your passcode: ");
	            		String input1 = scanner. nextLine();
	            		
	            		send sw1 = new send(Integer.parseInt(input1), true, false);
	            		out1.writeObject(sw1);	
					try {
						send received = (send)in1.readObject();
						String status = received.request_status;
						System.out.println("The status is: " + status);
						System.out.println("Your bill is: " + received.bill);
						if(status.equals("finish")) {
							printMatrix(received.result);							
						}
						in1.close();
    					out1.close();
    					socket1.close();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
	            		
	            		
	            		
	            	case "CS":
	            		 Socket socket11 = new Socket("43.240.99.156", 1235);
	        			    
    			         System.out.println("socket = " + socket11);
    			         ObjectOutputStream out11 = 
    			 	            new ObjectOutputStream (         
    			 	                        socket11.getOutputStream());
    			 	    ObjectInputStream in11 = 
    			 	            new ObjectInputStream (
    			 	                    socket11.getInputStream());
	            		System.out.println("Please enter your passcode: ");
	            		String input2 = scanner. nextLine();
	            		
	            		send sw2 = new send(Integer.parseInt(input2), false, true);
	            		out11.writeObject(sw2);
	            		
	            		try {
							send received = (send)in11.readObject();
							String status = received.request_status;
							System.out.println("The status is: " + status);
							if(status.equals("finish")) {
								printMatrix(received.result);
								System.out.println("The bill is: "+received.bill);
							}
							in11.close();
	    					out11.close();
	    					socket11.close();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
	            		break;
	            		
	            	default: System.out.println("Your input is wrong!");
	                     break;
	        	 }      
	        	 
	 	    }
	 	    
	 	   
	    
	    

    
    }	     
}
	

