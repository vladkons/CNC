package cnc.util;

import java.io.File;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;


public class CNCBase64 {
    public CNCBase64() {
        super();
    }


    public static void main(String[] args) {

long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            try {


                Base64 codec = new Base64();
                
                byte[] original = Files.readAllBytes(new File("D:\\CNCDemo\\Images\\bridge.jpg").toPath());
                String encodedString = Base64.encodeBase64String(original);
               
              

                System.out.println("Original file size: " + original.length + "\r\n" +
                                    "Encoded string size: " + encodedString.length() + "\r\n");

                Path encecodedFile = Paths.get("D:\\CNCDemo\\Images\\bridge_encoded.txt");
                Files.createFile(encecodedFile);
                Files.write(encecodedFile, encodedString.getBytes());
                
                byte[] decoded = Base64.decodeBase64(encodedString);
                
                Path decodedFile = Paths.get("D:\\CNCDemo\\Images\\bridge_decoded.jpg");
                Files.createFile(decodedFile);
                Files.write(decodedFile, decoded);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println( (System.currentTimeMillis() - start))  ;

    }
}
