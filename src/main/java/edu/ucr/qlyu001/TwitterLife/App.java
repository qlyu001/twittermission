package edu.ucr.qlyu001.TwitterLife;
import java.util.List;
import java.util.stream.Collectors;

import twitter4j.*;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
	public static List<String> searchtweets() throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();;
	        Query query = new Query("source:twitter4j baeldung");
	        QueryResult result = twitter.search(query);
	        List<Status> statuses = result.getTweets();
	        return statuses.stream().map(
				item -> item.getText()).collect(
						Collectors.toList());
	}

	public static void streamFeed() {
		 
	    StatusListener listener = new StatusListener() {
	 
	        @Override
	        public void onException(Exception e) {
	            e.printStackTrace();
	        }
	        @Override
	        public void onDeletionNotice(StatusDeletionNotice arg) {
	        }
	        @Override
	        public void onScrubGeo(long userId, long upToStatusId) {
	        }
	        @Override
	        public void onStallWarning(StallWarning warning) {
	        }
	        @Override
	        public void onStatus(Status status) {
	        }
	        @Override
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	        }
	    };
	 
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	 
	    twitterStream.addListener(listener);
	 
	    twitterStream.sample();
	    
	} 
	
    public static void main( String[] args )
    {
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey("E3hphDRvyFUhX6xVKw7S1Hyeu")
    	  .setOAuthConsumerSecret("0x5FiUpS7HBy3WUalTHb8c67ZFUbNjkNdFA010kSKOJ4Lf3plk")
    	  .setOAuthAccessToken("3254130061-N5DVKqmOKnyxGlrMM3rRMYGO8l7DK4sPgCFaSoP")
    	  .setOAuthAccessTokenSecret("OfuwWDAt3zAkzW5XDnXkeSkhMjXZ6i9B4A5Ec13qiVe49");
    	TwitterFactory tf = new TwitterFactory(cb.build());
    	Twitter twitter = tf.getInstance();
    	
    
    }
}
