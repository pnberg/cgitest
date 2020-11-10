package ee.pnb.cgitest.archive;

import ee.pnb.cgitest.CgitestConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnzipTask implements Runnable {

  private final FilePool pool;
  private final UnzipService unzipService;
  private final CgitestConfiguration config;

  @Override
  public void run() {
    File unzipFolder = config.getUnzipFolder().toFile();

    File zipFile;
    while ((zipFile = pool.getNextFile()) != null) {
      try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
        boolean unzipResult = unzipService.extract(zipInputStream, unzipFolder);
        if (unzipResult) {
          log.info("File {} unzipped successfully", zipFile.getName());
        }
      }
      catch (Exception e) {
        log.error("Error when unzipping file : ", e);
      }
    }

  }
}
