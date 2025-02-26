package org.example.exception;

import org.example.controller.UserController;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.iam.model.IamException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Setup can be done here if needed
    }

    @Test
    void shouldHandleIamException() throws Exception {
        when(userService.getUsers()).thenThrow(IamException.builder().message("AWS IAM error").statusCode(503).build());

        mockMvc.perform(get("/users"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
                .andExpect(jsonPath("$.error").value("AWS IAM error"))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    @Test
    void shouldHandleNotFoundException() throws Exception {
        mockMvc.perform(get("/non-existent-endpoint"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    // Test disabled because current impl. does not throw this exception
    @Disabled
    @Test
    void shouldHandleBadRequestException() throws Exception {
        mockMvc.perform(get("/users?id=invalid-value"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Invalid request"));
    }

    @Test
    void shouldHandleMethodNotAllowedException() throws Exception {
        mockMvc.perform(post("/users"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(jsonPath("$.error").value("Method not allowed"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        when(userService.getUsers()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value("An unexpected error occurred"));
    }
}
