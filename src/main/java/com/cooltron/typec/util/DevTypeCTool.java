package com.cooltron.typec.util;


import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class DevTypeCTool {

    private Long tId;
    private Integer tType;
    private String tDescribe;
    private String tVersion;
    private LocalDateTime tCreateTime;
    private String tUrl;

}
