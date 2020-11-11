package ee.pnb.cgitest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
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
  @Getter private int defaultFileCount;
  @Getter private int defaultThreadCount;

  @Bean
  public ExecutorService threadPoolTaskExecutor() {
    return Executors.newFixedThreadPool(defaultThreadCount);
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  public Path getZipFolder() {
    return Paths.get(zipDirectoryPath);
  }

  public Path getUnzipFolder() {
    return Paths.get(unzipDirectoryPath);
  }

}
