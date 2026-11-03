package uz.davrbank.courseservice.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.step.AttachStepResponseDTO;
import uz.davrbank.courseservice.aggregation.exception.condition.BadRequestException;
import uz.davrbank.courseservice.aggregation.exception.condition.ConnectionException;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.exception.condition.ThirdPartyException;
import uz.davrbank.courseservice.aggregation.exception.controller.ControllerAdvisor;
import uz.davrbank.courseservice.service.dao.step.StepEmployeeService;
import uz.davrbank.courseservice.utils.ApiConstants;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StepEmployeeController.class)
@Import(ControllerAdvisor.class)
class StepEmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StepEmployeeService stepEmployeeService;

    @BeforeEach
    void setUp() {
        reset(stepEmployeeService);
    }

    @Test
    void startStep_success() throws Exception {
        doNothing().when(stepEmployeeService).startedStep(1L, "e1");

        mockMvc.perform(post(ApiConstants.stepEmployeeRootApi + ApiConstants.start)
                        .param("stepId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("The step has started."));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void startStep_error(RuntimeException exception, int expectedStatus) throws Exception {
        doThrow(exception).when(stepEmployeeService).startedStep(anyLong(), anyString());

        mockMvc.perform(post(ApiConstants.stepEmployeeRootApi + ApiConstants.start)
                        .param("stepId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void completeStep_success() throws Exception {
        doNothing().when(stepEmployeeService).completedStep(1L, "e1");

        mockMvc.perform(post(ApiConstants.stepEmployeeRootApi + ApiConstants.complete)
                        .param("stepId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("The step has completed."));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void completeStep_error(RuntimeException exception, int expectedStatus) throws Exception {
        doThrow(exception).when(stepEmployeeService).completedStep(anyLong(), anyString());

        mockMvc.perform(post(ApiConstants.stepEmployeeRootApi + ApiConstants.complete)
                        .param("stepId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getPageable_success() throws Exception {
        PageResponseDTO<AttachStepResponseDTO> response = new PageResponseDTO<>(
                List.of(new AttachStepResponseDTO("e1", 1L, "Step", "User", false, "STARTED", null, null)),
                1,
                1,
                0,
                20
        );
        given(stepEmployeeService.getPageable(any())).willReturn(response);

        mockMvc.perform(get(ApiConstants.stepEmployeeRootApi)
                        .param("stepId", "1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].stepId").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getPageable_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepEmployeeService.getPageable(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepEmployeeRootApi)
                        .param("stepId", "1")
                        .param("orderBy", "bad_field"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getList_success() throws Exception {
        given(stepEmployeeService.getList(any()))
                .willReturn(List.of(new AttachStepResponseDTO("e1", 1L, "Step", "User", false, "STARTED", null, null)));

        mockMvc.perform(get(ApiConstants.stepEmployeeRootApi + ApiConstants.list)
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value("e1"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getList_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepEmployeeService.getList(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepEmployeeRootApi + ApiConstants.list)
                        .param("employeeId", "e1")
                        .param("orderBy", "bad_field"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void attachedStep_success() throws Exception {
        given(stepEmployeeService.getList(any()))
                .willReturn(List.of(new AttachStepResponseDTO("e1", 1L, "Step", "User", false, "STARTED", null, null)));

        mockMvc.perform(get(ApiConstants.stepEmployeeRootApi + "/attached")
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stepId").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void attachedStep_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepEmployeeService.getList(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepEmployeeRootApi + "/attached")
                        .param("employeeId", "e1"))
                .andExpect(status().is(expectedStatus));
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
