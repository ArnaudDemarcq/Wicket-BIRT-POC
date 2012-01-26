package org.krohm;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;
    // The fancy test string
    @SpringBean(name = "testString")
    String springTestString;
    // The birt engine
    @SpringBean(name = "birtEngine")
    IReportEngine birtEngine;
    // The report path
    private static final String basePath = "./target./classes./ReportEngine./samples/";
    private static final String reportPath = basePath + "hello_world.rptdesign";
    private static final String outputFileName = basePath + "output_test.html";

    public HomePage(final PageParameters parameters) throws EngineException {

        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
        add(new Label("version", springTestString));

        Label birtLabel = new Label("birtDump", testReport(new HashMap(), reportPath));
        birtLabel.setEscapeModelStrings(false);
        add(birtLabel);
        // TODO Add your page's components here
    }

    protected String testReport(final Map map, final String strReport) throws EngineException {


        IReportRunnable report = birtEngine.openReportDesign(strReport);
        IRunAndRenderTask task = birtEngine.createRunAndRenderTask(report);
        task.setParameterValues(map);
        // The RenderOptions
        HTMLRenderOption options = new HTMLRenderOption();
        options.setOutputFormat("HTML");
        options.setEmbeddable(true);
        options.setHTMLIDNamespace("mytest");
        // TODO : Sooo Dirty ...
        options.setOutputFileName(outputFileName);
        //options.setEnableAgentStyleEngine(true);
        //options.setEnableInlineStyle(false);

        task.setRenderOption(options);
        task.run();
        task.close();

        /*

        //options
        HTMLRenderOption options = new HTMLRenderOption();
        options.setOutputFormat(HTMLRenderOption.HTML_INDENT);
        PDFRenderOption options = new PDFRenderOption();
        options.setOutputFormat("pdf");
        // TODO : output steam FFS !
        task.setRenderOption(options);
        task.run();
        task.close(); /**/

        return fileToString(outputFileName);
    }

    protected void generateReport(final Map map, final String strReport) {
        AbstractResourceStreamWriter writer = new AbstractResourceStreamWriter() {

            public void write(OutputStream os) {
                EngineConfig config = new EngineConfig();
                String path = "";
                //String path = ((WebApplication)/*ReportParameterPage./**/this. this.getApplication()).getServletContext().getRealPath("WEB-INF/birt");
                config.setEngineHome(path);
                //  path = ((WebApplication)ReportParameterPage.this.getApplication()).getServletContext().getRealPath("WEB-INF/log");
                config.setLogConfig(path, Level.FINE);
                try {

                    Platform.startup(config);
                    IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
                    IReportEngine engine = factory.createReportEngine(config);
                    IReportRunnable report = engine.openReportDesign(strReport);

                    IRunAndRenderTask task = engine.createRunAndRenderTask(report);
                    task.setParameterValues(map);
                    HTMLRenderOption options = new HTMLRenderOption();
                    //  PDFRenderOption options = new PDFRenderOption();
                    //   options.setOutputFormat("pdf");
                    options.setOutputFormat(RenderOption.HTML_PAGINATION);
                    options.setOutputStream(os);
                    task.setRenderOption(options);
                    task.run();
                    task.close();

                } catch (BirtException e) {
                    //logger.error(e.getLocalizedMessage());
                }
            }

            @Override
            public String getContentType() {
                return "text/html";
            }

            public void write(Response rspns) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    public static String fileToString(String file) {
        String result = null;
        DataInputStream in = null;

        try {
            File f = new File(file);
            byte[] buffer = new byte[(int) f.length()];
            in = new DataInputStream(new FileInputStream(f));
            in.readFully(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException("IO problem in fileToString", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) { /* ignore it */

            }
        }
        return result;
    }
}
