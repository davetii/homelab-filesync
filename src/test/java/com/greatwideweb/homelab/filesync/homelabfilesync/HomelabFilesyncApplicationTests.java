package com.greatwideweb.homelab.filesync.homelabfilesync;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class HomelabFilesyncApplicationTests {

	@Test
	void contextLoads2() {
		Instant start = Instant.now();

		//Measure execution time for this method
		methodToTime();

		Instant finish = Instant.now();

		long timeElapsed = Duration.between(start, finish).toMillis();  //in millis
		System.out.println(timeElapsed/ 1000);
		System.out.println(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
	}

	private static void methodToTime() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
