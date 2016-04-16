package com.nekoz.proj.baiduvoicerecg;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Title      : Vox.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Nekoz Yukinshou
 * @version 1.0
 */

class Vox extends File {
    public Vox(String pathname) {
        super(pathname);
    }

    public Vox(String parent, String child) {
        super(parent, child);
    }

    public String toBase64() {
        InputStream in = null;
        byte[] data = null;
        Logger logger = Logger.getLogger("!toBase64");

        try {
            in = new FileInputStream(this.getAbsoluteFile());
            data = new byte[in.available()];
            in.read(data);
            in.close();

            String result = DatatypeConverter.printBase64Binary(data);
            logger.finest("Base64 Encode Result:\n" + result);
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
}
