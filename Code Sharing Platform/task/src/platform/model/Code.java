package platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "codes")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Column(name="codeID")
    private Long id;

    @JsonIgnore
    private String uuid;

    private String code;

    private String date;

    private Long time;

    private Long views;

    @JsonIgnore
    private LocalDateTime fullDate;

    @JsonIgnore
    private boolean limitedByTime;
    @JsonIgnore
    private boolean limitedByViews;

}
