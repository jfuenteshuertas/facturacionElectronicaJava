/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author javierfuenteshuertas
 */
public final class Validador {
    public static boolean isEmail(String text){
        Pattern pat=Pattern.compile("^[\\w]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mat=pat.matcher(text);
        return mat.find();
    }
}
