package ee.pnb.cgitest;

import ee.pnb.cgitest.archive.ArchiveService;
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

    @GetMapping(value = "build", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> buildFiles(
        @RequestParam(name = "count", required = false) Optional<Integer> count
    ) {
        int fileCount = count.orElse(DEFAULT_FILE_COUNT);
        archiveService.zip(fileCount);
        return ResponseEntity.ok().body(fileCount + " files are built");
    }

    @RequestMapping(value = "unzip", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> unzip() {
        try {
            archiveService.unzipAll();
            return ResponseEntity.ok().body("Zip files extracted");
        }
        catch (CgitestException e) {
            return ResponseEntity.ok()
                .body("Error " + e.getMessage() + " when loading zip files to pool");
        }
    }

}
