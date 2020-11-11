package ee.pnb.cgitest.report;

import lombok.Data;

@Data
public class UnzipReport {

  private int poolFiles;
  private int unzippedFiles;
  private long unzipTimeInSeconds;
  private long unzippedFileSizeInBytes;

  public void addUnzippedFile(long filesize) {
    unzippedFiles++;
    unzippedFileSizeInBytes += filesize;
  }

}
