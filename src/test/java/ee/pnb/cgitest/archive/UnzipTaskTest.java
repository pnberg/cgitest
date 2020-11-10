package ee.pnb.cgitest.archive;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import ee.pnb.cgitest.CgitestConfiguration;
import java.io.File;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnzipTaskTest {

  private static final String UNZIP_FOLDER = "/tmp/unzip";
  private static final String FILE_1 = "file_1.zip";
  private static final String FILE_2 = "file_2.zip";

  @Mock private FilePool pool;
  @Mock private UnzipService unzipService;

  @Captor private ArgumentCaptor<File> fileCaptor;

  private UnzipTask unzipTask;

  @BeforeEach
  void init() {
    CgitestConfiguration config = new CgitestConfiguration();
    config.setUnzipDirectoryPath(UNZIP_FOLDER);
    unzipTask = new UnzipTask(pool, unzipService, config);
  }

  @Test
  @DisplayName("Given pool with two files " +
               "when unzipTask is run " +
               "then call UnzipService#unzip for both files")
  void run() {
    // given
    givenPoolWithTwoFiles(new File(FILE_1), new File(FILE_2));

    // when
    unzipTask.run();

    // then
    then(unzipService).should(times(2)).extract(any(ZipInputStream.class), fileCaptor.capture());

    assertThat(fileCaptor.getAllValues()).containsOnly(new File(UNZIP_FOLDER));
  }

  private void givenPoolWithTwoFiles(File file1, File file2) {
    when(pool.getNextFile()).thenReturn(file1, file2, null);
  }

}