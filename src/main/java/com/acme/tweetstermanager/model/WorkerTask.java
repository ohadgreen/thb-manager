package com.acme.tweetstermanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkerTask {
    private UUID id;
    private String userName;
    private List<String> searchTerms;
    private Integer requestedNumber;
}
