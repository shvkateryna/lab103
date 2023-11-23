package ua.edu.ucu.apps;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class TimedDocument implements Document {
    public String gcsPath;

    @Override
    public String parse() {
        long startTime = System.currentTimeMillis();
        SmartDocument smartDocument = new SmartDocument(gcsPath);
        smartDocument.parse();
        long endTime = System.currentTimeMillis();
        return Long.toString(endTime - startTime);

    }

}
