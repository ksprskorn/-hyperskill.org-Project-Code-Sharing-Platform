package platform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import platform.repository.CodeRepository;
import platform.model.Code;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CodeService {

    private final CodeRepository codeRepository;

    private final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);

    @Autowired
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public Code save(Code code) {
        String formatDateTime = LocalDateTime.now().format(formatter);
        code.setDate(formatDateTime);
        code.setFullDate(LocalDateTime.now());

        UUID uuid = UUID.randomUUID();
        code.setUuid(uuid.toString());

        code.setLimitedByTime(code.getTime() != 0);
        code.setLimitedByViews(code.getViews() != 0);

        codeRepository.save(code);
        return code;
    }

    public Code findCodeByUuid(String uuid) {
        return codeRepository.findCodeByUuid(uuid);
    }

    public List<Code> findByOrderByFullDateDesc() {
        List<Code> codeList = codeRepository.findByOrderByFullDateDesc()
                .stream()
                .filter(c -> c.getTime() == 0 & c.getViews() == 0)
                .limit(10)
                .collect(Collectors.toList());

        return codeList;
    }

    public boolean isValid(Code code) {
        boolean valid = true;

        if (code == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (code.isLimitedByViews()) {
            if (code.getViews() <= 0) {
                codeRepository.delete(code);
                valid = false;
            } else {
                code.setViews(code.getViews() - 1);
                codeRepository.save(code);
                if (code.getViews() <= 0) {
                    codeRepository.delete(code);
                }
            }
        }

        if (code.isLimitedByTime()) {
            Long timePast = Duration.between(code.getFullDate(), LocalDateTime.now()).getSeconds();

            if (timePast >= code.getTime()) {
                codeRepository.delete(code);
                valid = false;
            } else {
                code.setTime(code.getTime() - timePast);
                codeRepository.save(code);
                if (code.getTime() <= 0) {
                    code.setTime(0L);
                    codeRepository.delete(code);
                }
            }
        }
        return valid;
    }
}
