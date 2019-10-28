package com.greatwideweb.homelab.filesync.homelabfilesync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class HomelabFilesyncApplication implements CommandLineRunner {

	private Date d1;

	@Autowired
	FileSyncUtils utils;
	@Value("${app.master.location}")
	private String masterDir;

	@Value("${app.staging.location}")
	private String stageDir;
	private static final Logger logger = LoggerFactory.getLogger(HomelabFilesyncApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HomelabFilesyncApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		FileSyncEnv master = new FileSyncEnv(masterDir);
		FileSyncEnv staging = new FileSyncEnv(stageDir);

		utils.maybeAddStagingDirs(
				master.getChildNames()
					.stream()
					.filter( e -> !staging.getChildNames().contains(e))
					.collect(Collectors.toList()));

		utils.maybeMarkUnknownStagingDirs(
				staging.getChildNames()
					.stream()
					.filter( e -> !master.getChildNames().contains(e))
					.collect(Collectors.toList()));

		List<FileSyncEnvItem> stagingFiles = staging.getContent();
		List<FileSyncEnvItem> dupFiles = utils.matchNames(stagingFiles, master);
		utils.maybeDeleteDupfilesFromStaging(dupFiles);
		stagingFiles = stagingFiles.stream().filter(e -> !dupFiles.contains(e)).collect(Collectors.toList());
		utils.moveFilesToMaster(stagingFiles);
		logger.info("end seconds: " + (new Date().getTime()-d1.getTime())/1000);



	}

	@PostConstruct
	private void onStart() {
		d1 = new Date();

	}
}
