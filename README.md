# ponyvm
RV32分支，采用RV32I指令集

采用ESP32C3工具链
/Users/ponyma/Library/Arduino15/packages/esp32/tools/riscv32-esp-elf-gcc/esp-2021r2-patch5-8.4.0/bin
/Users/ponyma/Library/Arduino15/packages/esp32/tools/esptool_py/4.5.1/esptool

https://docs.espressif.com/projects/esptool/en/latest/esp32c3/advanced-topics/firmware-image-format.html

汇编

riscv32-esp-elf-gcc -S loop.c -o loop.s

编译

riscv32-esp-elf-gcc -march=rv32i -c loop.s -o loop.o

链接

riscv32-esp-elf-gcc -march=rv32i --specs=nosys.specs loop.o -o loop







