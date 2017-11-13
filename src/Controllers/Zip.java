/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author javierfuenteshuertas
 */
public class Zip {
    private static final int BUFFER_SIZE = 1024;
    public static void Zippear(String pFile, String pZipFile) throws Exception {
		// objetos en memoria
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipOutputStream zipos = null;
 
		// buffer
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			// fichero a comprimir
			fis = new FileInputStream(pFile);
			// fichero contenedor del zip
			fos = new FileOutputStream(pZipFile);
			// fichero comprimido
			zipos = new ZipOutputStream(fos);
			ZipEntry zipEntry = new ZipEntry(pFile);
			zipos.putNextEntry(zipEntry);
			int len = 0;
			// zippear
			while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1)
				zipos.write(buffer, 0, len);
			// volcar la memoria al disco
			zipos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			// cerramos los files
			zipos.close();
			fis.close();
			fos.close();
		} // end try
	} // end Zippear
    public static void UnZip(String zipFilePath, String destDirectory) throws Exception {
        File destDir = new File(zipFilePath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
                System.out.println(filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                System.out.println(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    } // end UnZip
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
