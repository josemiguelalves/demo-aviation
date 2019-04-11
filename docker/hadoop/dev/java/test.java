package xom.powersmiths.hbase.connection;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.DateTimeConstants;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class Main {
	
	
	private static Connection conn = getConnection();
	
	
	public static Connection getConnection()
	{
		Configuration config = HBaseConfiguration.create();
		//config.set("hbase.zookeeper.quorum", "129.100.174.152");
		config.set("hbase.zookeeper.property.clientPort", "129.100.174.152:12181");
		config.set("hbase.master", "129.100.174.152:11601");
		try
		{
			conn = ConnectionFactory.createConnection(config);
		}
		catch (IOException ex)
		{
			System.out.println("IOException : " + ex.getMessage());
		}
		catch (NoClassDefFoundError ex)
		{
			System.out.println("Error : " + ex.getMessage());
		}
		return conn;
	}
	
		
	

	public static void main(String[] args) {
				
		
		Table table;
		ResultScanner rs;
		Scan scan = new Scan();
		String rowKey = null;
		Get theGet;
		
		
		
		
		  // Instantiating a configuration class
	     // Configuration conf = HBaseConfiguration.create();

	      // Instantiating HBaseAdmin class
	      try {
	      HBaseAdmin admin = new HBaseAdmin(conn);

	      // Getting all the list of tables using HBaseAdmin object
	      HTableDescriptor[] tableDescriptor = admin.listTables();

	      // printing all the table names.
	      for (int i=0; i<tableDescriptor.length;i++ ){
	    	  
	    		System.out.println("Here First");
	         System.out.println(tableDescriptor[i].getNameAsString());
	      }
	  
	      }catch(Exception e) {
	    	  
	      }
		
		
		
		
		
		System.out.println("Here");
		try {
		  table = conn.getTable(TableName.valueOf("emp"));
		  rs = table.getScanner(scan);
		  
		  for (Result res : rs)

			{
				rowKey = Bytes.toString(res.getRow());
				theGet = new Get(Bytes.toBytes(rowKey));
			  
				Result result = table.get(theGet);	
		  
				byte [] value = result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("name"));

				byte [] value1 = result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("city"));
				
				String name = Bytes.toString(value);
				
				String city = Bytes.toString(value1);
				      
				System.out.println("name: " + name + " city: " + city);
	      
			}
		
		}catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
		
//		 // Instantiating Configuration class
//	      Configuration config = HBaseConfiguration.create();
//
//	      // Instantiating HTable class
//	      HTable table = new HTable(config, "emp");
//	      
//	      
//
//	      // Instantiating Get class
//	      Get g = new Get(Bytes.toBytes("row1"));
//
//	      // Reading the data
//	      Result result = table.get(g);
//
//	      // Reading values from Result class object
//	      byte [] value = result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("name"));
//
//	      byte [] value1 = result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("city"));
//
//	      // Printing the values
//	      String name = Bytes.toString(value);
//	      String city = Bytes.toString(value1);
//	      
//	      System.out.println("name: " + name + " city: " + city);
//		
		
		
		
		// TODO Auto-generated method stub

	
	
	
//	public void Test () {
//		
//		Table table;
//		ResultScanner rs;
//		Scan scan = new Scan();
//		String rowKey = null;
//		Get theGet;
//		
//		try {
//		  table = conn.getTable(TableName.valueOf("powersmiths:stream"));
//		  rs = table.getScanner(scan);
//		  
//		  for (Result res : rs)
//
//			{
//				rowKey = Bytes.toString(res.getRow());
//				theGet = new Get(Bytes.toBytes(rowKey));
//			  
//				Result result = table.get(theGet);	
//		  
//				byte [] value = result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("name"));
//
//				byte [] value1 = result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("city"));
//				
//				String name = Bytes.toString(value);
//				
//				String city = Bytes.toString(value1);
//				      
//				System.out.println("name: " + name + " city: " + city);
//	      
//			}
//		
//		}catch(Exception e) {
//			
//			e.printStackTrace();
//		}
//	}
	
	
	
	
}

