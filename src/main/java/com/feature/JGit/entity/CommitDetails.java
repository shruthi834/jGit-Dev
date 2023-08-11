package com.feature.JGit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitDetails {

    private String commitId;
    private String author;
    private String email;
    private String date;
    private String message;
}
