package com.acme.tweetstermanager.services;

import com.acme.tweetstermanager.model.WorkerTask;

public interface WorkerService {
    void handleWorkerRequest(WorkerTask workerTask);
}
