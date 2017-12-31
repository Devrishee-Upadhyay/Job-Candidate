import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;
import java.sql.*;

public class  Candidate
{
       JFrame frame;
	   JPanel panel;

	   JLabel lblname,lbldesc,lblcampus,lblcourse;
	   JTextField name;
	   JTextArea description;
	   JRadioButton rdoug,rdopg;

	   JComboBox city;
	   ButtonGroup course;
	   JButton btnSubmit, btnReset, btnExit,btnDisplay;

static 
	{
		// PLAF
		try
		{
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		}
		catch (Exception ex)
		{
			System.exit(0);
		}


		try
		{
			// load the database driver
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception ex)
		{
			System.out.println("Database Driver Missing .... ");
			System.exit(0);
		}
	}


   class MySQL
   {

	   // protocol:subprotocol:dsn					dsn   //servername:portnumber/databasename
		public static final String url = "jdbc:mysql://localhost:3306/college";		
		public static final String user = "root";		
		public static final String pass = "";		


		public String insert(Model m) throws Exception
		{
			String query = "insert into candidate values ( ?, ?, ?, ?)";			//? placeholder     user supplied value
			Connection sqlcon = DriverManager.getConnection(url, user, pass); 
			PreparedStatement ps = sqlcon.prepareStatement(query);		
			
			ps.setString( 1, m.getName() );
			ps.setString( 2, m.getDescription() );
			ps.setString( 3, m.getCourse() );
			ps.setString( 4, m.getCity() );
			int rows = ps.executeUpdate();			//  insert update delete  ke saath .executeUpdate use hota hai
			sqlcon.close();
			if(rows>0)
				return "Success";
			else
				return "Insert Failed";
		}

		public ArrayList<Model> select() throws Exception
	   {
			ArrayList<Model> m=new ArrayList<Model>();
			String query = "select * from candidate";			
			Connection sqlcon = DriverManager.getConnection(url, user, pass); 
			Statement s = sqlcon.createStatement();		
			ResultSet rs = s.executeQuery(query);		

			while(rs.next())
		   {
				Model mi=new Model(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
				m.add(mi);

		   }

		   sqlcon.close();
			return m;
	   }

   }

   class ExitClickHandler implements ActionListener
   {
	   public void actionPerformed(ActionEvent e)
	   {
		   frame.dispose();
	   }

   }

   class ResetClickHandler implements ActionListener		// Action means Click
	{
		// handler method			whenever the button is clicked 
		public void actionPerformed(ActionEvent e)		
		{
			reset();
		}
	}

   class SubmitClickHandler implements ActionListener
   {
	  public void actionPerformed(ActionEvent e)		
	{
	   try
	   {
		 String n=name.getText().trim();
		 String d=description.getText().trim();
		 String ca= (city.getSelectedIndex()>0)?city.getSelectedItem().toString():"";
		 String co=rdoug.isSelected()?"Internship":(rdopg.isSelected()?"Full time":"");


		 String errors = "";

				if(  n.length() == 0  )	{	errors += "Name is missing\n";	}
				if(  ca.length() == 0  )	{	errors += "City is not selected\n";	}
				if(  co.length() == 0  )	{	errors += "Course not selected\n";	}

				if(errors.length() > 0 )
				{
					throw new RuntimeException(errors);
				}
				else
				{
				    Model candidate = new Model(n,d,co,ca);
					String result = new MySQL().insert(candidate);
					if(result.equals("Success"))
					{
						JOptionPane.showMessageDialog(frame, "Inserted Succesfully." );						
						reset();
					}
					else
					{
						throw new RuntimeException("Insert Failed");
					}
					
				}

	   }

	   catch (Exception ex)
	   {
		   JOptionPane.showMessageDialog(frame, "Errors in form : \n----------------------------\n" + ex.getMessage());
	   }
	  
	}

   }


   class DisplayClickHandler implements ActionListener		
	{
		public void actionPerformed(ActionEvent e)		
		{
			try
			{
				ArrayList<Model> cans = new MySQL().select();          // calling select() function from MySQL class which returns an Array List.
				ArrayList<String>  lines = new ArrayList<String>();    // Creating an array list of String type to store entries.
				for(Model m : cans)                                    
				{
					lines.add(m.toString());                         // Storing values in Array List in form of string crated by toString() method in Model class.
				}
                int size = lines.size();	
				Object[][] candidates = new Object[size][4];			// data
				for(int x=0; x<size; x++)	{	candidates[x] = lines.get(x).split("\t");	}			
				String[] headers = { "Name","Description and Experience","Job Type","Branch Preferred"};


                DefaultTableModel model = new 	DefaultTableModel (candidates, headers);
				JTable table = new JTable(model);


                JDialog dialog = new JDialog(frame, "Candidates List", true);		// modal
				dialog.setSize( 600, 500 );
				dialog.add( new JScrollPane(  table  ) );
				dialog.setVisible(true);
			}
			catch (Exception ex)
			{
			}
		}
	}



   public void registerListeners()
	{
		//source  addXXXListener	( instance of handler class )
		btnExit.addActionListener(new ExitClickHandler());
		btnSubmit.addActionListener(new SubmitClickHandler());
		btnReset.addActionListener(new ResetClickHandler());
		btnDisplay.addActionListener(new DisplayClickHandler());
	}
   public Candidate()
	{
	  frame =new JFrame("Candidate Information Form");
	  panel=new JPanel();
	  lblname=new JLabel("Name");
	  lblcampus=new JLabel("Branch Preferred");
	  lbldesc=new JLabel("Description and Experience");
	  lblcourse=new JLabel("Job Type");

	  name=new JTextField(30);
	  description=new JTextArea(10,40);
	  course=new ButtonGroup();
      
	  rdoug=new JRadioButton("Internship");
	  rdopg=new JRadioButton("Full Time");

	  campus=new JComboBox(new String[]{"Select","New Delhi","Jaipur","Pune","Banglore"});

     
		btnSubmit = new JButton("Submit");
		btnReset = new JButton("Reset"); 
		btnExit = new JButton("Exit");
		btnDisplay=new JButton("Display");
	}


    GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	public void place(int x,int y,JComponent j)
	{
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(5,5,5,5);		// top right bottom left spaces to be left
		gbc.gridx = x;			
		gbc.gridy = y;			
		gbl.setConstraints(j, gbc);				
		panel.add(j);
	}
    

	public void setWindow()
	{
      panel.setLayout(gbl);

      course.add(rdoug);
	  course.add(rdopg);

	  place(0,0,lblname);
	  place(1,0,name);
	  place(0,1,lbldesc);
	  place(1,1,new JScrollPane(description));
      place(0,2,lblcampus);
	  place(1,2,campus);

	  JPanel c=new JPanel();
	  c.add(rdoug);
	  c.add(rdopg);
	  place(0,3,lblcourse);
	  place(1,3,c);


      JPanel pButton = new JPanel( new GridLayout() );
		pButton.add(btnSubmit);
		pButton.add(btnReset);
		pButton.add(btnExit);
		pButton.add(btnDisplay);
		place(1,6,new JScrollPane(pButton));

		frame.add(panel);
	}


	public void launch()
	{
		setWindow();
		//style();
		registerListeners();
		 frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );			
		frame.setVisible(true);
		frame.pack();
		// frame.setResizable(false);
	}
     
	 public void reset()
	{
		name.setText("");
		description.setText("");
		course.clearSelection();
		campus.setSelectedIndex(0);
	
	}
	


	public static void main(String[] args) 
	{
		try
		{
			Candidate w = new Candidate();
		    w.launch();
		}
		catch (Exception ex)
		{
			System.out.println("Error");
		}
		
	}
}
