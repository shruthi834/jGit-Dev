package com.feature.JGit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDetails {
    private String id;
    private String userName;
    private String mapName;
    private Date createDate;
}
