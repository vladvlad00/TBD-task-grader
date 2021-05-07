package ro.uaic.info.taskgrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  /// @EnableEurekaClient
public class TaskGraderApplication
{

	public static void main(String[] args) {
		SpringApplication.run(TaskGraderApplication.class, args);
	}

}
