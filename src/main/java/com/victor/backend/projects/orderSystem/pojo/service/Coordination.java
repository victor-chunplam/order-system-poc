package com.victor.backend.projects.orderSystem.pojo.service;

import com.victor.backend.projects.orderSystem.annotation.IsNumericInRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coordination {
    @IsNumericInRange(min = "-90", max = "90", message = "Invalid latitude inputted")
    @Length(min = 1, max = 255)
    @NotBlank
    private String latitude;
    @IsNumericInRange(min = "-180", max = "180", message = "Invalid longitude inputted")
    @Length(min = 1, max = 255)
    @NotBlank
    private String longitude;
}
