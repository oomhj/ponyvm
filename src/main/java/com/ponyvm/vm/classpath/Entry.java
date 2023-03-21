package com.ponyvm.vm.classpath;

import com.ponyvm.vm.classfile.ClassFile;

/**
 * 类搜索接口，有不同的实现类
 */
public interface Entry {

  /**
   * 负责寻找和加载class文件
   *
   * @param name 类名或者类路径
   * @return 一个类结构文件对象
   */
  ClassFile findClass(String name);
}
