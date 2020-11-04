package kr.jadekim.ncloud.authenticator

import com.github.kittinunf.fuel.core.FoldableRequestInterceptor
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.RequestTransformer

abstract class Authenticator(val meta: AuthenticatorMeta) : FoldableRequestInterceptor {

    abstract fun Request.authenticate()

    override fun invoke(next: RequestTransformer): RequestTransformer = {
        if (it.getTag(AuthenticatorTag::class)?.meta?.name == meta.name) {
            it.authenticate()
        }

        next(it)
    }
}