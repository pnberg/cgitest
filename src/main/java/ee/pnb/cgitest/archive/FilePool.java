package ee.pnb.cgitest.archive;

import ee.pnb.cgitest.CgitestConfiguration;
import ee.pnb.cgitest.CgitestException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilePool {

  @Getter
  private Queue<File> zipFiles;

  public void loadPool() throws CgitestException {
    try (Stream<Path> paths = Files.walk(Paths.get(config.getZipFilePath()))) {
      paths
          .filter(Files::isRegularFile)
          .forEach(filePath -> zipFiles.add(filePath.toFile()));
    }
    catch (IOException e) {
      log.error("Error caught when loading files to pool", e);
      throw new CgitestException("Error when loading files to pool");
    }
  }

  public File getNextZipFile() throws CgitestException {
    if (zipFiles.isEmpty()) {
      throw new CgitestException("No zip files in pool found");
    }
    else {
      return zipFiles.remove();
    }
  }

}
