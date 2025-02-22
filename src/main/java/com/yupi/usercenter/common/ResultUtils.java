package com.yupi.usercenter.common;

/**
 * @author yjh
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     *失败
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse(errorCode.getCode(),message,null,description);
    }

    /**
     *失败
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),null,description);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int errorCode,String message,String description){
        return new BaseResponse(errorCode,message,null,description);
    }
}
