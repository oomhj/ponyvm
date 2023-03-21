package com.ponyvm.vm.jmm.heap;

import com.ponyvm.vm.jmm.Frame;

public interface NativeMethod {
  // do all things
  void invoke(Frame frame);
}
