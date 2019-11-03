package com.greatwideweb.homelab.filesync.homelabfilesync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileSyncEnv {
    private final List<FileSyncEnvDirItem> children = new ArrayList<>();
    private final List<String> childNames =  new ArrayList<>();

    public FileSyncEnv(String environment) throws IOException {
         Path p = Paths.get(environment);
        if (!Files.isDirectory(p)) { return; }
        Files.list(p).forEach(d -> {
            try {
                children.add(new FileSyncEnvDirItem(d));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        children.forEach(e -> childNames.add(e.getName()));
    }

    public List<String> getChildNames() {
        return childNames;
    }

    public List<FileSyncEnvItem> getContent() {
        List<FileSyncEnvItem> result = new ArrayList<>();
        children.forEach(e -> result.addAll(e.getContent()));
        return result;
    }

    public boolean containsFile(FileSyncEnvItem e) {
        for (FileSyncEnvDirItem item : children) {
            if (item.containsFile(e.getName())) { return true; }
        }
        return false;
    }

    public List<String> findDupsWithinEnvironment() {
        List<String> result = new ArrayList<>();
        List<FileSyncEnvItem> allItems = new ArrayList<>();
        children.forEach((e) -> { allItems.addAll(e.getContent()); });
        Set<String> names = new HashSet<>();
        Set<String> dups = new HashSet<>();
        allItems.forEach((e) -> {
            if(!names.add(e.getName())) { dups.add(e.getName()); }
        });

        dups.forEach((e) -> {
            allItems.forEach((ai) -> {
                if(e.equals(ai.getName())) {
                    result.add(ai.getPathedName());
                }
            });
        });
        return result;
    }


}
