/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.java.gmail.api.test;

import org.java.gmail.api.GmailJavaApi;

/**
 *
 * @author anantha
 */
public class GmailTest {
    
    public static void main(String[] ar)
    {
        GmailJavaApi gmailapi = new GmailJavaApi();
        
        boolean status = gmailapi.login("interviewstreethacker@gmail.com", "interviewstreet1!");
        
        if(status == true)
        {
            System.out.println("Logged in successfully!!");
        }
        else
        {
            System.out.println("Logged in failed!!");
        }
        
        gmailapi.getFolderNames();
        gmailapi.checkNewMail("inbox","9994955340","anankris","8374566903");
    }
}
