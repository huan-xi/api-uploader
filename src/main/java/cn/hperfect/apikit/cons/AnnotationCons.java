package cn.hperfect.apikit.cons;

public interface AnnotationCons {
    String Deprecated = "java.lang.Deprecated";
    String Validated = "org.springframework.validation.annotation";

    interface SPRING {
        String REST_CONTROLLER = "org.springframework.web.bind.annotation.RestController";
        String CONTROLLER = "org.springframework.web.bind.annotation.Controller";
        String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";
        String GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";
        String PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";
        String POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";
        String DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";

        String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";
        String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";
        String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
    }

    /**
     * swagger
     */
    interface SWAGGER {
        String API = "io.swagger.annotations.Api";
        String API_OPERATION = "io.swagger.annotations.ApiOperation";
        String API_PARAM = "io.swagger.annotations.ApiParam";
        String API_MODEL_PROPERTY = "io.swagger.annotations.ApiModelProperty";
    }
}
