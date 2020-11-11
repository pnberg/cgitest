package ee.pnb.cgitest.report;

import ee.pnb.cgitest.CgitestException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final Clock clock;

  @Getter private UnzipReport report = null;
  private LocalDateTime reportStart;

  public void startReport() throws CgitestException {
    if (report != null) {
      throw new CgitestException("Report already exists");
    }
    report = new UnzipReport();
    reportStart = LocalDateTime.now(clock);
  }

  public void updatePoolFiles(int poolFiles) {
    report.setPoolFiles(poolFiles);
  }

  public void addUnzippedFile(long filesize) {
    report.addUnzippedFile(filesize);
  }

  public UnzipReport finishReport() {
    report.setUnzipTimeInSeconds(ChronoUnit.SECONDS.between(reportStart, LocalDateTime.now(clock)));
    UnzipReport result = report;
    this.report = null;
    this.reportStart = null;
    return result;
  }

}
