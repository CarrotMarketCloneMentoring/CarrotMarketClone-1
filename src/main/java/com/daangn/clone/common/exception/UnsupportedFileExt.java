package com.daangn.clone.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class UnsupportedFileExt extends RuntimeException {

    private List<String> inputFileExt;
}
