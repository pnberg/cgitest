package ee.pnb.cgitest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cgitest")
@Getter @Setter
public class CgitestConfiguration {

  private String zipFilePath;
  private String unzipDirectoryPath;

}
