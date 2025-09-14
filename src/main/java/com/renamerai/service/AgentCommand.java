package com.renamerai.service;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Component
@Command(
    name = "agent",
    mixinStandardHelpOptions = true,
    description = "Renames files using AI assistance"
)
@Log4j2
public class AgentCommand implements Callable<Integer> {

    private final AiAgentService aiAgentService;

    public AgentCommand(AiAgentService aiAgentService) {
        this.aiAgentService = aiAgentService;
    }

    @Parameters(index = "0", description = "The path to the file or directory")
    private String path;

    @Option(names = {"-d", "--dry-run"}, description = "Perform a dry run without renaming files")
    private boolean dryRun = false;

    @Override
    public Integer call() {
        try {
            log.info("Running on path: " + path + " (dryRun=" + dryRun + ")");
            aiAgentService.renameFileWithAgent(path, dryRun);
            return 0;
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return 1;
        }
    }
}

