package com.ponyvm.vm.classloader;

import com.ponyvm.vm.classfile.ClassFile;
import com.ponyvm.vm.classfile.FieldInfo;
import com.ponyvm.vm.classfile.Interface;
import com.ponyvm.vm.classfile.MethodInfo;
import com.ponyvm.vm.classfile.attribute.BootstrapMethods;
import com.ponyvm.vm.classfile.attribute.Code;
import com.ponyvm.vm.classpath.Entry;
import com.ponyvm.vm.jmm.heap.Heap;
import com.ponyvm.vm.jmm.heap.Class;
import com.ponyvm.vm.jmm.heap.Field;
import com.ponyvm.vm.jmm.heap.Method;
import com.ponyvm.vm.jmm.heap.Instance;
import com.ponyvm.vm.jmm.heap.NativeMethod;
import com.ponyvm.vm.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ClassLoader {

  private final String name;
  private final Entry entry;

  public ClassLoader(String name, Entry entry) {
    this.name = name;
    this.entry = entry;
  }

  public void loadPrimitiveClass(String name) {
    Class cache = Heap.findClass(name);
    if (cache != null) {
      return;
    }
    Class cls = new Class(1, name, this);
    Instance metaCls = Heap.findClass("java/lang/Class").newInstance();
    cls.setRuntimeClass(metaCls);
    metaCls.setMetaClass(cls);

    doRegister(cls);
  }

  public void loadPrimitiveArrayClass(String name) {
    Class cache = Heap.findClass(name);
    if (cache != null) {
      return;
    }
    Class cls = new Class(1, name, this);
    Instance metaCls = Heap.findClass("java/lang/Class").newInstance();
    cls.setRuntimeClass(metaCls);
    metaCls.setMetaClass(cls);

    doRegister(cls);
  }

  public Class loadClass(String name) {
    Class cache = Heap.findClass(name);
    if (cache != null) {
      return cache;
    }

    Class clazz = doLoadClass(name);
    doRegister(clazz);

    return clazz;
  }

  public void doRegister(Class clazz) {
    Heap.registerClass(clazz.name, clazz);
    for (Method method : clazz.methods) {
      if (method.isNative()) {
        String key = Utils.genNativeMethodKey(method.clazz.name, method.name, method.descriptor);
        NativeMethod nm = Heap.findMethod(key);
        if (nm == null) {
          System.err.println("not found native method " + key + " " + method);
        }
      }
    }
  }

  public Class doLoadClass(String name) {
    ClassFile clazz = entry.findClass(name);
    Class aClass = doLoadClass(name, clazz);

    // superclass
    if (aClass.superClassName != null) {
      aClass.setSuperClass(this.loadClass(aClass.superClassName));
    }

    if (Heap.findClass("java/lang/Class") != null) {
      Instance rcs = Heap.findClass("java/lang/Class").newInstance();
      aClass.setRuntimeClass(rcs);
      rcs.setMetaClass(aClass);
    }

    return aClass;
  }

  public Class doLoadClass(String name, ClassFile classFile) {
    List<Method> methods = new ArrayList<>();
    for (MethodInfo methodInfo : classFile.methods.methodInfos) {
      methods.add(this.map(methodInfo));
    }
    List<Field> fields = new ArrayList<>();
    for (FieldInfo fieldInfo : classFile.fields.fieldInfos) {
      fields.add(this.map(fieldInfo));
    }

    // field interfaceInit
    for (Field it : fields) {
      if (it.isStatic()) {
        it.init();
      }
    }

    int scIdx = classFile.superClass;
    String superClassName = null;
    if (scIdx != 0) {
      superClassName = Utils.getClassName(classFile.cpInfo, scIdx);
    }

    List<String> interfaceNames = new ArrayList<>();
    if (classFile.interfaces.interfaces.length != 0) {
      for (Interface anInterface : classFile.interfaces.interfaces) {
        interfaceNames.add(anInterface.getName());
      }
    }

    BootstrapMethods bootstrapMethods = classFile.getBootstrapMethods();

    return new Class(classFile.accessFlags, name, superClassName, interfaceNames, methods, fields,
        bootstrapMethods, classFile.cpInfo, this, classFile);
  }

  public Method map(MethodInfo cfMethodInfo) {
    Code code = cfMethodInfo.getCode();
    if (code == null) {
      return new Method(cfMethodInfo.accessFlags, cfMethodInfo.name, cfMethodInfo.descriptor.descriptor, 0, 0,
          null, null, cfMethodInfo.getLineNumber());
    }
    return new Method(cfMethodInfo.accessFlags, cfMethodInfo.name, cfMethodInfo.descriptor.descriptor,
        code.maxStacks, code.maxLocals, code.getInstructions(), code.exceptionTable,
        cfMethodInfo.getLineNumber());
  }

  public Field map(FieldInfo fieldInfo) {
    return new Field(fieldInfo.accessFlags, fieldInfo.name, fieldInfo.descriptor.descriptor);
  }

  public String getName() {
    return name;
  }
}
