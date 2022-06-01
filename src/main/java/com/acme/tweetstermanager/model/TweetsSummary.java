package model.tweet;

import model.TwitterPullTask;

import java.util.Map;

public class TweetsSummary {
    private TwitterPullTask pullTask;
    private Map<String, Integer> wordsCountMap;
    private Integer tweetsCount;
    private boolean isLastBatch;

    public TweetsSummary() {
    }

    public TweetsSummary(TwitterPullTask pullTask, Map<String, Integer> wordsCountMap, Integer tweetsCount, boolean isLastBatch) {
        this.pullTask = pullTask;
        this.wordsCountMap = wordsCountMap;
        this.tweetsCount = tweetsCount;
        this.isLastBatch = isLastBatch;
    }

    public TwitterPullTask getPullTask() {
        return pullTask;
    }

    public void setPullTask(TwitterPullTask pullTask) {
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
