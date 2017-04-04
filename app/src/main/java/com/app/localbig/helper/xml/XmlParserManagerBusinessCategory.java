package com.app.localbig.helper.xml;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import com.app.localbig.model.BusinessCategory;
import org.xml.sax.Attributes;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParserManagerBusinessCategory {

    private static XmlParserManagerBusinessCategory singleton;
    List<BusinessCategory> list;
    BusinessCategory item;

    public synchronized static XmlParserManagerBusinessCategory getInstance() {
        if (singleton == null) {
            singleton = new XmlParserManagerBusinessCategory();
        }
        return singleton;
    }

    public ArrayList<BusinessCategory> Parse(InputStream is) {

        list = new ArrayList<>();

        RootElement root = new RootElement("items");
        Element value = root.getChild("item");

        value.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attrs) {
                item = new BusinessCategory();
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
                        item.setId(body);
                    }
                });

        value.getChild("Name").setEndTextElementListener(
                new EndTextElementListener() {
                    @Override
                    public void end(String body) {
                        item.setName(body);
                    }
                });

        value.getChild("Description").setEndTextElementListener(
                new EndTextElementListener() {
                    @Override
                    public void end(String body) {
                        item.setDescription(body);
                    }
                });

        try {
            Xml.parse(is, Xml.Encoding.UTF_8,
                    root.getContentHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return (ArrayList<BusinessCategory>) list;
    }


}
