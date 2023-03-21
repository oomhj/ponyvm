package com.ponyvm.vm.classfile;

import com.ponyvm.vm.classfile.attribute.BootstrapMethods;
import com.ponyvm.vm.classfile.attribute.SourceFile;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1">
 * </a>https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1</a>
 * <p>
 *     类结构用于解析类
 */
public class ClassFile {

  public final int magic;//魔数,确认是否能让虚拟机接受的class文件 在java中 通常为0xCAFEBABE（咖啡宝贝？）
  public final int minorVersion;//jvm的次版本号
  public final int majorVersion;//jvm的主版本号
  public final int constantPoolSize;//常量池大小 用来解析后面的常量池
  public final ConstantPool cpInfo;//常量池结构
  public final int accessFlags;//类访问标志,16位的“bitmask”指出class文件定义的是类还是接口，访问级别是public还是private，
  public final int thisClass;//类索引,class文件存储的类名类似完全限定名
  public final int superClass;//父类索引,同上
  public final int interfacesCount;//接口索引数
  public final Interfaces interfaces;//接口索引表，给出该类实现的所有接口的名字
  public final int fieldCount;//字段数
  public final Fields fields;//字段结构，存储字段信息
  public final int methodsCount;//方法数
  public final Methods methods;//方法结构，存储方法信息
  public final int attributesCount;//属性数
  public final Attributes attributes;//属性结构，存储属性信息


  // ext info
  private String source;

  public ClassFile(int magic, int minorVersion, int majorVersion, int constantPoolSize,
      ConstantPool cpInfo, int accessFlags, int thisClass, int superClass, int interfacesCount,
      Interfaces interfaces, int fieldCount, Fields fields, int methodsCount, Methods methods, int attributesCount,
      Attributes attributes) {
    this.magic = magic;
    this.minorVersion = minorVersion;
    this.majorVersion = majorVersion;
    this.constantPoolSize = constantPoolSize;
    this.cpInfo = cpInfo;
    this.accessFlags = accessFlags;
    this.thisClass = thisClass;
    this.superClass = superClass;
    this.interfacesCount = interfacesCount;
    this.interfaces = interfaces;
    this.fieldCount = fieldCount;
    this.fields = fields;
    this.methodsCount = methodsCount;
    this.methods = methods;
    this.attributesCount = attributesCount;
    this.attributes = attributes;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getSource() {
    return source;
  }

  public String getSourceFile() {
    for (Attribute attribute : this.attributes.attributes) {
      if (attribute instanceof SourceFile) {
        return ((SourceFile) attribute).name;
      }
    }
    return "unknown";
  }

  public BootstrapMethods getBootstrapMethods() {
    for (Attribute attribute : attributes.attributes) {
      if (attribute instanceof BootstrapMethods) {
        return (BootstrapMethods) attribute;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "ClassFile{" +
      "\n, magic=" + magic +
      "\n, minorVersion=" + minorVersion +
      "\n, majorVersion=" + majorVersion +
      "\n, constantPoolSize=" + constantPoolSize +
      "\n, cpInfo=" + cpInfo +
      "\n, accessFlags=" + accessFlags +
      "\n, thisClass=" + thisClass +
      "\n, superClass=" + superClass +
      "\n, interfacesCount=" + interfacesCount +
      "\n, interfaces=" + interfaces +
      "\n, fieldCount=" + fieldCount +
      "\n, fields=" + fields +
      "\n, methodsCount=" + methodsCount +
      "\n, methods=" + methods +
      "\n, attributesCount=" + attributesCount +
      "\n, attributes=" + attributes +
      "\n}";
  }
}
