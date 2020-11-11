package ee.pnb.cgitest.archive;

import ee.pnb.cgitest.CgitestConfiguration;
import ee.pnb.cgitest.CgitestException;
import ee.pnb.cgitest.report.ReportService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {

  private final ZipService zipService;
  private final UnzipService unzipService;
  private final FilePool filePool;
  private final ExecutorService taskExecutor;
  private final ReportService reportService;
  private final CgitestConfiguration config;

  public void zip(int fileCount) {
    IntStream.range(0, fileCount).forEach(i -> {
      ZipOutputStream zipOutputStream = null;
      try {
        String filename = "file_" + i;
        zipOutputStream = makeZipOutputStream(filename);
        zipService.createArchive(zipOutputStream, filename);
      }
      catch (Exception e) {
        log.error("Error {} composing file {}", e.getMessage(), i);
      }
      finally {
        try {
          zipOutputStream.close();
        }
        catch (IOException e) {}
      }
    });
  }

  public void unzipAll() throws CgitestException {
    try {
      filePool.loadPool(config.getZipFolder());
      reportService.updatePoolFiles(filePool.getSize());

      for (int i = 0; i < config.getDefaultThreadCount(); i++) {
        taskExecutor.execute(new UnzipTask(filePool, unzipService, reportService, config));
      }
      taskExecutor.awaitTermination(1, TimeUnit.SECONDS);
      taskExecutor.shutdown();

    }
    catch (Exception e) {
      log.error("Exception caught when unzipping files: ", e);
      throw new CgitestException(e.getMessage());
    }
  }

  private ZipOutputStream makeZipOutputStream(String filename) throws IOException {
    File file = new File(config.getZipFolder().toFile(), filename + ".zip");

    return new ZipOutputStream(new FileOutputStream(file, false));
  }

}
