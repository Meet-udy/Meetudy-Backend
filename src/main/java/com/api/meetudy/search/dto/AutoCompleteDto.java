package com.api.meetudy.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AutoCompleteDto {

    private List<String> categories;

    private List<String> groupNames;

}