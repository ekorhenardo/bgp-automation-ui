package com.github.ekorhenardo.bgpautomation.ui.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRunnerController {

    @PostMapping("/run-tests")
    public String runTests(@RequestParam(required = false) String testClass) {
        String command = (testClass != null && !testClass.isEmpty())
            ? "mvn clean test -Dtest=" + testClass
            : "mvn clean test";

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash", "-c", command);
            builder.directory(new File("/Users/ekorhenardo/Documents/Java Projects/bgp-automation"));
            builder.redirectErrorStream(true);

            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("\nExited with code ").append(exitCode);
            return output.toString();

        } catch (Exception e) {
            return "Error running tests: " + e.getMessage();
        }
    }
}
