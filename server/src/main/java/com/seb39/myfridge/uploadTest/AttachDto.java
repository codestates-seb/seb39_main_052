package com.seb39.myfridge.uploadTest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachDto {

    private Long id;

    private Long recipeId;

    private String originalName;

    private String saveName;

    private Long size;
}
