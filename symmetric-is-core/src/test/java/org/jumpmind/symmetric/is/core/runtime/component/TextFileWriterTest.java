package org.jumpmind.symmetric.is.core.runtime.component;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jumpmind.symmetric.is.core.model.Component;
import org.jumpmind.symmetric.is.core.model.Flow;
import org.jumpmind.symmetric.is.core.model.FlowStep;
import org.jumpmind.symmetric.is.core.model.Folder;
import org.jumpmind.symmetric.is.core.model.Resource;
import org.jumpmind.symmetric.is.core.model.Setting;
import org.jumpmind.symmetric.is.core.runtime.Message;
import org.jumpmind.symmetric.is.core.runtime.flow.IMessageTarget;
import org.jumpmind.symmetric.is.core.runtime.resource.IResourceFactory;
import org.jumpmind.symmetric.is.core.runtime.resource.LocalFileResource;
import org.jumpmind.symmetric.is.core.runtime.resource.ResourceFactory;
import org.jumpmind.symmetric.is.core.utils.TestUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TextFileWriterTest {

    private static IResourceFactory resourceFactory;
    private static FlowStep writerFlowStep;
    private static final String FILE_PATH = "build/files/";
    private static final String FILE_NAME = "text_test_writer.txt";

    @BeforeClass
    public static void setup() throws Exception {
        resourceFactory = new ResourceFactory();
        writerFlowStep = createWriterFlowStep();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTextWriterMultipleRowsPerMessage() throws Exception {
        TextFileWriter writer = new TextFileWriter();
        writer.setFlowStep(writerFlowStep);
        writer.start(null, resourceFactory);
        writer.handle("test", createMultipleRowTextMessageToWrite(), null);
        checkTextFile();
    }

    @Test
    public void testTextWriterSingleRowPerMessage() throws Exception {
        TextFileWriter writer = new TextFileWriter();
        writer.setFlowStep(writerFlowStep);
        writer.start(null, resourceFactory);
        writer.handle("test", createSingleRowTextMessageToWrite(1, false), null);
        writer.handle("test", createSingleRowTextMessageToWrite(2, false), null);
        writer.handle("test", createSingleRowTextMessageToWrite(3, false), null);
        writer.handle("test", createSingleRowTextMessageToWrite(4, true), null);
        checkTextFile();
    }

    private static void checkTextFile() throws Exception {
        Path path = Paths.get(FILE_PATH + FILE_NAME);
        List<String> fileLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        assertEquals(4, fileLines.size());
        assertEquals("Line 1", fileLines.get(0));
        assertEquals("Line 2", fileLines.get(1));
        assertEquals("Line 3", fileLines.get(2));
        assertEquals("Line 4", fileLines.get(3));
    }

    private static Message createMultipleRowTextMessageToWrite() {
        Message msg = new Message("originating step id");
        msg.getHeader().setSequenceNumber(1);
        msg.getHeader().setLastMessage(true);
        ArrayList<String> payload = new ArrayList<String>();
        payload.add("Line 1");
        payload.add("Line 2");
        payload.add("Line 3");
        payload.add("Line 4");
        msg.setPayload(payload);
        return msg;
    }

    private static Message createSingleRowTextMessageToWrite(int lineNumber, boolean lastMsg) {
        Message msg = new Message("originating step id");
        msg.getHeader().setSequenceNumber(lineNumber);
        msg.getHeader().setLastMessage(lastMsg);
        ArrayList<String> payload = new ArrayList<String>();
        payload.add("Line " + lineNumber);
        msg.setPayload(payload);
        return msg;
    }

    private static FlowStep createWriterFlowStep() {
        Folder folder = TestUtils.createFolder("Test Folder");
        Flow flow = TestUtils.createFlow("TestFlow", folder);
        Setting[] settingData = createWriterSettings();
        Component component = TestUtils.createComponent(TextFileWriter.TYPE, false, 
                createResource(createResourceSettings()), null, null, null, null, settingData);
        FlowStep writerComponent = new FlowStep();
        writerComponent.setFlowId(flow.getId());
        writerComponent.setComponentId(component.getId());
        writerComponent.setCreateBy("Test");
        writerComponent.setCreateTime(new Date());
        writerComponent.setLastModifyBy("Test");
        writerComponent.setLastModifyTime(new Date());
        writerComponent.setComponent(component);
        return writerComponent;
    }

    private static Resource createResource(List<Setting> settings) {
        Resource resource = new Resource();
        Folder folder = TestUtils.createFolder("Test Folder Resource");
        resource.setName("Test Resource");
        resource.setFolderId("Test Folder Resource");
        resource.setType(LocalFileResource.TYPE);
        resource.setFolder(folder);
        resource.setSettings(settings);

        return resource;
    }

    private static Setting[] createWriterSettings() {
        Setting[] settingData = new Setting[1];
        settingData[0] = new Setting(TextFileWriter.TEXTFILEWRITER_RELATIVE_PATH, FILE_NAME);

        return settingData;
    }

    private static List<Setting> createResourceSettings() {
        List<Setting> settings = new ArrayList<Setting>(2);
        settings.add(new Setting(LocalFileResource.LOCALFILE_PATH, FILE_PATH));
        settings.add(new Setting(LocalFileResource.LOCALFILE_MUST_EXIST, "true"));
        return settings;
    }

    class MessageTarget implements IMessageTarget {

        List<Message> targetMsgArray = new ArrayList<Message>();

        @Override
        public void put(Message message) {
            targetMsgArray.add(message);
        }

        public Message getMessage(int idx) {
            return targetMsgArray.get(idx);
        }

        public int getTargetMessageCount() {
            return targetMsgArray.size();
        }
    }
}