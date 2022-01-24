package com.acme.tweetstermanager.controllers;

import com.acme.tweetstermanager.model.WorkerTask;
import com.acme.tweetstermanager.services.twitter.TwitterClientInit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
public class TwitterPullerController {

    private ExecutorService twitterWorkers = Executors.newFixedThreadPool(5);

    @PostMapping(value = "/api/pull")
    public DeferredResult<String> twitterPuller(@RequestBody WorkerTask workerTask) {
        DeferredResult<String> output = new DeferredResult<>();

        twitterWorkers.execute(() -> {
            try {
                workerTask.setId(UUID.randomUUID());
                System.out.println("puller task: " + workerTask);
                TwitterClientInit twitterClient = new TwitterClientInit(new LinkedBlockingQueue<>(10000), workerTask.getSearchTerms());
                twitterClient.clientInit();
                long twitterPullDuration = twitterClient.printTweets(workerTask.getRequestedNumber());
                twitterClient.stopClient();
                output.setResult(workerTask.getRequestedNumber() + " tweets finished in " + twitterPullDuration + " seconds");
            } catch (Exception e) {
                output.setErrorResult("twitter pull error - " + e.getMessage());
            }
        });

        return output;
    }
}
