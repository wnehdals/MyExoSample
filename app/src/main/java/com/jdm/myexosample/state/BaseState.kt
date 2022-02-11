package com.jdm.myexosample.state

import com.jdm.myexosample.response.FailResp

sealed class BaseState{
    object Uninitialized: BaseState()   //초기 상태

    object Loading: BaseState()         //로딩 상태

    data class Success<T>(              //API 호출 성공
        val successResp: T
    ): BaseState()
    data class Fail(                 //API 호출 실패
        val failResp: FailResp
    ): BaseState()
}
