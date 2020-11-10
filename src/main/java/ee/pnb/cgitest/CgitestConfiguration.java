package ee.pnb.cgitest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ConfigurationProperties(prefix = "cgitest")
@Getter @Setter
public class CgitestConfiguration {

  private String zipFilePath;
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

}
