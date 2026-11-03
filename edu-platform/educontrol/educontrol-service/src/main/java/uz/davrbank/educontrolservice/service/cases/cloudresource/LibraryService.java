package uz.davrbank.educontrolservice.service.cases.cloudresource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.library.LibraryRequestDTO;
import uz.davrbank.educontrolservice.feign.cloudresource.LibraryInterfaceFeign;
import uz.davrbank.educontrolservice.service.dao.cloudresource.LibraryInterface;

import java.util.List;

@Service
@Slf4j
public class LibraryService implements LibraryInterface {

    private final LibraryInterfaceFeign libraryInterfaceFeign;

    public LibraryService(LibraryInterfaceFeign libraryInterfaceFeign) {
        this.libraryInterfaceFeign = libraryInterfaceFeign;
    }

    @Override
    public Object createLibrary(LibraryRequestDTO libraryRequestDTO) {
        log.info("REQUEST (createLibrary) FROM [cloudresource-service]");
        return libraryInterfaceFeign.createLibrary(libraryRequestDTO);
    }

    @Override
    public Object getOneLibrary(Long id) {
        log.info("REQUEST (getOneLibrary) FROM [cloudresource-service]");
        return libraryInterfaceFeign.getOneLibraryData(id);
    }



    @Override
    public Object getAllLibraries(String categoryId, List<Long> departmentIds, int page, int size) {
        log.info("REQUEST (getAllLibraries) FROM [cloudresource-service]");
        return libraryInterfaceFeign.getAllLibraries(categoryId, departmentIds, page, size);
    }

    @Override
    public Object updateLibrary(Long id, LibraryRequestDTO libraryRequestDTO) {
        log.info("REQUEST (updateLibrary) FROM [cloudresource-service]");
        return libraryInterfaceFeign.updateLibrary(id, libraryRequestDTO);
    }

    @Override
    public Object deleteLibrary(Long id) {
        log.info("REQUEST (deleteLibrary) FROM [cloudresource-service]");
        return libraryInterfaceFeign.deleteLibrary(id);
    }

    @Override
    public List<Object> getLibraryData(Long id) {
        log.info("REQUEST (getLibraryData) FROM [cloudresource-service]");
        return libraryInterfaceFeign.getLibraryData(id);
    }
}
