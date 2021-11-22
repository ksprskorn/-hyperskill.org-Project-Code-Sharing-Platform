package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.model.Code;
import platform.services.CodeService;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public class CodeApiController {

    @Autowired
    private CodeService codeService;

    @PostMapping("/api/code/new")
    public Map<String, String> createCodeSnippetApi(@RequestBody Code code) {
        Code savedCode = codeService.save(code);
        return Map.of("id", savedCode.getUuid());
    }

    @GetMapping("/api/code/{uuid}")
    public Code getCodeApi(@PathVariable String uuid) {
        Code code = codeService.findCodeByUuid(uuid);

        if (!codeService.isValid(code)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return code;
    }

    @GetMapping("/api/code/latest")
    public List<Code> getFirstCodesApi() {
        return codeService.findByOrderByFullDateDesc();
    }
}

