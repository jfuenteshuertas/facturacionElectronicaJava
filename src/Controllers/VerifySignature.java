/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
/**
 *
 * @author javierfuenteshuertas
 */
public class VerifySignature {
    public static boolean validarCertificado(String signatureFileName) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException, XMLSecurityException{
        org.apache.xml.security.Init.init();
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
	File			f	= new File(signatureFileName);
	DocumentBuilder db	= dbf.newDocumentBuilder();
	Document	 doc	= db.parse(new java.io.FileInputStream(f));
	Element		 sigElement = (Element) doc.getElementsByTagName("Signature").item(0);
	XMLSignature signature  = new XMLSignature(sigElement, f.toURL().toString());
        KeyInfo keyInfo = signature.getKeyInfo();
	if (keyInfo != null) {
            X509Certificate cert = keyInfo.getX509Certificate();
            if (cert != null) {
            // Validamos la firma usando un certificado X509
		if (signature.checkSignatureValue(cert)){
                    System.out.println("Válido según el certificado");	
                    return true;
		} 
                else {
                    return false;
		}
            }
            else {
		// No encontramos un Certificado intentamos validar por la cláve pública
		PublicKey pk = keyInfo.getPublicKey();
		if (pk != null) {
		// Validamos usando la clave pública
                    if (signature.checkSignatureValue(pk)){
			System.out.println("Válido según la clave pública");
                        return true;
                    }
                    else {
			System.out.println("Inválido según la clave pública");	
                        return false;
                    }
		}
                else {
                    System.out.println("No podemos validar, tampoco hay clave pública");
                    return false;
		}
            }
	}
        else {
            System.out.println("No ha sido posible encontrar el KeyInfo");
            return false;
	}
    }
}
