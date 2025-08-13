package com.aigirlfriend.commen.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T>  implements Serializable {
    private Integer code;
    private T data;

    private String message;

    public  static  <T> Result<T> ok(){
        Result<T> result = new Result<>();
        result.code = 200;
        return result;
    }

    public static <T> Result<T> ok(T object){
        Result<T> result = new Result<>();
        result.code = 200;
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String s){
        Result<T> result = new Result<>();
        result.code = 0;
        result.message = s;
        return result;
    }
}
