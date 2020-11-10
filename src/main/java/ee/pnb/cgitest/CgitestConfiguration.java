package ee.pnb.cgitest;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ConfigurationProperties(prefix = "cgitest")
@Setter
public class CgitestConfiguration {

  private String zipDirectoryPath;
  private String unzipDirectoryPath;

  @Bean
  public TaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(5);
    executor.setThreadNamePrefix("default_task_executor_thread");
    executor.initialize();

    return executor;
  }

  public Path getZipFolder() {
    return Paths.get(zipDirectoryPath);
  }

  public Path getUnzipFolder() {
    return Paths.get(unzipDirectoryPath);
  }

}
