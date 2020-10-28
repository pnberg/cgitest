package ee.pnb.cgitest.pack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import ee.pnb.cgitest.RandomTextService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateZipServiceTest {

  private static final String RANDOM_TEXT = "lorem ipsum";
  private static final String FILE_NAME = "file";

  @Mock private RandomTextService randomTextService;
  @Mock private ZipOutputStream zipOutputStream;

  @Captor private ArgumentCaptor<ZipEntry> zipEntryCaptor;
  @Captor private ArgumentCaptor<byte[]> dataCaptor;

  private CreateZipService createZipService;

  @BeforeEach
  void init() {
    createZipService = new CreateZipService(randomTextService);
  }

  @Test
  @DisplayName("Given given output stream for zip file, filename and random text " +
      "when createArchive is called " +
      "then create zip file, which contains file with expected name and expected content")
  void createArchive() throws Exception {
    // given
    String givenText = givenRandomText();

    // when
    createZipService.createArchive(zipOutputStream, FILE_NAME);

    // then
    then(zipOutputStream).should().putNextEntry(zipEntryCaptor.capture());
    ZipEntry actualZipEntry = zipEntryCaptor.getValue();
    assertThat(actualZipEntry.getName()).isEqualTo(FILE_NAME);

    then(zipOutputStream).should().write(dataCaptor.capture(), eq(0), eq(givenText.length()));
    assertThat(dataCaptor.getValue()).isEqualTo(RANDOM_TEXT.getBytes());
  }

  private String givenRandomText() {
    given(randomTextService.generateRandomText()).willReturn(RANDOM_TEXT);
    return RANDOM_TEXT;
  }

}