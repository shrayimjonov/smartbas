package uz.davrbank.userinfobase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.userinfobase.service.DbService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/db")
public class DbController {

    private final DbService dbService;


    @DeleteMapping("/drop-davrhub-table")
    public String dropTable() {
        dbService.dropDavrhubTable();
        return "davrhub_files table dropped";
    }

}