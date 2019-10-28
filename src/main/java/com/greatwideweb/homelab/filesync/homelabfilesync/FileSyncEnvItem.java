package com.greatwideweb.homelab.filesync.homelabfilesync;

import java.nio.file.Path;

public class FileSyncEnvItem {
    private final String parentName;
    private final String name;
    private final String pathedName;


    public FileSyncEnvItem(Path p) {
        pathedName = p.toString();
        name = p.getFileName().toString();
        parentName = p.getParent().getFileName().toString();
    }

    public String getName() {
        return name;
    }

    public String getPathedName() {
        return pathedName;
    }

    public String getParentName() {
        return parentName;
    }

    @Override
    public String toString() {
        return "FileSyncEnvItem{" +
                "name='" + name + '\'' +
                "parentName=" + parentName + '\'' +
                ", pathedName='" + pathedName + '\'' +
                '}';
    }
}
