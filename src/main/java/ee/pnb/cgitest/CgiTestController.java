package ee.pnb.cgitest;

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

//    private final ZipFileBuildService buildService;

    @PutMapping(value = "build/{count}")
    public ResponseEntity<String> buildFiles(
        @PathVariable(value = "count", required = false) Optional<Integer> fileCount
    ) {
        int buildFiles = DEFAULT_FILE_COUNT;
        if (fileCount.isPresent()) {
            buildFiles = fileCount.get();
        }
        return ResponseEntity.ok().body(buildFiles + " files will be built");
    }


}
