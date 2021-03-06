package ee.pnb.cgitest.archive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

import ee.pnb.cgitest.CgitestConfiguration;
import ee.pnb.cgitest.CgitestException;
import ee.pnb.cgitest.report.ReportService;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;

@ExtendWith(MockitoExtension.class)
class ArchiveServiceTest {

  private static final int DEFAULT_THREAD_COUNT = 5;

  @TempDir Path zipFolder;
  @TempDir Path unzipFolder;

  @Mock private ZipService zipService;
  @Mock private UnzipService unzipService;
  @Mock private FilePool filePool;
  @Mock private ExecutorService taskExecutor;
  @Mock private ReportService reportService;
  @Mock private CgitestConfiguration config;

  @Captor private ArgumentCaptor<Runnable> taskCaptor;
  @Captor private ArgumentCaptor<String> nameCaptor;

  private ArchiveService archiveService;

  @BeforeEach
  void init() {
    zipFolder.resolve("/tmp/zip");
    unzipFolder.resolve("/tmp/unzip");
    archiveService =
        new ArchiveService(zipService, unzipService, filePool, taskExecutor, reportService, config);
  }

  @Test
  @DisplayName("Given file count " +
               "when zip files are created " +
               "then create files with expected names")
  void zip() {
    // given
    givenZipFolder();
    int givenCount = 3;

    // when
    archiveService.zip(givenCount);

    // then
    then(zipService).should(times(givenCount)).createArchive(any(), nameCaptor.capture());

    List<String> actualNames = nameCaptor.getAllValues();
    assertThat(actualNames).containsExactly("file_0", "file_1", "file_2");
  }

  @Test
  @DisplayName("Given folder for zipped files set in configuration, " +
               "and value for thread count set in properties " +
               "when unzipAll is called " +
               "then populate pool with files, call ReportService#startReport, " +
               "execute expected instances of UnzipTask and shutdown taskpool")
  void unzipAll() throws CgitestException {
    // given
    givenZipFolder();
    given(config.getDefaultThreadCount()).willReturn(DEFAULT_THREAD_COUNT);

    // when
    archiveService.unzipAll();

    // then
    InOrder unzipOrder = inOrder(filePool, taskExecutor);
    then(filePool).should(unzipOrder).loadPool(config.getZipFolder());

    then(taskExecutor).should(unzipOrder, times(DEFAULT_THREAD_COUNT))
        .execute(taskCaptor.capture());
    assertThat(taskCaptor.getAllValues())
        .allSatisfy(task -> assertThat(task).isInstanceOf(UnzipTask.class));
    then(taskExecutor).should(unzipOrder).shutdown();
  }

  private void givenZipFolder() {
    given(config.getZipFolder()).willReturn(zipFolder);
  }

}