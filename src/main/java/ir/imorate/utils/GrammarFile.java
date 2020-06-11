package ir.imorate.utils;

import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Data
public class GrammarFile implements Constants {
    private String fileName;

    public GrammarFile(String fileName) {
        this.fileName = FILE_DIR + fileName;
    }

    public GrammarFile() {
        this.fileName = FILE_DIR + "grammar.txt";
    }

    public Optional<List<String>> readGrammar() {
        File file = new File(this.fileName);
        try {
            if (FileUtils.sizeOf(file) == 0) {
                throw new Exception("File is empty");
            }
            return Optional.of(FileUtils.readLines(file, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
