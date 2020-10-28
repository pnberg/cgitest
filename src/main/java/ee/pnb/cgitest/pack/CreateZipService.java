package ee.pnb.cgitest.pack;

import ee.pnb.cgitest.CgitestException;
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

  public boolean createArchive(ZipOutputStream zipOutputStream, String filename) {
    try {
      String content = randomTextService.generateRandomText();
      makeZipFile(zipOutputStream, filename, content);
      return true;
    } catch (CgitestException e) {
      return false;
    }
  }

  private void makeZipFile(ZipOutputStream out, String filename, String content)
      throws CgitestException {
    try {
      ZipEntry e = new ZipEntry(filename);
      out.putNextEntry(e);

      byte[] data = content.getBytes();
      out.write(data, 0, data.length);
      out.closeEntry();
    }
    catch(Exception e) {
      log.error("Exception caught when making zipfile", e);
      throw new CgitestException(e.getMessage());
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
