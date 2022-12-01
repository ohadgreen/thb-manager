package com.acme.tweetstermanager.services.twitter;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class TwitterClientInit {
    static final String CONSUMER_KEY = "";
    static final String CONSUMER_SECRET = "";
    static final String TOKEN = "";
    static final String SECRET = "";

    private Client twitterClient;
    private final BlockingQueue queue;
    private final List searchTermList;

    public TwitterClientInit(BlockingQueue queue, List searchTermList) {
        this.queue = queue;
        this.searchTermList = searchTermList;
    }

    public Client clientInit() {
        Authentication oAuth1 = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, SECRET);

        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(this.searchTermList);

        twitterClient = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(oAuth1)
                .processor(new StringDelimitedProcessor(this.queue))
                .build();

        // Establish a connection
        twitterClient.connect();
        log.info("Established Connection to Twitter API");

        return twitterClient;
    }

    public long printTweets(int tweetsCount){
        Instant twitterPullStart = Instant.now();
        for (int i = 0; i < tweetsCount; i++) {
            try {
                log.info(String.valueOf(queue.take()));
            } catch (InterruptedException e) {
                log.error("Twitter pull failure - " + e.getMessage());
                return 0l;
            }
        }

        Instant twitterPullEnd = Instant.now();
        long twitterPullElapsedTime = Duration.between(twitterPullStart, twitterPullEnd).toSeconds();
        return twitterPullElapsedTime;
    }

    public void stopClient() {
        twitterClient.stop();
    }
}
