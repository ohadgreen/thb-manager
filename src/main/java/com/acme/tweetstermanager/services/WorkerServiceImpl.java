package com.acme.tweetstermanager.services;

import com.acme.tweetstermanager.model.WorkerTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j

@Service
public class WorkerServiceImpl implements WorkerService{

    private static final Integer BATCH_SIZE = 100;
    private static final String TOPIC = "twitter.job";
    @Autowired
    private KafkaTemplate<String, WorkerTask> kafka;

    @Override
    public void handleWorkerRequest(WorkerTask workerTask) {
        if (workerTask.getRequestedNumber() > BATCH_SIZE) {
            int batchesNumber = workerTask.getRequestedNumber() / BATCH_SIZE;
            for (int i = 0; i < batchesNumber; i++) {
                UUID uuid = UUID.randomUUID();
                WorkerTask task = WorkerTask.builder()
                        .id(uuid)
                        .userName(workerTask.getUserName())
                        .requestedNumber(BATCH_SIZE)
                        .searchTerms(workerTask.getSearchTerms())
                        .build();
                kafka.send(TOPIC, task);
                log.info("new task id: " + uuid);
            }
        }
//        showMetrics();
    }

    private void showMetrics() {
        Map<MetricName, ? extends Metric> metrics = kafka.metrics();

        for (Map.Entry<MetricName, ? extends Metric> entry : metrics.entrySet()) {
//            if (entry.getKey().name().contains("compression") ||
//                    entry.getKey().name().contains("batch") ||
//                    entry.getKey().name().contains("incoming-byte") ||
//                    entry.getKey().name().equals("count")
//            )
                log.info("### kafmet: " + entry.getKey().name() + " : " + entry.getValue().metricValue());
        }
    }
}
