import com.ponyvm.soc.board.PonySoc;
import com.ponyvm.soc.board.Soc;

import java.io.File;
import java.io.IOException;

public class VMTest {
    public static void main(String[] args) throws IOException {
        Soc vm = new PonySoc();
        vm.launchROM(new File(ClassLoader.getSystemResource("pi-imc.bin").getFile()));
    }
}
