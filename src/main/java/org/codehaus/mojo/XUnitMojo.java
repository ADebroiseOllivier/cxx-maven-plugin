/*
 * Copyright (C) 2011, Neticoa SAS France - Tous droits réservés.
 * Author(s) : Franck Bonin, Neticoa SAS France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codehaus.mojo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Goal which launch unit test  suing custum command.
 * This mojo just tell programmer where to produce xUnit reports
 *
 * @author Franck Bonin 
 */
@Mojo( name = "xunit", defaultPhase = LifecyclePhase.TEST )
public class XUnitMojo extends LaunchMojo
{
    /**
     * The Report OutputFile Location.
     * 
     * @since 0.0.4
     */
    @Parameter( property = "xunit.reportsfilePath", defaultValue = "xunit-reports" )
    private File reportsfileDir;
    
    protected void preExecute(Executor exec, CommandLine commandLine, Map enviro) throws MojoExecutionException
    {
        // this
        String OutputReportDir = new String();
        if ( reportsfileDir.isAbsolute() )
        {
            OutputReportDir = reportsfileDir.getAbsolutePath();
        }
        else
        {
            OutputReportDir = basedir.getAbsolutePath() + "/" + reportsfileDir.getPath();
        }
        new File( OutputReportDir ).mkdirs();
        getLog().info( "You shall produce a xUnit report called \"" + OutputReportDir + File.separator + "xunit-result-*.xml\" within this xunit goal" );
        File file = new File( OutputReportDir + "/Readme.txt" );
        try
        {
            DataOutputStream out = new DataOutputStream( new FileOutputStream(file) );
            out.writeBytes( "You shall produce xUnit reports called \"xunit-result-*.xml\" within this directory.\n");
        }
        catch (IOException e)
        {
             getLog().info( "Could not write to " + OutputReportDir + File.separator + "Readme.txt" );
        }
    }
    
    /**
     * The Xunit Legacy Skip feature.
     * 
     * @since 0.0.5
     */
    @Parameter( property = "xunit.skiptests", defaultValue = "false" )
    private boolean skiptests;
    
    /**
     * Set this to "true" to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
     * convenient on occasion.
     *
     * @since 0.0.5
     */
    @Parameter( property = "skipTests", defaultValue = "false" )
    protected boolean skipTests;
    
    /**
     * Set this to "true" to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you enable it using
     * the "maven.test.skip" property, because maven.test.skip shall disables both running the tests and compiling the tests.
     * Consider using the <code>skipTests</code> parameter instead.
     *
     * @since 0.0.5
     */
    @Parameter( property = "maven.test.skip", defaultValue = "false" )
    protected boolean skip;
    
    protected boolean isSkip()
    {
        return super.isSkip() || skiptests || skipTests || skip;
    }
}
