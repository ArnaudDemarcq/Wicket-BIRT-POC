/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.krohm.birtstuff;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

/**
 *
 * @author arnaud
 */
public class BirtEngineFactory {

    private EngineConfig config;
    private IReportEngineFactory factory;
    private IReportEngine engine;

    public void init() throws BirtException {
        Platform.startup(config);
        factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
        engine = factory.createReportEngine(config);
    }

    public IReportEngine getEngine() {
        return engine;
    }

    public EngineConfig getConfig() {
        return config;
    }

    public void setConfig(EngineConfig config) {
        this.config = config;
    }
}
