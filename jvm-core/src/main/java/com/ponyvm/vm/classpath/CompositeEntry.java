package com.ponyvm.vm.classpath;

import com.ponyvm.vm.classfile.ClassFile;
import java.util.List;

/** 组合模式
 * 多个类路径 搜索实现类
 * 把每个小路径都转换成具体的搜索实例
 */
public class CompositeEntry implements Entry {

  private final List<Entry> entries;

  public CompositeEntry(List<Entry> entries) {
    this.entries = entries;
  }

  @Override
  public ClassFile findClass(String name) {
    for (Entry entry : entries) {
      ClassFile cf = entry.findClass(name);
      if (cf != null) {
        return cf;
      }
    }
    return null;
  }
}
