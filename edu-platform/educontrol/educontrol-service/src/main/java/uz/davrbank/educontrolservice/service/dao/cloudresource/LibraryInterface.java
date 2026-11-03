package uz.davrbank.educontrolservice.service.dao.cloudresource;

import uz.davrbank.educontrolservice.aggregate.dto.library.LibraryRequestDTO;

import java.util.List;

public interface LibraryInterface {

    Object createLibrary(LibraryRequestDTO libraryRequestDTO);

    Object getOneLibrary(Long id);

    Object getAllLibraries(String categoryId, List<Long> departmentIds, int page, int size);

    Object updateLibrary(Long id, LibraryRequestDTO libraryRequestDTO);

    Object deleteLibrary(Long id);

    List<Object> getLibraryData(Long id);
}
