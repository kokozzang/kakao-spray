package com.wspark.kakaospray.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public abstract class CommonException extends RuntimeException {

	@Getter
	@NonNull
	protected String code;

	@Getter
	@NonNull
	protected String message;

	@Getter
	private String data;

}
