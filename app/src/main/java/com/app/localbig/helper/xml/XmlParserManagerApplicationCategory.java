package com.app.localbig.helper.xml;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import com.app.localbig.model.ApplicationCategory;
import org.xml.sax.Attributes;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParserManagerApplicationCategory {

    private static XmlParserManagerApplicationCategory singleton;
    List<ApplicationCategory> list;
    ApplicationCategory item;

    public synchronized static XmlParserManagerApplicationCategory getInstance() {
        if (singleton == null) {
            singleton = new XmlParserManagerApplicationCategory();
        }
        return singleton;
    }

    public ArrayList<ApplicationCategory> Parse(InputStream is) {

        list = new ArrayList<>();

        RootElement root = new RootElement("items");
        Element value = root.getChild("item");

        value.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attrs) {
                item = new ApplicationCategory();
            }
        });

        value.setEndElementListener(new EndElementListener() {
            @Override
            public void end() {
                list.add(item);
            }
        });

        value.getChild("Id").setEndTextElementListener(
                new EndTextElementListener() {
                    @Override
                    public void end(String body) {
                        item.setId(Integer.parseInt(body));
                    }
                });

        value.getChild("Description").setEndTextElementListener(
                new EndTextElementListener() {
                    @Override
                    public void end(String body) {
                        item.setDescription(body);
                    }
                });

        value.getChild("Image").setEndTextElementListener(
                new EndTextElementListener() {
                    @Override
                    public void end(String body) {
                        item.setImage(body);
                    }
                });

        try {
            Xml.parse(is, Xml.Encoding.UTF_8,
                    root.getContentHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return (ArrayList<ApplicationCategory>) list;
    }


}
