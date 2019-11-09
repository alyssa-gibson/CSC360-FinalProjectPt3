// Name: 		  Alyssa Gibson
// Last Modified: 6 November 2019
// Class:		  CSCE 360: Database Systems
// Assignment:	  Final Project Part 3

package part3;
import java.sql.*;
import java.util.*;
public class part3 {
	
	public static void main(String[] args){
            try{
            	// Initiate driver
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }catch (Exception e){
                System.out.println("Can't load driver");
            }
            try{
            	// Establish connection to PHPMyAdmin
               System.out.println("Starting Connection........");
               Connection con = DriverManager.getConnection(
                      "jdbc:mysql://localhost/realtorbase", "username", "password");
               System.out.println("Connection Established");
               // heck yea
               Scanner kb = new Scanner(System.in); // kb = keyboard
               int uin = 1;							// uin = user input
               // uin is initialized as 1, but the 1st menu option won't be triggered because
               // 	  the code prompts the user for an input before the switch statement is even reached.
               while(uin != 0) {
            	   // 0 is the exit clause of the menu.
            	   System.out.println("\n-------------------------------------------------------------------------\n\n"
                  	   		+ "\t    ::::    ::::  :::::::::: ::::    ::: :::    ::: \n" + 
                  	   		"\t    +:+:+: :+:+:+ :+:        :+:+:   :+: :+:    :+: \n" + 
                  	   		"\t    +:+ +:+:+ +:+ +:+        :+:+:+  +:+ +:+    +:+ \n" + 
                  	   		"\t    +#+  +:+  +#+ +#++:++#   +#+ +:+ +#+ +#+    +:+ \n" + 
                  	   		"\t    +#+       +#+ +#+        +#+  +#+#+# +#+    +#+ \n" + 
                  	   		"\t    #+#       #+# #+#        #+#   #+#+# #+#    #+# \n" + 
                  	   		"\t    ###       ### ########## ###    ####  ########  \n" + 
                  	   		"\n-------------------------------------------------------------------------"
                  	   		+ "\n   Input the number corresponding to the query you"
                  			+ " would like to run.\n-------------------------------------------------------------------------\n"
                  			+ "   1 - Show the realtor id and realtor name for all realtors\n      "
                  			+ " that are working with only buying clients.\n\n"
                  			+ "   2 - For a particular land buying client, show all properties\n      "
                  			+ " that match the client’s interests.\n       Show the property id, listing price,"
                  			+ " address, and acreage.\n\n"
                  			+ "   3 - Show the realtor id and name for any realtor involved\n       in more than three "
                  			+ "transactions as the buying realtor.\n\n"
                  			+ "   4 - Show the property id for the property that gained the\n       most value (selling price "
                  			+ "exceeded listing price).\n\n"
                  			+ "   5 - Show the realtor that made the most money as a buying\n       realtor involved in a transaction."
                  			+ "\n       Realtors earn 3% of the selling price.\n\n"
                  			+ "   0 - Exit.\n");
                  	  uin = kb.nextInt();
                      switch (uin) {
          	        	case (1): {
					// Query #1
	       	   	        	ExecQuery1(con);
          	                	break;
          	        	}
          	        	case (2): {
					// Query #2
					// Note: Recommended ID to use for grading is 587170
          	        		System.out.println("Enter the Land Buyer ID:\n");
          	        		String id = kb.next();
          	        		ExecQuery2(con, id);
       	   	            		break;
          	        	}
          	        	case (3): {
					// Query #6
					ExecQuery6(con);
					break;
          	        	}
          	        	case (4): {
					// Query #7
					ExecQuery7(con);
					break;
          	        	}
          	        	case(5): {
					// Query #10
					ExecQuery10(con);
					break;
          	        	}
                  	}
               }
               kb.close();		// Close the scanner to avoid the annoying "resource leak" warning.
               // If the program has reached this point, it is safe to print a cute goodbye message. :)
               System.out.println("  ,-.       _,---._ __  / \\\n" + 
               		" /  )    .-'       `./ /   \\\n" + 
               		"(  (   ,'            `/    /|\n" + 
               		" \\  `-\"             \\'\\   / |\n" + 
               		"  `.              ,  \\ \\ /  |\n" + 
               		"   /`.          ,'-`----Y   |\n" + 
               		"  (            ;        |   '\n" + 
               		"  |  ,-.    ,-'         |  /\n" + 
               		"  |  | (   |        bye | /\n" + 
               		"  )  |  \\  `.___________|/\n" + 
               		"  `--'   `--'");
            }
            catch (SQLException e){
                System.out.println(e.getMessage() + " Can't connect to database");
                while(e!=null){
                   System.out.println("Message: " + e.getMessage());
                   e = e.getNextException();
                }
            }
            catch (Exception e){
                System.out.println("Other Error");
            }
        }
		public static void ExecQuery1(Connection con) throws SQLException {
			// Purpose: Execution of Query 1.
			// Parameters: Connection con
			//			   - allows for access to the prepareStatement() 
			//			     and executeQuery() methods.
			String query = "";
			query = "select distinct r.rname, r.rid\n" + 
	                 		"from realtor r join buyer b on r.rid = b.rid\n" + 
	                 		" where not exists \n" + 
	                 		" (select *\n" + 
	                 		" from seller s\n" + 
	                 		" where r.rid = s.rid);";
	        PreparedStatement stmt = con.prepareStatement(query);
	        ResultSet result = stmt.executeQuery();
	        System.out.println("\n----------------------------\n" 
	            + "   RID  |     RNAME     " 
	            + "\n----------------------------");
	        while(result.next()) {
	            System.out.println(" " + result.getString("rid") 
	            	+ " | " + result.getString("rname")); 
	        }
		}
		
		public static void ExecQuery2(Connection con, String id) throws SQLException {
			// Purpose: Execution of Query 2.
			// Parameters: Connection con
			//			   - allows for access to the prepareStatement() 
			//			     and executeQuery() methods.
			//			   String id
			//			   - takes in a user chosen ID to search the data by.
			String query = "";
			query = "select la.lid, l.price, l.address, la.acreage" +
					" from land la join listing l on la.lid = l.lid, landbuyer lb" +
      			    " where (lb.cid = " + id +
      			    ") and (lb.pmin <= la.acreage) and (lb.pmax >= la.acreage)";
	        PreparedStatement stmt = con.prepareStatement(query);
	        ResultSet result = stmt.executeQuery();
	        System.out.println("\n--------------------------------------------------------------------------\n" 
            	+ " LID |  PRICE   | ACREAGE | ADDRESS                                      " 
            	+ "\n--------------------------------------------------------------------------");
	        while(result.next()) {
	        System.out.println("  " + result.getString("lid") 
            	+ "  |  " + result.getString("price")
            	+ " | " + result.getString("acreage")
            	+ "     | " + result.getString("address")); 
	        }
		}
		
		public static void ExecQuery6(Connection con) throws SQLException {
			// Purpose: Execution of Query 6.
			// Parameters: Connection con
			//			   - allows for access to the prepareStatement() 
			//			     and executeQuery() methods.
			String query = "";
			query = "select distinct r.rid, r.rname " + 
					"from realtor r, client c, transaction t join partake p on t.tid = p.tid " +
					"where p.bid = c.cid " +
					"group by c.rid " +
					"having (count(*) >= 3);";
  	        PreparedStatement stmt = con.prepareStatement(query);
  	        ResultSet result = stmt.executeQuery();
  	        System.out.println("\n----------------------------\n" 
  	            + "   RID  |     RNAME     " 
  	            + "\n----------------------------");
  	        while(result.next()) {
  	        	System.out.println(" " + result.getString("r.rid") 
  	            	+ " | " + result.getString("r.rname")); 
  	        }
		}
		
		public static void ExecQuery7(Connection con) throws SQLException {
			// Purpose: Execution of Query 7.
			// Parameters: Connection con
			//			   - allows for access to the prepareStatement() 
			//				 and executeQuery() methods.
			String query = "";
			query = "select t.lid, MAX(t.price - l.price) " +
					"from listing l, transaction t " +
					"where t.lid = l.lid and l.price < t.price;";
  	        PreparedStatement stmt = con.prepareStatement(query);
  	        ResultSet result = stmt.executeQuery();
  	        System.out.println("\n----------------------------\n" 
  	        		+ "   LID  | PRICE " 
  	        		+ "\n----------------------------");
  	        while(result.next()) {
  	        	System.out.println("      " + result.getString("t.lid") 
  	            	+ " | " + result.getString("MAX(t.price - l.price)")); 
  	        }
		}
		
		public static void ExecQuery10(Connection con) throws SQLException {
			// Purpose: Execution of Query 10.
			// Parameters: Connection con
			//			   - allows for access to the prepareStatement() 
			//				 and executeQuery() methods.
			String query = "";
			query = "select distinct r.rid, r.rname, MAX(t.price*0.03) " +
					"from transaction t join partake p on t.tid = p.tid, realtor r join client c on r.rid = c.cid " +
					"where p.bid = c.cid " +
					"group by c.rid " +
					"having MAX(t.price*0.03);";
  	        PreparedStatement stmt = con.prepareStatement(query);
  	        ResultSet result = stmt.executeQuery();
  	        System.out.println("\n----------------------------\n" 
  	            + "   RID  |   MAX   | RNAME " 
  	            + "\n----------------------------");
  	        while(result.next()) {
  	            System.out.println(" " + result.getString("r.rid") 
  	            	+ " | " + result.getString("MAX(t.price*0.03")
  	            	+ " | " + result.getString("r.rname")); 
  	        }
		}
    }

