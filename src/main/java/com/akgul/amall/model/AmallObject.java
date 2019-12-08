package com.akgul.amall.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AmallObject {
    private long id;
    private boolean deleted;
    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;
}
