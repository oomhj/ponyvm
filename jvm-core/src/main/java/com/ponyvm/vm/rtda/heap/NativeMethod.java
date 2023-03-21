package com.ponyvm.vm.rtda.heap;

import com.ponyvm.vm.rtda.Frame;

public interface NativeMethod {
  // do all things
  void invoke(Frame frame);
}
