package com.acme.tweetstermanager.configuration;

import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;

import java.util.List;
import java.util.Map;

public class MonitoringIntegration implements MetricsReporter {
    @Override
    public void init(List<KafkaMetric> metrics) {
        for (KafkaMetric kafkaMetric : metrics) {
            System.out.println(kafkaMetric.metricName());
            System.out.println(kafkaMetric.metricValue());
        }
    }

    @Override
    public void metricChange(KafkaMetric metric) {

    }

    @Override
    public void metricRemoval(KafkaMetric metric) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
