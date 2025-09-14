package com.renamerai.tools;

import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool; 
import org.springframework.ai.tool.annotation.ToolParam;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

@Component
public class FileEditorTool {

    @Tool(description = "Rename a file on the local file system (with dryRun support)")
    public FileRenameResponse renameFile(
        @ToolParam(description = "Full path to the file") String filePath,
        @ToolParam(description = "New name without extension") String newName,
        @ToolParam(description = "Whether to perform a dry run") boolean dryRun) 
    {
        Path original = Path.of(filePath);
        if (!Files.exists(original)) 
        {
            return new FileRenameResponse("ERROR", "Original file not found.", null);
        }

        String fileName = original.getFileName().toString();
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) 
        {
            extension = fileName.substring(dotIndex);
        }

        Path parent = original.getParent();
        Path target = parent.resolve(newName + extension);

        String suggestion = target.getFileName().toString();

        if (dryRun) {
            return new FileRenameResponse("DRY_RUN", "Suggestion: " + suggestion, suggestion);
        }

        if (Files.exists(target)) {
            return new FileRenameResponse("ERROR", "A file with the new name already exists. Skipping.", null);
        }

        try {
            Files.move(original, target);
            return new FileRenameResponse("SUCCESS", "File renamed successfully to: " + suggestion, suggestion);
        } catch (IOException e) {
            return new FileRenameResponse("ERROR", "Failed to rename file: " + e.getMessage(), null);
        }
    }

    public static class FileRenameResponse {
        public String status;
        public String message;
        public String newFileName;

        public FileRenameResponse(String status, String message, String newFileName) {
            this.status = status;
            this.message = message;
            this.newFileName = newFileName;
        }
    }
}
