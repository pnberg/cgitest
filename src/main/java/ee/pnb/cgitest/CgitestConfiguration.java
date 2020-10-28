package ee.pnb.cgitest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter @Setter
public class CgitestConfiguration {

  private String zipFilePath;

}
