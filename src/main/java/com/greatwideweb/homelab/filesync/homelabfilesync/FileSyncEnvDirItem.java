package com.greatwideweb.homelab.filesync.homelabfilesync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileSyncEnvDirItem extends FileSyncEnvItem{

    private final List<FileSyncEnvItem> content = new ArrayList<>();

    public FileSyncEnvDirItem(Path p) throws IOException {
        super(p);
        if (Files.isDirectory(p)) {
            Files.list(p)
                    .filter(this::isTargetFile)
                    .forEach(d -> {
                content.add(new FileSyncEnvItem(d));
            });
        }
    }

    private boolean isTargetFile(Path file) {
        if(file.getFileName().toString().endsWith("bif")) { return false; }
        if(file.getFileName().toString().endsWith("jpg")) { return false; }
        return !file.getFileName().toString().endsWith("txt");
    }

    public List<FileSyncEnvItem> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "FileSyncEnvDirItem{" +
                "name='" + this.getName() + '\'' +
                ", pathedName='" + this.getPathedName() + '\'' +
                "content=" + content +
                '}';
    }

    public boolean containsFile(String name) {
        for (FileSyncEnvItem e : content) {
            if (e.getName().equals(name)) { return true; }
        }
        return false;
    }
}
