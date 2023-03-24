package com.ponyvm.soc.board;

import java.io.File;
import java.io.IOException;

public interface Soc {
    int launchROM(File rom) throws IOException;
}
