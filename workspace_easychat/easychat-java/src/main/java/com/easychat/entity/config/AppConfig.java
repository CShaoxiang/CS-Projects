package com.easychat.entity.config;


import com.easychat.utils.StringTools;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);


    // websocket port number
    @Value("5051")
    private Integer wsPort;

    @Value("D:/repo_CS-projects/CS-Projects/workspace_easychat/")
    private String projectFolder;

    @Value("test@qq.com")
    private String adminEmails;

    public String getProjectFolder() {
        if (!StringTools.isEmpty(projectFolder) && !projectFolder.endsWith("/")) {
            projectFolder = projectFolder + "/";

        }
        return projectFolder;
    }
    public String getAdminEmails() {return adminEmails;}
    public Integer getWsPort() {return wsPort;}


}
