package com.renamerai.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class RenamerTool {

    private final ChatClient chatClient;

    public RenamerTool(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Tool(description = "Analyzes all images in a folder and returns suggested new names, prioritizing text and falling back to labels.")
    public Map<String, String> getNewNamesForFolder(
            @ToolParam(description = "Path to folder containing images.") String folderPath,
            @ToolParam(description = "Whether to perform a dry run.") boolean dryRun
    ) throws Exception {

        Map<String, String> results = new HashMap<>();

        if (dryRun) {
            log.info("Dry run mode: no AI calls will be made.");
            return results;
        }

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Folder not found: " + folder.getAbsolutePath());
        }

        // Filter for common image types
        File[] files = folder.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
        });

        if (files == null || files.length == 0) {
            log.info("No images found in folder: " + folderPath);
            return results;
        }

        for (File file : files) {
            try {
                Media imageMedia = new Media(MimeTypeUtils.IMAGE_PNG,new FileSystemResource(file));

                String promptText = """
                        Analyze this image.
                        1. If the image contains visible text, return the first few words (max 5 words).
                        2. If no text is visible, describe the main objects (max 5 words).
                        Return only the description, no punctuation.
                        """;

                UserMessage userMessage = new UserMessage(promptText, List.of(imageMedia));
                Prompt prompt = new Prompt(userMessage);

                String response = chatClient.prompt(prompt).call().content();

                // Trim to max 5 words
                String[] words = response.trim().split("\\s+");
                String newName = String.join(" ", Arrays.copyOfRange(words, 0, Math.min(words.length, 5)));

                results.put(file.getName(), newName);

            } catch (Exception e) {
                log.error("Failed to process file " + file.getName() + ": " + e.getMessage());
            }
        }

        return results;
    }
}
