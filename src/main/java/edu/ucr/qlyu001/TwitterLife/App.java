package edu.ucr.qlyu001.TwitterLife;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Hello world!
 *
 */


class Globals {
	  static int twittercnt;
	  static long MiB = 1024 * 1024 * 10;
	  static long wholefile = 1024 * 1024 * 1024 * 5;
	  int numDocs;
	  int numTweets;
	  String path;
	  boolean tweetCount;
	  int currentTweet;
	}
class URLTweetDealer extends Thread {
	Status statuslocal;
	Globals globalvlocal;
	public URLTweetDealer(Status status, Globals globalv)
	{
		statuslocal = status;
		globalvlocal = globalv;
	}
	
	@Override
	public void run() {
		//System.out.println("MAKING NEW THREAD: count " + globalvlocal.twittercnt);
		JSONObject jsob=new JSONObject();
    	
    	
    	
    	String numberAsString = Integer.toString(globalvlocal.twittercnt);
    	String absolutepath = globalvlocal.path  + numberAsString + ".json";
    	
    	//status.getUser();
    	
		File file=new File(absolutepath);
		long length = file.length();
		// System.out.println(length);
    
    	globalvlocal.currentTweet++;
    	
        // ~5k tweets for 1mb, ~50k tweets for 10mb
    	if (globalvlocal.numTweets != 0)
    	{
    		if (globalvlocal.currentTweet > globalvlocal.numTweets)
    			System.exit(0);
    	}
    	else if(length > globalvlocal.MiB){
    		
    		try {
    			globalvlocal.twittercnt++;
    			if(globalvlocal.twittercnt > 500){
    				System.exit(0);
    			}
    				
    			numberAsString = Integer.toString(globalvlocal.twittercnt);
                absolutepath = globalvlocal.path  + numberAsString + ".json";
        		file=new File(absolutepath);
				file.createNewFile();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
    	}
    	
    	
    	//FileWriter fileWriter = new FileWriter(file); 
    	try {
    		String title = "";
    		
    		/*here I write into the file*/
    		jsob.put("id", statuslocal.getId());
        	jsob.put("lang", statuslocal.getLang());
        	
        	//text
        	String text=statuslocal.getText();        	
        	text=text.replace("\n"," ");
        	
        	jsob.put("text",text); 
        	
        	
        	
        	jsob.put("retweet count", statuslocal.getRetweetCount()); // int
        	//jsob.put("place type",  status.getPlaceType());
        	jsob.put("source", statuslocal.getSource());
        	try {
        		jsob.put("place", statuslocal.getPlace().getName());
        	}
        	catch (NullPointerException e){
        		jsob.put("place",  "");
        	}
        	
        	jsob.put("fav count", statuslocal.getFavoriteCount());
        	jsob.put("retweet?", statuslocal.isRetweet());
        	jsob.put("truncated?", statuslocal.isTruncated());
        	jsob.put("Date", statuslocal.getCreatedAt().toString());
        	
        	
        	try {
            	GeoLocation g = statuslocal.getGeoLocation();
            	//System.out.println(g.toString());

            	jsob.put("latitude",  g.getLatitude());
            	jsob.put("longitude", g.getLongitude());
            	
        	}
        	catch (NullPointerException e) {
        		//System.out.println("Null pointer exception caught! :D");
        		jsob.put("latitude",  "");
            	jsob.put("longitude", "");
        	}
        	finally {
        		
        	}
			
        	
        	
        	List<String> hashtaglist=new ArrayList<String>();
        	
        	HashtagEntity[] hashtagsEntities = statuslocal.getHashtagEntities();
            for (HashtagEntity hashtag : hashtagsEntities){
            	hashtaglist.add(hashtag.getText());
                //System.out.println("HASHTAG:" + hashtag.getText());
            }
        	
        	jsob.put("Hashtags", hashtaglist);
        	
        	 // Search for URLs
        	//text = "check me out here! https://apps.twitter.com/app/15187951/keys #blessed";
        	
        	URLEntity[] url=statuslocal.getURLEntities();
        	List<String> urlstring=new ArrayList<String>();
        	List<String> urltitle =new ArrayList<String>();
        	List<String> urltitle2 =new ArrayList<String>();
        	
            
            if(text.contains("http")){
            	while (text.contains("http")) {
                    int indexOfHttp = text.indexOf("http");
                    int endPoint = (text.indexOf(' ', indexOfHttp) != -1) ? text.indexOf(' ', indexOfHttp) : text.length();
                    String urls = text.substring(indexOfHttp, endPoint);
                    //String targetUrlHtml=  "<a href='${url}' target='_blank'>${url}</a>";
                    //text = text.replace(urls,targetUrlHtml );
                    
                    if(!urls.contains("…")){
                    	// System.out.println(urls);
                    	 urlstring.add(urls);
                    	 
                    	 //jsob.put("tweet_urls", urls);
                    } 
                    text = text.substring(endPoint, text.length());
    
                }
                jsob.put("tweet_urls", urlstring);
            }else{
            	jsob.put("tweet_urls", "");
            }
            
            String jesus = "";
            
            
            for (String s : urlstring)
			{
    			Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36").get();
    			//Document doc = Jsoup.connect(s).get();
                jesus = doc.title();  
                //System.out.println("title is: " + jesus); 
                urltitle.add(jesus);
			}
    		//jsob.put("url_title", urltitle);
    		
    		for (String s : urltitle)
			{
    			Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36").get();
    			//Document doc = Jsoup.connect(s).get();
                jesus = doc.title();  
                //System.out.println("title is: " + jesus); 
                urltitle2.add(jesus);
			}
    		jsob.put("url_title", urltitle2);
         
        
        	/*
        	if(url!=null){
        		for(int k=0;k<url.length;k++){
        			urlstring.add(url[k].getExpandedURL());
        		}
        		jsob.put("tweet_urls",urlstring);
        		String jesus = "";
        		for (String s : urlstring)
    			{
        			Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36").get();
        			//Document doc = Jsoup.connect(s).get();
                    jesus = doc.title();  
                    //System.out.println("title is: " + jesus); 
                    urltitle.add(jesus);
    			}
        		jsob.put("url_title", urltitle);
        	}*/
        	
        	
			//FileWriter fileWriter = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			
			writer.append("\n");

		    writer.append(jsob.toString());
		     
		    writer.close();
		    
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("caught possible socket timeout exception?? :D");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
    	
		
	}
}
public class App 
{
	/*
	class URLTweetDealer implements Thread {
		@Overide
		public void run() {
			
		}
	}*/
    // ~5k tweets for 1mb, ~50k tweets for 10mb
	// arg1 = number of tweets to record
	// arg2 = path to write .json file
    public static void main( String[] args )
    {
    	Globals globalv = new Globals();
    	
    	globalv.path = "";
    	globalv.numTweets = 0;
    	globalv.currentTweet = 0;
    	
        try {
        	
            String tweets = args[0];
            globalv.numTweets = Integer.parseInt(tweets);
            //globalv.numTweets = 100;
            globalv.path = args[1];
            //String numDocs = args[1];
            
            
            

        }
        catch (ArrayIndexOutOfBoundsException e){
            //System.out.println("ArrayIndexOutOfBoundsException caught");
        }
        catch (NumberFormatException e){
        	//System.out.println("NumberFormatException caught :D");
        }
        finally {

        }	 


        
        if (globalv.path == "")        
        {
        	globalv.path = "/Users/nellylyu/Development/eclipse/TwitterLife/datafile/";	
        	//String path = "/Users/nellylyu/Development/eclipse/TwitterLife/datafile/";
        }
    	
    	globalv.twittercnt = 0;
    	JSONObject jsob=new JSONObject();
    	ConfigurationBuilder cb = new ConfigurationBuilder();/*
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey("E3hphDRvyFUhX6xVKw7S1Hyeu")
    	  .setOAuthConsumerSecret("0x5FiUpS7HBy3WUalTHb8c67ZFUbNjkNdFA010kSKOJ4Lf3plk")
    	  .setOAuthAccessToken("3254130061-N5DVKqmOKnyxGlrMM3rRMYGO8l7DK4sPgCFaSoP")
    	  .setOAuthAccessTokenSecret("OfuwWDAt3zAkzW5XDnXkeSkhMjXZ6i9B4A5Ec13qiVe49");*/
    	
    	cb.setDebugEnabled(true)
  	  		.setOAuthConsumerKey("GXppoUPTPmOjV0et6L3EpeEFR")
  	  		.setOAuthConsumerSecret("DpocsHswF3px1LVUD5uhW4UQV7gyg6p5BLQg97K3D9Clcty0mb")
  	  		.setOAuthAccessToken("23901618-7Zb9lnFtcP9GZvxWGI7nL7dbDsitk9eaPKAck0Qdm")
  	  		.setOAuthAccessTokenSecret("dtdRrdx00sNMad4Q86BEkg9pyfS9xJBaaLCWBKW9TPYeG");
    	
    	
    	
    	
    	StatusListener listener = new StatusListener() {
    		
    		
    		
    		
            @Override
            public void onStatus(Status status) {
            	//if(text.contains("http"))
            	String text1=status.getText();        	
            	text1=text1.replace("\n"," ");
            	
            	//if (status.getURLEntities().length > 1) {
            	if (text1.contains("http")) {
            		Thread newThread = new URLTweetDealer(status, globalv);
            		newThread.start();
            	}
            	
            	else { // ADDED
            	
            	String numberAsString = Integer.toString(globalv.twittercnt);
            	String absolutepath = globalv.path  + numberAsString + ".json";
            	
            	//status.getUser();
            	
        		File file=new File(absolutepath);
        		long length = file.length();
        		// System.out.println(length);
            
            	globalv.currentTweet++;
            	
                // ~5k tweets for 1mb, ~50k tweets for 10mb
            	if (globalv.numTweets != 0)
            	{
            		if (globalv.currentTweet > globalv.numTweets)
            			System.exit(0);
            	}
            	else if(length > globalv.MiB){
            		
            		try {
            			globalv.twittercnt++;
            			if(globalv.twittercnt > 500){
            				System.exit(0);
            			}
            				
            			numberAsString = Integer.toString(globalv.twittercnt);
                        absolutepath = globalv.path  + numberAsString + ".json";
                		file=new File(absolutepath);
    					file.createNewFile();
    					
    					
    					
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}  
            	}
            	
            	
            	//FileWriter fileWriter = new FileWriter(file); 
            	try {
            		String title = "";
            		
            		/*here I write into the file*/
            		jsob.put("id", status.getId());
                	jsob.put("lang", status.getLang());
                	
                	//text
                	String text=status.getText();        	
                	text=text.replace("\n"," ");
                	
                	jsob.put("text",text); 
                	
                	
                	
                	jsob.put("retweet count", status.getRetweetCount()); // int
                	//jsob.put("place type",  status.getPlaceType());
                	jsob.put("source", status.getSource());
                	try {
                		jsob.put("place", status.getPlace().getName());
                	}
                	catch (NullPointerException e){
                		jsob.put("place",  "");
                	}
                	
                	jsob.put("fav count", status.getFavoriteCount());
                	jsob.put("retweet?", status.isRetweet());
                	jsob.put("truncated?", status.isTruncated());
                	jsob.put("Date", status.getCreatedAt().toString());
                	
                	try {
                    	GeoLocation g = status.getGeoLocation();
                    	//System.out.println(g.toString());
        
                    	jsob.put("latitude",  g.getLatitude());
                    	jsob.put("longitude", g.getLongitude());
                    	
                	}
                	catch (NullPointerException e) {
                		//System.out.println("Null pointer exception caught! :D");
                		jsob.put("latitude",  "");
                    	jsob.put("longitude", "");
                	}
                	finally {
                		
                	}
					
                	
                	
                	List<String> hashtaglist=new ArrayList<String>();
                	
                	HashtagEntity[] hashtagsEntities = status.getHashtagEntities();
                    for (HashtagEntity hashtag : hashtagsEntities){
                    	hashtaglist.add(hashtag.getText());
                        //System.out.println("HASHTAG:" + hashtag.getText());
                    }
                	
                	jsob.put("Hashtags", hashtaglist);
                	
                	 // Search for URLs
                	//text = "check me out here! https://apps.twitter.com/app/15187951/keys #blessed";
                	
                	URLEntity[] url=status.getURLEntities();
                	List<String> urlstring=new ArrayList<String>();
                	List<String> urltitle =new ArrayList<String>();
                	List<String> urltitle2 =new ArrayList<String>();
                	/*
                	while(text.contains("http")){
                		int indexOfHttp = text.indexOf("http");
                        int endPoint = (text.indexOf(' ', indexOfHttp) != -1) ? text.indexOf(' ', indexOfHttp) : text.length();
                        String urls = text.substring(indexOfHttp, endPoint);
                        String targetUrlHtml=  "<a href='${url}' target='_blank'>${url}</a>";
                        text = text.replace(urls,targetUrlHtml );
                	}*/
                	/*
                    if (text.contains("http")) {
                        int indexOfHttp = text.indexOf("http");
                        int endPoint = (text.indexOf(' ', indexOfHttp) != -1) ? text.indexOf(' ', indexOfHttp) : text.length();
                        String urls = text.substring(indexOfHttp, endPoint);
                        String targetUrlHtml=  "<a href='${url}' target='_blank'>${url}</a>";
                        text = text.replace(urls,targetUrlHtml );
                        if(!urls.contains("…")){
                        	 System.out.println(urls);
                        	 jsob.put("tweet_urls", urls);
                        }             
        
                    }else{
                    	jsob.put("tweet_urls", "");
                    }*/
                    
                    if(text.contains("http")){
                    	while (text.contains("http")) {
                            int indexOfHttp = text.indexOf("http");
                            int endPoint = (text.indexOf(' ', indexOfHttp) != -1) ? text.indexOf(' ', indexOfHttp) : text.length();
                            String urls = text.substring(indexOfHttp, endPoint);
                            //String targetUrlHtml=  "<a href='${url}' target='_blank'>${url}</a>";
                            //text = text.replace(urls,targetUrlHtml );
                            
                            if(!urls.contains("…")){
                            	// System.out.println(urls);
                            	 urlstring.add(urls);
                            	 
                            	 //jsob.put("tweet_urls", urls);
                            } 
                            text = text.substring(endPoint, text.length());
            
                        }
                        jsob.put("tweet_urls", urlstring);
                    }else{
                    	jsob.put("tweet_urls", "");
                    }
                    
                    String jesus = "";
                    
                    
                    for (String s : urlstring)
        			{
            			Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36").get();
            			//Document doc = Jsoup.connect(s).get();
                        jesus = doc.title();  
                        //System.out.println("title is: " + jesus); 
                        urltitle.add(jesus);
        			}
            		//jsob.put("url_title", urltitle);
            		
            		for (String s : urltitle)
        			{
            			Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36").get();
            			//Document doc = Jsoup.connect(s).get();
                        jesus = doc.title();  
                        //System.out.println("title is: " + jesus); 
                        urltitle2.add(jesus);
        			}
            		jsob.put("url_title", urltitle2);
                 
                
                	/*
                	if(url!=null){
                		for(int k=0;k<url.length;k++){
                			urlstring.add(url[k].getExpandedURL());
                		}
                		jsob.put("tweet_urls",urlstring);
                		String jesus = "";
                		for (String s : urlstring)
            			{
                			Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36").get();
                			//Document doc = Jsoup.connect(s).get();
                            jesus = doc.title();  
                            //System.out.println("title is: " + jesus); 
                            urltitle.add(jesus);
            			}
                		jsob.put("url_title", urltitle);
                	}*/
                	
                	
					//FileWriter fileWriter = new FileWriter(file);
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					
					writer.append("\n");

				    writer.append(jsob.toString());
				     
				    writer.close();
				    
					

				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//System.out.println("caught possible socket timeout exception?? :D");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            	} // ADDED
                
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                //System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        String d = new String("en");
        String s = "en";
        double[][] loc = new double[][]{ {-123.40,35.59}, {-66.79,48.25} };
        double[][] testloc = new double[][]{  {-117.659, 35.039}, {-68.441, 43.969} };
        //FilterQuery tweetFilterQuery = new FilterQuery(); // See 
        //tweetFilterQuery.language(new String[]{"en"}); // Note that language does not work properly on Norwegian tweets 
        //tweetFilterQuery.language(s); // Note that language does not work properly on Norwegian tweets 
        // SW: 33.7, -118.5		NE: 34.2, -117.8
        
        //tweetFilterQuery.locations(new double[][]{new double[]{33.7,34.2},
        //    new double[]{-117.8,-118.5 }}); 
        
        //tweetFilterQuery.count(0);
        
    
        //tweetFilterQuery.locations(testloc);
    	// Create the Twitter stream object.
    	TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
    	
        twitterStream.addListener(listener);
        //twitterStream.filter(tweetFilterQuery);
        
        //String s = new String("en");
        twitterStream.sample(s);
    
        
       
        
      
    }
}
