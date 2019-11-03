package com.greatwideweb.homelab.filesync.homelabfilesync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class FileSyncReport {

    List<String> newFiles = new ArrayList<>();
    List<String> dupFiles;
    private String SEP = "********************************************";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     Instant startTime;
    private LocalDateTime reportDate;

    public void setStartTime(Instant i) {
        this.startTime = i;
    }

    @Value("${app.master.location}")
    private String masterDir;

    public void setMovedFiles(List<FileSyncEnvItem> stagingFiles) {
        if(stagingFiles.isEmpty()) { return; }
        stagingFiles.forEach((e) -> {
            newFiles.add(masterDir + File.separator + e.getParentName() + File.separator + e.getName());
        });
    }

    public void setDupsFilesFound(List<String> dupsWithinEnvironment) {
        dupFiles = dupsWithinEnvironment;
        if(dupFiles == null) { dupFiles = new ArrayList<>(); }
    }


    public void createReport() {
        List list = new LinkedList();
        list.add(SEP);
        list.add("File Sync process " + reportDate.format(formatter));
        if (newFiles.isEmpty()) {
            list.add("no files moved to master");
        } else {
            list.add("Files moved to master");
            list.addAll(newFiles);
        }
        list.add("");
        if(dupFiles.isEmpty()) {
            list.add("no duplicate files found in master");
        } else {
            list.add("Duplicate Files Found in master");
            list.addAll(dupFiles);
        }
        list.add(SEP);
        list.forEach( e -> {
            System.out.println(e);
        });
    }

    public void setReportTime(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }
}
