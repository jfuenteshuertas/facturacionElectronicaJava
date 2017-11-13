/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
 
/**
 *
 * @author www.javadb.com
 */
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {
 
    public boolean handleMessage(SOAPMessageContext smc) {
 
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
 
        if (outboundProperty) {
 
            SOAPMessage message = smc.getMessage();
 
            try {
 
                SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
                if (envelope.getHeader() != null) {
                      envelope.getHeader().detachNode();
                }
                
                envelope.addAttribute(new QName("xmlns:ser"), "http://service.sunat.gob.pe");
                envelope.addAttribute(new QName("xmlns:wsse"),"http://docs.oasis- open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
                SOAPHeader header = envelope.addHeader();
 
                SOAPElement security =
                        header.addChildElement("Security", "wsse");// "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
 
                SOAPElement usernameToken =
                        security.addChildElement("UsernameToken", "wsse");
                //usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
 
                SOAPElement username =
                        usernameToken.addChildElement("Username", "wsse");
                username.addTextNode("20602138390MODDATOS");
 
                SOAPElement password =
                        usernameToken.addChildElement("Password", "wsse");
                //password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
                password.addTextNode("moddatos");
                
                
                message.saveChanges();
                //Print out the outbound SOAP message to System.out
                //System.out.println("IF");
               // message.writeTo(System.out);
                 
            } catch (Exception e) {
                e.printStackTrace();
            }
 
        } else {
            try {
                 
                //This handler does nothing with the response from the Web Service so
                //we just print out the SOAP message.
                SOAPMessage message = smc.getMessage();
                //System.out.println("Else");
                //message.writeTo(System.out);
                
 
            } catch (Exception ex) {
                ex.printStackTrace();
            } 
        }
 
 
        return outboundProperty;
 
    }
 
    public Set getHeaders() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }
 
    public boolean handleFault(SOAPMessageContext context) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }
 
    public void close(MessageContext context) {
    //throw new UnsupportedOperationException("Not supported yet.");
    }

}

