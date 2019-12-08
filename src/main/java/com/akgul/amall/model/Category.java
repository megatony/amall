package com.akgul.amall.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class Category extends AmallObject {

    public Category(String name) {
        this.name = name;
    }

    private String name;
    @Nullable
    private Category parentCategory;
}
