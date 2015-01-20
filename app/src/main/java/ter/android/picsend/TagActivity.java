package ter.android.picsend;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import dataclass.PictureData;
import mailSender.MailFeedTask;

/**
 * Created by yhoupert on 17/01/15.
 */
public class TagActivity extends ActionBarActivity {

    private static final String ROOT_ELEMENT = "root";
    private static final String LABEL_ELEMENT = "label";
    private static final String TYPE1_ELEMENT = "type1";
    private static final String TYPE2_ELEMENT = "type2";
    private static final String TYPE3_ELEMENT = "type3";
    private static final String TYPE4_ELEMENT = "type4";
    private static final String TYPE5_ELEMENT = "type5";

    private PictureData picData = new PictureData();

    private NodeList currentNodeList1;
    private NodeList currentNodeList2;
    private NodeList currentNodeList3;
    private NodeList currentNodeList4;
    private NodeList currentNodeList5;

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;

    private EditText commentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_layout);
        commentText = (EditText) findViewById(R.id.tag_ed_comment);
        picData = (PictureData) getIntent().getSerializableExtra("picData");
        Log.v("DEBUG",picData.toString());

        initSpinner();

        AssetManager manager = getAssets();
        InputStream stream;

        try {
            stream = manager.open("tag_content_values.xml");
            Document doc = this.getDocument(stream);

            Node node = doc.getFirstChild();
            if (!ROOT_ELEMENT.equals(node.getNodeName())) {
                throw new Exception("root element not found");
            }

            currentNodeList1 = node.getChildNodes();

            List<String> spinnerList = buildCurrentSpinnerList(currentNodeList1, TYPE1_ELEMENT);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
            toastMessage("XML parsing error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initSpinner() {
        spinner1 = (Spinner) findViewById(R.id.spinner01);
        spinner2 = (Spinner) findViewById(R.id.spinner02);
        spinner3 = (Spinner) findViewById(R.id.spinner03);
        spinner4 = (Spinner) findViewById(R.id.spinner04);
        spinner5 = (Spinner) findViewById(R.id.spinner05);

        spinner2.setVisibility(View.INVISIBLE);
        spinner3.setVisibility(View.INVISIBLE);
        spinner4.setVisibility(View.INVISIBLE);
        spinner5.setVisibility(View.INVISIBLE);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                currentNodeList2 = currentNodeList1.item((position * 2) + 1).getChildNodes();

                List<String> spinnerList = buildCurrentSpinnerList(currentNodeList2, TYPE2_ELEMENT);
                if (spinnerList != null && !spinnerList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentView.getContext(),
                            android.R.layout.simple_spinner_item, spinnerList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    spinner2.setVisibility(View.VISIBLE);
                } else {
                    spinner2.setVisibility(View.INVISIBLE);
                }
                spinner3.setVisibility(View.INVISIBLE);
                spinner4.setVisibility(View.INVISIBLE);
                spinner5.setVisibility(View.INVISIBLE);
                currentNodeList3 = null;
                currentNodeList4 = null;
                currentNodeList5 = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                currentNodeList3 = currentNodeList2.item((position * 2) + 1).getChildNodes();

                List<String> spinnerList = buildCurrentSpinnerList(currentNodeList3, TYPE3_ELEMENT);
                if (spinnerList != null && !spinnerList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentView.getContext(),
                            android.R.layout.simple_spinner_item, spinnerList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    spinner3.setVisibility(View.VISIBLE);
                } else {
                    spinner3.setVisibility(View.INVISIBLE);
                }
                spinner4.setVisibility(View.INVISIBLE);
                spinner5.setVisibility(View.INVISIBLE);
                currentNodeList4 = null;
                currentNodeList5 = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                currentNodeList4 = currentNodeList3.item((position * 2) + 1).getChildNodes();

                List<String> spinnerList = buildCurrentSpinnerList(currentNodeList4, TYPE4_ELEMENT);
                if (spinnerList != null && !spinnerList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentView.getContext(),
                            android.R.layout.simple_spinner_item, spinnerList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner4.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    spinner4.setVisibility(View.VISIBLE);
                } else {
                    spinner4.setVisibility(View.INVISIBLE);
                }
                spinner5.setVisibility(View.INVISIBLE);
                currentNodeList5 = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                currentNodeList5 = currentNodeList4.item((position * 2) + 1).getChildNodes();

                List<String> spinnerList = buildCurrentSpinnerList(currentNodeList5, TYPE5_ELEMENT);
                if (spinnerList != null && !spinnerList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentView.getContext(),
                            android.R.layout.simple_spinner_item, spinnerList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner5.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    spinner5.setVisibility(View.VISIBLE);
                } else {
                    spinner5.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });

    }

    public List<String> buildCurrentSpinnerList(NodeList nodeList, String type) {
        List<String> spinnerList = new ArrayList<String>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (type.equals(nodeList.item(i).getNodeName())) {
                NodeList childs = nodeList.item(i).getChildNodes();
                for (int j = 0; j < childs.getLength(); j++) {
                    if (LABEL_ELEMENT.equals(childs.item(j).getNodeName())) {
                        spinnerList.add(childs.item(j).getFirstChild().getNodeValue());
                    }
                }
            }
        }

        return spinnerList;
    }

    public Document getDocument(InputStream inputStream) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(inputStream);
            document = db.parse(inputSource);
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return document;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                System.exit(RESULT_OK);
                return true;
            case R.id.menu_photo:
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            case R.id.menu_settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            default:
                return false;
        }

    }

    private void toastMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void sendEmail(){
        MailFeedTask mft = new MailFeedTask(this,picData);
        mft.execute();

        toastMessage("Email being sent");

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void validData(View view) {

        List<String> tags = new ArrayList<String>();

        tags.add(spinner1.getSelectedItem().toString());
        if (spinner2.isShown()) {
            tags.add(spinner2.getSelectedItem().toString());
            if (spinner3.isShown()) {
                tags.add(spinner3.getSelectedItem().toString());
                if (spinner4.isShown()) {
                    tags.add(spinner4.getSelectedItem().toString());
                    if (spinner5.isShown()) {
                        tags.add(spinner5.getSelectedItem().toString());
                    }
                }
            }
        }

        picData.setTags(tags);
        picData.setComment(commentText.getText().toString());

        sendEmail();
        Log.v("DEBUG", picData.toString());

    }
}
