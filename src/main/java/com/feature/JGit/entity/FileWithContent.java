package com.feature.JGit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileWithContent {
    private String filePath;
    private String fileContent;
}
