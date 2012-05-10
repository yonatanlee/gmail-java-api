/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.java.gmail.api;

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import org.java.gmail.api.newmailchecker.NewMailChecker;

/**
 *
 * @author anantha
 */
public class GmailJavaApi {

    private Properties props = null;
    private Session session = null;
    private Store store = null;
    private String host = "imap.gmail.com";
    private Folder storeFolder = null;

    public GmailJavaApi() {
        props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        session = Session.getDefaultInstance(props, null);
        store = getStore("imaps");
    }

    public GmailJavaApi(Properties props) {
        this();

        if (props != null) {
            this.props = props;
        }


    }

    public GmailJavaApi(Properties props, Store store) {
        this(props);

        if (store != null) {
            this.store = store;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private void connect(String username, String password) throws MessagingException {

        store.connect(host, username, password);

    }

    private Store getStore(String protocol) {
        Store store = null;
        try {
            store = session.getStore(protocol);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(GmailJavaApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return store;
    }

    public boolean login(String username, String password) {
        boolean status = true;
        try {
            connect(username, password);
        } catch (MessagingException ex) {
            Logger.getLogger(GmailJavaApi.class.getName()).log(Level.SEVERE, null, ex);
            status = false;
        }

        return status;
    }

    public ArrayList<String> getFolderNames() {
        ArrayList<String> foldernames = new ArrayList<String>();
        Folder[] folderList = null;
        try {
            folderList = store.getDefaultFolder().list("*");
        } catch (MessagingException ex) {
            Logger.getLogger(GmailJavaApi.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < folderList.length; i++) {
            System.out.println(folderList[i].getFullName());
            foldernames.add(folderList[i].getFullName());
        }
        return foldernames;
    }

    public void readmailbox(String folder) {
        try {
            storeFolder = store.getFolder(folder);
            storeFolder.open(Folder.READ_ONLY);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        try {
            for (int start = 1; start <= storeFolder.getMessageCount(); start += 101) {
                Message messages[] = null;

                int end = 0;
                if (start + 100 > storeFolder.getMessageCount()) {
                    end = storeFolder.getMessageCount();
                } else {
                    end = start + 100;
                }
                messages = storeFolder.getMessages(start, end);

                for(Message message:messages)
                {
                    for(Address address:message.getFrom())
                    {
                        
                    }
                }
                
            }
        } catch (MessagingException ex) {
            Logger.getLogger(GmailJavaApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void checkNewMail(String folder,String smsUserName,String smsPassword,String mobileNo) {
        try {
            storeFolder = store.getFolder(folder);
            storeFolder.open(Folder.READ_ONLY);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        
        NewMailChecker newMailChecker = new NewMailChecker(storeFolder,smsUserName,smsPassword,mobileNo);
        Thread newMailCheckerThread = new Thread(newMailChecker);
        newMailCheckerThread.start();
    }
}
