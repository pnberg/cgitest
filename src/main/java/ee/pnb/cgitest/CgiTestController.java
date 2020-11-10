package ee.pnb.cgitest;

import ee.pnb.cgitest.archive.ArchiveService;
import ee.pnb.cgitest.archive.FilePool;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/zipfiles")
@RequiredArgsConstructor
public class CgiTestController {

    public static final Integer DEFAULT_FILE_COUNT = 100_000;

    private final ArchiveService archiveService;
    private final FilePool filePool;
    private final CgitestConfiguration config;

    @GetMapping(value = "build", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> buildFiles(
        @RequestParam(name = "count", required = false) Optional<Integer> count
    ) {
        int fileCount = count.orElse(DEFAULT_FILE_COUNT);
        archiveService.zip(fileCount);
        return ResponseEntity.ok().body(fileCount + " files are built");
    }

    @RequestMapping(value = "load", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> loadPool() {
        try {
            Path zipFileFolder = Paths.get(config.getZipFilePath());
            filePool.loadPool(zipFileFolder);
            return ResponseEntity.ok().body("Zip files pool is loaded");
        }
        catch (CgitestException e) {
            return ResponseEntity.ok()
                .body("Error " + e.getMessage() + " when loading zip files to pool");
        }
    }

    @RequestMapping(value = "unzip", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> unzip() {
        archiveService.unzipAll();
        return ResponseEntity.ok().body("Zip files extracted");
    }

}
