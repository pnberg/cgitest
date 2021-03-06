package ee.pnb.cgitest.archive;

import ee.pnb.cgitest.CgitestException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilePool {

  @Setter @Getter // visible for testing
  private Queue<File> files;

  public void loadPool(Path zipFileFolder) throws CgitestException {
    files = new LinkedList<>();
    try (Stream<Path> paths = Files.walk(zipFileFolder)) {
      paths
          .filter(Files::isRegularFile)
          .forEach(filePath -> files.add(filePath.toFile()));
    }
    catch (IOException e) {
      log.error("Error caught when loading files to pool", e);
      throw new CgitestException("Error when loading files to pool");
    }
  }

  public File getNextFile() {
    if (files.isEmpty()) {
      return null;
    }
    else {
      return files.remove();
    }
  }

  public int getSize() {
    return files.size();
  }

}
