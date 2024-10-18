package com.autocrypt.safe_no.safe_no.api_client.exception;

import com.autocrypt.safe_no.safe_no.config.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class SejongSafeNoError extends CustomException {

    private final SejongSafeNoErrorCode errorCode;

    public SejongSafeNoError(String message, SejongSafeNoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SejongSafeNoError(String message, HttpStatus httpStatus, SejongSafeNoErrorCode errorCode) {
        super(message, httpStatus);
        this.errorCode = errorCode;
    }

    // Error code enum 정의
    @Getter
    public enum SejongSafeNoErrorCode {
        // 4000번대 에러
        NO_RECORDING_FILE("4000", "녹취파일 정보를 찾을 수가 없습니다"),
        PROCESSING_ERROR("4000", "처리 중 오류가 발생했습니다"),

        // 5000번대 에러
        INVALID_PAGE_NUMBER("5000", "잘못된 페이지 번호입니다"),
        TOO_MANY_RESULTS("5000", "페이지 당 100 건 이내만 조회할 수 있습니다"),
        INVALID_SORT_ORDER("5000", "잘못된 정렬 순서입니다"),
        INVALID_DATE_FORMAT_SDATE("5000", "날짜 형식이 잘못 되었습니다"),
        INVALID_DATE_FORMAT_EDATE("5000", "날짜 형식이 잘못 되었습니다"),
        PAST_MONTH_ONLY("5000", "이전 달까지만 조회됩니다"),
        END_DATE_AFTER_CURRENT("5000", "검색 종료 시간이 현재 시간보다 큽니다"),
        SEARCH_PERIOD_EXCEEDS("5000", "검색 기간이 7 일 이내만 조회 가능합니다"),
        INVALID_DATE_RANGE("5000", "날짜 형식이 잘못 되었습니다"),
        RESET_PAST_MONTH_ONLY("5000", "이전 달까지만 reset 가능합니다"),
        RESET_END_TIME_EXCEEDS_CURRENT("5000", "reset 종료 시간이 현재 시간보다 큽니다"),
        MENT_FILE_NOT_FOUND("5000", "지정된 ment file 이 없습니다"),
        NO_TTS_MESSAGE("5000", "TTS 메세지가 없습니다"),
        INVALID_VOICE_FILE_CREATION("5000", "잘못된 음성파일 생성 방식입니다"),
        TYPE_INCORRECT("5000", "type 이 올바르지않습니다"),
        SAFE_NUMBER_IN_USE("5000", "사용 중인 가상번호입니다"),

        // 6000번대 에러 - 가상번호 및 데이터 관련 에러
        RECORDING_FILE_NOT_FOUND("6000", "녹취파일을 찾을 수가 없습니다"),
        NO_DATA("6000", "NO DATA"),
        INVALID_OUTBOUND_INFO("6000", "Outbound 정보가 올바르지 않습니다"),
        INVALID_SAFE_NUMBER("6000", "가상번호가 올바르지 않습니다"),
        SAFE_NUMBER_NOT_FOUND("6000", "해당되는 가상번호가 없습니다"),

        // 8000번대 에러 - 업로드/다운로드 관련 에러
        RECORDING_FILE_DOWNLOAD_ERROR("8000", "녹취파일을 다운로드할 수 없습니다"),
        UPLOAD_ERROR("8000", "업로드할 수 없습니다"),
        TTS_PROCESS_ERROR("8000", "음성합성 과정에 오류가 발생했습니다"),
        VOICE_FILE_UPLOAD_ERROR("8000", "음성 파일 업로드 과정에 오류가 발생했습니다"),
        UNKNOWN("XXXX", "Unknown Error Code"),
        CLIENT_ERROR("XXXX", "client side error"),
        SEJONG_SERVER_ERROR("XXXX","sejong server side error");

        private final String code;
        private final String message;

        SejongSafeNoErrorCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public static SejongSafeNoErrorCode fromCodeAndMessage(String code, String message) {
            //코드로 매칭
            List<SejongSafeNoErrorCode> list = Arrays.stream(values()).filter(sejongSafeNoErrorCode -> sejongSafeNoErrorCode.getCode().equals(code)).toList();
            //메세지가 유사한것 매칭
            return list.stream().filter(sejongSafeNoErrorCode -> message.contains(sejongSafeNoErrorCode.getMessage())).findFirst().orElse(UNKNOWN);
        }
    }
}
