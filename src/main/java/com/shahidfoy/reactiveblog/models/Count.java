package com.shahidfoy.reactiveblog.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
public class Count implements Serializable {

    private long count;
}
