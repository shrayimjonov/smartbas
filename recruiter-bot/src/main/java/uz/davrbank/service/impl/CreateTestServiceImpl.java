package uz.davrbank.service.impl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.dto.request.CreateTestRequest;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.CreateTestResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.CreateTest;
import uz.davrbank.entity.Department;
import uz.davrbank.entity.QuestionText;
import uz.davrbank.enums.ResponseStatus;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.CreateTestMapper;
import uz.davrbank.repository.CreateTestRepository;
import uz.davrbank.repository.specification.CreateTestSpecification;
import uz.davrbank.service.CreateTestService;
import uz.davrbank.service.DepartmentService;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@Slf4j
public class CreateTestServiceImpl implements CreateTestService {
    private final CreateTestRepository createTestRepository;
    private final CreateTestMapper createTestMapper;
    private final DepartmentService departmentService;

    public CreateTestServiceImpl(CreateTestRepository createTestRepository, DepartmentService departmentService, CreateTestMapper createTestMapper) {
        this.createTestRepository = createTestRepository;
        this.departmentService = departmentService;
        this.createTestMapper = createTestMapper;
    }

    @Override
    @Transactional
    public ResultData<CreateTestResponse> createTest(@Valid CreateTestRequest dto) {
        Department department = departmentService.findDepartmentById(dto.getDepartmentId());
        CreateTest entity = createTestMapper.toEntity(dto);
        entity.setDepartment(department);
        CreateTest saved = createTestRepository.save(entity);
        return new ResultData<>(createTestMapper.toResponse(saved));
    }

    @Override
    @Transactional
    public ResultData<CreateTestResponse> updateTest(Long id, @Valid CreateTestRequest dto) {
        CreateTest existingTest = createTestRepository.findById(id).orElseThrow(() -> new NotFoundException("Test with ID " + id + " could not be found."));
        Department department = departmentService.findDepartmentById(dto.getDepartmentId());
        createTestMapper.updateEntity(dto, existingTest);
        existingTest.setDepartment(department);
        if (dto.getPhoto() != null) {
            existingTest.setPhoto(dto.getPhoto());
        }
        return new ResultData<>(createTestMapper.toResponse(existingTest));
    }

    @Override
    public ResultData<Page<CreateTestResponse>> getAllTest(Long departmentId, int page, int size) {
        Department department = departmentService.findDepartmentById(departmentId);
        Specification<CreateTest> specification = CreateTestSpecification.findByDepartment(department);
        Pageable pageable = PageRequest.of(page, size);
        Page<CreateTest> surveyFormsPage = createTestRepository.findAll(specification, pageable);
        return new ResultData<>(surveyFormsPage.map(createTestMapper::toResponse));
    }

    @Override
    @Transactional
    public ResultData<ResponseDto> deleteTestByDepartmentIdAndId(Long departmentId, Long id) {
        Specification<CreateTest> specification = CreateTestSpecification.deleteTestByDepartmentIdAndId(departmentId, id);
        CreateTest test = createTestRepository.findOne(specification).orElseThrow(() -> new NotFoundException("Test with ID " + id + " could not be found."));
        createTestRepository.delete(test);
        return new ResultData<>(new ResponseDto(200, "Successfully deleted test"));
    }

    @Override
    public ResultData<List<CreateTestResponse>> getAvailableTests(Long departmentId, String language) {
        boolean uz = "uz".equals(language);
        if (!language.equals("ru") && !uz) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Invalid language selected.");
        }
        Specification<CreateTest> specification = CreateTestSpecification.findByDepartmentAndLanguage(departmentId, language);
        List<CreateTest> createTests = createTestRepository.findAll(specification);
        return new ResultData<>(createTests.stream()
                .map(test -> {
                    Function<QuestionText, String> get = qt -> qt != null ? (uz ? qt.getUz() : qt.getRu()) : null;
                    BiConsumer<QuestionText, String> set = uz
                            ? QuestionText::setUz
                            : QuestionText::setRu;
                    QuestionText q = new QuestionText();
                    set.accept(q, get.apply(test.getTestQuestion()));
                    QuestionText a = new QuestionText();
                    set.accept(a, get.apply(test.getQuestionA()));
                    QuestionText b = new QuestionText();
                    set.accept(b, get.apply(test.getQuestionB()));
                    QuestionText c = new QuestionText();
                    set.accept(c, get.apply(test.getQuestionC()));
                    QuestionText d = new QuestionText();
                    set.accept(d, get.apply(test.getQuestionD()));
                    return CreateTestResponse.builder()
                            .id(test.getId())
                            .photo(test.getPhoto())
                            .departmentId(test.getDepartment().getId())
                            .testQuestion(q).questionA(a).questionB(b).questionC(c).questionD(d)
                            .correctAnswer(null)
                            .build();
                })
                .filter(r -> Stream.of(
                        uz ? r.getTestQuestion().getUz() : r.getTestQuestion().getRu(),
                        uz ? r.getQuestionA().getUz() : r.getQuestionA().getRu(),
                        uz ? r.getQuestionB().getUz() : r.getQuestionB().getRu(),
                        uz ? r.getQuestionC().getUz() : r.getQuestionC().getRu(),
                        uz ? r.getQuestionD().getUz() : r.getQuestionD().getRu()
                ).anyMatch(Objects::nonNull))
                .toList());
    }
}