package com.ponyvm.vm.classfile.attribute;

import com.ponyvm.vm.classfile.Attribute;

public class SourceFile extends Attribute {

  public final String name;

  //SourceFile_attribute {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u2 sourcefile_index;
//  }
  public SourceFile(String name) {
    this.name = name;
  }
}
