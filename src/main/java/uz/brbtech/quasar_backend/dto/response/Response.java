package uz.brbtech.quasar_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private Integer code;
    private HttpStatus status;
    private String message;
    private Boolean success;
    private List<ErrorResponse> errors;
    private Map<String, Object> meta = new HashMap<>();
    private Object data;
    /**
     * Jami natija soni
     */
    private Long elements;
    /**
     * Jami sahifa soni
     */
    private Integer pages;
    private String timestamp;
    private String path;

    public static <T> Response<T> success(String message, T data) {
        return Response.<T>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Response<T> success(T data, Long elements, Integer pages) {
        return Response.<T>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message("Successfully")
                .data(data)
                .elements(elements)
                .pages(pages)
                .build();
    }

    public static <T> Response<T> success(String message, T data, Long elements, Integer pages) {
        return Response.<T>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message(message)
                .data(data)
                .elements(elements)
                .pages(pages)
                .build();
    }

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message("Successfully")
                .data(data)
                .build();
    }

    public static Response<?> success(String message) {
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message(message)
                .build();
    }

    public static Response<?> error(HttpStatus status, String message) {
        return Response.builder()
                .code(status.value())
                .status(status)
                .success(false)
                .message(message)
                .build();
    }

    public static Response<?> error(String message) {
        return Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .message(message)
                .build();
    }
}