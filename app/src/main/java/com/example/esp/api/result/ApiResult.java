package com.example.esp.api.result;

import java.io.Serializable;

public abstract class ApiResult implements Serializable {

    public int errcode;
    public String errmsg;

    public boolean success() {
        return errcode == 0;
    }
}
