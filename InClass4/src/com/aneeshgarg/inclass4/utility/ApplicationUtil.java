package com.aneeshgarg.inclass4.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ApplicationUtil {

    static public class ApplicationsXMLPullParser {

        private static final String HEIGHT = "height";
        private static final String IMAGE  = "im:image";
        private static final String AMOUNT = "amount";
        private static final String PRICE  = "im:price";
        private static final String NAME   = "im:name";
        private static final String ENTRY  = "entry";

        static ArrayList<Application> parsePersons(InputStream xmlIn) throws XmlPullParserException,
                NumberFormatException, IOException {

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(xmlIn, "UTF-8");

            Application application = null;
            ArrayList<Application> applications = null;

            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        applications = new ArrayList<Application>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(ENTRY)) {
                            application = new Application();
                        } else if (parser.getName().equals(NAME) && application != null) {
                            application.setName(parser.nextText().trim());
                        } else if (parser.getName().equals(PRICE) && application != null) {
                            application.setPrice(Double.valueOf(parser.getAttributeValue(null, AMOUNT)));
                            application.setDisplayPrice(parser.nextText().trim());
                        } else if (parser.getName().equals(IMAGE) && application != null) {
                            Image image = new Image();
                            image.setHeight(Integer.valueOf(parser.getAttributeValue(null, HEIGHT)));
                            image.setUrl(parser.nextText().trim());
                            application.getImageList().add(image);
                            image = null;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ENTRY)) {
                            if (application != null)
                                applications.add(application);
                            // sorting images according to height in descending
                            // order
                            Collections.sort(application.getImageList(), Collections.reverseOrder());
                            application = null;
                        }
                    default:
                        break;
                }
                event = parser.next();
            }

            return applications;
        }
    }
}