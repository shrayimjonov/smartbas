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
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockResponseDTO;
import uz.davrbank.courseservice.aggregation.exception.condition.BadRequestException;
import uz.davrbank.courseservice.aggregation.exception.condition.ConnectionException;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.exception.condition.ThirdPartyException;
import uz.davrbank.courseservice.aggregation.exception.controller.ControllerAdvisor;
import uz.davrbank.courseservice.service.dao.step.StepTextBlockService;
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

@WebMvcTest(StepTextBlockController.class)
@Import(ControllerAdvisor.class)
class StepTextBlockControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StepTextBlockService stepTextBlockService;

    @BeforeEach
    void setUp() {
        reset(stepTextBlockService);
    }

    @Test
    void createBlock_success() throws Exception {
        given(stepTextBlockService.create(any())).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(post(ApiConstants.stepTextBlockRootApi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void createBlock_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepTextBlockService.create(any())).willThrow(exception);

        mockMvc.perform(post(ApiConstants.stepTextBlockRootApi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void updateBlock_success() throws Exception {
        given(stepTextBlockService.update(anyLong(), any())).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(put(ApiConstants.stepTextBlockRootApi + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void updateBlock_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepTextBlockService.update(anyLong(), any())).willThrow(exception);

        mockMvc.perform(put(ApiConstants.stepTextBlockRootApi + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void deleteBlock_success() throws Exception {
        given(stepTextBlockService.delete(1L)).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(delete(ApiConstants.stepTextBlockRootApi + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void deleteBlock_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepTextBlockService.delete(1L)).willThrow(exception);

        mockMvc.perform(delete(ApiConstants.stepTextBlockRootApi + "/{id}", 1L))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getList_success() throws Exception {
        given(stepTextBlockService.getList(1L)).willReturn(List.of(sampleResponse()));

        mockMvc.perform(get(ApiConstants.stepTextBlockRootApi + ApiConstants.list)
                        .param("stepId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getList_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepTextBlockService.getList(1L)).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepTextBlockRootApi + ApiConstants.list)
                        .param("stepId", "1"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getBlock_success() throws Exception {
        given(stepTextBlockService.get(1L)).willReturn(sampleResponse());

        mockMvc.perform(get(ApiConstants.stepTextBlockRootApi + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getBlock_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(stepTextBlockService.get(1L)).willThrow(exception);

        mockMvc.perform(get(ApiConstants.stepTextBlockRootApi + "/{id}", 1L))
                .andExpect(status().is(expectedStatus));
    }

    private StepTextBlockRequestDTO sampleRequest() {
        return new StepTextBlockRequestDTO(1L, "Title", "Body");
    }

    private StepTextBlockResponseDTO sampleResponse() {
        return new StepTextBlockResponseDTO(1L, 1L, "Title", "Body", 1);
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
