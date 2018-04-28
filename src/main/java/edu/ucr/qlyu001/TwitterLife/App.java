package edu.ucr.qlyu001.TwitterLife;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import org.jsoup.Jsoup;

/**
 * Hello world!
 *
 */


class Globals {
	  static int twittercnt;
	  static long MiB = 1024 * 1024 * 10;
	  static long wholefile = 1024 * 1024 * 1024 * 5;
	}

public class App 
{
	
	
	
    public static void main( String[] args )
    {
    	 

    	String path = "/Users/nellylyu/Development/eclipse/TwitterLife/datafile/";	
    	Globals globalv = new Globals();
    	globalv.twittercnt = 0;
    	JSONObject jsob=new JSONObject();
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey("E3hphDRvyFUhX6xVKw7S1Hyeu")
    	  .setOAuthConsumerSecret("0x5FiUpS7HBy3WUalTHb8c67ZFUbNjkNdFA010kSKOJ4Lf3plk")
    	  .setOAuthAccessToken("3254130061-N5DVKqmOKnyxGlrMM3rRMYGO8l7DK4sPgCFaSoP")
    	  .setOAuthAccessTokenSecret("OfuwWDAt3zAkzW5XDnXkeSkhMjXZ6i9B4A5Ec13qiVe49");
    	
    	
    	
    	StatusListener listener = new StatusListener() {
    		
    		
    		
    		
            @Override
            public void onStatus(Status status) {
            	
            	
            	String numberAsString = Integer.toString(globalv.twittercnt);
            	String absolutepath = path  + numberAsString + ".json";
            	
        		File file=new File(absolutepath);
        		long length = file.length();
        		// System.out.println(length);
            
            	
            	
            	if(length > globalv.MiB){
            		
            		try {
            			globalv.twittercnt++;
            			if(globalv.twittercnt > 500){
            				System.exit(0);
            			}
            				
            			numberAsString = Integer.toString(globalv.twittercnt);
                        absolutepath = path  + numberAsString + ".json";
                		file=new File(absolutepath);
    					file.createNewFile();
    					
    					
    					
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}  
            	}
            	
            	
            	//FileWriter fileWriter = new FileWriter(file); 
            	try {
            		
            		/*here I write into the file*/
            		jsob.put("id", status.getId());
                	jsob.put("lang", status.getLang());
                	
                	//text
                	String text=status.getText();        	
                	text=text.replace("\n"," ");
                	
                	jsob.put("text_de",text); 
                	URLEntity[] url=status.getURLEntities();
                	List<String> urlstring=new ArrayList<String>();
                	if(url!=null){
                		for(int k=0;k<url.length;k++){
                			urlstring.add(url[k].getExpandedURL());
                		}
                	}
                	jsob.put("tweet_urls",urlstring);
					//FileWriter fileWriter = new FileWriter(file);
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					
					writer.append("\n");

				    writer.append(jsob.toString());
				     
				    writer.close();
				    
					//FileWriter fileWriter1 = new FileWriter(file);
					//BufferedWriter writer1 = new BufferedWriter(new FileWriter(file, true));

				    //writer1.append("TEST");
				     
				    //writer1.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                
                
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        String s = new String("en");
        double[][] loc = new double[][]{ {-123.40,35.59}, {-66.79,48.25} };
        FilterQuery tweetFilterQuery = new FilterQuery(); // See 
        //tweetFilterQuery.language(new String[]{"en"}); // Note that language does not work properly on Norwegian tweets 
        tweetFilterQuery.language(s); // Note that language does not work properly on Norwegian tweets 
        // SW: 33.7, -118.5		NE: 34.2, -117.8
        
        //tweetFilterQuery.locations(new double[][]{new double[]{33.7,34.2},
        //    new double[]{-117.8,-118.5 }}); 
        
        //tweetFilterQuery.track(s);
        
    
        tweetFilterQuery.locations(loc);
    	// Create the Twitter stream object.
    	TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
    	
        twitterStream.addListener(listener);
        twitterStream.filter(tweetFilterQuery);
        
        //String s = new String("en");
        //twitterStream.sample(s);
    
        
       
        
      
    }
}
