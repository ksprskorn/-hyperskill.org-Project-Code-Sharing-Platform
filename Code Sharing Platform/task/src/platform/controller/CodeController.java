package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.model.Code;
import platform.services.CodeService;

import java.util.List;

@Controller
@Validated
public class CodeController {

    @Autowired
    private CodeService codeService;

    @GetMapping("/code/new")
    public String createCodeSnippet(Model model) {
        return "create";
    }

    @GetMapping("/code/{uuid}")
    public String getCode(@PathVariable String uuid, Model model) {
        Code code = codeService.findCodeByUuid(uuid);

        if (!codeService.isValid(code)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("code", code.getCode());
        model.addAttribute("date", code.getDate());
        model.addAttribute("time", code.getTime());
        model.addAttribute("views", code.getViews());
        model.addAttribute("limitedByViews",code.isLimitedByViews());
        model.addAttribute("limitedByTime",code.isLimitedByTime());

        return "code";
    }

    @GetMapping("/code/latest")
    public String getCode(Model model) {
        model.addAttribute("codes", codeService.findByOrderByFullDateDesc());
        return "codes";
    }
}
