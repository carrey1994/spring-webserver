package com.jameswu.demo.exception;

// @ControllerAdvice
// public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(value = {IllegalArgumentException.class, UserException.class})
//    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
//        String message = ex.getMessage();
//        return handleExceptionInternal(
//                ex, new FailureResult<>(message), new HttpHeaders(), HttpStatus.NOT_FOUND,
// request);
//    }
// }
