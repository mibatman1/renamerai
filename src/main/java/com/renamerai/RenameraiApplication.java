package com.renamerai;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.renamerai.service.AgentCommand;

import picocli.CommandLine;

@SpringBootApplication
public class RenameraiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RenameraiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public CommandLineRunner run(ApplicationContext context) {
        return args -> {
            if (args.length > 0) {
                int exitCode = new CommandLine(context.getBean(AgentCommand.class))
                        .execute(args);
                System.exit(exitCode);
            } else {
                System.out.println("No command-line arguments provided. Please specify a file or directory.");
                new CommandLine(context.getBean(AgentCommand.class)).usage(System.out);
            }
        };
    }
}

