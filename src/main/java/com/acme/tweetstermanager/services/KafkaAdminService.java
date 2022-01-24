package com.acme.tweetstermanager.services;

import com.acme.tweetstermanager.model.WorkerTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KafkaAdminService {

    private final String WORKER_TOPIC = "twitter.job";

    @Autowired
    private AdminClient adminClient;
    @Autowired
    private KafkaConsumer<String, WorkerTask> kafkaConsumer;

    public void getOffset() {
        ListConsumerGroupOffsetsResult info = adminClient.listConsumerGroupOffsets("worker-group");
        DescribeConsumerGroupsResult describeConsumerGroupsResult = adminClient.describeConsumerGroups(Collections.singleton("worker-group"));
        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = null;
        try {
            topicPartitionOffsetAndMetadataMap = info.partitionsToOffsetAndMetadata().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : topicPartitionOffsetAndMetadataMap.entrySet()) {
            TopicPartition topicPartition = entry.getKey();
            int partition = topicPartition.partition();

            OffsetAndMetadata offsetAndMetadata = entry.getValue();
            long offset = offsetAndMetadata.offset();

            log.info("@@@ partition: " + partition + " offset: " + offset);
        }
    }

    public void getLag() {
        kafkaConsumer.poll(Duration.ofMillis(1));
        List<PartitionInfo> partitions = kafkaConsumer.partitionsFor(WORKER_TOPIC);

        Set<TopicPartition> topicPartitionSet = partitions.stream().map(p -> new TopicPartition(WORKER_TOPIC, p.partition())).collect(Collectors.toSet());

//        kafkaConsumer.assign(topicPartitionSet);
//        Map<TopicPartition, OffsetAndMetadata> committed = kafkaConsumer.committed(topicPartitionSet);
//        kafkaConsumer.assignment();
//        Map<TopicPartition, Long> endOffsets = kafkaConsumer.endOffsets(topicPartitionSet);
//
//        for (TopicPartition tp : topicPartitionSet) {
//            log.info("### part: " + tp.partition() + " end offset: " + endOffsets.get(tp) + " current offset: " + kafkaConsumer.position(tp));
//        }

        Set<TopicPartition> partitionSet = kafkaConsumer.assignment();
        Map<TopicPartition, Long> beginningOffsets = kafkaConsumer.beginningOffsets(partitionSet);
        Map<TopicPartition, Long> endOffsets = kafkaConsumer.endOffsets(partitionSet);
        Map<TopicPartition, OffsetAndMetadata> committed = kafkaConsumer.committed(topicPartitionSet);

        for(TopicPartition tp : partitionSet) {
            log.info("Partition: {}, BeginOffset: {}, EndOffset: {}, CurrentPosition: {}",
                    tp.partition(),
                    beginningOffsets.get(tp),
                    endOffsets.get(tp),
                    kafkaConsumer.position(tp));
        }
    }
}
