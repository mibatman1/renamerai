package com.renamerai.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class NamerTool {

    private final ChatClient chatClient;

    public NamerTool(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Tool(description = "Generates clean, hyphenated filenames from multiple image descriptions.")
    public Map<String, String> generateCleanFilenames(
            @ToolParam(description = "Map of original filename -> description") Map<String, String> descriptions
    ) {
        Map<String, String> result = new HashMap<>();

        descriptions.forEach((originalName, description) -> {
            try {
                String prompt = """
                        You are a helpful assistant that generates a clean, simple, and descriptive filename from a description.
                        The filename should be lowercase and use hyphens instead of spaces. Do not include a file extension.
                        
                        Description: %s
                        Filename:
                        """.formatted(description);

                String cleanName = chatClient.prompt(prompt).call().content().trim();
                result.put(originalName, cleanName);
            } catch (Exception e) {
                log.error("Failed to generate filename for " + originalName + ": " + e.getMessage());
                result.put(originalName, description.replaceAll("\\s+", "-").toLowerCase()); // fallback
            }
        });

        return result;
    }
}
