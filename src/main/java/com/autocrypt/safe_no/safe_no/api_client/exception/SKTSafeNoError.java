package com.autocrypt.safe_no.safe_no.api_client.exception;

import com.autocrypt.safe_no.safe_no.config.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class SKTSafeNoError extends CustomException {

    private final SKTSafeNoErrorCode errorCode;

    public SKTSafeNoError(String message, SKTSafeNoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SKTSafeNoError(String message, HttpStatus httpStatus, SKTSafeNoErrorCode errorCode) {
        super(message, httpStatus);
        this.errorCode = errorCode;
    }

    // Error code enum 정의
    @Getter
    public enum SKTSafeNoErrorCode {
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
        UNKNOWN("Unknown Error Code"),
        CLIENT_ERROR("client side error"),
        SKT_SERVER_ERROR("skt server side error");

        private final String mean;

        SKTSafeNoErrorCode(String mean) {
            this.mean = mean;
        }

        public static SKTSafeNoErrorCode fromCode(String code) {
            for (SKTSafeNoErrorCode errorCode : values()) {
                if (errorCode.name().equals(code)) {
                    return errorCode;
                }
            }
            return UNKNOWN;
        }
    }
}
