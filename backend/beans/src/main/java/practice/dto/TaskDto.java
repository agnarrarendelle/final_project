package practice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Builder
public class TaskDto {
    private String name;
    private String status;
    private String priorityLevel;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredAt;
}
