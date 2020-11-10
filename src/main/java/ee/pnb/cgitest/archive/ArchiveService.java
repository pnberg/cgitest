package ee.pnb.cgitest.archive;

import ee.pnb.cgitest.CgitestConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.stream.IntStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {

  private final ZipService zipService;
  private final UnzipService unzipService;
  private final FilePool filePool;
  private final TaskExecutor taskExecutor;
  private final CgitestConfiguration config;

  public void zip(int fileCount) {
    IntStream.range(0, fileCount).forEach(i -> {
      try {
        String filename = "file_" + i;
        ZipOutputStream zipOutputStream = makeZipOutputStream(filename);
        zipService.createArchive(zipOutputStream, filename);
      }
      catch (FileNotFoundException e) {
        log.error("Error {} composing file {}", e.getMessage(), i);
      }
    });
  }

  public void unzip(File zipfile) {
    try {
      ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipfile));
      File unzipFolder = new File(config.getUnzipDirectoryPath());
      unzipService.extract(zipInputStream, unzipFolder);
    }
    catch (FileNotFoundException e) {
      log.error("Error {} extracting file {}", e.getMessage(), zipfile.getName());
    }
  }

  public void unzipAll() {
    for (int i = 0; i < 5; i++) {
      taskExecutor.execute(new UnzipTask(filePool, unzipService, config));
    }
  }

  private ZipOutputStream makeZipOutputStream(String filename) throws FileNotFoundException {
    File file = new File(config.getZipFilePath(), filename + ".zip");
    return new ZipOutputStream(new FileOutputStream(file));
  }

}
