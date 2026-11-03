package uz.davrbank.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.response.ArchiveResponseDto;
import uz.davrbank.dto.response.ResponseData.Error;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Archive;
import uz.davrbank.entity.LocalizedText;
import uz.davrbank.enums.LanguageType;
import uz.davrbank.enums.SearchType;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.ArchiveMapperImpl;
import uz.davrbank.repository.ArchiveRepository;

class ArchiveServiceImplDiffblueTest {
    /**
     * Test {@link ArchiveServiceImpl#saveArchive(Archive)}.
     *
     * <p>Method under test: {@link ArchiveServiceImpl#saveArchive(Archive)}
     */
    @Test
    @DisplayName("Test saveArchive(Archive)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void ArchiveServiceImpl.saveArchive(Archive)"})
    void testSaveArchive() {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Archive archive = new Archive();
        archive.setDeletedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        archive.setDescription(description);
        archive.setId(1L);
        archive.setMessageId(1);
        archive.setTitle(title);

        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.save(Mockito.<Archive>any())).thenReturn(archive);
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        LocalizedText description2 = new LocalizedText();
        description2.setEn("En");
        description2.setRu("Ru");
        description2.setUz("Uz");

        LocalizedText title2 = new LocalizedText();
        title2.setEn("En");
        title2.setRu("Ru");
        title2.setUz("Uz");

        Archive archive2 = new Archive();
        archive2.setDeletedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        archive2.setDescription(description2);
        archive2.setId(1L);
        archive2.setMessageId(1);
        archive2.setTitle(title2);

        // Act
        archiveServiceImpl.saveArchive(archive2);

        // Assert
        verify(archiveRepository).save(isA(Archive.class));
    }

    /**
     * Test {@link ArchiveServiceImpl#saveArchive(Archive)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#saveArchive(Archive)}
     */
    @Test
    @DisplayName("Test saveArchive(Archive); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void ArchiveServiceImpl.saveArchive(Archive)"})
    void testSaveArchive_thenThrowNotFoundException() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.save(Mockito.<Archive>any())).thenThrow(new NotFoundException());
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Archive archive = new Archive();
        archive.setDeletedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        archive.setDescription(description);
        archive.setId(1L);
        archive.setMessageId(1);
        archive.setTitle(title);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> archiveServiceImpl.saveArchive(archive));
        verify(archiveRepository).save(isA(Archive.class));
    }

    /**
     * Test {@link ArchiveServiceImpl#getAllArchievedMessages(PaginationRequest)}.
     *
     * <ul>
     *   <li>Given {@code null}.
     *   <li>Then calls {@link PaginationRequest#toPageable()}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#getAllArchievedMessages(PaginationRequest)}
     */
    @Test
    @DisplayName(
            "Test getAllArchievedMessages(PaginationRequest); given 'null'; then calls toPageable()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData ArchiveServiceImpl.getAllArchievedMessages(PaginationRequest)"})
    void testGetAllArchievedMessages_givenNull_thenCallsToPageable() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.findAll(Mockito.<Specification<Archive>>any(), Mockito.<Pageable>any()))
                .thenThrow(new NotFoundException());
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        PaginationRequest paginationRequest = mock(PaginationRequest.class);
        when(paginationRequest.toPageable()).thenReturn(null);

        // Act and Assert
        assertThrows(
                NotFoundException.class,
                () -> archiveServiceImpl.getAllArchievedMessages(paginationRequest));
        verify(archiveRepository).findAll(isA(Specification.class), (Pageable) isNull());
        verify(paginationRequest).toPageable();
    }

    /**
     * Test {@link ArchiveServiceImpl#getAllArchievedMessages(PaginationRequest)}.
     *
     * <ul>
     *   <li>Then return Error is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#getAllArchievedMessages(PaginationRequest)}
     */
    @Test
    @DisplayName("Test getAllArchievedMessages(PaginationRequest); then return Error is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData ArchiveServiceImpl.getAllArchievedMessages(PaginationRequest)"})
    void testGetAllArchievedMessages_thenReturnErrorIsNull() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.findAll(Mockito.<Specification<Archive>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act
        ResultData<PaginationResponse<ArchiveResponseDto>> actualAllArchievedMessages =
                archiveServiceImpl.getAllArchievedMessages(new PaginationRequest());

        // Assert
        verify(archiveRepository).findAll(isA(Specification.class), isA(Pageable.class));
        assertNull(actualAllArchievedMessages.getError());
        PaginationResponse<ArchiveResponseDto> data = actualAllArchievedMessages.getData();
        assertEquals(0, data.getPage());
        assertEquals(0, data.getSize());
        assertEquals(0L, data.getTotalElements());
        assertEquals(1, data.getTotalPages());
        assertEquals(200, actualAllArchievedMessages.getCode().intValue());
        assertTrue(data.getContent().isEmpty());
        assertTrue(actualAllArchievedMessages.isSuccess());
    }

    /**
     * Test {@link ArchiveServiceImpl#getAllArchievedMessages(PaginationRequest)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#getAllArchievedMessages(PaginationRequest)}
     */
    @Test
    @DisplayName("Test getAllArchievedMessages(PaginationRequest); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData ArchiveServiceImpl.getAllArchievedMessages(PaginationRequest)"})
    void testGetAllArchievedMessages_thenThrowNotFoundException() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.findAll(Mockito.<Specification<Archive>>any(), Mockito.<Pageable>any()))
                .thenThrow(new NotFoundException());
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act and Assert
        assertThrows(
                NotFoundException.class,
                () -> archiveServiceImpl.getAllArchievedMessages(new PaginationRequest()));
        verify(archiveRepository).findAll(isA(Specification.class), isA(Pageable.class));
    }

    /**
     * Test {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType, SearchType, String)}.
     *
     * <ul>
     *   <li>Then return Error Error is {@code No matching archived messages found.}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType,
     * SearchType, String)}
     */
    @Test
    @DisplayName(
            "Test searchArchivedMessages(LanguageType, SearchType, String); then return Error Error is 'No matching archived messages found.'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "ResultData ArchiveServiceImpl.searchArchivedMessages(LanguageType, SearchType, String)"
    })
    void testSearchArchivedMessages_thenReturnErrorErrorIsNoMatchingArchivedMessagesFound() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.findAll(Mockito.<Specification<Archive>>any()))
                .thenReturn(new ArrayList<>());
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act
        ResultData<List<ArchiveResponseDto>> actualSearchArchivedMessagesResult =
                archiveServiceImpl.searchArchivedMessages(LanguageType.uz, SearchType.title, "Text");

        // Assert
        verify(archiveRepository).findAll(isA(Specification.class));
        Error error = actualSearchArchivedMessagesResult.getError();
        assertEquals("No matching archived messages found.", error.getError());
        assertNull(actualSearchArchivedMessagesResult.getData());
        assertEquals(400, actualSearchArchivedMessagesResult.getCode().intValue());
        assertEquals(500, error.getCode().intValue());
        assertFalse(actualSearchArchivedMessagesResult.isSuccess());
    }

    /**
     * Test {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType, SearchType, String)}.
     *
     * <ul>
     *   <li>Then return Error is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType,
     * SearchType, String)}
     */
    @Test
    @DisplayName(
            "Test searchArchivedMessages(LanguageType, SearchType, String); then return Error is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "ResultData ArchiveServiceImpl.searchArchivedMessages(LanguageType, SearchType, String)"
    })
    void testSearchArchivedMessages_thenReturnErrorIsNull() {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("No matching archived messages found.");
        description.setRu("No matching archived messages found.");
        description.setUz("No matching archived messages found.");

        LocalizedText title = new LocalizedText();
        title.setEn("No matching archived messages found.");
        title.setRu("No matching archived messages found.");
        title.setUz("No matching archived messages found.");

        Archive archive = new Archive();

        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        archive.setDeletedDate(ofResult.atStartOfDay());
        archive.setDescription(description);
        archive.setId(1L);
        archive.setMessageId(1);
        archive.setTitle(title);

        ArrayList<Archive> archiveList = new ArrayList<>();
        archiveList.add(archive);

        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.findAll(Mockito.<Specification<Archive>>any())).thenReturn(archiveList);
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act
        ResultData<List<ArchiveResponseDto>> actualSearchArchivedMessagesResult =
                archiveServiceImpl.searchArchivedMessages(LanguageType.uz, SearchType.title, "Text");

        // Assert
        verify(archiveRepository).findAll(isA(Specification.class));
        assertNull(actualSearchArchivedMessagesResult.getError());
        List<ArchiveResponseDto> data = actualSearchArchivedMessagesResult.getData();
        assertEquals(1, data.size());
        ArchiveResponseDto getResult = data.get(0);
        assertEquals(1, getResult.getMessageId().intValue());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(200, actualSearchArchivedMessagesResult.getCode().intValue());
        assertTrue(actualSearchArchivedMessagesResult.isSuccess());
        assertSame(description, getResult.getDescription());
        assertSame(title, getResult.getTitle());
        assertSame(ofResult, getResult.getDeletedDate().toLocalDate());
    }

    /**
     * Test {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType, SearchType, String)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType,
     * SearchType, String)}
     */
    @Test
    @DisplayName(
            "Test searchArchivedMessages(LanguageType, SearchType, String); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "ResultData ArchiveServiceImpl.searchArchivedMessages(LanguageType, SearchType, String)"
    })
    void testSearchArchivedMessages_thenThrowNotFoundException() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.findAll(Mockito.<Specification<Archive>>any()))
                .thenThrow(new NotFoundException());
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act and Assert
        assertThrows(
                NotFoundException.class,
                () -> archiveServiceImpl.searchArchivedMessages(LanguageType.uz, SearchType.title, "Text"));
        verify(archiveRepository).findAll(isA(Specification.class));
    }

    /**
     * Test {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType, SearchType, String)}.
     *
     * <ul>
     *   <li>When space.
     *   <li>Then return Error Error is {@code Text should not be empty.}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#searchArchivedMessages(LanguageType,
     * SearchType, String)}
     */
    @Test
    @DisplayName(
            "Test searchArchivedMessages(LanguageType, SearchType, String); when space; then return Error Error is 'Text should not be empty.'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "ResultData ArchiveServiceImpl.searchArchivedMessages(LanguageType, SearchType, String)"
    })
    void testSearchArchivedMessages_whenSpace_thenReturnErrorErrorIsTextShouldNotBeEmpty() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act
        ResultData<List<ArchiveResponseDto>> actualSearchArchivedMessagesResult =
                archiveServiceImpl.searchArchivedMessages(LanguageType.uz, SearchType.title, " ");

        // Assert
        Error error = actualSearchArchivedMessagesResult.getError();
        assertEquals("Text should not be empty.", error.getError());
        assertNull(actualSearchArchivedMessagesResult.getData());
        assertEquals(400, actualSearchArchivedMessagesResult.getCode().intValue());
        assertEquals(500, error.getCode().intValue());
        assertFalse(actualSearchArchivedMessagesResult.isSuccess());
    }

    /**
     * Test {@link ArchiveServiceImpl#deleteArchiveById(Long)}.
     *
     * <ul>
     *   <li>Given {@link ArchiveRepository} {@link ArchiveRepository#deleteById(Object)} throw {@link
     *       NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#deleteArchiveById(Long)}
     */
    @Test
    @DisplayName(
            "Test deleteArchiveById(Long); given ArchiveRepository deleteById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData ArchiveServiceImpl.deleteArchiveById(Long)"})
    void testDeleteArchiveById_givenArchiveRepositoryDeleteByIdThrowNotFoundException() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        doThrow(new NotFoundException()).when(archiveRepository).deleteById(Mockito.<Long>any());
        when(archiveRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> archiveServiceImpl.deleteArchiveById(1L));
        verify(archiveRepository).deleteById(1L);
        verify(archiveRepository).existsById(1L);
    }

    /**
     * Test {@link ArchiveServiceImpl#deleteArchiveById(Long)}.
     *
     * <ul>
     *   <li>Given {@link ArchiveRepository} {@link ArchiveRepository#existsById(Object)} return
     *       {@code false}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#deleteArchiveById(Long)}
     */
    @Test
    @DisplayName(
            "Test deleteArchiveById(Long); given ArchiveRepository existsById(Object) return 'false'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData ArchiveServiceImpl.deleteArchiveById(Long)"})
    void testDeleteArchiveById_givenArchiveRepositoryExistsByIdReturnFalse() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> archiveServiceImpl.deleteArchiveById(1L));
        verify(archiveRepository).existsById(1L);
    }

    /**
     * Test {@link ArchiveServiceImpl#deleteArchiveById(Long)}.
     *
     * <ul>
     *   <li>Given {@link ArchiveRepository} {@link ArchiveRepository#existsById(Object)} throw {@link
     *       NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#deleteArchiveById(Long)}
     */
    @Test
    @DisplayName(
            "Test deleteArchiveById(Long); given ArchiveRepository existsById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData ArchiveServiceImpl.deleteArchiveById(Long)"})
    void testDeleteArchiveById_givenArchiveRepositoryExistsByIdThrowNotFoundException() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        when(archiveRepository.existsById(Mockito.<Long>any())).thenThrow(new NotFoundException());
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> archiveServiceImpl.deleteArchiveById(1L));
        verify(archiveRepository).existsById(1L);
    }

    /**
     * Test {@link ArchiveServiceImpl#deleteArchiveById(Long)}.
     *
     * <ul>
     *   <li>Then return Data Message is {@code Archive entry deleted successfully.}.
     * </ul>
     *
     * <p>Method under test: {@link ArchiveServiceImpl#deleteArchiveById(Long)}
     */
    @Test
    @DisplayName(
            "Test deleteArchiveById(Long); then return Data Message is 'Archive entry deleted successfully.'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData ArchiveServiceImpl.deleteArchiveById(Long)"})
    void testDeleteArchiveById_thenReturnDataMessageIsArchiveEntryDeletedSuccessfully() {
        // Arrange
        ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
        doNothing().when(archiveRepository).deleteById(Mockito.<Long>any());
        when(archiveRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        ArchiveServiceImpl archiveServiceImpl =
                new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());

        // Act
        ResultData<ResponseDto> actualDeleteArchiveByIdResult =
                archiveServiceImpl.deleteArchiveById(1L);

        // Assert
        verify(archiveRepository).deleteById(1L);
        verify(archiveRepository).existsById(1L);
        ResponseDto data = actualDeleteArchiveByIdResult.getData();
        assertEquals("Archive entry deleted successfully.", data.getMessage());
        assertNull(actualDeleteArchiveByIdResult.getError());
        assertEquals(200, actualDeleteArchiveByIdResult.getCode().intValue());
        assertEquals(200, data.getCode().intValue());
        assertTrue(actualDeleteArchiveByIdResult.isSuccess());
    }
}
