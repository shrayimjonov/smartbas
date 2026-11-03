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
import uz.davrbank.courseservice.aggregation.dto.course.UserCourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.exception.condition.BadRequestException;
import uz.davrbank.courseservice.aggregation.exception.condition.ConnectionException;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.exception.condition.ThirdPartyException;
import uz.davrbank.courseservice.aggregation.exception.controller.ControllerAdvisor;
import uz.davrbank.courseservice.service.dao.course.CourseEmployeeService;
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

@WebMvcTest(CourseEmployeeController.class)
@Import(ControllerAdvisor.class)
class CourseEmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CourseEmployeeService courseEmployeeService;

    @BeforeEach
    void setUp() {
        reset(courseEmployeeService);
    }

    @Test
    void startCourse_success() throws Exception {
        doNothing().when(courseEmployeeService).startedCourse(1L, "e1");

        mockMvc.perform(post(ApiConstants.courseEmployeeRootApi + ApiConstants.start)
                        .param("courseId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("The course has started."));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void startCourse_error(RuntimeException exception, int expectedStatus) throws Exception {
        doThrow(exception).when(courseEmployeeService).startedCourse(anyLong(), anyString());

        mockMvc.perform(post(ApiConstants.courseEmployeeRootApi + ApiConstants.start)
                        .param("courseId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void completeCourse_success() throws Exception {
        doNothing().when(courseEmployeeService).completedCourse(1L, "e1");

        mockMvc.perform(post(ApiConstants.courseEmployeeRootApi + ApiConstants.complete)
                        .param("courseId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("The course has completed."));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void completeCourse_error(RuntimeException exception, int expectedStatus) throws Exception {
        doThrow(exception).when(courseEmployeeService).completedCourse(anyLong(), anyString());

        mockMvc.perform(post(ApiConstants.courseEmployeeRootApi + ApiConstants.complete)
                        .param("courseId", "1")
                        .param("employeeId", "e1"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getPageable_success() throws Exception {
        PageResponseDTO<UserCourseResponseDTO> response = new PageResponseDTO<>(
                List.of(new UserCourseResponseDTO("e1", 1L, "Course", "User", "STARTED", null, null)),
                1,
                1,
                0,
                20
        );
        given(courseEmployeeService.getPageable(any())).willReturn(response);

        mockMvc.perform(get(ApiConstants.courseEmployeeRootApi)
                        .param("courseId", "1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].courseId").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getPageable_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseEmployeeService.getPageable(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.courseEmployeeRootApi)
                        .param("courseId", "1")
                        .param("orderBy", "bad_field"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void getList_success() throws Exception {
        given(courseEmployeeService.getList(any()))
                .willReturn(List.of(new UserCourseResponseDTO("e1", 1L, "Course", "User", "STARTED", null, null)));

        mockMvc.perform(get(ApiConstants.courseEmployeeRootApi + ApiConstants.list)
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value("e1"));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getList_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseEmployeeService.getList(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.courseEmployeeRootApi + ApiConstants.list)
                        .param("employeeId", "e1")
                        .param("orderBy", "bad_field"))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void attachedCourse_success() throws Exception {
        given(courseEmployeeService.getList(any()))
                .willReturn(List.of(new UserCourseResponseDTO("e1", 1L, "Course", "User", "STARTED", null, null)));

        mockMvc.perform(get(ApiConstants.courseEmployeeRootApi + "/attached")
                        .param("employeeId", "e1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1L));
    }

    @ParameterizedTest
    @MethodSource("errorScenarios")
    void attachedCourse_error(RuntimeException exception, int expectedStatus) throws Exception {
        given(courseEmployeeService.getList(any())).willThrow(exception);

        mockMvc.perform(get(ApiConstants.courseEmployeeRootApi + "/attached")
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
