package ee.pnb.cgitest;

import ee.pnb.cgitest.archive.ArchiveService;
import ee.pnb.cgitest.archive.ZipFilePool;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("zipfiles")
@RequiredArgsConstructor
public class CgiTestController {

    private static final int DEFAULT_FILE_COUNT = 100_000;

    private final ArchiveService archiveService;
    private final ZipFilePool zipFilePool;

    @PutMapping(value = "build/{count}")
    public ResponseEntity<String> buildFiles(
        @PathVariable(value = "count", required = false) Optional<Integer> fileCount
    ) {
        int buildFiles = DEFAULT_FILE_COUNT;
        if (fileCount.isPresent()) {
            buildFiles = fileCount.get();
        }
        archiveService.zip(buildFiles);
        return ResponseEntity.ok().body(buildFiles + " files are built");
    }

    @PutMapping(value = "load")
    public ResponseEntity<String> loadPool() {
        try {
            zipFilePool.loadPool();
            return ResponseEntity.ok().body("Zip files pool is loaded");
        }
        catch (CgitestException e) {
            return ResponseEntity.ok().body("Error " + e.getMessage() + " when loading zip files to pool");
        }
    }

}
