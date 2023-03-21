package com.ponyvm.vm.rtda;

import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.NativeMethod;

public class MetaSpace {

    public static Thread main;

    public static Thread getMainEnv() {
        return main;
    }

    public static NativeMethod findNativeMethod(String key) {
        return Heap.findMethod(key);
    }
}
