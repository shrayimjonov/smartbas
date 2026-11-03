package uz.davrbank.service.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.dto.request.CreateTestRequest;
import uz.davrbank.dto.request.CreateTestRequest.CreateTestRequestBuilder;
import uz.davrbank.dto.request.QuestionTextDto;
import uz.davrbank.dto.response.CreateTestResponse;
import uz.davrbank.dto.response.CreateTestResponse.CreateTestResponseBuilder;
import uz.davrbank.dto.response.ResponseData.Error;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.CreateTest;
import uz.davrbank.entity.Department;
import uz.davrbank.entity.QuestionText;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.CreateTestMapper;
import uz.davrbank.mapper.CreateTestMapperImpl;
import uz.davrbank.mapper.DepartmentMapperImpl;
import uz.davrbank.repository.CreateTestRepository;
import uz.davrbank.repository.DepartmentRepository;

class CreateTestServiceImplDiffblueTest {
    /**
     * Test {@link CreateTestServiceImpl#createTest(CreateTestRequest)}.
     *
     * <ul>
     *   <li>Given {@link CreateTestMapper} {@link CreateTestMapper#toResponse(CreateTest)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#createTest(CreateTestRequest)}
     */

    CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
    @Test
    @DisplayName(
            "Test createTest(CreateTestRequest); given CreateTestMapper toResponse(CreateTest) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.createTest(CreateTestRequest)"})
    void testCreateTest_givenCreateTestMapperToResponseThrowNotFoundException()
            throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.save(Mockito.<CreateTest>any())).thenReturn(createTest);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(1L);
        department2.setName("Name");
        Optional<Department> ofResult = Optional.of(department2);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(),createTestRepository);

        Department department3 = new Department();
        department3.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department3.setId(1L);
        department3.setName("Name");

        QuestionText questionA2 = new QuestionText();
        questionA2.setRu("Ru");
        questionA2.setUz("Uz");

        QuestionText questionB2 = new QuestionText();
        questionB2.setRu("Ru");
        questionB2.setUz("Uz");

        QuestionText questionC2 = new QuestionText();
        questionC2.setRu("Ru");
        questionC2.setUz("Uz");

        QuestionText questionD2 = new QuestionText();
        questionD2.setRu("Ru");
        questionD2.setUz("Uz");

        QuestionText testQuestion2 = new QuestionText();
        testQuestion2.setRu("Ru");
        testQuestion2.setUz("Uz");

        CreateTest createTest2 = new CreateTest();
        createTest2.setCorrectAnswer("Correct Answer");
        createTest2.setDepartment(department3);
        createTest2.setId(1L);
        createTest2.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest2.setQuestionA(questionA2);
        createTest2.setQuestionB(questionB2);
        createTest2.setQuestionC(questionC2);
        createTest2.setQuestionD(questionD2);
        createTest2.setTestQuestion(testQuestion2);

        CreateTestMapper createTestMapper = mock(CreateTestMapper.class);
        when(createTestMapper.toResponse(Mockito.<CreateTest>any())).thenThrow(new NotFoundException());
        when(createTestMapper.toEntity(Mockito.<CreateTestRequest>any())).thenReturn(createTest2);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(createTestRepository, departmentService, createTestMapper);

        // Act and Assert
        assertThrows(
                NotFoundException.class, () -> createTestServiceImpl.createTest(new CreateTestRequest()));
        verify(repository).findById(isNull());
        verify(createTestRepository).save(isA(CreateTest.class));
        verify(createTestMapper).toEntity(isA(CreateTestRequest.class));
        verify(createTestMapper).toResponse(isA(CreateTest.class));
    }

    /**
     * Test {@link CreateTestServiceImpl#createTest(CreateTestRequest)}.
     *
     * <ul>
     *   <li>Given {@link CreateTestRepository} {@link CreateTestRepository#save(Object)} throw {@link
     *       NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#createTest(CreateTestRequest)}
     */
    @Test
    @DisplayName(
            "Test createTest(CreateTestRequest); given CreateTestRepository save(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.createTest(CreateTestRequest)"})
    void testCreateTest_givenCreateTestRepositorySaveThrowNotFoundException() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.save(Mockito.<CreateTest>any())).thenThrow(new NotFoundException());

        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");
        Optional<Department> ofResult = Optional.of(department);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        CreateTestRepository createTestRepository1 = mock(CreateTestRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(),createTestRepository1);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act and Assert
        assertThrows(
                NotFoundException.class, () -> createTestServiceImpl.createTest(new CreateTestRequest()));
        verify(repository).findById(isNull());
        verify(createTestRepository).save(isA(CreateTest.class));
    }

    /**
     * Test {@link CreateTestServiceImpl#createTest(CreateTestRequest)}.
     *
     * <ul>
     *   <li>Then return Data DepartmentId is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#createTest(CreateTestRequest)}
     */
    @Test
    @DisplayName("Test createTest(CreateTestRequest); then return Data DepartmentId is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.createTest(CreateTestRequest)"})
    void testCreateTest_thenReturnDataDepartmentIdIsNull() throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.save(Mockito.<CreateTest>any())).thenReturn(createTest);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(1L);
        department2.setName("Name");
        Optional<Department> ofResult = Optional.of(department2);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<CreateTestResponse> actualCreateTestResult =
                createTestServiceImpl.createTest(new CreateTestRequest());

        // Assert
        verify(repository).findById(isNull());
        verify(createTestRepository).save(isA(CreateTest.class));
        CreateTestResponse data = actualCreateTestResult.getData();
        assertEquals("Correct Answer", data.getCorrectAnswer());
        assertNull(data.getDepartmentId());
        assertNull(actualCreateTestResult.getError());
        assertEquals(1L, data.getId().longValue());
        assertEquals(200, actualCreateTestResult.getCode().intValue());
        assertTrue(actualCreateTestResult.isSuccess());
        assertSame(questionA, data.getQuestionA());
        assertSame(questionB, data.getQuestionB());
        assertSame(questionC, data.getQuestionC());
        assertSame(questionD, data.getQuestionD());
        assertSame(testQuestion, data.getTestQuestion());
        assertArrayEquals("AXAXAXAX".getBytes("UTF-8"), data.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#createTest(CreateTestRequest)}.
     *
     * <ul>
     *   <li>Then return Data DepartmentId longValue is one.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#createTest(CreateTestRequest)}
     */
    @Test
    @DisplayName("Test createTest(CreateTestRequest); then return Data DepartmentId longValue is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.createTest(CreateTestRequest)"})
    void testCreateTest_thenReturnDataDepartmentIdLongValueIsOne()
            throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.save(Mockito.<CreateTest>any())).thenReturn(createTest);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(1L);
        department2.setName("Name");
        Optional<Department> ofResult = Optional.of(department2);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        Department department3 = new Department();
        department3.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department3.setId(1L);
        department3.setName("Name");

        QuestionText questionA2 = new QuestionText();
        questionA2.setRu("Ru");
        questionA2.setUz("Uz");

        QuestionText questionB2 = new QuestionText();
        questionB2.setRu("Ru");
        questionB2.setUz("Uz");

        QuestionText questionC2 = new QuestionText();
        questionC2.setRu("Ru");
        questionC2.setUz("Uz");

        QuestionText questionD2 = new QuestionText();
        questionD2.setRu("Ru");
        questionD2.setUz("Uz");

        QuestionText testQuestion2 = new QuestionText();
        testQuestion2.setRu("Ru");
        testQuestion2.setUz("Uz");

        CreateTest createTest2 = new CreateTest();
        createTest2.setCorrectAnswer("Correct Answer");
        createTest2.setDepartment(department3);
        createTest2.setId(1L);
        createTest2.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest2.setQuestionA(questionA2);
        createTest2.setQuestionB(questionB2);
        createTest2.setQuestionC(questionC2);
        createTest2.setQuestionD(questionD2);
        createTest2.setTestQuestion(testQuestion2);

        QuestionText questionA3 = new QuestionText();
        questionA3.setRu("Ru");
        questionA3.setUz("Uz");

        CreateTestResponseBuilder questionAResult =
                CreateTestResponse.builder()
                        .correctAnswer("Correct Answer")
                        .departmentId(1L)
                        .id(1L)
                        .photo("AXAXAXAX".getBytes("UTF-8"))
                        .questionA(questionA3);

        QuestionText questionB3 = new QuestionText();
        questionB3.setRu("Ru");
        questionB3.setUz("Uz");

        CreateTestResponseBuilder questionBResult = questionAResult.questionB(questionB3);

        QuestionText questionC3 = new QuestionText();
        questionC3.setRu("Ru");
        questionC3.setUz("Uz");

        CreateTestResponseBuilder questionCResult = questionBResult.questionC(questionC3);

        QuestionText questionD3 = new QuestionText();
        questionD3.setRu("Ru");
        questionD3.setUz("Uz");

        CreateTestResponseBuilder questionDResult = questionCResult.questionD(questionD3);

        QuestionText testQuestion3 = new QuestionText();
        testQuestion3.setRu("Ru");
        testQuestion3.setUz("Uz");

        CreateTestMapper createTestMapper = mock(CreateTestMapper.class);
        when(createTestMapper.toResponse(Mockito.<CreateTest>any()))
                .thenReturn(questionDResult.testQuestion(testQuestion3).build());
        when(createTestMapper.toEntity(Mockito.<CreateTestRequest>any())).thenReturn(createTest2);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(createTestRepository, departmentService, createTestMapper);

        // Act
        ResultData<CreateTestResponse> actualCreateTestResult =
                createTestServiceImpl.createTest(new CreateTestRequest());

        // Assert
        verify(repository).findById(isNull());
        verify(createTestRepository).save(isA(CreateTest.class));
        verify(createTestMapper).toEntity(isA(CreateTestRequest.class));
        verify(createTestMapper).toResponse(isA(CreateTest.class));
        CreateTestResponse data = actualCreateTestResult.getData();
        assertEquals("Correct Answer", data.getCorrectAnswer());
        assertNull(actualCreateTestResult.getError());
        assertEquals(1L, data.getDepartmentId().longValue());
        assertEquals(1L, data.getId().longValue());
        assertEquals(200, actualCreateTestResult.getCode().intValue());
        assertTrue(actualCreateTestResult.isSuccess());
        assertSame(questionA3, data.getQuestionA());
        assertSame(questionB3, data.getQuestionB());
        assertSame(questionC3, data.getQuestionC());
        assertSame(questionD3, data.getQuestionD());
        assertSame(testQuestion3, data.getTestQuestion());
        assertArrayEquals("AXAXAXAX".getBytes("UTF-8"), data.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}.
     *
     * <ul>
     *   <li>Given {@link CreateTestRepository} {@link CreateTestRepository#findById(Object)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}
     */
    @Test
    @DisplayName(
            "Test updateTest(Long, CreateTestRequest); given CreateTestRepository findById(Object) return empty")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.updateTest(Long, CreateTestRequest)"})
    void testUpdateTest_givenCreateTestRepositoryFindByIdReturnEmpty() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        Optional<CreateTest> emptyResult = Optional.empty();
        when(createTestRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, mock(CreateTestMapper.class));

        // Act and Assert
        assertThrows(
                NotFoundException.class,
                () -> createTestServiceImpl.updateTest(1L, new CreateTestRequest()));
        verify(createTestRepository).findById(1L);
    }

    /**
     * Test {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}.
     *
     * <ul>
     *   <li>Given {@link CreateTestRepository} {@link CreateTestRepository#findById(Object)} throw
     *       {@link NotFoundException#NotFoundException()}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}
     */
    @Test
    @DisplayName(
            "Test updateTest(Long, CreateTestRequest); given CreateTestRepository findById(Object) throw NotFoundException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.updateTest(Long, CreateTestRequest)"})
    void testUpdateTest_givenCreateTestRepositoryFindByIdThrowNotFoundException() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException());
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act and Assert
        assertThrows(
                NotFoundException.class,
                () -> createTestServiceImpl.updateTest(1L, new CreateTestRequest()));
        verify(createTestRepository).findById(1L);
    }

    /**
     * Test {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}.
     *
     * <ul>
     *   <li>Then return Data DepartmentId is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}
     */
    @Test
    @DisplayName("Test updateTest(Long, CreateTestRequest); then return Data DepartmentId is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.updateTest(Long, CreateTestRequest)"})
    void testUpdateTest_thenReturnDataDepartmentIdIsNull() throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);
        Optional<CreateTest> ofResult = Optional.of(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(1L);
        department2.setName("Name");
        Optional<Department> ofResult2 = Optional.of(department2);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        CreateTestRequestBuilder photoResult =
                CreateTestRequest.builder()
                        .correctAnswer("Correct Answer")
                        .departmentId(1L)
                        .photo("AXAXAXAX".getBytes("UTF-8"));

        CreateTestRequestBuilder questionAResult =
                photoResult.questionA(QuestionTextDto.builder().ru("Ru").uz("Uz").build());

        CreateTestRequestBuilder questionBResult =
                questionAResult.questionB(QuestionTextDto.builder().ru("Ru").uz("Uz").build());

        CreateTestRequestBuilder questionCResult =
                questionBResult.questionC(QuestionTextDto.builder().ru("Ru").uz("Uz").build());

        CreateTestRequestBuilder questionDResult =
                questionCResult.questionD(QuestionTextDto.builder().ru("Ru").uz("Uz").build());

        // Act
        ResultData<CreateTestResponse> actualUpdateTestResult =
                createTestServiceImpl.updateTest(
                        1L,
                        questionDResult
                                .testQuestion(QuestionTextDto.builder().ru("Ru").uz("Uz").build())
                                .build());

        // Assert
        verify(createTestRepository).findById(1L);
        verify(repository).findById(1L);
        CreateTestResponse data = actualUpdateTestResult.getData();
        assertEquals("Correct Answer", data.getCorrectAnswer());
        assertNull(data.getDepartmentId());
        assertSame(questionA, data.getQuestionA());
        assertSame(questionB, data.getQuestionB());
        assertSame(questionC, data.getQuestionC());
        assertSame(questionD, data.getQuestionD());
        assertSame(testQuestion, data.getTestQuestion());
        assertArrayEquals("AXAXAXAX".getBytes("UTF-8"), data.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}.
     *
     * <ul>
     *   <li>Then return Data DepartmentId longValue is one.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}
     */
    @Test
    @DisplayName(
            "Test updateTest(Long, CreateTestRequest); then return Data DepartmentId longValue is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.updateTest(Long, CreateTestRequest)"})
    void testUpdateTest_thenReturnDataDepartmentIdLongValueIsOne()
            throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);
        Optional<CreateTest> ofResult = Optional.of(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(1L);
        department2.setName("Name");
        Optional<Department> ofResult2 = Optional.of(department2);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        QuestionText questionA2 = new QuestionText();
        questionA2.setRu("Ru");
        questionA2.setUz("Uz");

        CreateTestResponseBuilder questionAResult =
                CreateTestResponse.builder()
                        .correctAnswer("Correct Answer")
                        .departmentId(1L)
                        .id(1L)
                        .photo("AXAXAXAX".getBytes("UTF-8"))
                        .questionA(questionA2);

        QuestionText questionB2 = new QuestionText();
        questionB2.setRu("Ru");
        questionB2.setUz("Uz");

        CreateTestResponseBuilder questionBResult = questionAResult.questionB(questionB2);

        QuestionText questionC2 = new QuestionText();
        questionC2.setRu("Ru");
        questionC2.setUz("Uz");

        CreateTestResponseBuilder questionCResult = questionBResult.questionC(questionC2);

        QuestionText questionD2 = new QuestionText();
        questionD2.setRu("Ru");
        questionD2.setUz("Uz");

        CreateTestResponseBuilder questionDResult = questionCResult.questionD(questionD2);

        QuestionText testQuestion2 = new QuestionText();
        testQuestion2.setRu("Ru");
        testQuestion2.setUz("Uz");

        CreateTestMapper createTestMapper = mock(CreateTestMapper.class);
        when(createTestMapper.toResponse(Mockito.<CreateTest>any()))
                .thenReturn(questionDResult.testQuestion(testQuestion2).build());
        doNothing()
                .when(createTestMapper)
                .updateEntity(Mockito.<CreateTestRequest>any(), Mockito.<CreateTest>any());

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(createTestRepository, departmentService, createTestMapper);

        // Act
        ResultData<CreateTestResponse> actualUpdateTestResult =
                createTestServiceImpl.updateTest(1L, new CreateTestRequest());

        // Assert
        verify(repository).findById(isNull());
        verify(createTestRepository).findById(1L);
        verify(createTestMapper).toResponse(isA(CreateTest.class));
        verify(createTestMapper).updateEntity(isA(CreateTestRequest.class), isA(CreateTest.class));
        CreateTestResponse data = actualUpdateTestResult.getData();
        assertEquals("Correct Answer", data.getCorrectAnswer());
        assertEquals(1L, data.getDepartmentId().longValue());
        assertSame(questionA2, data.getQuestionA());
        assertSame(questionB2, data.getQuestionB());
        assertSame(questionC2, data.getQuestionC());
        assertSame(questionD2, data.getQuestionD());
        assertSame(testQuestion2, data.getTestQuestion());
        assertArrayEquals("AXAXAXAX".getBytes("UTF-8"), data.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}.
     *
     * <ul>
     *   <li>Then return Data Photo is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#updateTest(Long, CreateTestRequest)}
     */
    @Test
    @DisplayName("Test updateTest(Long, CreateTestRequest); then return Data Photo is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.updateTest(Long, CreateTestRequest)"})
    void testUpdateTest_thenReturnDataPhotoIsNull() throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);
        Optional<CreateTest> ofResult = Optional.of(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(1L);
        department2.setName("Name");
        Optional<Department> ofResult2 = Optional.of(department2);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<CreateTestResponse> actualUpdateTestResult =
                createTestServiceImpl.updateTest(1L, new CreateTestRequest());

        // Assert
        verify(repository).findById(isNull());
        verify(createTestRepository).findById(1L);
        CreateTestResponse data = actualUpdateTestResult.getData();
        assertNull(data.getPhoto());
        assertNull(data.getCorrectAnswer());
        assertNull(data.getQuestionA());
        assertNull(data.getQuestionB());
        assertNull(data.getQuestionC());
        assertNull(data.getQuestionD());
        assertNull(data.getTestQuestion());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAllTest(Long, int, int)}.
     *
     * <ul>
     *   <li>Given {@link Department#Department()} Id is two.
     *   <li>When three.
     *   <li>Then return Data toList size is two.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAllTest(Long, int, int)}
     */
    @Test
    @DisplayName(
            "Test getAllTest(Long, int, int); given Department() Id is two; when three; then return Data toList size is two")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAllTest(Long, int, int)"})
    void testGetAllTest_givenDepartmentIdIsTwo_whenThree_thenReturnDataToListSizeIsTwo() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(2L);
        department2.setName("42");

        QuestionText questionA2 = new QuestionText();
        questionA2.setRu("42");
        questionA2.setUz("42");

        QuestionText questionB2 = new QuestionText();
        questionB2.setRu("42");
        questionB2.setUz("42");

        QuestionText questionC2 = new QuestionText();
        questionC2.setRu("42");
        questionC2.setUz("42");

        QuestionText questionD2 = new QuestionText();
        questionD2.setRu("42");
        questionD2.setUz("42");

        QuestionText testQuestion2 = new QuestionText();
        testQuestion2.setRu("42");
        testQuestion2.setUz("42");

        CreateTest createTest2 = new CreateTest();
        createTest2.setCorrectAnswer("42");
        createTest2.setDepartment(department2);
        createTest2.setId(2L);
        createTest2.setPhoto(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        createTest2.setQuestionA(questionA2);
        createTest2.setQuestionB(questionB2);
        createTest2.setQuestionC(questionC2);
        createTest2.setQuestionD(questionD2);
        createTest2.setTestQuestion(testQuestion2);

        ArrayList<CreateTest> content = new ArrayList<>();
        content.add(createTest2);
        content.add(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(
                Mockito.<Specification<CreateTest>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(content));

        Department department3 = new Department();
        department3.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department3.setId(1L);
        department3.setName("Name");
        Optional<Department> ofResult = Optional.of(department3);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<Page<CreateTestResponse>> actualAllTest = createTestServiceImpl.getAllTest(1L, 1, 3);

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class), isA(Pageable.class));
        verify(repository).findById(1L);
        Page<CreateTestResponse> data = actualAllTest.getData();
        assertTrue(data instanceof PageImpl);
        List<CreateTestResponse> toListResult = data.toList();
        assertEquals(2, toListResult.size());
        CreateTestResponse getResult = toListResult.get(0);
        assertEquals("42", getResult.getCorrectAnswer());
        CreateTestResponse getResult2 = toListResult.get(1);
        assertEquals("Correct Answer", getResult2.getCorrectAnswer());
        assertNull(getResult2.getDepartmentId());
        assertEquals(1L, getResult2.getId().longValue());
        assertEquals(2L, getResult.getId().longValue());
        assertSame(questionA, getResult2.getQuestionA());
        assertSame(questionB, getResult2.getQuestionB());
        assertSame(questionC, getResult2.getQuestionC());
        assertSame(questionD, getResult2.getQuestionD());
        assertSame(testQuestion, getResult2.getTestQuestion());
        assertArrayEquals(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1}, getResult.getPhoto());
        assertArrayEquals(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1}, getResult2.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAllTest(Long, int, int)}.
     *
     * <ul>
     *   <li>Given {@link QuestionText#QuestionText()} Ru is {@code Ru}.
     *   <li>When three.
     *   <li>Then return Data toList size is one.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAllTest(Long, int, int)}
     */
    @Test
    @DisplayName(
            "Test getAllTest(Long, int, int); given QuestionText() Ru is 'Ru'; when three; then return Data toList size is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAllTest(Long, int, int)"})
    void testGetAllTest_givenQuestionTextRuIsRu_whenThree_thenReturnDataToListSizeIsOne() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        ArrayList<CreateTest> content = new ArrayList<>();
        content.add(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(
                Mockito.<Specification<CreateTest>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(content));

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(1L);
        department2.setName("Name");
        Optional<Department> ofResult = Optional.of(department2);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<Page<CreateTestResponse>> actualAllTest = createTestServiceImpl.getAllTest(1L, 1, 3);

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class), isA(Pageable.class));
        verify(repository).findById(1L);
        Page<CreateTestResponse> data = actualAllTest.getData();
        assertTrue(data instanceof PageImpl);
        List<CreateTestResponse> toListResult = data.toList();
        assertEquals(1, toListResult.size());
        CreateTestResponse getResult = toListResult.get(0);
        assertEquals("Correct Answer", getResult.getCorrectAnswer());
        QuestionText questionA2 = getResult.getQuestionA();
        assertEquals("Ru", questionA2.getRu());
        QuestionText questionB2 = getResult.getQuestionB();
        assertEquals("Ru", questionB2.getRu());
        QuestionText questionC2 = getResult.getQuestionC();
        assertEquals("Ru", questionC2.getRu());
        QuestionText questionD2 = getResult.getQuestionD();
        assertEquals("Ru", questionD2.getRu());
        QuestionText testQuestion2 = getResult.getTestQuestion();
        assertEquals("Ru", testQuestion2.getRu());
        assertEquals("Uz", questionA2.getUz());
        assertEquals("Uz", questionB2.getUz());
        assertEquals("Uz", questionC2.getUz());
        assertEquals("Uz", questionD2.getUz());
        assertEquals("Uz", testQuestion2.getUz());
        assertEquals(1L, getResult.getId().longValue());
        assertArrayEquals(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1}, getResult.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAllTest(Long, int, int)}.
     *
     * <ul>
     *   <li>Then return Error is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAllTest(Long, int, int)}
     */
    @Test
    @DisplayName("Test getAllTest(Long, int, int); then return Error is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAllTest(Long, int, int)"})
    void testGetAllTest_thenReturnErrorIsNull() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(
                Mockito.<Specification<CreateTest>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");
        Optional<Department> ofResult = Optional.of(department);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<Page<CreateTestResponse>> actualAllTest = createTestServiceImpl.getAllTest(1L, 1, 3);

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class), isA(Pageable.class));
        verify(repository).findById(1L);
        Page<CreateTestResponse> data = actualAllTest.getData();
        assertTrue(data instanceof PageImpl);
        assertNull(actualAllTest.getError());
        assertEquals(200, actualAllTest.getCode().intValue());
        assertTrue(data.toList().isEmpty());
        assertTrue(actualAllTest.isSuccess());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAllTest(Long, int, int)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAllTest(Long, int, int)}
     */
    @Test
    @DisplayName("Test getAllTest(Long, int, int); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAllTest(Long, int, int)"})
    void testGetAllTest_thenThrowNotFoundException() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(
                Mockito.<Specification<CreateTest>>any(), Mockito.<Pageable>any()))
                .thenThrow(new NotFoundException());

        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");
        Optional<Department> ofResult = Optional.of(department);

        DepartmentRepository repository = mock(DepartmentRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> createTestServiceImpl.getAllTest(1L, 1, 3));
        verify(createTestRepository).findAll(isA(Specification.class), isA(Pageable.class));
        verify(repository).findById(1L);
    }

    /**
     * Test {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}.
     *
     * <p>Method under test: {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}
     */
    @Test
    @DisplayName("Test deleteTestByDepartmentIdAndId(Long, Long)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.deleteTestByDepartmentIdAndId(Long, Long)"})
    void testDeleteTestByDepartmentIdAndId() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findOne(Mockito.<Specification<CreateTest>>any()))
                .thenThrow(new NotFoundException());
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act and Assert
        assertThrows(
                NotFoundException.class, () -> createTestServiceImpl.deleteTestByDepartmentIdAndId(1L, 1L));
        verify(createTestRepository).findOne(isA(Specification.class));
    }

    /**
     * Test {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}.
     *
     * <p>Method under test: {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}
     */
    @Test
    @DisplayName("Test deleteTestByDepartmentIdAndId(Long, Long)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.deleteTestByDepartmentIdAndId(Long, Long)"})
    void testDeleteTestByDepartmentIdAndId2() throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);
        Optional<CreateTest> ofResult = Optional.of(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        doThrow(new NotFoundException()).when(createTestRepository).delete(Mockito.<CreateTest>any());
        when(createTestRepository.findOne(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(ofResult);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act and Assert
        assertThrows(
                NotFoundException.class, () -> createTestServiceImpl.deleteTestByDepartmentIdAndId(1L, 1L));
        verify(createTestRepository).findOne(isA(Specification.class));
        verify(createTestRepository).delete(isA(CreateTest.class));
    }

    /**
     * Test {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}.
     *
     * <ul>
     *   <li>Given {@link CreateTestRepository} {@link CreateTestRepository#findOne(Specification)}
     *       return empty.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}
     */
    @Test
    @DisplayName(
            "Test deleteTestByDepartmentIdAndId(Long, Long); given CreateTestRepository findOne(Specification) return empty")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.deleteTestByDepartmentIdAndId(Long, Long)"})
    void testDeleteTestByDepartmentIdAndId_givenCreateTestRepositoryFindOneReturnEmpty() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        Optional<CreateTest> emptyResult = Optional.empty();
        when(createTestRepository.findOne(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(emptyResult);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act and Assert
        assertThrows(
                NotFoundException.class, () -> createTestServiceImpl.deleteTestByDepartmentIdAndId(1L, 1L));
        verify(createTestRepository).findOne(isA(Specification.class));
    }

    /**
     * Test {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}.
     *
     * <ul>
     *   <li>Then return Data Message is {@code Successfully deleted test}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#deleteTestByDepartmentIdAndId(Long, Long)}
     */
    @Test
    @DisplayName(
            "Test deleteTestByDepartmentIdAndId(Long, Long); then return Data Message is 'Successfully deleted test'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.deleteTestByDepartmentIdAndId(Long, Long)"})
    void testDeleteTestByDepartmentIdAndId_thenReturnDataMessageIsSuccessfullyDeletedTest()
            throws UnsupportedEncodingException {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("Name");

        QuestionText questionA = new QuestionText();
        questionA.setRu("Ru");
        questionA.setUz("Uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("Ru");
        questionB.setUz("Uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("Ru");
        questionC.setUz("Uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("Ru");
        questionD.setUz("Uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("Ru");
        testQuestion.setUz("Uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("Correct Answer");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);
        Optional<CreateTest> ofResult = Optional.of(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        doNothing().when(createTestRepository).delete(Mockito.<CreateTest>any());
        when(createTestRepository.findOne(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(ofResult);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<ResponseDto> actualDeleteTestByDepartmentIdAndIdResult =
                createTestServiceImpl.deleteTestByDepartmentIdAndId(1L, 1L);

        // Assert
        verify(createTestRepository).findOne(isA(Specification.class));
        verify(createTestRepository).delete(isA(CreateTest.class));
        ResponseDto data = actualDeleteTestByDepartmentIdAndIdResult.getData();
        assertEquals("Successfully deleted test", data.getMessage());
        assertNull(actualDeleteTestByDepartmentIdAndIdResult.getError());
        assertEquals(200, actualDeleteTestByDepartmentIdAndIdResult.getCode().intValue());
        assertEquals(200, data.getCode().intValue());
        assertTrue(actualDeleteTestByDepartmentIdAndIdResult.isSuccess());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAvailableTests(Long, String)}.
     *
     * <ul>
     *   <li>Given {@link Department#Department()} Id is two.
     *   <li>When {@code uz}.
     *   <li>Then return Data size is two.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAvailableTests(Long, String)}
     */
    @Test
    @DisplayName(
            "Test getAvailableTests(Long, String); given Department() Id is two; when 'uz'; then return Data size is two")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAvailableTests(Long, String)"})
    void testGetAvailableTests_givenDepartmentIdIsTwo_whenUz_thenReturnDataSizeIsTwo() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("uz");

        QuestionText questionA = new QuestionText();
        questionA.setRu("uz");
        questionA.setUz("uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("uz");
        questionB.setUz("uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("uz");
        questionC.setUz("uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("uz");
        questionD.setUz("uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("uz");
        testQuestion.setUz("uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("uz");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        Department department2 = new Department();
        department2.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department2.setId(2L);
        department2.setName("ru");

        QuestionText questionA2 = new QuestionText();
        questionA2.setRu("ru");
        questionA2.setUz("ru");

        QuestionText questionB2 = new QuestionText();
        questionB2.setRu("ru");
        questionB2.setUz("ru");

        QuestionText questionC2 = new QuestionText();
        questionC2.setRu("ru");
        questionC2.setUz("ru");

        QuestionText questionD2 = new QuestionText();
        questionD2.setRu("ru");
        questionD2.setUz("ru");

        QuestionText testQuestion2 = new QuestionText();
        testQuestion2.setRu("ru");
        testQuestion2.setUz("ru");

        CreateTest createTest2 = new CreateTest();
        createTest2.setCorrectAnswer("ru");
        createTest2.setDepartment(department2);
        createTest2.setId(2L);
        createTest2.setPhoto(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        createTest2.setQuestionA(questionA2);
        createTest2.setQuestionB(questionB2);
        createTest2.setQuestionC(questionC2);
        createTest2.setQuestionD(questionD2);
        createTest2.setTestQuestion(testQuestion2);

        ArrayList<CreateTest> createTestList = new ArrayList<>();
        createTestList.add(createTest2);
        createTestList.add(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(createTestList);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<List<CreateTestResponse>> actualAvailableTests =
                createTestServiceImpl.getAvailableTests(1L, "uz");

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class));
        List<CreateTestResponse> data = actualAvailableTests.getData();
        assertEquals(2, data.size());
        CreateTestResponse getResult = data.get(1);
        assertNull(getResult.getCorrectAnswer());
        assertEquals(1L, getResult.getDepartmentId().longValue());
        assertEquals(1L, getResult.getId().longValue());
        CreateTestResponse getResult2 = data.get(0);
        assertEquals(2L, getResult2.getDepartmentId().longValue());
        assertEquals(2L, getResult2.getId().longValue());
        assertArrayEquals(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1}, getResult2.getPhoto());
        assertArrayEquals(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1}, getResult.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAvailableTests(Long, String)}.
     *
     * <ul>
     *   <li>Then return Data first QuestionA Uz is {@code uz}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAvailableTests(Long, String)}
     */
    @Test
    @DisplayName("Test getAvailableTests(Long, String); then return Data first QuestionA Uz is 'uz'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAvailableTests(Long, String)"})
    void testGetAvailableTests_thenReturnDataFirstQuestionAUzIsUz() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("uz");

        QuestionText questionA = new QuestionText();
        questionA.setRu("uz");
        questionA.setUz("uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("uz");
        questionB.setUz("uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("uz");
        questionC.setUz("uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("uz");
        questionD.setUz("uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("uz");
        testQuestion.setUz("uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("uz");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        ArrayList<CreateTest> createTestList = new ArrayList<>();
        createTestList.add(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(createTestList);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<List<CreateTestResponse>> actualAvailableTests =
                createTestServiceImpl.getAvailableTests(1L, "uz");

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class));
        List<CreateTestResponse> data = actualAvailableTests.getData();
        assertEquals(1, data.size());
        CreateTestResponse getResult = data.get(0);
        assertEquals("uz", getResult.getQuestionA().getUz());
        assertEquals("uz", getResult.getQuestionB().getUz());
        assertEquals("uz", getResult.getQuestionC().getUz());
        assertEquals("uz", getResult.getQuestionD().getUz());
        assertEquals("uz", getResult.getTestQuestion().getUz());
        assertArrayEquals(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1}, getResult.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAvailableTests(Long, String)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAvailableTests(Long, String)}
     */
    @Test
    @DisplayName("Test getAvailableTests(Long, String); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAvailableTests(Long, String)"})
    void testGetAvailableTests_thenThrowNotFoundException() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(Mockito.<Specification<CreateTest>>any()))
                .thenThrow(new NotFoundException());
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> createTestServiceImpl.getAvailableTests(1L, "uz"));
        verify(createTestRepository).findAll(isA(Specification.class));
    }

    /**
     * Test {@link CreateTestServiceImpl#getAvailableTests(Long, String)}.
     *
     * <ul>
     *   <li>When {@code en}.
     *   <li>Then return Error Error is {@code Invalid language selected.}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAvailableTests(Long, String)}
     */
    @Test
    @DisplayName(
            "Test getAvailableTests(Long, String); when 'en'; then return Error Error is 'Invalid language selected.'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAvailableTests(Long, String)"})
    void testGetAvailableTests_whenEn_thenReturnErrorErrorIsInvalidLanguageSelected() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<List<CreateTestResponse>> actualAvailableTests =
                createTestServiceImpl.getAvailableTests(1L, "en");

        // Assert
        Error error = actualAvailableTests.getError();
        assertEquals("Invalid language selected.", error.getError());
        assertNull(actualAvailableTests.getData());
        assertEquals(400, actualAvailableTests.getCode().intValue());
        assertEquals(400, error.getCode().intValue());
        assertFalse(actualAvailableTests.isSuccess());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAvailableTests(Long, String)}.
     *
     * <ul>
     *   <li>When {@code ru}.
     *   <li>Then return Data first QuestionA Ru is {@code uz}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAvailableTests(Long, String)}
     */
    @Test
    @DisplayName(
            "Test getAvailableTests(Long, String); when 'ru'; then return Data first QuestionA Ru is 'uz'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAvailableTests(Long, String)"})
    void testGetAvailableTests_whenRu_thenReturnDataFirstQuestionARuIsUz() {
        // Arrange
        Department department = new Department();
        department.setCreated_at(LocalDate.of(1970, 1, 1).atStartOfDay());
        department.setId(1L);
        department.setName("uz");

        QuestionText questionA = new QuestionText();
        questionA.setRu("uz");
        questionA.setUz("uz");

        QuestionText questionB = new QuestionText();
        questionB.setRu("uz");
        questionB.setUz("uz");

        QuestionText questionC = new QuestionText();
        questionC.setRu("uz");
        questionC.setUz("uz");

        QuestionText questionD = new QuestionText();
        questionD.setRu("uz");
        questionD.setUz("uz");

        QuestionText testQuestion = new QuestionText();
        testQuestion.setRu("uz");
        testQuestion.setUz("uz");

        CreateTest createTest = new CreateTest();
        createTest.setCorrectAnswer("uz");
        createTest.setDepartment(department);
        createTest.setId(1L);
        createTest.setPhoto(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        createTest.setQuestionA(questionA);
        createTest.setQuestionB(questionB);
        createTest.setQuestionC(questionC);
        createTest.setQuestionD(questionD);
        createTest.setTestQuestion(testQuestion);

        ArrayList<CreateTest> createTestList = new ArrayList<>();
        createTestList.add(createTest);

        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(createTestList);
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<List<CreateTestResponse>> actualAvailableTests =
                createTestServiceImpl.getAvailableTests(1L, "ru");

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class));
        List<CreateTestResponse> data = actualAvailableTests.getData();
        assertEquals(1, data.size());
        CreateTestResponse getResult = data.get(0);
        QuestionText questionA2 = getResult.getQuestionA();
        assertEquals("uz", questionA2.getRu());
        QuestionText questionB2 = getResult.getQuestionB();
        assertEquals("uz", questionB2.getRu());
        QuestionText questionC2 = getResult.getQuestionC();
        assertEquals("uz", questionC2.getRu());
        QuestionText questionD2 = getResult.getQuestionD();
        assertEquals("uz", questionD2.getRu());
        QuestionText testQuestion2 = getResult.getTestQuestion();
        assertEquals("uz", testQuestion2.getRu());
        assertNull(questionA2.getUz());
        assertNull(questionB2.getUz());
        assertNull(questionC2.getUz());
        assertNull(questionD2.getUz());
        assertNull(testQuestion2.getUz());
        assertArrayEquals(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1}, getResult.getPhoto());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAvailableTests(Long, String)}.
     *
     * <ul>
     *   <li>When {@code ru}.
     *   <li>Then return Error is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAvailableTests(Long, String)}
     */
    @Test
    @DisplayName("Test getAvailableTests(Long, String); when 'ru'; then return Error is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAvailableTests(Long, String)"})
    void testGetAvailableTests_whenRu_thenReturnErrorIsNull() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(new ArrayList<>());
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<List<CreateTestResponse>> actualAvailableTests =
                createTestServiceImpl.getAvailableTests(1L, "ru");

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class));
        assertNull(actualAvailableTests.getError());
        assertEquals(200, actualAvailableTests.getCode().intValue());
        assertTrue(actualAvailableTests.getData().isEmpty());
        assertTrue(actualAvailableTests.isSuccess());
    }

    /**
     * Test {@link CreateTestServiceImpl#getAvailableTests(Long, String)}.
     *
     * <ul>
     *   <li>When {@code uz}.
     *   <li>Then return Error is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link CreateTestServiceImpl#getAvailableTests(Long, String)}
     */
    @Test
    @DisplayName("Test getAvailableTests(Long, String); when 'uz'; then return Error is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData CreateTestServiceImpl.getAvailableTests(Long, String)"})
    void testGetAvailableTests_whenUz_thenReturnErrorIsNull() {
        // Arrange
        CreateTestRepository createTestRepository = mock(CreateTestRepository.class);
        when(createTestRepository.findAll(Mockito.<Specification<CreateTest>>any()))
                .thenReturn(new ArrayList<>());
        DepartmentRepository repository = mock(DepartmentRepository.class);
        DepartmentServiceImpl departmentService =
                new DepartmentServiceImpl(repository, new DepartmentMapperImpl(), createTestRepository);

        CreateTestServiceImpl createTestServiceImpl =
                new CreateTestServiceImpl(
                        createTestRepository, departmentService, new CreateTestMapperImpl());

        // Act
        ResultData<List<CreateTestResponse>> actualAvailableTests =
                createTestServiceImpl.getAvailableTests(1L, "uz");

        // Assert
        verify(createTestRepository).findAll(isA(Specification.class));
        assertNull(actualAvailableTests.getError());
        assertEquals(200, actualAvailableTests.getCode().intValue());
        assertTrue(actualAvailableTests.getData().isEmpty());
        assertTrue(actualAvailableTests.isSuccess());
    }
}
