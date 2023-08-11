package com.feature.JGit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifiedList {
    private FileWithContent files;
    private String author;
    private String commitMessage;
}
