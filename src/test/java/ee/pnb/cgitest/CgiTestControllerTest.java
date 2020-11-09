package ee.pnb.cgitest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ee.pnb.cgitest.archive.ArchiveService;
import ee.pnb.cgitest.archive.FilePool;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CgiTestController.class)
@ContextConfiguration(classes = { CgiTestController.class, CgitestConfiguration.class })
class CgiTestControllerTest {

  private static final String API_URI = "/zipfiles";
  private static final String ZIP_FOLDER = "/tmp/zip";

  @Autowired private MockMvc mockMvc;
  @Autowired private CgitestConfiguration config;

  @MockBean private ArchiveService archiveService;
  @MockBean private FilePool filePool;

  @Captor private ArgumentCaptor<Path> pathCaptor;


  @BeforeEach
  void init() {
    config.setZipFilePath(ZIP_FOLDER);
  }

  @DisplayName("Given file count value " +
               "when build endpoint is called " +
               "then call ArchiveService#zip with correct file count")
  @ParameterizedTest
  @MethodSource("provideCounts")
  void buildFiles(String givenFiles, int expectedFileCount) throws Exception {
    // given
    MockHttpServletRequestBuilder requestBuilder = givenBuildRequest(givenFiles);

    // when
    ResultActions actualResult = this.mockMvc.perform(requestBuilder);

    // then
   actualResult
        .andExpect(status().isOk());

    then(archiveService).should().zip(expectedFileCount);
  }

  @Test
  @DisplayName("Given folder for zipped files set in configuration " +
               "when load endpoint is called " +
               "then call FilePool#loadPool with correct argument")
  void load() throws Exception {
    // when
    ResultActions actualResult = this.mockMvc.perform(get(API_URI + "/load"));

    // then
    actualResult
        .andExpect(status().isOk());

    then(filePool).should().loadPool(pathCaptor.capture());
    Path actualPath = pathCaptor.getValue();
    assertThat(actualPath).isEqualTo(Paths.get(ZIP_FOLDER));
  }

  private static Stream<Arguments> provideCounts() {
    return Stream.of(
        Arguments.of("10", 10),
        Arguments.of(null, CgiTestController.DEFAULT_FILE_COUNT)
    );
  }

  private MockHttpServletRequestBuilder givenBuildRequest(String givenFiles) {
    MockHttpServletRequestBuilder builder = get(API_URI + "/build");
    if (givenFiles != null) {
      builder.queryParam("count", givenFiles);
    }
    return builder;
  }


}