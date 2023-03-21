package com.ponyvm.vm.classfile;

public final class ExceptionTable {

    public final Exception[] exceptions;

    public ExceptionTable(Exception[] exceptions) {
        this.exceptions = exceptions;
    }
}
