package com.greatwideweb.homelab.filesync.homelabfilesync;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileSyncUtils {

    @Value("${app.master.location}")
    private String masterDir;

    @Value("${app.staging.location}")
    private String stageDir;

    @Value("${app.staging.unknownmarker.filename}")
    private String unknownMarkerFile;

    @Value("${app.staging.unknownmarker.content}")
    private String unknownMarkerContent;

    public FileSyncUtils() { }

    public void maybeAddStagingDirs(List<String> list) {
        System.out.println("maybeAddStagingDirs");
        System.out.println(list);
        if(list.isEmpty()) { return; }
        list.parallelStream().forEach((f) -> {
            try {
                maybeCreateStagingDir(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void maybeCreateStagingDir(String f) throws IOException {
        Path p = getPathToStaging(f);
        if(!Files.exists(p)) { Files.createDirectory(p); }
    }

    private Path getPathToStaging(String f) {
        return Paths.get(stageDir + File.separator + f);
    }

    private Path getPathToUnknownMarker(String f) {
        return Paths.get(stageDir + File.separator + f + File.separator + unknownMarkerFile);
    }

    public void maybeMarkUnknownStagingDirs(List<String> list) {
        if(list.isEmpty()) { return; }
        list.parallelStream().forEach((f) -> maybeCreateUnknownMarker(f));
    }

    private void maybeCreateUnknownMarker(String f) {
        Path p = getPathToStaging(f);
        if(Files.exists(p)) {
            p = getPathToUnknownMarker(f);
            if(!Files.exists(p)) {
                try {
                    Files.write(p, unknownMarkerContent.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void maybeDeleteDupfilesFromStaging(List<FileSyncEnvItem> list) {
        if(list.isEmpty()) { return; }
        list.forEach((f) -> {
            try {
                Files.delete(Paths.get(f.getPathedName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public List<FileSyncEnvItem> matchNames(List<FileSyncEnvItem> list, FileSyncEnv env) {
        List<FileSyncEnvItem> result = new ArrayList<>();
        if(list.isEmpty()) { return result;}
        list.forEach(e -> {
            if (env.containsFile(e)) {
                result.add(e);
            }
        });
        return result;

    }

    public void moveFilesToMaster(List<FileSyncEnvItem> list) {
        if(list.isEmpty()) { return; }

        list.parallelStream().forEach( e -> {
            File src = new File(e.getPathedName());
            File destDir =  new File(masterDir + File.separator + e.getParentName());
            try {
                FileUtils.moveFileToDirectory(src,destDir, true );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
