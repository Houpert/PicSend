package mailSender;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import dataclass.PictureData;

/**
 * Created by yhoupert on 15/01/15.
 */
public class MailFeedTask extends AsyncTask<Void, Void, Boolean> {
    static final String psw = "picSend_50";
    static final String mailServe = "picSendServer@gmail.com";
    private Mail m = new Mail(mailServe, psw);

    private Context context;
    private PictureData data;
    private Exception exception;

    public MailFeedTask(Context applicationContext, PictureData picData) {
        context = applicationContext;
        data = picData;
    }

    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        System.out.println(params);
        return sendEmail();
    }

    public boolean sendEmail(){
        String[] toArr = {data.getEmail()};
        m.setTo(toArr);
        m.setFrom(mailServe);
        m.setSubject("PicSend " + data.getDate());
        m.setBody(data.toString());

        try {
            m.addAttachment(data.getFilePath());
            if(m.send()) {
                System.out.println("Take");
                return true;
            } else {
                System.out.println("Error");
                return false;
            }
        } catch(Exception e) {
            return false;
        }

    }
}