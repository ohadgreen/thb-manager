package com.acme.tweetstermanager.model;


import com.acme.tweetstermanager.model.WorkerTask;

import java.util.Map;

public class TweetsSummary {
    private WorkerTask pullTask;
    private Map<String, Integer> wordsCountMap;
    private Integer tweetsCount;
    private boolean isLastBatch;

    public TweetsSummary() {
    }

    public TweetsSummary(WorkerTask pullTask, Map<String, Integer> wordsCountMap, Integer tweetsCount, boolean isLastBatch) {
        this.pullTask = pullTask;
        this.wordsCountMap = wordsCountMap;
        this.tweetsCount = tweetsCount;
        this.isLastBatch = isLastBatch;
    }

    public WorkerTask getPullTask() {
        return pullTask;
    }

    public void setPullTask(WorkerTask pullTask) {
        this.pullTask = pullTask;
    }

    public Map<String, Integer> getWordsCountMap() {
        return wordsCountMap;
    }

    public void setWordsCountMap(Map<String, Integer> wordsCountMap) {
        this.wordsCountMap = wordsCountMap;
    }

    public Integer getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(Integer tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public boolean isLastBatch() {
        return isLastBatch;
    }

    public void setLastBatch(boolean lastBatch) {
        isLastBatch = lastBatch;
    }
}
