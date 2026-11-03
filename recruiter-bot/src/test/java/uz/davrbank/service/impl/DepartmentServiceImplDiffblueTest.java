package uz.davrbank.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.DepartmentDto;
import uz.davrbank.dto.response.DepartmentResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Department;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.DepartmentMapperImpl;
import uz.davrbank.repository.CreateTestRepository;
import uz.davrbank.repository.DepartmentRepository;

class DepartmentServiceImplDiffblueTest {
    /**
     * Test {@link DepartmentServiceImpl#create(DepartmentDto)}.
     *
     * <ul>
     *   <li>Then return Data Name is {@code Name}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#create(DepartmentDto)}
     */
    CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
    @Test
    @DisplayName("Test create(DepartmentDto); then return Data Name is 'Name'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.create(DepartmentDto)"})
    void testCreate_thenReturnDataNameIsName() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.save(Mockito.<Department>any())).thenReturn(department);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act
        ResultData<DepartmentResponse> actualCreateResult =
                departmentServiceImpl.create(new DepartmentDto());

        // Assert
        verify(repository).save(isA(Department.class));
        DepartmentResponse data = actualCreateResult.getData();
        assertEquals("Name", data.getName());
        assertNull(data.getCreatedAt());
        assertNull(actualCreateResult.getError());
        assertEquals(1L, data.getId().longValue());
        assertEquals(200, actualCreateResult.getCode().intValue());
        assertTrue(actualCreateResult.isSuccess());
    }

    /**
     * Test {@link DepartmentServiceImpl#create(DepartmentDto)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#create(DepartmentDto)}
     */
    @Test
    @DisplayName("Test create(DepartmentDto); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.create(DepartmentDto)"})
    void testCreate_thenThrowNotFoundException() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.save(Mockito.<Department>any())).thenThrow(new NotFoundException());
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.create(new DepartmentDto()));
        verify(repository).save(isA(Department.class));
    }

    /**
     * Test {@link DepartmentServiceImpl#delete(Long)}.
     *
     * <ul>
     *   <li>Given {@link DepartmentRepository} {@link DepartmentRepository#deleteById(Object)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#delete(Long)}
     */
    @Test
    @DisplayName(
            "Test delete(Long); given DepartmentRepository deleteById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.delete(Long)"})
    void testDelete_givenDepartmentRepositoryDeleteByIdThrowNotFoundException() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");
        Optional<Department> ofResult = Optional.of(department);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        doThrow(new NotFoundException()).when(repository).deleteById(Mockito.<Long>any());
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.delete(1L));
        verify(repository).deleteById(1L);
        verify(repository).findById(1L);
    }

    /**
     * Test {@link DepartmentServiceImpl#delete(Long)}.
     *
     * <ul>
     *   <li>Given {@link DepartmentRepository} {@link DepartmentRepository#findById(Object)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#delete(Long)}
     */
    @Test
    @DisplayName("Test delete(Long); given DepartmentRepository findById(Object) return empty")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.delete(Long)"})
    void testDelete_givenDepartmentRepositoryFindByIdReturnEmpty() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        Optional<Department> emptyResult = Optional.empty();
        when(repository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.delete(1L));
        verify(repository).findById(1L);
    }

    /**
     * Test {@link DepartmentServiceImpl#delete(Long)}.
     *
     * <ul>
     *   <li>Given {@link DepartmentRepository} {@link DepartmentRepository#findById(Object)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#delete(Long)}
     */
    @Test
    @DisplayName(
            "Test delete(Long); given DepartmentRepository findById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.delete(Long)"})
    void testDelete_givenDepartmentRepositoryFindByIdThrowNotFoundException() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException());
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.delete(1L));
        verify(repository).findById(1L);
    }

    /**
     * Test {@link DepartmentServiceImpl#delete(Long)}.
     *
     * <ul>
     *   <li>Then return Data Message is {@code Successfully deleted Department by Id: 1}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#delete(Long)}
     */
    @Test
    @DisplayName(
            "Test delete(Long); then return Data Message is 'Successfully deleted Department by Id: 1'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.delete(Long)"})
    void testDelete_thenReturnDataMessageIsSuccessfullyDeletedDepartmentById1() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");
        Optional<Department> ofResult = Optional.of(department);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        doNothing().when(repository).deleteById(Mockito.<Long>any());
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act
        ResultData<ResponseDto> actualDeleteResult = departmentServiceImpl.delete(1L);

        // Assert
        verify(repository).deleteById(1L);
        verify(repository).findById(1L);
        ResponseDto data = actualDeleteResult.getData();
        assertEquals("Successfully deleted Department by Id: 1", data.getMessage());
        assertNull(actualDeleteResult.getError());
        assertEquals(200, actualDeleteResult.getCode().intValue());
        assertEquals(200, data.getCode().intValue());
        assertTrue(actualDeleteResult.isSuccess());
    }

    /**
     * Test {@link DepartmentServiceImpl#findById(Long)}.
     *
     * <ul>
     *   <li>Given {@link DepartmentRepository} {@link DepartmentRepository#findById(Object)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findById(Long)}
     */
    @Test
    @DisplayName("Test findById(Long); given DepartmentRepository findById(Object) return empty")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.findById(Long)"})
    void testFindById_givenDepartmentRepositoryFindByIdReturnEmpty() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        Optional<Department> emptyResult = Optional.empty();
        when(repository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.findById(1L));
        verify(repository).findById(1L);
    }

    /**
     * Test {@link DepartmentServiceImpl#findById(Long)}.
     *
     * <ul>
     *   <li>Given {@link DepartmentRepository} {@link DepartmentRepository#findById(Object)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findById(Long)}
     */
    @Test
    @DisplayName(
            "Test findById(Long); given DepartmentRepository findById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.findById(Long)"})
    void testFindById_givenDepartmentRepositoryFindByIdThrowNotFoundException() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException());
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.findById(1L));
        verify(repository).findById(1L);
    }

    /**
     * Test {@link DepartmentServiceImpl#findById(Long)}.
     *
     * <ul>
     *   <li>Then return Data Name is {@code Name}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findById(Long)}
     */
    @Test
    @DisplayName("Test findById(Long); then return Data Name is 'Name'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.findById(Long)"})
    void testFindById_thenReturnDataNameIsName() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");
        Optional<Department> ofResult = Optional.of(department);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act
        ResultData<DepartmentResponse> actualFindByIdResult = departmentServiceImpl.findById(1L);

        // Assert
        verify(repository).findById(1L);
        DepartmentResponse data = actualFindByIdResult.getData();
        assertEquals("Name", data.getName());
        assertNull(data.getCreatedAt());
        assertNull(actualFindByIdResult.getError());
        assertEquals(1L, data.getId().longValue());
        assertEquals(200, actualFindByIdResult.getCode().intValue());
        assertTrue(actualFindByIdResult.isSuccess());
    }

    /**
     * Test {@link DepartmentServiceImpl#findAll(PaginationRequest)}.
     *
     * <ul>
     *   <li>Given {@code null}.
     *   <li>Then calls {@link PaginationRequest#toPageable()}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findAll(PaginationRequest)}
     */
    @Test
    @DisplayName("Test findAll(PaginationRequest); given 'null'; then calls toPageable()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.findAll(PaginationRequest)"})
    void testFindAll_givenNull_thenCallsToPageable() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findAll(Mockito.<Specification<Department>>any(), Mockito.<Pageable>any()))
                .thenThrow(new NotFoundException());
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        PaginationRequest paginationRequest = mock(PaginationRequest.class);
        when(paginationRequest.toPageable()).thenReturn(null);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.findAll(paginationRequest));
        verify(repository).findAll(isA(Specification.class), (Pageable) isNull());
        verify(paginationRequest).toPageable();
    }

    /**
     * Test {@link DepartmentServiceImpl#findAll(PaginationRequest)}.
     *
     * <ul>
     *   <li>Then return Error is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findAll(PaginationRequest)}
     */
    @Test
    @DisplayName("Test findAll(PaginationRequest); then return Error is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.findAll(PaginationRequest)"})
    void testFindAll_thenReturnErrorIsNull() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findAll(Mockito.<Specification<Department>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act
        ResultData<PaginationResponse<DepartmentResponse>> actualFindAllResult =
                departmentServiceImpl.findAll(new PaginationRequest());

        // Assert
        verify(repository).findAll(isA(Specification.class), isA(Pageable.class));
        assertNull(actualFindAllResult.getError());
        PaginationResponse<DepartmentResponse> data = actualFindAllResult.getData();
        assertEquals(0, data.getPage());
        assertEquals(0, data.getSize());
        assertEquals(0L, data.getTotalElements());
        assertEquals(1, data.getTotalPages());
        assertEquals(200, actualFindAllResult.getCode().intValue());
        assertTrue(data.getContent().isEmpty());
        assertTrue(actualFindAllResult.isSuccess());
    }

    /**
     * Test {@link DepartmentServiceImpl#findAll(PaginationRequest)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findAll(PaginationRequest)}
     */
    @Test
    @DisplayName("Test findAll(PaginationRequest); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData DepartmentServiceImpl.findAll(PaginationRequest)"})
    void testFindAll_thenThrowNotFoundException() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findAll(Mockito.<Specification<Department>>any(), Mockito.<Pageable>any()))
                .thenThrow(new NotFoundException());
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(
                NotFoundException.class, () -> departmentServiceImpl.findAll(new PaginationRequest()));
        verify(repository).findAll(isA(Specification.class), isA(Pageable.class));
    }

    /**
     * Test {@link DepartmentServiceImpl#findDepartmentById(Long)}.
     *
     * <ul>
     *   <li>Given {@link DepartmentRepository} {@link DepartmentRepository#findById(Object)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findDepartmentById(Long)}
     */
    @Test
    @DisplayName(
            "Test findDepartmentById(Long); given DepartmentRepository findById(Object) return empty")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"Department DepartmentServiceImpl.findDepartmentById(Long)"})
    void testFindDepartmentById_givenDepartmentRepositoryFindByIdReturnEmpty() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        Optional<Department> emptyResult = Optional.empty();
        when(repository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.findDepartmentById(1L));
        verify(repository).findById(1L);
    }

    /**
     * Test {@link DepartmentServiceImpl#findDepartmentById(Long)}.
     *
     * <ul>
     *   <li>Given {@link DepartmentRepository} {@link DepartmentRepository#findById(Object)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findDepartmentById(Long)}
     */
    @Test
    @DisplayName(
            "Test findDepartmentById(Long); given DepartmentRepository findById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"Department DepartmentServiceImpl.findDepartmentById(Long)"})
    void testFindDepartmentById_givenDepartmentRepositoryFindByIdThrowNotFoundException() {
        // Arrange
        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException());
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.findDepartmentById(1L));
        verify(repository).findById(1L);
    }

    /**
     * Test {@link DepartmentServiceImpl#findDepartmentById(Long)}.
     *
     * <ul>
     *   <li>Then return {@link Department#Department()}.
     * </ul>
     *
     * <p>Method under test: {@link DepartmentServiceImpl#findDepartmentById(Long)}
     */
    @Test
    @DisplayName("Test findDepartmentById(Long); then return Department()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"Department DepartmentServiceImpl.findDepartmentById(Long)"})
    void testFindDepartmentById_thenReturnDepartment() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");
        Optional<Department> ofResult = Optional.of(department);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentServiceImpl =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        // Act
        Department actualFindDepartmentByIdResult = departmentServiceImpl.findDepartmentById(1L);

        // Assert
        verify(repository).findById(1L);
        assertSame(department, actualFindDepartmentByIdResult);
    }
}
