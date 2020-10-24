package com.scoto.quotememory.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef({OperationTypeDef.SUCCESS, OperationTypeDef.UPDATE, OperationTypeDef.FAILURE, OperationTypeDef.EMPTY_FIELD})
public @interface OperationTypeDef {
    int SUCCESS = 1;
    int FAILURE = 0;
    int UPDATE = 2;
    int EMPTY_FIELD = -1;
}
