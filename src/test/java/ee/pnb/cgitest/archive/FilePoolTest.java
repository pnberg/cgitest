package ee.pnb.cgitest.archive;

import static org.assertj.core.api.Assertions.assertThat;

import ee.pnb.cgitest.CgitestException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilePoolTest {

  private static final String FILENAME_1 = "file1.txt";
  private static final String FILENAME_2 = "file2.txt";

  @TempDir
  Path zipFolder;

  private FilePool pool;

  @BeforeEach
  void init() {
    pool = new FilePool();
    zipFolder.resolve("/tmp");
  }

  @Test
  @DisplayName("Given directory containing two files " +
               "when pool is loaded " +
               "then both files should be added to the pool")
  void loadPool() throws CgitestException, IOException {
    // given
    addFileToFolder(zipFolder, FILENAME_1);
    addFileToFolder(zipFolder, FILENAME_2);

    // when
    pool.loadPool(zipFolder);

    // then
    assertThat(pool.getZipFiles()).hasSize(2);
    assertThat(pool.getZipFiles()).extracting(File::getName)
        .containsExactlyInAnyOrder(FILENAME_1, FILENAME_2);
  }

  @Test
  void getNextZipFile() {
  }

  private void addFileToFolder(Path folder, String filename) throws IOException {
    File newFile = new File(folder.toFile(), filename);
    BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
    writer.append("Lorem ipsum");
    writer.close();
  }

}