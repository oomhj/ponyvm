package com.ponyvm.vm.classfile;

import com.ponyvm.vm.classfile.attribute.BootstrapMethods;
import com.ponyvm.vm.classfile.attribute.Code;
import com.ponyvm.vm.classfile.attribute.LineNumberTable;
import com.ponyvm.vm.classfile.attribute.SourceFile;
import com.ponyvm.vm.classfile.cp.ClassCp;
import com.ponyvm.vm.classfile.cp.DoubleCp;
import com.ponyvm.vm.classfile.cp.FieldDef;
import com.ponyvm.vm.classfile.cp.FloatCp;
import com.ponyvm.vm.classfile.cp.IntegerCp;
import com.ponyvm.vm.classfile.cp.InterfaceMethodDef;
import com.ponyvm.vm.classfile.cp.InvokeDynamic;
import com.ponyvm.vm.classfile.cp.LongCp;
import com.ponyvm.vm.classfile.cp.MethodDef;
import com.ponyvm.vm.classfile.cp.MethodHandle;
import com.ponyvm.vm.classfile.cp.MethodType;
import com.ponyvm.vm.classfile.cp.NameAndType;
import com.ponyvm.vm.classfile.cp.StringCp;
import com.ponyvm.vm.classfile.cp.Utf8;
import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.util.Utils;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 类文件的读取和解析
 */
public abstract class ClassReader {

  /**
   * 读class文件
   * @param path
   * @return
   * @throws IOException
   */
  public static ClassFile read(String path) throws IOException {

    try (InputStream is = new FileInputStream(path);
//        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream stm = new DataInputStream(is) {
        }) {
      return read(stm);
    }
  }

  /**
   * 解析class文件
   * @param is
   * @return 类结构
   * @throws IOException
   */
  public static ClassFile read(DataInputStream is) throws IOException {
    //读前四个字节为魔数 并返回读取的整数值
    int magic = is.readInt();
    //再读两个字节次版本号
    int minorVersion = is.readUnsignedShort();
    //继续读两个字节为主版本号
    int majorVersion = is.readUnsignedShort();

    //常量池表大小
    int cpSize = is.readUnsignedShort();
    //表头给出的常量池大小比实际大1。假设表头给出的值是n，那么常
    //量池的实际大小是n–1。第二，有效的常量池索引是1~n–1。0是无效索引，表示不指向任何常量
    ConstantPool constantPool = readConstantPool(is, cpSize - 1);

    //类访问标志
    int accessFlag = is.readUnsignedShort();
    //类索引
    int thisClass = is.readUnsignedShort();
    //超类索引
    int superClass = is.readUnsignedShort();
    //接口数
    int interfaceCount = is.readUnsignedShort();
    //解析接口结构类
    Interfaces interfaces = readInterfaces(is, interfaceCount, constantPool);

    //字段数
    int fieldCount = is.readUnsignedShort();
    //解析字段结构
    Fields fields = readFields(is, fieldCount, constantPool);

    //方法数
    int methodCount = is.readUnsignedShort();
    //解析方法结构
    Methods methods = readMethods(is, methodCount, constantPool);

    //属性数
    int attributeCount = is.readUnsignedShort();
    //解析属性结构
    Attributes attributes = readAttributes(is, attributeCount, constantPool);

    //放回一个类结构对象
    return new ClassFile(
        magic,
        minorVersion,
        majorVersion,
        cpSize,
        constantPool,
        accessFlag,
        thisClass,
        superClass,
        interfaceCount,
        interfaces,
        fieldCount,
        fields,
        methodCount,
        methods,
        attributeCount,
        attributes
    );
  }

  private static Fields readFields(DataInputStream is, int fieldCount, ConstantPool constantPool)
      throws IOException {
    FieldInfo[] fieldInfos = new FieldInfo[fieldCount];
    for (int i = 0; i < fieldCount; i++) {
      int accessFlag = is.readUnsignedShort();
      int nameIndex = is.readUnsignedShort();
      int descriptorIndex = is.readUnsignedShort();
      int attributesCount = is.readUnsignedShort();

      Attributes attributes = readAttributes(is, attributesCount, constantPool);

      ConstantInfo info = constantPool.infos[nameIndex - 1];
      String name = ((Utf8) info).getString();

      String descriptor = ((Utf8) constantPool.infos[descriptorIndex - 1]).getString();

      FieldInfo fieldInfo = new FieldInfo(accessFlag, name, new Descriptor(descriptor), attributes);
      fieldInfos[i] = fieldInfo;
    }
    return new Fields(fieldInfos);
  }

  private static Interfaces readInterfaces(DataInputStream is, int interfaceCount, ConstantPool cp)
      throws IOException {
    Interface[] interfaces = new Interface[interfaceCount];
    for (int i = 0; i < interfaceCount; i++) {
      int idx = is.readUnsignedShort();
      String name = Utils.getClassName(cp, idx);
      interfaces[i] = new Interface(name);
    }
    return new Interfaces(interfaces);
  }

  //method_info {
//    u2             access_flags;
//    u2             name_index;
//    u2             descriptor_index;
//    u2             attributes_count;
//    attribute_info attributes[attributes_count];
//    }
  private static Methods readMethods(DataInputStream is, int methodCount,
      ConstantPool constantPool) throws IOException {
    Methods methods = new Methods(methodCount);

    for (int i = 0; i < methodCount; i++) {
      int accessFlag = is.readUnsignedShort();
      int nameIndex = is.readUnsignedShort();
      int descriptorIndex = is.readUnsignedShort();
      int attributesCount = is.readUnsignedShort();

      Attributes attributes = readAttributes(is, attributesCount, constantPool);

      ConstantInfo info = constantPool.infos[nameIndex - 1];
      String name = ((Utf8) info).getString();

      String descriptor = ((Utf8) constantPool.infos[descriptorIndex - 1]).getString();

      methods.methodInfos[i] = new MethodInfo(accessFlag, name, new Descriptor(descriptor), attributes);
    }
    return methods;
  }

  private static ConstantPool readConstantPool(DataInputStream is, int cpSize) throws IOException {
    ConstantPool constantPool = new ConstantPool(cpSize);
    for (int i = 0; i < cpSize; i++) {
      int tag = is.readUnsignedByte();

      int infoEnum = tag;

      ConstantInfo info = null;
      switch (infoEnum) {
        case ConstantPoolInfoEnum.CONSTANT_Fieldref:
          info = new FieldDef(infoEnum, is.readUnsignedShort(), is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_Methodref:
          info = new MethodDef(infoEnum, is.readUnsignedShort(), is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_InterfaceMethodref:
          info = new InterfaceMethodDef(infoEnum, is.readUnsignedShort(), is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_String:
          info = new StringCp(infoEnum, is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_Class:
          info = new ClassCp(infoEnum, is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_NameAndType:
          info = new NameAndType(infoEnum, is.readUnsignedShort(), is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_Utf8:
          int length = is.readUnsignedShort();
          byte[] bytes = Utils.readNBytes(is, length);
          info = new Utf8(infoEnum, bytes);
          break;
        case ConstantPoolInfoEnum.CONSTANT_MethodHandle:
          info = new MethodHandle(infoEnum, is.readUnsignedByte(), is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_MethodType:
          info = new MethodType(infoEnum, is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_InvokeDynamic:
          info = new InvokeDynamic(infoEnum, is.readUnsignedShort(), is.readUnsignedShort());
          break;
        case ConstantPoolInfoEnum.CONSTANT_Integer:
          info = new IntegerCp(infoEnum, is.readInt());
          break;
        case ConstantPoolInfoEnum.CONSTANT_Long:
          info = new LongCp(infoEnum, is.readLong());
          break;
        case ConstantPoolInfoEnum.CONSTANT_Float:
          info = new FloatCp(infoEnum, is.readFloat());
          break;
        case ConstantPoolInfoEnum.CONSTANT_Double:
          info = new DoubleCp(infoEnum, is.readDouble());
          break;
      }
      if (info == null) {
        throw new UnsupportedOperationException("un parse cp " + infoEnum);
      }
      constantPool.infos[i] = info;
      if (info.infoEnum == ConstantPoolInfoEnum.CONSTANT_Double
          || info.infoEnum == ConstantPoolInfoEnum.CONSTANT_Long) {
        i++;
      }
    }
    return constantPool;
  }

//  public static String byteArrayToHex(byte[] a) {
//    StringBuilder sb = new StringBuilder(a.length * 2);
//    for (byte b : a) {
//      sb.append(String.format("%02x", b));
//    }
//    return sb.toString();
//  }

  //  attribute_info {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u1 info[attribute_length];
//  }
  private static Attributes readAttributes(DataInputStream is, int attributeCount,
      ConstantPool constantPool)
      throws IOException {
    Attributes attributes = new Attributes(attributeCount);

    for (int i = 0; i < attributeCount; i++) {
      Attribute attribute = null;
      int attributeNameIndex = is.readUnsignedShort();
      String attributeName = Utils.getString(constantPool, attributeNameIndex);

      int attributeLength = is.readInt();
      switch (attributeName) {
        case AttributeEnum.SourceFile:
          int sourceFileIndex = is.readUnsignedShort();
          String file = Utils.getString(constantPool, sourceFileIndex);
          attribute = new SourceFile(file);
          break;
        case AttributeEnum.Code:
          int maxStack = is.readUnsignedShort();
          int maxLocals = is.readUnsignedShort();

          int codeLength = is.readInt();
          byte[] byteCode = Utils.readNBytes(is, codeLength);

          Instruction[] instructions = readByteCode(byteCode, constantPool);

          int exceptionTableLength = is.readUnsignedShort();
          Exception[] exceptions = new Exception[exceptionTableLength];
          for (int i1 = 0; i1 < exceptionTableLength; i1++) {
            int etsp = is.readUnsignedShort();
            int etep = is.readUnsignedShort();
            int ethp = is.readUnsignedShort();
            int ctIdx = is.readUnsignedShort();

            // null => catch any exception
            String etClassname = null;
            if (ctIdx != 0) {
              etClassname = Utils.getClassName(constantPool, ctIdx);
            }

            Exception exception = new Exception(etsp, etep, ethp, etClassname);
            exceptions[i1] = exception;
          }
          ExceptionTable exceptionTable = new ExceptionTable(exceptions);
          int codeAttributeCount = is.readUnsignedShort();
          Attributes codeAttributes = readAttributes(is, codeAttributeCount, constantPool);

          attribute = new Code(maxStack, maxLocals, instructions, exceptionTable, codeAttributes);
          break;
        case AttributeEnum.LineNumberTable:
          int length = is.readUnsignedShort();
          LineNumberTable.Line[] lines = new LineNumberTable.Line[length];
          for (int i1 = 0; i1 < length; i1++) {
            lines[i1] = new LineNumberTable.Line(is.readUnsignedShort(), is.readUnsignedShort());
          }
          attribute = new LineNumberTable(lines);
          break;
        case AttributeEnum.BootstrapMethods:
          int bsmLen = is.readUnsignedShort();
          BootstrapMethods.BootstrapMethod[] bootstrapMethods = new BootstrapMethods.BootstrapMethod[bsmLen];
          for (int i1 = 0; i1 < bsmLen; i1++) {
            int bsmr = is.readUnsignedShort();
            int nbma = is.readUnsignedShort();
            Integer[] args = new Integer[nbma];
            for (int i2 = 0; i2 < nbma; i2++) {
              args[i2] = is.readUnsignedShort();
            }

            bootstrapMethods[i1] = new BootstrapMethods.BootstrapMethod(bsmr, args);
          }

          attribute = new BootstrapMethods(bootstrapMethods);
          break;
        default:
          byte[] bytes = Utils.readNBytes(is, attributeLength);
//          System.out.println("bytes = " + byteArrayToHex(bytes));
      }

      attributes.attributes[i] = attribute;
    }

    return attributes;
  }

  public static Instruction[] readByteCode(byte[] byteCode, ConstantPool constantPool)
      throws IOException {
    List<Instruction> instructions = new ArrayList<>();
    try (MyDataInputStream stm = new MyDataInputStream(new MyByteArrayInputStream(byteCode))) {
      while (stm.available() > 0) {
        int opCode = stm.readUnsignedByte();
        try {
          Instruction inst = InstructionReader.read(opCode, stm, constantPool);
          if (inst == null) {
            System.out.println(Integer.toHexString(opCode));
            break;
          }
          instructions.add(inst);
        } catch (java.lang.Exception e) {
          e.printStackTrace();
        }
      }
    }
    Instruction[] ret = new Instruction[instructions.size()];
    instructions.toArray(ret);
    return ret;
  }
}
