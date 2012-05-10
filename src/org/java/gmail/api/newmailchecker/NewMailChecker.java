/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.java.gmail.api.newmailchecker;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import org.java.gmail.api.GmailJavaApi;
import org.sms.jfreesms.SMS;
import org.sms.jfreesms.exception.NotAuthenticatedException;
import org.sms.jfreesms.way2sms.Way2SMS;

/**
 *
 * @author anantha
 */
public class NewMailChecker implements Runnable {

    private Folder storeFolder = null;
    private boolean canCheck = true;
    private String smsUserName=null;
    private String smsPassword=null;
    private String mobileNo = null;
    private String fromemail = null;
    
    public NewMailChecker(Folder storeFolder,String fromemail,String smsUserName,String smsPassword,String mobileNo) {
        this.storeFolder = storeFolder;
        this.fromemail = fromemail;
        this.smsUserName = smsUserName;
        this.smsPassword = smsPassword;
        this.mobileNo = mobileNo;
    }

    public boolean isCanCheck() {
        return canCheck;
    }

    public void setCanCheck(boolean canCheck) {
        this.canCheck = canCheck;
    }

    public Folder getStoreFolder() {
        return storeFolder;
    }

    public void setStoreFolder(Folder storeFolder) {
        this.storeFolder = storeFolder;
    }

    @Override
    public void run() {
        
        SMS sms = new Way2SMS();
        sms.login(smsUserName, smsPassword);
        
        UIDFolder uidfolder = (UIDFolder) storeFolder;
            long uid = 0;
            Message lastMessage = null;
            Message[] newmails = null;
            try {
                Message firstmsg = storeFolder.getMessage(storeFolder.getMessageCount());
                uid = uidfolder.getUID(firstmsg);

                newmails = uidfolder.getMessagesByUID(uid + 1, UIDFolder.LASTUID);
                lastMessage =newmails[0];
            } catch (MessagingException ex) {
                Logger.getLogger(GmailJavaApi.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(newmails.length);
            
        while (canCheck == true) {
            
            for (Message newmail : newmails) {
                if(newmail==lastMessage)
                {
                    continue;
                }
                try {
                    lastMessage = newmail;
                    System.out.println(newmail.getSubject());
                    uid = uidfolder.getUID(newmail);
                    
                    if(newmail.getFrom()[0].toString().contains(fromemail))
                    {
                        System.out.println("Sending SMS...");
                        try {
                            sms.send(mobileNo, newmail.getSubject());
                        } catch (NotAuthenticatedException ex) {
                            Logger.getLogger(NewMailChecker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                } catch (MessagingException ex) {
                    Logger.getLogger(GmailJavaApi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Thread.sleep(12000);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(NewMailChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                newmails = uidfolder.getMessagesByUID(uid + 1, UIDFolder.LASTUID);
            } catch (MessagingException ex) {
                Logger.getLogger(NewMailChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
