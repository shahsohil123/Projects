import java.sql.*;
import java.text.DateFormatSymbols;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Udayan
 */
public class GUI extends javax.swing.JFrame {
	public JTable table;
	public JPanel panel;
	public JFrame frame = new JFrame("Mining");
	public String[] products = { "", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10" };
	public JScrollPane scrollPane ;
	public JComboBox productlist ;
	public String[][] original = new String[166][4];
	String columnNames[] = { "Original", "Trends",
			"Seasonal", "Irregular" };
    double alpha=0.3;
    double beta=0.3;
    double gamma=0.3;
    int period=12;
    int m=12;
    long[] y = new long[168];
    DataInputStream input = new DataInputStream(System.in);
    
    //Initialize: Read parameters and input data and establish connection
	void initialize() throws IOException
	{
		int i=0;
		Connection c = null;
		Connection c2 = null;
        Statement stmnt = null;
        Statement stmnt2 = null;
        String temp=null;
        String query2=null;
        int rs2=0;
        try
        {
            Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
            c = DriverManager.getConnection( "jdbc:odbc:Book2", "", "" );
            stmnt = c.createStatement();            
            String query = "select * from [Sheet1$];";
			ResultSet rs = stmnt.executeQuery( query );
            while( rs.next() )
            {
                y[i] = (rs.getLong( "Sales" ));
                i++; 
                
            }
            stmnt.close();
        }
        catch( Exception e )
        {
            System.err.println( e );
        }
        
                		
			
		final double f[]=forecast(y, alpha, beta, gamma, period, m, false);
		//printArray("\nForecast", f);
		final String rowData[][];
		rowData= new String[f.length][4];
		
		for(int j =0;j<f.length;j++)
			for(int k =0;k<1;k++)
		{
			rowData[j][0]=Double.toString(f[j]);
		}
		double avg=0;
		for(int j =11;j<f.length;j++)
			
		{
			
			
			avg+=f[j];
			
		}
		avg=avg/(f.length-12);
	
		System.out.println();
		System.out.println("Average " + avg);
		
		String[] months = new DateFormatSymbols().getMonths();
		int year=2013;
		int increment = 0;
		for(int j=11;j<f.length;j++)
		{
			rowData[j][2]=Integer.toString((int) (avg-f[j]));
			if(avg-f[j]<0)
				rowData[j][3]="The loss in this month will be "+((int)(avg-f[j])*-10000);
			else
				rowData[j][3]="The profit in this month will be "+((int)(avg-f[j])*10000);
		}
		while(increment!=f.length)
		{
		for(int j=0;j<months.length-1;j++)
		{
			
			rowData[increment][1]=months[j]+" "+Integer.toString(year);
		
			increment++;
		}
		year++;
		}
		try
		{
			
			stmnt2 = c.createStatement();			
			
			for(int j=0 ; j<168+m ; j++)
			{
				
				temp=Double.toString(f[j]);
				query2 = "insert into [Sheet3$] (Forecast) values('"+temp+"');";
				stmnt2.executeUpdate( query2 );				
			}	
			stmnt2.close();
		}
		catch( Exception e )
		{
		}
		finally
        {
            try
            {
                
                c.close();
            }
            catch( Exception e )
            {
                System.err.println( e );
            }
        }
		
		String[] columnNames = { "Forecast","Year","Sales Difference","Estimation" };
		JTable table = new JTable(rowData, columnNames);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JFrame frame= new JFrame();
		JPanel forecast_panel = new JPanel();
		frame.setSize(300,300);
		forecast_panel.add(new JScrollPane(table));
		JButton graph = new JButton("Bar Graph");
graph.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("lol");
				JFrame frame = new JFrame();
			  frame.getContentPane().add(new SimpleBarChart(f, rowData,
			    "Forecast"));
			  frame.setVisible(true);
			  frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			}
		});
		forecast_panel.add(graph);
		frame.add(forecast_panel);
		frame.setVisible(true);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}	
	
	//Obtaining the forecast
	double[] forecast(long[] y, double alpha, double beta, double gamma, int period, int m, boolean debug) throws IOException 
	{
		//validateArguments(y, alpha, beta, gamma, period, m);
		int seasons = y.length / period;
        double a0 = calculateInitialLevel(y);
        double b0 = calculateInitialTrend(y, period);
        double[] initialSeasonalIndices = calculateSeasonalIndices(y, period, seasons);

        if (debug) 
        {
            System.out.println(String.format("Total observations: %d, Seasons %d, Periods %d", y.length, seasons, period));
            System.out.println("Initial level value a0: " + a0);
            System.out.println("Initial trend value b0: " + b0);
            printArray("Seasonal Indices: ", initialSeasonalIndices);
        }

        double[] forecast = calculateHoltWinters(y, a0, b0, alpha, beta, gamma, initialSeasonalIndices, period, m, debug);

        if (debug) 
        {
            printArray("Forecast", forecast);
        }

        return forecast;
    }
    
    //Parameter Validation
    void validateArguments(long[] y, double alpha, double beta, double gamma, int period, int m) throws IOException
    {
        if (y == null) 
        {
        	throw new IllegalArgumentException("Value of y should be not null");
        }
        
        if(m <= 0)
        {
           	throw new IllegalArgumentException("Value of m must be greater than 0.");
        }
        
        if(m > period)
        {
           	throw new IllegalArgumentException("Value of m must be <= period.");
        }

        if((alpha < 0.0) || (alpha > 1.0))
        {
           	throw new IllegalArgumentException("Value of Alpha should satisfy 0.0 <= alpha <= 1.0");
        }

       	if((beta < 0.0) || (beta > 1.0))
       	{
           	throw new IllegalArgumentException("Value of Beta should satisfy 0.0 <= beta <= 1.0");
       	}

       	if((gamma < 0.0) || (gamma > 1.0))
       	{
           	throw new IllegalArgumentException("Value of Gamma should satisfy 0.0 <= gamma <= 1.0");
       	}
    }
    
    //Calculating Initial Level a0
    double calculateInitialLevel(long[] y) throws IOException
    {
        return y[0];
    }
    
    //Calculating Initial Trend b0
    double calculateInitialTrend(long[] y, int period) throws IOException
    {
        double sum = 0;

        for (int i = 0; i < period; i++) 
        {
            sum += (y[period + i] - y[i]);
        }

        return sum / (period * period);
    }
    
    //Calculating Initial Seasonal Indices
    double[] calculateSeasonalIndices(long[] y, int period, int seasons) throws IOException
    {
        double[] seasonalAverage = new double[seasons];
        double[] seasonalIndices = new double[period];
        double[] averagedObservations = new double[y.length];

        for (int i = 0; i < seasons; i++) 
        {
            for (int j = 0; j < period; j++) 
            {
                seasonalAverage[i] += y[(i * period) + j];
            }
            seasonalAverage[i] /= period;
        }

        for (int i = 0; i < seasons; i++) 
        {
            for (int j = 0; j < period; j++) 
            {
                averagedObservations[(i * period) + j] = y[(i * period) + j] / seasonalAverage[i];
            }
        }

        for (int i = 0; i < period; i++) 
        {
            for (int j = 0; j < seasons; j++) 
            {
                seasonalIndices[i] += averagedObservations[(j * period) + i];
            }
            seasonalIndices[i] /= seasons;
        }

        return seasonalIndices;
    }
    
    //Calculating Forecast by HoltWinters TES
    double[] calculateHoltWinters(long[] y, double a0, double b0, double alpha, double beta, double gamma, 
            double[] initialSeasonalIndices, int period, int m, boolean debug) throws IOException
    {
        double[] St = new double[y.length];
        double[] Bt = new double[y.length];
        double[] It = new double[y.length];
        double[] Ft = new double[y.length + m];

        // Initialize base values
        St[1] = a0;
        Bt[1] = b0;

        for (int i = 0; i < period; i++) 
        {
            It[i] = initialSeasonalIndices[i];
        }

        // Start calculations
        for (int i = 2; i < y.length; i++) 
        {

        	// Calculate overall smoothing
	        if ((i - period) >= 0) 
    	    {
        	    St[i] = alpha * y[i] / It[i - period] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
        	} 
        	else 
        	{
            	St[i] = alpha * y[i] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
        	}

        	// Calculate trend smoothing
        	Bt[i] = gamma * (St[i] - St[i - 1]) + (1 - gamma) * Bt[i - 1];

        	// Calculate seasonal smoothing
        	if ((i - period) >= 0) 
        	{
            	It[i] = beta * y[i] / St[i] + (1.0 - beta) * It[i - period];
        	}

        	// Calculate forecast
        	if (((i + m) >= period)) 
        	{
            	Ft[i + m] = (St[i] + (m * Bt[i])) * It[i - period + m];
        	}

        	if (debug) 
        	{	
            	System.out.println(String.format("i = %d, y = %d, S = %f, Bt = %f, It = %f, F = %f", i, y[i], St[i], Bt[i], It[i], Ft[i]));
        	}
        }

        return Ft;
    }
    
    //Utility print array method
    void printArray(String description, double[] data) throws IOException
    {
        System.out.println(description);
        for (int i = 0; i < data.length; i++) 
        {
            System.out.println(data[i]);
        }
    }
    
    public GUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame2 = new javax.swing.JFrame();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        
        jComboBox1 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jFrame1 = new javax.swing.JFrame();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jFrame3 = new javax.swing.JFrame();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jFrame4 = new javax.swing.JFrame();
        jLabel12 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();

        jFrame2.setMinimumSize(new java.awt.Dimension(363, 302));
        jFrame2.setPreferredSize(new java.awt.Dimension(363, 302));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("WhatNxt");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Welcome Mr Vice-President!");

        jComboBox1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Clothing", "Books", "Cell Phones", " " }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton2.setText("View Sales Report");
        jButton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				mining();
				
			}
			public void mining() {

				
			
				productlist = new JComboBox(products);
				panel = new JPanel();
				
				panel.setPreferredSize(new Dimension(1000, 500));
				panel.add(productlist);
				frame.setLocationRelativeTo(null);
				
				table = new JTable(original, columnNames);

				scrollPane = new JScrollPane(table);
				panel.add(scrollPane);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setVisible(true);
				productlist.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent arg0) {
						if(arg0.getStateChange()==ItemEvent.SELECTED)
						{
							String str = (String) productlist
									.getSelectedItem();

							try {
								Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
								Connection con = null;
								if (str == "1") {
									con = DriverManager
											.getConnection("jdbc:odbc:1");
								}
								if (str == "2") {
									con = DriverManager
											.getConnection("jdbc:odbc:2");
								}
								if (str == "3") {
									con = DriverManager
											.getConnection("jdbc:odbc:3");
								}
								if (str == "4") {
									con = DriverManager
											.getConnection("jdbc:odbc:4");
								}
								if (str == "5") {
									con = DriverManager
											.getConnection("jdbc:odbc:5");
								}
								if (str == "6") {
									con = DriverManager
											.getConnection("jdbc:odbc:6");
								}
								if (str == "7") {
									con = DriverManager
											.getConnection("jdbc:odbc:7");
								}
								if (str == "8") {
									con = DriverManager
											.getConnection("jdbc:odbc:8");
								}
								if (str == "9") {
									con = DriverManager
											.getConnection("jdbc:odbc:9");
								}
								if (str == "10") {
									con = DriverManager
											.getConnection("jdbc:odbc:10");
								}
								Statement st = con.createStatement();
								ResultSet rs = st
										.executeQuery("Select * from [Sheet1$]");

								
								int numberOfColumns = 5;
								int j = 0;
								while (rs.next()) {

									for (int i = 1; i <= numberOfColumns; i++) {
										if (i == 1) {
											String columnValue = rs
													.getString(i);
											original[j][0] = columnValue;
											System.out
													.println(original[j][0]);

										}
										if (i == 2) {
											String columnValue = rs
													.getString(i);
											original[j][1] = columnValue;

										}

										if (i == 4) {
											String columnValue = rs
													.getString(i);
											original[j][2] = columnValue;

										}
										if (i == 5) {
											String columnValue = rs
													.getString(i);
											original[j][3] = columnValue;

										}

									}

									j++;
								}
							for(;j<166;j++)
							{
								for(int i=0;i<4;i++)
								{
								original[j][i]="";
								}
							}
								
								frame.repaint();
								
								
								

						}
							catch (Exception e) {
								// TODO: handle exception
							}
						// TODO Auto-generated method stub
						
					}
					}});
				
			}
			private void datamining() {
			
				
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	
		            	 JFrame frame=new JFrame("Data Mining");
		            	 final JPanel panel = new JPanel();
		            	 panel.setPreferredSize(new Dimension(1000,500));
		        		 String[] products={"","1","2","3","4","5","6","7","8","9","10"};
		        		 final JComboBox productlist=new JComboBox(products);
		        		 JScrollPane pane = new JScrollPane(panel);
		        		 panel.add(productlist);
		        		 frame.getContentPane().add(pane);
		        		 frame.pack();
		                 frame.setLocationRelativeTo(null);
		                 frame.setVisible(true);
						 
		                 final JTextArea text = new JTextArea();
//		                 text.setPreferredSize(new Dimension(4000,));
		                 final JTextArea text2 = new JTextArea();
//		                 text2.setPreferredSize(new Dimension(4000,300));
		                 final JTextArea text4 = new JTextArea();
//		                 text4.setPreferredSize(new Dimension(4000,300));
		                 final JTextArea text5 = new JTextArea();
//		                 text5.setPreferredSize(new Dimension(4000,300));
						final JTextArea text6 = new JTextArea();
						final JTextArea text7 = new JTextArea();
				  		final JTextArea text8 = new JTextArea();
						final JTextArea text9 = new JTextArea();
						final JTextArea text10 = new JTextArea();
		                 text.setEditable(false);
		                 text2.setEditable(false);
		                 text4.setEditable(false);
		                 text5.setEditable(false);
		                 text6.setEditable(false);
		                 text7.setEditable(false);
		                 text8.setEditable(false);
		                 text9.setEditable(false);
		                 text10.setEditable(false);
		                 text.setAutoscrolls(true);
		                 text.setLineWrap(true);
		                 text2.setAutoscrolls(true);
		                 text2.setLineWrap(true);
		                 text4.setAutoscrolls(true);
		                 text4.setLineWrap(true);
		                 text5.setAutoscrolls(true);
		                 text5.setLineWrap(true);
		                 text6.setAutoscrolls(true);
		                 text6.setLineWrap(true);
		                 text7.setAutoscrolls(true);
		                 text7.setLineWrap(true);
		                 text8.setAutoscrolls(true);
		                 text8.setLineWrap(true);
		                 text9.setAutoscrolls(true);
		                 text9.setLineWrap(true);
		                 text10.setAutoscrolls(true);
		                 text10.setLineWrap(true);
		                 JScrollPane scroll = new JScrollPane(text) ;
		                 JScrollPane scroll2 = new JScrollPane(text2);
		                 JScrollPane scroll4 = new JScrollPane(text4);
		                 JScrollPane scroll5 = new JScrollPane(text5);
		                 scroll.setPreferredSize(new Dimension(300,200));
		                 scroll2.setPreferredSize(new Dimension(300,200));
		                 scroll4.setPreferredSize(new Dimension(300,200));
		                 scroll5.setPreferredSize(new Dimension(300,200));
		                 JLabel l1 = new JLabel("Original");
		                 panel.add(l1);
		                 panel.add(scroll);
		                 JLabel l2 = new JLabel("Trends");
		                 panel.add(l2);
		                 panel.add(scroll2);
		                 JLabel l4 = new JLabel("seasonal");
		                 panel.add(l4);
		                 panel.add(scroll4);
		                 JLabel l5 = new JLabel("irregular");
		                 panel.add(l5);
		                 panel.add(scroll5);
		                 

		                 productlist.addItemListener(new ItemListener()
		                 {
		                	 
		                		String original[][]=new String[166][4];
		                    	String columnNames[]= {"Original","Trends","Seasonal","Irregular"};
		           
							@Override
							public void itemStateChanged(ItemEvent arg0) {
								// TODO Auto-generated method stub
								String str = (String)productlist.getSelectedItem();

								try
								{
								Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
								Connection con = null;
								if(str== "1")
								{
									  con = DriverManager.getConnection("jdbc:odbc:1");
								}
								if(str==  "2"){
									  con = DriverManager.getConnection("jdbc:odbc:2");
								}
								if(str==  "3"){
									  con = DriverManager.getConnection("jdbc:odbc:3");
								}
								if(str==  "4"){
									  con = DriverManager.getConnection("jdbc:odbc:4");
								}
								if(str== "5"){
									  con = DriverManager.getConnection("jdbc:odbc:5");
								}
								if(str== "6")
								{
									  con = DriverManager.getConnection("jdbc:odbc:6");
								}
								if(str==  "7"){
									  con = DriverManager.getConnection("jdbc:odbc:7");
								}
								if(str==  "8"){
									  con = DriverManager.getConnection("jdbc:odbc:8");
								}
								if(str==  "9"){
									  con = DriverManager.getConnection("jdbc:odbc:9");
								}
								if(str== "10"){
									  con = DriverManager.getConnection("jdbc:odbc:10");
								}
								 Statement st = con.createStatement();
						            ResultSet rs = st.executeQuery("Select * from [Sheet1$]");

						            ResultSetMetaData rsmd = rs.getMetaData();
						            int numberOfColumns = 5;
						            text.setText("");
					            	text2.setText("");
					            	text4.setText("");
					            	text5.setText("");
					            	
					            	int j=0;
						            while (rs.next()) {

						            	for (int i = 1; i <= numberOfColumns; i++) {
						                	if (i == 1)
						                    {
						                        String columnValue = rs.getString(i);
						                        text.append(columnValue+"\n");
						                        original[j][0]= columnValue;
						                        System.out.println(original[j][0]);
						                        
						                       
						                    }
						                	if (i == 2)
						                    {
						                        String columnValue = rs.getString(i);
						                        text2.append(columnValue+"\n");
						                        original[j][1]= columnValue;
						                 
						                        	
						                        
						                    }

						                	if (i == 4)
						                    {
						                        String columnValue = rs.getString(i);
						                        text4.append(columnValue+"\n");
						                        original[j][2]= columnValue;
						                        	
						                        	
						                        
						                    }
						                	if (i == 5)
						                    {
						                        String columnValue = rs.getString(i);
						                        text5.append(columnValue+"\n");
						                        original[j][3]= columnValue;
						                        	
						                        
						                    }
						                    

						                }
						            		
						            	
						            	j++;
						            }
						            System.out.println("lol");
						            JTable table= new JTable(original,columnNames);
						            panel.add(new JScrollPane(table));
//						            
								}
								catch(Exception e){

							}
								
							}
		                 });


		            }
		        });
		    }
        });
        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton3.setText("View Forecast Report");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jButton3ActionPerformed(evt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        jButton4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton4.setText("LOGOUT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame2Layout.createSequentialGroup()
                .addGroup(jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame2Layout.createSequentialGroup()
                        .addGroup(jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                   
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
                            .addGroup(jFrame2Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jFrame2Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel5)))
                        .addGap(0, 63, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(34, 34, 34)
                .addGroup(jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );

        jFrame1.setMinimumSize(new java.awt.Dimension(363, 302));
        jFrame1.setPreferredSize(new java.awt.Dimension(363, 302));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("WhatNxt");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Welcome Mr Sales Manager!");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Select Product:");

        jComboBox2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Clothing", "Books", "Cell Phones", " " }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton5.setText("View Sales");

        jButton6.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton6.setText("View Forecast");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton7.setText("LOGOUT");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton8.setText("Check Stock");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel8))
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))))
                        .addGap(0, 41, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton7)))
                .addContainerGap())
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addContainerGap())
        );

        jFrame3.setMinimumSize(new java.awt.Dimension(363, 302));
        jFrame3.setPreferredSize(new java.awt.Dimension(363, 302));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("WhatNxt");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Welcome Mr System Admin!");

        jButton11.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton11.setText("LOGOUT");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton12.setText("Check System");
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrame3Layout = new javax.swing.GroupLayout(jFrame3.getContentPane());
        jFrame3.getContentPane().setLayout(jFrame3Layout);
        jFrame3Layout.setHorizontalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame3Layout.createSequentialGroup()
                .addGroup(jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame3Layout.createSequentialGroup()
                        .addGroup(jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame3Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jFrame3Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel11)))
                        .addGap(0, 41, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton11)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
        );
        jFrame3Layout.setVerticalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addGap(36, 36, 36)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(jButton11)
                .addContainerGap())
        );

        jFrame4.setMinimumSize(new java.awt.Dimension(363, 302));
        jFrame4.setPreferredSize(new java.awt.Dimension(363, 302));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("INVALID USER ID OR PASSWORD");

        jButton9.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton9.setText("OK");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrame4Layout = new javax.swing.GroupLayout(jFrame4.getContentPane());
        jFrame4.getContentPane().setLayout(jFrame4Layout);
        jFrame4Layout.setHorizontalGroup(
            jFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame4Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel12)
                .addContainerGap(76, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton9)
                .addGap(154, 154, 154))
        );
        jFrame4Layout.setVerticalGroup(
            jFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame4Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jLabel12)
                .addGap(46, 46, 46)
                .addComponent(jButton9)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WhatNxt");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("WhatNxt");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel2.setText("USER ID:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel3.setText("PASSWORD:");

        jTextField1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton1.setText("LOGIN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField1)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jButton1)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    	initialize();
    }//GEN-LAST:event_jButton3ActionPerformed
 

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if((jTextField1.getText().equals("666"))&&jPasswordField1.getText().equals("kjsce"))
        {
            jFrame2.setVisible(true);
            setVisible(false);            
        }
        else if((jTextField1.getText().equals("555"))&&jPasswordField1.getText().equals("mumbai"))
        {
            jFrame1.setVisible(true);
            setVisible(false);
        }
        else
        {
            jFrame4.setVisible(true);
        }
        jTextField1.setText("");
        jPasswordField1.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try{
          initialize();}
        catch(Exception e)
        {}
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        setVisible(true);
        jFrame2.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        setVisible(true);
        jFrame1.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        setVisible(true);
        jFrame3.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jFrame4.setVisible(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JFrame jFrame3;
    private javax.swing.JFrame jFrame4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
