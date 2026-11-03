package uz.davrbank.service.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.dto.response.FileUploadResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.FileUpload;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.FileUploadMapper;
import uz.davrbank.repository.FileUploadRepository;

class FileUploadServiceImplDiffblueTest {
    /**
     * Test {@link FileUploadServiceImpl#uploadFile(MultipartFile)}.
     *
     * <ul>
     *   <li>Given {@link FileUploadMapper} {@link FileUploadMapper#toDto(FileUpload)} throw {@link
     *       NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#uploadFile(MultipartFile)}
     */
    @Test
    @DisplayName(
            "Test uploadFile(MultipartFile); given FileUploadMapper toDto(FileUpload) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData FileUploadServiceImpl.uploadFile(MultipartFile)"})
    void testUploadFile_givenFileUploadMapperToDtoThrowNotFoundException() throws IOException {
        // Arrange
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile("AXAXAXAX".getBytes("UTF-8"));
        fileUpload.setFileName("foo.txt");
        fileUpload.setId(1L);
        fileUpload.setUploadedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.save(Mockito.<FileUpload>any())).thenReturn(fileUpload);

        FileUploadMapper fileUploadMapper = mock(FileUploadMapper.class);
        when(fileUploadMapper.toDto(Mockito.<FileUpload>any())).thenThrow(new NotFoundException());

        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, fileUploadMapper);
        MockMultipartFile file =
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> fileUploadServiceImpl.uploadFile(file));
        verify(fileUploadRepository).save(isA(FileUpload.class));
        verify(fileUploadMapper).toDto(isA(FileUpload.class));
    }

    /**
     * Test {@link FileUploadServiceImpl#uploadFile(MultipartFile)}.
     *
     * <ul>
     *   <li>Given {@link FileUploadRepository} {@link FileUploadRepository#save(Object)} throw {@link
     *       NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#uploadFile(MultipartFile)}
     */
    @Test
    @DisplayName(
            "Test uploadFile(MultipartFile); given FileUploadRepository save(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData FileUploadServiceImpl.uploadFile(MultipartFile)"})
    void testUploadFile_givenFileUploadRepositorySaveThrowNotFoundException() throws IOException {
        // Arrange
        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.save(Mockito.<FileUpload>any())).thenThrow(new NotFoundException());
        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, mock(FileUploadMapper.class));
        MockMultipartFile file =
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> fileUploadServiceImpl.uploadFile(file));
        verify(fileUploadRepository).save(isA(FileUpload.class));
    }

    /**
     * Test {@link FileUploadServiceImpl#uploadFile(MultipartFile)}.
     *
     * <ul>
     *   <li>Then return Data UploadedAt toLocalTime toString is {@code 00:00}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#uploadFile(MultipartFile)}
     */
    @Test
    @DisplayName(
            "Test uploadFile(MultipartFile); then return Data UploadedAt toLocalTime toString is '00:00'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData FileUploadServiceImpl.uploadFile(MultipartFile)"})
    void testUploadFile_thenReturnDataUploadedAtToLocalTimeToStringIs0000() throws IOException {
        // Arrange
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile("AXAXAXAX".getBytes("UTF-8"));
        fileUpload.setFileName("foo.txt");
        fileUpload.setId(1L);
        fileUpload.setUploadedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.save(Mockito.<FileUpload>any())).thenReturn(fileUpload);

        FileUploadMapper fileUploadMapper = mock(FileUploadMapper.class);

        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        when(fileUploadMapper.toDto(Mockito.<FileUpload>any()))
                .thenReturn(
                        FileUploadResponse.builder()
                                .file("AXAXAXAX".getBytes("UTF-8"))
                                .fileName("foo.txt")
                                .id(1L)
                                .uploadedAt(ofResult.atStartOfDay())
                                .build());

        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, fileUploadMapper);
        MockMultipartFile file =
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        // Act
        ResultData<FileUploadResponse> actualUploadFileResult = fileUploadServiceImpl.uploadFile(file);

        // Assert
        verify(fileUploadRepository).save(isA(FileUpload.class));
        verify(fileUploadMapper).toDto(isA(FileUpload.class));
        FileUploadResponse data = actualUploadFileResult.getData();
        LocalDateTime uploadedAt = data.getUploadedAt();
        assertEquals("00:00", uploadedAt.toLocalTime().toString());
        LocalDate toLocalDateResult = uploadedAt.toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        assertEquals("foo.txt", data.getFileName());
        assertNull(actualUploadFileResult.getError());
        assertEquals(1L, data.getId().longValue());
        assertEquals(200, actualUploadFileResult.getCode().intValue());
        assertTrue(actualUploadFileResult.isSuccess());
        assertSame(ofResult, toLocalDateResult);
        assertArrayEquals("AXAXAXAX".getBytes("UTF-8"), data.getFile());
    }

    /**
     * Test {@link FileUploadServiceImpl#downloadFile(Long)}.
     *
     * <ul>
     *   <li>Given {@link FileUploadMapper} {@link FileUploadMapper#toDto(FileUpload)} throw {@link
     *       NotFoundException#NotFoundException()}.
     *   <li>Then calls {@link FileUploadMapper#toDto(FileUpload)}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#downloadFile(Long)}
     */
    @Test
    @DisplayName(
            "Test downloadFile(Long); given FileUploadMapper toDto(FileUpload) throw NotFoundException(); then calls toDto(FileUpload)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData FileUploadServiceImpl.downloadFile(Long)"})
    void testDownloadFile_givenFileUploadMapperToDtoThrowNotFoundException_thenCallsToDto()
            throws UnsupportedEncodingException {
        // Arrange
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile("AXAXAXAX".getBytes("UTF-8"));
        fileUpload.setFileName("foo.txt");
        fileUpload.setId(1L);
        fileUpload.setUploadedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<FileUpload> ofResult = Optional.of(fileUpload);

        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        FileUploadMapper fileUploadMapper = mock(FileUploadMapper.class);
        when(fileUploadMapper.toDto(Mockito.<FileUpload>any())).thenThrow(new NotFoundException());

        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, fileUploadMapper);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> fileUploadServiceImpl.downloadFile(1L));
        verify(fileUploadRepository).findById(1L);
        verify(fileUploadMapper).toDto(isA(FileUpload.class));
    }

    /**
     * Test {@link FileUploadServiceImpl#downloadFile(Long)}.
     *
     * <ul>
     *   <li>Given {@link FileUploadRepository} {@link FileUploadRepository#findById(Object)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#downloadFile(Long)}
     */
    @Test
    @DisplayName("Test downloadFile(Long); given FileUploadRepository findById(Object) return empty")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData FileUploadServiceImpl.downloadFile(Long)"})
    void testDownloadFile_givenFileUploadRepositoryFindByIdReturnEmpty() {
        // Arrange
        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        Optional<FileUpload> emptyResult = Optional.empty();
        when(fileUploadRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, mock(FileUploadMapper.class));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> fileUploadServiceImpl.downloadFile(1L));
        verify(fileUploadRepository).findById(1L);
    }

    /**
     * Test {@link FileUploadServiceImpl#downloadFile(Long)}.
     *
     * <ul>
     *   <li>Given {@link FileUploadRepository} {@link FileUploadRepository#findById(Object)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#downloadFile(Long)}
     */
    @Test
    @DisplayName(
            "Test downloadFile(Long); given FileUploadRepository findById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData FileUploadServiceImpl.downloadFile(Long)"})
    void testDownloadFile_givenFileUploadRepositoryFindByIdThrowNotFoundException() {
        // Arrange
        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException());
        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, mock(FileUploadMapper.class));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> fileUploadServiceImpl.downloadFile(1L));
        verify(fileUploadRepository).findById(1L);
    }

    /**
     * Test {@link FileUploadServiceImpl#downloadFile(Long)}.
     *
     * <ul>
     *   <li>Then return Data UploadedAt toLocalTime toString is {@code 00:00}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#downloadFile(Long)}
     */
    @Test
    @DisplayName(
            "Test downloadFile(Long); then return Data UploadedAt toLocalTime toString is '00:00'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData FileUploadServiceImpl.downloadFile(Long)"})
    void testDownloadFile_thenReturnDataUploadedAtToLocalTimeToStringIs0000()
            throws UnsupportedEncodingException {
        // Arrange
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile("AXAXAXAX".getBytes("UTF-8"));
        fileUpload.setFileName("foo.txt");
        fileUpload.setId(1L);
        fileUpload.setUploadedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<FileUpload> ofResult = Optional.of(fileUpload);

        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        FileUploadMapper fileUploadMapper = mock(FileUploadMapper.class);

        LocalDate ofResult2 = LocalDate.of(1970, 1, 1);
        when(fileUploadMapper.toDto(Mockito.<FileUpload>any()))
                .thenReturn(
                        FileUploadResponse.builder()
                                .file("AXAXAXAX".getBytes("UTF-8"))
                                .fileName("foo.txt")
                                .id(1L)
                                .uploadedAt(ofResult2.atStartOfDay())
                                .build());

        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, fileUploadMapper);

        // Act
        ResultData<FileUploadResponse> actualDownloadFileResult =
                fileUploadServiceImpl.downloadFile(1L);

        // Assert
        verify(fileUploadRepository).findById(1L);
        verify(fileUploadMapper).toDto(isA(FileUpload.class));
        FileUploadResponse data = actualDownloadFileResult.getData();
        LocalDateTime uploadedAt = data.getUploadedAt();
        assertEquals("00:00", uploadedAt.toLocalTime().toString());
        LocalDate toLocalDateResult = uploadedAt.toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        assertEquals("foo.txt", data.getFileName());
        assertNull(actualDownloadFileResult.getError());
        assertEquals(1L, data.getId().longValue());
        assertEquals(200, actualDownloadFileResult.getCode().intValue());
        assertTrue(actualDownloadFileResult.isSuccess());
        assertSame(ofResult2, toLocalDateResult);
        assertArrayEquals("AXAXAXAX".getBytes("UTF-8"), data.getFile());
    }

    /**
     * Test {@link FileUploadServiceImpl#findById(Long)}.
     *
     * <ul>
     *   <li>Given {@link FileUpload#FileUpload()} File is {@code AXAXAXAX} Bytes is {@code UTF-8}.
     *   <li>Then return {@link FileUpload#FileUpload()}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#findById(Long)}
     */
    @Test
    @DisplayName(
            "Test findById(Long); given FileUpload() File is 'AXAXAXAX' Bytes is 'UTF-8'; then return FileUpload()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"FileUpload FileUploadServiceImpl.findById(Long)"})
    void testFindById_givenFileUploadFileIsAxaxaxaxBytesIsUtf8_thenReturnFileUpload()
            throws UnsupportedEncodingException {
        // Arrange
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile("AXAXAXAX".getBytes("UTF-8"));
        fileUpload.setFileName("foo.txt");
        fileUpload.setId(1L);
        fileUpload.setUploadedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<FileUpload> ofResult = Optional.of(fileUpload);

        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, mock(FileUploadMapper.class));

        // Act
        FileUpload actualFindByIdResult = fileUploadServiceImpl.findById(1L);

        // Assert
        verify(fileUploadRepository).findById(1L);
        assertSame(fileUpload, actualFindByIdResult);
    }

    /**
     * Test {@link FileUploadServiceImpl#findById(Long)}.
     *
     * <ul>
     *   <li>Given {@link FileUploadRepository} {@link FileUploadRepository#findById(Object)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#findById(Long)}
     */
    @Test
    @DisplayName("Test findById(Long); given FileUploadRepository findById(Object) return empty")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"FileUpload FileUploadServiceImpl.findById(Long)"})
    void testFindById_givenFileUploadRepositoryFindByIdReturnEmpty() {
        // Arrange
        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        Optional<FileUpload> emptyResult = Optional.empty();
        when(fileUploadRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, mock(FileUploadMapper.class));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> fileUploadServiceImpl.findById(1L));
        verify(fileUploadRepository).findById(1L);
    }

    /**
     * Test {@link FileUploadServiceImpl#findById(Long)}.
     *
     * <ul>
     *   <li>Given {@link FileUploadRepository} {@link FileUploadRepository#findById(Object)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link FileUploadServiceImpl#findById(Long)}
     */
    @Test
    @DisplayName(
            "Test findById(Long); given FileUploadRepository findById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"FileUpload FileUploadServiceImpl.findById(Long)"})
    void testFindById_givenFileUploadRepositoryFindByIdThrowNotFoundException() {
        // Arrange
        FileUploadRepository fileUploadRepository = mock(FileUploadRepository.class);
        when(fileUploadRepository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException());
        FileUploadServiceImpl fileUploadServiceImpl =
                new FileUploadServiceImpl(fileUploadRepository, mock(FileUploadMapper.class));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> fileUploadServiceImpl.findById(1L));
        verify(fileUploadRepository).findById(1L);
    }
}
