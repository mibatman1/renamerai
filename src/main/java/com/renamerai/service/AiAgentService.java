package com.renamerai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.renamerai.tools.FileEditorTool;
import com.renamerai.tools.NamerTool;
import com.renamerai.tools.RenamerTool;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AiAgentService {

    private final ChatClient chatClient;
    private final RenamerTool renamerTool;
    private final NamerTool namerTool;
    private final FileEditorTool fileEditorTool;

    public AiAgentService(ChatClient.Builder chatClientBuilder, RenamerTool renamerTool, NamerTool namerTool, FileEditorTool fileEditorTool) {
        this.namerTool=namerTool;
        this.renamerTool=renamerTool;
        this.fileEditorTool=fileEditorTool;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                    You are an expert file organizer.
                    You can use the 'renamer' tool to analyze image content,
                    the 'namer' tool to suggest a name,
                    and the 'file-editor' tool to rename the file.
                    When renaming a file, always follow these steps in order:
                    1. renamer
                    2. namer
                    3. file-editor
                    You must explicitly use these tools and follow the instructions precisely.
                    """)
                .defaultTools(renamerTool, namerTool, fileEditorTool)
                .build();
    }

    public void renameFileWithAgent(String filePath, boolean dryRun) {
        System.out.println("AI Agent is starting to rename: " + filePath);

        String userMessage = String.format(
                "Please rename the file at this path: %s. Use dryRun: %s",
                filePath, dryRun
        );

        Prompt prompt = new Prompt(userMessage);

        try 
        {
            var response = chatClient.prompt(prompt)
                        .call()
                        .chatResponse();
            log.info("Running this one {}", response.getResult().getOutput());
        } catch (Exception e) {
            log.error("An error occurred during agent execution: " + e.getMessage());
        }
    }
}
