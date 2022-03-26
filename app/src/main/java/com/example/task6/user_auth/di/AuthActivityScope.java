package com.example.task6.user_auth.di;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.inject.Scope;

@Scope
@Retention(RUNTIME)
public @interface AuthActivityScope {
}
