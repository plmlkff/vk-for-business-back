package ru.itmo.blpslab1.utils.service

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.context.ApplicationEvent
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
class Result<T> internal constructor(
    @JsonProperty("body")
    val body: T?,
    @JsonProperty("status")
    val status: HttpStatus,
    @field:JsonIgnore
    val events: MutableSet<ApplicationEvent>
){
    @JsonIgnore
    fun isOk() = status == HttpStatus.OK

    @JsonIgnore
    fun isError() = !isOk()

    inline fun <R> map(crossinline function: (T?) -> R): Result<R> {
        return if (status != HttpStatus.OK) error(status, events)
        else ok(function(body))
    }

    inline fun <R> flatMap(crossinline function: (T?) -> Result<R>): Result<R> {
        return if (status != HttpStatus.OK) error(status, events)
        else function(body)
    }

    inline fun <R> fold(
        crossinline ifError: (HttpStatus) -> R,
        crossinline ifOk: (T?) -> R
    ): R{
        return if (status == HttpStatus.OK) ifOk(body)
        else ifError(status)
    }

    fun withEvents(vararg events: ApplicationEvent): Result<T>{
        this.events.addAll(events)
        return this
    }
}

fun <T> error(
    status: HttpStatus = HttpStatus.BAD_REQUEST,
    events: MutableSet<ApplicationEvent> = mutableSetOf()
) = Result<T>(null, status, events)

fun <T> ok(
    body: T? = null,
    status: HttpStatus = HttpStatus.OK,
    events: MutableSet<ApplicationEvent> = mutableSetOf()
) = Result(body, status, events)