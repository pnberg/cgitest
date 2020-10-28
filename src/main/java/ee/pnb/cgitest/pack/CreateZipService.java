package ee.pnb.cgitest.pack;

import ee.pnb.cgitest.RandomTextService;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateZipService {

  private final RandomTextService randomTextService;

  public void createArchive(ZipOutputStream zipOutputStream, String filename) {
    String content = randomTextService.generateRandomText();
    makeZipFile(zipOutputStream, filename, content);
  }

  private void makeZipFile(ZipOutputStream out, String filename, String content) {
    try {
      ZipEntry e = new ZipEntry(filename);
      out.putNextEntry(e);

      byte[] data = content.getBytes();
      out.write(data, 0, data.length);
      out.closeEntry();
    }
    catch(Exception e) {
      log.error("Exception when making zipfile", e);
    }
    finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          log.error("Exception when closing zipfile", e);
        }
      }

    }
  }

}
