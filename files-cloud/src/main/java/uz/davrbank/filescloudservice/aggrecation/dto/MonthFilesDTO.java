package uz.davrbank.filescloudservice.aggrecation.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class MonthFilesDTO {
    private final String path;
    private final List<String> fileNames = new ArrayList<>();

    public MonthFilesDTO(String path) {
        this.path = path;
    }

    public void addFileName(String name) {
        this.fileNames.add(name);
    }
}
