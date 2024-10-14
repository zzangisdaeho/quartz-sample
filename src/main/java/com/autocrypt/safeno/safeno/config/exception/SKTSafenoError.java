package com.autocrypt.safeno.safeno.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class SKTSafenoError extends CustomException{

    private final SKTSafenoErrorCode errorCode;

    public SKTSafenoError(String message, SKTSafenoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SKTSafenoError(String message, HttpStatus httpStatus, SKTSafenoErrorCode errorCode) {
        super(message, httpStatus);
        this.errorCode = errorCode;
    }

    // Error code enum 정의
    @Getter
    public enum SKTSafenoErrorCode {
        E101("필수값 누락"),
        E102("System 장애"),
        E201("비회원 제휴사 코드가 아님"),
        E202("유효한 기간 만료"),
        E203("제휴사에서 부여하지 않은 값임"),
        E301("유효하지 않은 데이터"),
        E401("Data Not Found"),
        E402("Data Overlap"),
        E403("Data Duplication"),
        E501("요청한 Data가 이미 삭제됨"),
        E502("요청한 Data가 비정상 상태임"),
        E503("금액 처리 오류"),
        UNKNOWN("Unknown Error Code");

        private final String mean;

        SKTSafenoErrorCode(String mean) {
            this.mean = mean;
        }

        public static SKTSafenoErrorCode fromCode(String code) {
            for (SKTSafenoErrorCode errorCode : values()) {
                if (errorCode.name().equals(code)) {
                    return errorCode;
                }
            }
            return UNKNOWN;
        }
    }
}
