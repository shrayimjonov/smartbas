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
import uz.davrbank.courseservice.aggregation.dto.course.CourseRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.course.CourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.dto.step.StepRequestDTO;
import uz.davrbank.courseservice.aggregation.exception.condition.BadRequestException;
import uz.davrbank.courseservice.aggregation.exception.condition.ConnectionException;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.exception.condition.ThirdPartyException;
import uz.davrbank.courseservice.aggregation.exception.controller.ControllerAdvisor;
import uz.davrbank.courseservice.service.dao.course.CourseService;
import uz.davrbank.courseservice.utils.ApiConstants;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@Import(ControllerAdvisor.class)
class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CourseService courseService;

    @BeforeEach
    void setUp() {
        reset(courseService);
    }

    @Test
    void createCourse_success() throws Exception {
        given(courseService.create(any(), anyString())).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(post(ApiConstants.courseRootApi)
                        .param("userId", "u1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourseRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void createCourse_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseService.create(any(), anyString())).willThrow(exception);

        mockMvc.perform(post(ApiConstants.courseRootApi)
                        .param("userId", "u1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourseRequest())))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void updateCourse_success() throws Exception {
        given(courseService.update(eq(1L), any())).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(put(ApiConstants.courseRootApi + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourseRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void updateCourse_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseService.update(eq(1L), any())).willThrow(exception);

        mockMvc.perform(put(ApiConstants.courseRootApi + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourseRequest())))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getPageable_success() throws Exception {
        PageResponseDTO<CourseResponseDTO> response = new PageResponseDTO<>(
                List.of(new CourseResponseDTO(1L, "Course", 10)),
                1,
                1,
                0,
                20
        );
        given(courseService.getPageable(any())).willReturn(response);

        mockMvc.perform(get(ApiConstants.courseRootApi)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getPageable_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseService.getPageable(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.courseRootApi)
                        .param("orderBy", "bad_field"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getList_success() throws Exception {
        given(courseService.getList(any()))
                .willReturn(List.of(new CourseResponseDTO(1L, "Course", 10)));

        mockMvc.perform(get(ApiConstants.courseRootApi + ApiConstants.list))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getList_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseService.getList(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.courseRootApi + ApiConstants.list)
                        .param("orderBy", "bad_field"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void deleteCourse_success() throws Exception {
        given(courseService.delete(1L)).willReturn(new ResponseSuccess("ok"));

        mockMvc.perform(delete(ApiConstants.courseRootApi + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void deleteCourse_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseService.delete(1L)).willThrow(exception);

        mockMvc.perform(delete(ApiConstants.courseRootApi + "/{id}", 1L))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getCourse_success() throws Exception {
        given(courseService.get(1L)).willReturn(new CourseResponseDTO(1L, "Course", 10));

        mockMvc.perform(get(ApiConstants.courseRootApi + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getCourse_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseService.get(1L)).willThrow(exception);

        mockMvc.perform(get(ApiConstants.courseRootApi + "/{id}", 1L))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void accessEmployees_success() throws Exception {
        doNothing().when(courseService).accessEmployees(eq(1L), anySet());

        mockMvc.perform(post(ApiConstants.courseRootApi + "/{id}/access", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Set.of("e1", "e2"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully!"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void accessEmployees_error(RuntimeException exception, int expectedStatus) throws Exception {
        doThrow(exception).when(courseService).accessEmployees(eq(1L), anySet());

        mockMvc.perform(post(ApiConstants.courseRootApi + "/{id}/access", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Set.of("e1", "e2"))))
                .andExpect(status().is(expectedStatus));
    }

    private CourseRequestDTO sampleCourseRequest() {
        return new CourseRequestDTO(
                "Course",
                10,
                1L,
                Set.of(new StepRequestDTO("Step")),
                1.5,
                true
        );
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
