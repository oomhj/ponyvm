package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.classfile.attribute.BootstrapMethods;
import com.ponyvm.vm.classfile.cp.MethodHandle;
import com.ponyvm.vm.classfile.cp.MethodType;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Heap;
import com.ponyvm.vm.jmm.heap.Class;
import com.ponyvm.vm.jmm.heap.Instance;
import com.ponyvm.vm.jmm.heap.LambdaObject;
import com.ponyvm.vm.jmm.heap.Method;
import com.ponyvm.vm.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvokeDynamicInst implements Instruction {

  public final String methodName;
  public final String methodDescriptor;

  public final int bsIdx;

  public InvokeDynamicInst(String methodName, String methodDescriptor, int bsIdx) {
    this.methodName = methodName;
    this.methodDescriptor = methodDescriptor;
    this.bsIdx = bsIdx;
  }

  @Override
  public int offset() {
    return 5;
  }

  @Override
  public void execute(Frame frame) {
    BootstrapMethods bootstrapMethods = frame.method.clazz.bootstrapMethods;
    if (bootstrapMethods == null) {
      throw new IllegalStateException();
    }

    BootstrapMethods.BootstrapMethod bootstrapMethod = bootstrapMethods.methods[bsIdx];
    Integer argsRef = bootstrapMethod.argsRefs[1];
    MethodHandle info = (MethodHandle) frame.method.clazz.constantPool.infos[argsRef - 1];
    String bsTargetClass = Utils.getClassNameByMethodDefIdx(frame.method.clazz.constantPool, info.referenceIndex);
    String bsTargetMethod = Utils.getMethodNameByMethodDefIdx(frame.method.clazz.constantPool, info.referenceIndex);

    Integer descRef0 = bootstrapMethod.argsRefs[0];
    MethodType methodType0= (MethodType) frame.method.clazz.constantPool.infos[descRef0 - 1];
    String bstMethodDesc0 = Utils.getString(frame.method.clazz.constantPool, methodType0.descriptorIndex);

    Integer descRef = bootstrapMethod.argsRefs[2];
    MethodType methodType= (MethodType) frame.method.clazz.constantPool.infos[descRef - 1];
    String bstMethodDesc = Utils.getString(frame.method.clazz.constantPool, methodType.descriptorIndex);

    Class clazz = Heap.findClass(bsTargetClass);
    Method method = clazz.getLambdaMethod(bsTargetMethod);
    int maxLocals = method.maxLocals;

    String lcname = frame.method.clazz.name + "$" + frame.method.name + "$" + bsTargetClass + "$" + bsTargetMethod;
    List<Method> lcMehods = new ArrayList<>();
    Method lm = new Method(method.accessFlags, methodName, bstMethodDesc0, method.maxStacks, maxLocals + 1, null, null,
        null);
    lcMehods.add(lm);

    String format =Utils.genNativeMethodKey( lcname, lm.name, lm.descriptor);
    if (Heap.findMethod(format) == null) {
      Heap.registerMethod(format, (f) -> {
        Class bsc= Heap.findClass(bsTargetClass);
        Method bsm = bsc.getLambdaMethod(bsTargetMethod);

        List<String> args = bsm.getArgs();
        int bsSize = Utils.parseMethodDescriptor(bstMethodDesc).size();

        List<Object> argObjs = new ArrayList<>();
        for (int i = bsSize - 1; i >= 0; i--) {
          String arg = args.get(i);
          argObjs.add(Utils.pop(f, arg));
        }

        LambdaObject ref = (LambdaObject) f.popRef();
        Collections.reverse(argObjs);

        Frame newFrame = new Frame(bsm);

        for (Object arg : ref.args) {
          argObjs.add(0, arg);
        }

        int slotIdx = bsm.isStatic() ? 0 : 1;
        int aoi = bsm.isStatic() ? 0 : 1;
        for (int i = 0; i < args.size(); i++) {
          String arg = args.get(i);
          int si = Utils.setLocals(newFrame, slotIdx, arg, argObjs.get(aoi));
          slotIdx += si;
          aoi++;
        }

        if (!bsm.isStatic()) {
          newFrame.setRef(0, (Instance) argObjs.get(0));
        }

        f.thread.pushFrame(newFrame);
      });
    }

    Class lcClazz = new Class(1, lcname, "java/lang/Object", new ArrayList<>(), lcMehods, new ArrayList<>(), null, null, frame.method.clazz.classLoader, null);

    int realSize = method.getArgs().size();
    int bsSize = Utils.parseMethodDescriptor(bstMethodDesc).size();

    List<Object> args = new ArrayList<>(realSize - bsSize);
    while (realSize > bsSize) {
      String arg = method.getArgs().get(bsSize);
      args.add(Utils.pop(frame, arg));
      bsSize++;
    }
    if (!lm.isStatic()) {
      // this
      args.add(frame.popRef());
    }

    LambdaObject kObject = lcClazz.newLambdaObject(args);
    frame.pushRef(kObject);
  }

  @Override
  public String format() {
    return "invokedynamic " + methodName + " " + methodDescriptor + " " + bsIdx;
  }

}

