package com.acme.tweetstermanager.controllers;

import com.acme.tweetstermanager.model.WorkerTask;
import com.acme.tweetstermanager.services.KafkaAdminService;
import com.acme.tweetstermanager.services.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class WorkerController {

    private static final String TOPIC = "twitter.job";
    private final WorkerService workerService;
    private final KafkaAdminService adminService;

    @Autowired
    private KafkaTemplate<String, WorkerTask> kafka;

    public WorkerController(WorkerService workerService, KafkaAdminService adminService) {
        this.workerService = workerService;
        this.adminService = adminService;
    }

    @PostMapping(value = "/api/worker")
    public void workerPostHandle(@RequestBody WorkerTask workerTask) {
        System.out.println("controller task: " + workerTask.getUserName());
        workerTask.setId(UUID.randomUUID());
        kafka.send(TOPIC, workerTask);
//        workerService.handleWorkerRequest(workerTask);
    }

    @GetMapping(value = "/api/lag")
    public void getOffsetAndLag() {
//        adminService.getOffset();
        adminService.getLag();
    }


    @GetMapping(value = "/api/test")
    public String controllerTest() {
        return "worker controller V1";
    }
}
