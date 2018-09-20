import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

public class P2 {
	static Map<String, Double> responseMap = new LinkedHashMap<String, Double>();
	static Map<String, Long> timestampMap = new LinkedHashMap<String, Long>();
	static LinkedList<String> arrlist = new LinkedList<String>();
	static boolean flag;
	static int cmdarg;
		
public static void main(String[] args) throws UnknownHostException, IOException {
	
	BufferedReader input1 = new BufferedReader(new InputStreamReader(System.in));
	if ((args == null) || (args.length == 0)) {
    	System.out.println("Invalid number of arguments.Enter port number");
        System.exit(0);
  	}else  {
    	cmdarg =Integer.parseInt(args[0]);
    	}
	System.out.println("Enter the decaying constant");
	try {
		String a =input1.readLine();
        	flag = true;
	        if(a.matches("^\\d+(()|(\\.\\d+)?)$")){
        	double c =Double.valueOf(a);
	        if (c > 0.85 && c < 1.0) {
        
        	String serverAddr = "localhost";
        	
        	Socket client = new Socket(serverAddr, cmdarg);
        	System.out.println("Connected");

                try {
                      //  client.setKeepAlive(true);
                        System.out.println("Assigning new threads for this client");
                        Thread t1 = new InputHandler(client,c);
                        t1.start();
                        Thread t2 = new QueryHandler();
                        t2.start();
                       	
                 } catch (Exception e) {
                        client.close();        
                        e.printStackTrace();
                	}   
         	}else {
       	 		System.out.println("Invalid decaying constant.Decay constant should be between 0.8 and 1"); 
       	 		main(args);
       	 	}
        	}else {
        		 System.out.println("Invalid decaying constant.Decay constant should be between 0.8 and 1"); 
        	 	main(args);
        	 }
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
public static class InputHandler extends Thread
	{
	    final Socket client;
	    final Double c;
	    String s;
	    static Map<String, Long> map = new LinkedHashMap<String, Long>();
	    static ArrayList<String> entries = new ArrayList<String>();
	    static Map<String, Double> map1 = new LinkedHashMap<String, Double>();
	    static ArrayList<String> entries1 = new ArrayList<String>();
	    BufferedReader input;
	    public InputHandler(Socket client, Double c)  {
	        this.client = client;
	        this.c = c;
	    }

	    @Override
	    public void run()
	    {
	    	try {
	    	    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    	    } catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    while(true){
	    	if(flag ==false) {
	    		break;
	    	}
		Collections.synchronizedMap(timestampMap);
            	synchronized(timestampMap) {
                	try {
				if ((s=input.readLine()) != null)  {
					entries1.add(s);
					for (String entry : entries1) {
				    		if (entry.contains(" ")) {
				        		String[] keyValue = entry.split(" ");
				        		if (map1.containsKey(keyValue[1])) {
					         		Iterator<Entry<String, Double>> it = map1.entrySet().iterator();
				        		  	while (it.hasNext()) {
				              				Entry<String, Double> pair = it.next();
				              				Double newCount = pair.getValue() * c;
				              				pair.setValue(newCount);
				         		 	}		
				        		  	Double x = (map1.get(keyValue[1]) + 1);
				        		  	map1.put(String.valueOf(keyValue[1]), x);
				        		}else{
				        			Iterator<Entry<String, Double>> it = map1.entrySet().iterator();
				        		  	while (it.hasNext()) {
				              				Entry<String, Double> pair = it.next();
				              				Double newCount = pair.getValue() * c;
				              				pair.setValue(newCount);
				         		 	}	
				        			map1.put(String.valueOf(keyValue[1]), (double) 1);}
				        		for(Iterator<Map.Entry<String,Double>>it=map1.entrySet().iterator();it.hasNext();){
				        		     Map.Entry<String, Double> entry1 = it.next();
				        		     if (entry1.getValue() < 0.5) {
				        		          it.remove();
				        		     }
				        		 }
				    		}
					}
						entries1.clear();
						responseMap = map1;
						entries.add(s);
						for (String entry : entries) {
					    		if (entry.contains(" ")) {
					        		String[] keyValue = entry.split(" ");
					        		map.put(keyValue[1], Long.valueOf(keyValue[0]));
					   		}
						}
						entries.clear();
						timestampMap = map;
						}
				else {
        			flag = false;
        				System.out.println("Server not sending any input !!!!");
        				//break;
        				System.exit(0);
        			}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
                 }
	     	}
	    }	    
}

public static class QueryHandler extends Thread
	{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    public QueryHandler()
	    {
	    }

	    @Override
	    public void run()
	     {
	    	String received = null;

	      while (true) 
	        {
	    	  if(flag ==false) {
		    	break;
		    }
	    	System.out.println("Enter the Query.");
	        try {
			received = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(received.equals("What is the most current popular itemID?"))
		{
			Collections.synchronizedMap(timestampMap);
			synchronized(timestampMap) {
                  		Map.Entry<String, Double> maxEntry  = responseMap.entrySet().stream().max(Map.Entry.comparingByValue()).get();
                   		Map.Entry<String, Long> maxEntry1 = timestampMap.entrySet().stream().max(Map.Entry.comparingByValue()).get();
				System.out.println("Current timestamp : "+maxEntry1.getValue()+"\nMost popular item : "+maxEntry.getKey()+"\nDecaying constant of popular item : "+maxEntry.getValue());
				}
		}else{
			System.out.println("Invalid query input");
	         }
	       }
   	    }
	}
}
	




