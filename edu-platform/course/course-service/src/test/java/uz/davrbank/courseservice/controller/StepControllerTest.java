package uz.davrbank.courseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.dto.step.FileUploadDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepDTO;
import uz.davrbank.courseservice.aggregation.exception.condition.BadRequestException;
import uz.davrbank.courseservice.aggregation.exception.condition.ConnectionException;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.exception.condition.ThirdPartyException;
import uz.davrbank.courseservice.aggregation.exception.controller.ControllerAdvisor;
import uz.davrbank.courseservice.service.dao.step.StepService;
import uz.davrbank.courseservice.utils.ApiConstants;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StepController.class)
@Import(ControllerAdvisor.class)
class StepControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StepService stepService;

    @BeforeEach
    void setUp() {
        reset(stepService);
    }

    @Test
    void createStep_success() throws Exception {
        given(stepService.create(any())).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(post(ApiConstants.stepRootApi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStep())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void createStep_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepService.create(any())).willThrow(exception);

        mockMvc.perform(post(ApiConstants.stepRootApi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStep())))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void updateStep_success() throws Exception {
        given(stepService.update(any(), anyLong())).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(put(ApiConstants.stepRootApi + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStep())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void updateStep_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepService.update(any(), anyLong())).willThrow(exception);

        mockMvc.perform(put(ApiConstants.stepRootApi + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStep())))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getAllByCourseId_success() throws Exception {
        given(stepService.getAllByCourseId(1L)).willReturn(List.of(sampleStep()));

        mockMvc.perform(get(ApiConstants.stepRootApi + ApiConstants.list)
                        .param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getAllByCourseId_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepService.getAllByCourseId(1L)).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepRootApi + ApiConstants.list)
                        .param("courseId", "1"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void deleteStep_success() throws Exception {
        given(stepService.delete(1L)).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(delete(ApiConstants.stepRootApi + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void deleteStep_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepService.delete(1L)).willThrow(exception);

        mockMvc.perform(delete(ApiConstants.stepRootApi + "/{id}", 1L))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getStep_success() throws Exception {
        given(stepService.get(1L)).willReturn(sampleStep());

        mockMvc.perform(get(ApiConstants.stepRootApi + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getStep_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepService.get(1L)).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepRootApi + "/{id}", 1L))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getAllFiles_success() throws Exception {
        given(stepService.getAllFiles(1L)).willReturn(List.of(sampleFile()));

        mockMvc.perform(get(ApiConstants.stepRootApi + ApiConstants.files)
                        .param("stepId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getAllFiles_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepService.getAllFiles(1L)).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepRootApi + ApiConstants.files)
                        .param("stepId", "1"))
                .andExpect(status().is(expectedStatus));
    }

    private StepDTO sampleStep() {
        return new StepDTO(1L, "Step", 10L);
    }

    private FileUploadDTO sampleFile() {
        return new FileUploadDTO(1L, "file", "txt", "1kb", "text/plain", 1L, "desc", 10L, 0.0);
    }

    private static Stream<Arguments> errorScenarios() {
        return Stream.of(
                Arguments.of(new BadRequestException("bad request"), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new DataNotFoundException("not found"), HttpStatus.CONFLICT.value()),
                Arguments.of(new ConnectionException("connection"), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                Arguments.of(new ThirdPartyException("third party"), HttpStatus.NOT_FOUND.value())
        );
    }
}
