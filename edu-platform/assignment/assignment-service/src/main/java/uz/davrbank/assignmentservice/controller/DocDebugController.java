package uz.davrbank.assignmentservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class DocDebugController {

    @GetMapping("/check-doc")
    public String checkDoc() {
        File f = new File("/home/index.html"); // ← правильный путь
        return f.exists() ? "YES /home/index.html FOUND" : "NO /home/index.html MISSING";
    }
}
