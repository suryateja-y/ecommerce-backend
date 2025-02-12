package com.academy.projects.ecommerce.productservice.dtos;

import com.academy.projects.ecommerce.productservice.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetResponseDto implements Serializable {
    private String message;
    private Category category;
    private Date retrivedDate;
}
