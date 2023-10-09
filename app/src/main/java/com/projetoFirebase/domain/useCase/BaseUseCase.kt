package com.projetoFirebase.domain.useCase

import com.projetoFirebase.util.DefaultListenerResponse


abstract class BaseUseCase<T, SuccessType, ErrorType>(val repository: T) {

    abstract suspend fun <T> run(
        defaultListenerResponse: DefaultListenerResponse<SuccessType, ErrorType>,
        vararg data: T
    )
}