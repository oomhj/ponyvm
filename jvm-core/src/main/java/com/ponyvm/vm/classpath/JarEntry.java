package com.ponyvm.vm.classpath;

import com.ponyvm.vm.classfile.ClassFile;
import com.ponyvm.vm.classfile.ClassReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip/jar 类搜索实现类
 * 搜索压缩包中的class文件。比如jar或者zip
 */
public class JarEntry implements Entry {
  //压缩文件路径
  public final String path;

  public JarEntry(String path) {
    this.path = path;
  }

  @Override
  public ClassFile findClass(String name) {
    ZipFile file;
    try {
      file = new ZipFile(path);
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  //得到压缩包里.class后缀的文件
    ZipEntry entry = file.getEntry(name + ".class");
    if (entry == null) {
      return null;
    }
    //将class文件进行结构解析
    try (InputStream is = file.getInputStream(entry)) {
      ClassFile cf = ClassReader.read(new DataInputStream(is));
      cf.setSource(path.toString());

      return cf;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException();
    }
  }
}
