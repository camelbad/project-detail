import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class GUI {
    int[][] MatrixA;
    int[][] MatrixB;
    int passcode;
    send sw;
    JTextArea textArea = new JTextArea("Running status:");
	 public static int[][] GenerateMatrix(int size) {
		 int matrix[][] = new int[size][size];
		 for(int i=0;i<size;i++) {
			 for(int j=0;j<size;j++) {
				 matrix[i][j] = new java.util.Random().nextInt(100);
			 }
		 }
		 return matrix;
	 }
    private JFrame frame;
    String[] methods = {"---SELECT METHODS---","+","-","*"};
    private String fileName;
    JFileChooser fc = new JFileChooser();
    private JTextField textField;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI window = new GUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GUI() {
        initialize();
    }

    //private void myButtonAction(ActionEvent e) {


    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        JPanel panel = new JPanel();
        panel.setBounds(0, 11, 434, 44);
        frame.getContentPane().add(panel);
       
        JLabel lblMatrixA = new JLabel("Matrix A :");
        lblMatrixA.setHorizontalAlignment(SwingConstants.CENTER);
        lblMatrixA.setVerticalAlignment(SwingConstants.CENTER);

        JComboBox comboBox = new JComboBox();

        JButton btnNewButton = new JButton("Browse");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //lblMatrixA.setText("clicked");
                //myButtonAction(e);
                int returnVal = fc.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        fileName = fc.getSelectedFile().getAbsolutePath();                      
                        comboBox.addItem(fileName);
                                  
                        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
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
       			 InputStreamReader reader1 = new InputStreamReader(new FileInputStream(file), "UTF-8");
       			 BufferedReader br1 = new BufferedReader(reader1);
           			 MatrixA = new int[size-1][size-1];
           			String line1 = "";
           			 System.out.print(size-1);
           			 for(int i = 0; i<(size-1); i++)
           			 {
           				 line1 = br1.readLine();
           				 String[] token = line1.split(" ");
           				 for(int j = 0; j<(size-1); j++) {
           					 MatrixA[i][j]=Integer.parseInt(token[j]);          					
           				 }
           			 }
           			 br1.close();  
                       System.out.println("MatrixA is ready");
                    } catch (Exception ex) {
                        System.out.println("problem accessing file"+file.getAbsolutePath());
                    }
                }
                else {
                    System.out.println("File access cancelled by user.");
                }

            }
        });


        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
                                .addContainerGap(29, Short.MAX_VALUE)
                                .addComponent(lblMatrixA, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(btnNewButton)
                                .addGap(24))
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGap(6)
                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnNewButton)
                                        .addComponent(lblMatrixA, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                .addGap(2))
        );
        panel.setLayout(gl_panel);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 54, 434, 44);
        frame.getContentPane().add(panel_1);

        JLabel lblMatrixB = new JLabel("Matrix B :");
        lblMatrixB.setVerticalAlignment(SwingConstants.CENTER);
        lblMatrixB.setHorizontalAlignment(SwingConstants.CENTER);

        JComboBox comboBox_1 = new JComboBox();

        JButton button = new JButton("Browse");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //lblMatrixA.setText("clicked");
                //myButtonAction(e);
                int returnVal = fc.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        fileName = fc.getSelectedFile().getAbsolutePath();
                        comboBox_1.addItem(fileName);

                        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
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
       			 InputStreamReader reader1 = new InputStreamReader(new FileInputStream(file), "UTF-8");
       			 BufferedReader br1 = new BufferedReader(reader1);
           			 MatrixB = new int[size-1][size-1];
           			String line1 = "";
           			 System.out.print(size-1);
           			 for(int i = 0; i<(size-1); i++)
           			 {
           				 line1 = br1.readLine();
           				 String[] token = line1.split(" ");
           				 for(int j = 0; j<(size-1); j++) {
           					 MatrixB[i][j]=Integer.parseInt(token[j]);
           				 }
           			 }
           			 br1.close();  
           			System.out.println("MATRIX B IS READY");
                    } catch (Exception ex) {
                        System.out.println("problem accessing file"+file.getAbsolutePath());
                    }
                }
                else {
                    System.out.println("File access cancelled by user.");
                }

            }
        });
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
                gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 434, Short.MAX_VALUE)
                        .addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
                                .addContainerGap(29, Short.MAX_VALUE)
                                .addComponent(lblMatrixB, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(button)
                                .addGap(24))
        );
        gl_panel_1.setVerticalGroup(
                gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 44, Short.MAX_VALUE)
                        .addGroup(gl_panel_1.createSequentialGroup()
                                .addGap(6)
                                .addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button)
                                        .addComponent(lblMatrixB, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                .addGap(2))
        );
        panel_1.setLayout(gl_panel_1);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(0, 97, 434, 44);
        frame.getContentPane().add(panel_2);

        JLabel lblOperation = new JLabel("Operation :");
        lblOperation.setVerticalAlignment(SwingConstants.CENTER);
        lblOperation.setHorizontalAlignment(SwingConstants.CENTER);

        JComboBox comboBox_2 = new JComboBox(methods);

        comboBox_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	;
 	            System.out.println(comboBox_2.getSelectedItem().toString());
				sw=new send(MatrixA, MatrixB, comboBox_2.getSelectedItem().toString());
            }
        });
        
                JButton btnNewButton_1 = new JButton("Start");
                btnNewButton_1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
 
                		 new MatrixClientSide(sw,textArea);
                	 }});
        GroupLayout gl_panel_2 = new GroupLayout(panel_2);
        gl_panel_2.setHorizontalGroup(
        	gl_panel_2.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_panel_2.createSequentialGroup()
        			.addGap(23)
        			.addComponent(lblOperation, GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
        			.addGap(51)
        			.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
        			.addGap(23))
        );
        gl_panel_2.setVerticalGroup(
        	gl_panel_2.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel_2.createSequentialGroup()
        			.addGap(6)
        			.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblOperation, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnNewButton_1)
        				.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(2))
        );
        panel_2.setLayout(gl_panel_2);

        JButton btnCheck = new JButton("Check");
        btnCheck.setBounds(230, 161, 85, 23);
        frame.getContentPane().add(btnCheck);
        btnCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 passcode = Integer.parseInt(textField.getText());
                 sw = new send(passcode, false,true);
        		 new MatrixClientSide(sw,textArea);
        	 }});
        
        JLabel lblPasscode = new JLabel("PassCode:");
        lblPasscode.setBounds(32, 165, 61, 14);
        frame.getContentPane().add(lblPasscode);
        
        textField = new JTextField();
        textField.setBounds(103, 162, 106, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
                JButton btnStop = new JButton("Stop");
                btnStop.setBounds(334, 161, 77, 23);
                frame.getContentPane().add(btnStop);
                JScrollPane jScrollPane = new JScrollPane();
                jScrollPane.getViewport().add(textArea);
                jScrollPane.setBounds(67, 213, 308, 187);
                jScrollPane.setPreferredSize(new Dimension(400,400));
                frame.add(jScrollPane);
                btnStop.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                         passcode = Integer.parseInt(textField.getText());
                         sw = new send(passcode, true, false);
                		 new MatrixClientSide(sw,textArea);
                	 }});
    }
}
class MatrixClientSide extends Thread{
	static void printMatrix(int[][] x)
    {
        for (int i=0; i<x.length; i++)
        {
            for(int j=0; j<x[i].length; j++)
            {
            	consoleOutput.append(x[i][j]+" ");
            }
            consoleOutput.append("\r\n");
        }
        consoleOutput.append("\r\n");
    }
	static JTextArea consoleOutput;
	private send sw;
	public MatrixClientSide(send sw,JTextArea console) {
		this.sw = sw;
		this.consoleOutput = console;
		start();
	}
	public void run() {
 
     		try {
 				 Socket socket = new Socket("43.240.99.156", 1235);			    				
 			         ObjectOutputStream out = 
 			 	            new ObjectOutputStream (         
 			 	                        socket.getOutputStream());
 			 	    ObjectInputStream in = 
 			 	            new ObjectInputStream (
 			 	                    socket.getInputStream());
 			 
 		    out.writeObject(sw);
 		    
					send received = (send)in.readObject();
					if(received.request_status == null) {
						consoleOutput.append("Your passcode is: " + received.passcode+"\r\n");
					}else if(received.request_status.equals("finish")) {
						printMatrix(received.result);
						consoleOutput.append("The bill is: "+received.bill+"\r\n");
					}else if(received.request_status.equals("stop")) {						
						consoleOutput.append("Stop successfully. The bill is: "+received.bill);
					}else if(received.request_status.equals("Wrong passcode")){
						consoleOutput.append("Wrong passcode");
					}
					else{
						consoleOutput.append("processing"+"\r\n");
					}
					in.close();
					out.close();
					socket.close();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	 
  }
}