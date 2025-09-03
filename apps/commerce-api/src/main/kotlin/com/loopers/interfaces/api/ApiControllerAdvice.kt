package com.loopers.interfaces.api

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ServerWebInputException

@RestControllerAdvice
class ApiControllerAdvice {
    private val log = LoggerFactory.getLogger(ApiControllerAdvice::class.java)

    @ExceptionHandler(IllegalAccessException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handle(e: IllegalAccessException): ProblemDetail {
        log.error("IllegalAccess exception : {}", e.localizedMessage)
        return failureResponse(errorType = HttpStatus.FORBIDDEN, errorMessage = e.localizedMessage)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handle(e: DataIntegrityViolationException): ProblemDetail {
        log.warn("ConstraintViolationException : {}", e.localizedMessage)
        return failureResponse(errorType = HttpStatus.CONFLICT, errorMessage = e.localizedMessage)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: MethodArgumentTypeMismatchException): ProblemDetail {
        val name = e.name
        val type = e.requiredType?.simpleName ?: "unknown"
        val value = e.value ?: "null"
        val message = "요청 파라미터 '$name' (타입: $type)의 값 '$value'이(가) 잘못되었습니다."
        return failureResponse(errorType = HttpStatus.BAD_REQUEST, errorMessage = message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: MethodArgumentNotValidException): ProblemDetail {
        log.warn(e.stackTraceToString())
        return failureResponse(errorType = HttpStatus.BAD_REQUEST, errorMessage = e.localizedMessage)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: IllegalArgumentException): ProblemDetail {
        log.warn(e.stackTraceToString())
        return failureResponse(errorType = HttpStatus.BAD_REQUEST, errorMessage = e.localizedMessage)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: MissingServletRequestParameterException): ProblemDetail {
        val name = e.parameterName
        val type = e.parameterType
        val message = "필수 요청 파라미터 '$name' (타입: $type)가 누락되었습니다."
        return failureResponse(errorType = HttpStatus.BAD_REQUEST, errorMessage = message)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: HttpMessageNotReadableException): ProblemDetail {
        val errorMessage = when (val rootCause = e.rootCause) {
            is InvalidFormatException -> {
                val fieldName = rootCause.path.joinToString(".") { it.fieldName ?: "?" }

                val valueIndicationMessage = when {
                    rootCause.targetType.isEnum -> {
                        val enumClass = rootCause.targetType
                        val enumValues = enumClass.enumConstants.joinToString(", ") { it.toString() }
                        "사용 가능한 값 : [$enumValues]"
                    }

                    else -> ""
                }

                val expectedType = rootCause.targetType.simpleName
                val value = rootCause.value

                "필드 '$fieldName'의 값 '$value'이(가) 예상 타입($expectedType)과 일치하지 않습니다. $valueIndicationMessage"
            }

            is MismatchedInputException -> {
                val fieldPath = rootCause.path.joinToString(".") { it.fieldName ?: "?" }
                "필수 필드 '$fieldPath'이(가) 누락되었습니다."
            }

            is JsonMappingException -> {
                val fieldPath = rootCause.path.joinToString(".") { it.fieldName ?: "?" }
                "필드 '$fieldPath'에서 JSON 매핑 오류가 발생했습니다: ${rootCause.originalMessage}"
            }

            else -> "요청 본문을 처리하는 중 오류가 발생했습니다. JSON 메세지 규격을 확인해주세요."
        }

        return failureResponse(errorType = HttpStatus.BAD_REQUEST, errorMessage = errorMessage)
    }

    @ExceptionHandler(ServerWebInputException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: ServerWebInputException): ProblemDetail {
        fun extractMissingParameter(message: String): String {
            val regex = "'(.+?)'".toRegex()
            return regex.find(message)?.groupValues?.get(1) ?: ""
        }

        val missingParams = extractMissingParameter(e.reason ?: "")
        return if (missingParams.isNotEmpty()) {
            failureResponse(errorType = HttpStatus.BAD_REQUEST, errorMessage = "필수 요청 값 \'$missingParams\'가 누락되었습니다.")
        } else {
            failureResponse(errorType = HttpStatus.BAD_REQUEST)
        }
    }

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(
        e: NoSuchElementException,
    ): ProblemDetail = failureResponse(
        errorType = HttpStatus.NOT_FOUND,
        errorMessage = e.localizedMessage,
    ).also {
        log.warn("No such element: ${e.localizedMessage}")
    }

    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(e: Throwable): ProblemDetail {
        log.error("Exception : {}", e.message, e)
        val errorType = HttpStatus.INTERNAL_SERVER_ERROR
        return failureResponse(errorType = errorType)
    }

    private fun failureResponse(errorType: HttpStatus, errorMessage: String? = null): ProblemDetail =
        ProblemDetail.forStatus(errorType).apply {
            detail = errorMessage
        }
}
