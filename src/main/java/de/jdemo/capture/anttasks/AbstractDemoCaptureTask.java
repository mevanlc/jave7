package de.jdemo.capture.anttasks;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import de.jdemo.framework.DemoIdentifier;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteWatchdog;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;

/**
 * @author Markus Gebhard
 */
//TODO Mar 11, 2004 (Markus Gebhard): cleanup
public abstract class AbstractDemoCaptureTask extends Task {
  private String demoId;
  private CommandlineJava commandline = new CommandlineJava();
  private File dir = null;
  private Integer timeout = null;
  private boolean newEnvironment = false;
  private Environment env = new Environment();
  private boolean includeAntRuntime = true;
  private Path antRuntimeClasses = null;
  private AbstractDemoAntRunner runner;
  private boolean fork = false;
  private File outputFile;

  protected AbstractDemoCaptureTask() {
    commandline.setClassname(getRunnerClassName());
  }

  public final String getDemoId() {
    return demoId;
  }

  public final void setDemoId(String string) {
    demoId = string;
  }

  public final File getOutputFile() {
    return outputFile;
  }

  public final void setOutputFile(File file) {
    outputFile = file;
  }

  protected void checkParameters() {
    if (getOutputFile() == null) {
      throw new BuildException("Required parameter 'outputFile' is not set.");
    }
    if (getDemoId() == null) {
      throw new BuildException("Required parameter 'demoId' is not set.");
    }
  }

  /**
   * If true, JVM should be forked for each test.
   * 
   * <p>
   * It avoids interference between testcases and possibly avoids hanging the
   * build. this property is applied on all BatchTest (batchtest) and JUnitTest
   * (test) however it can possibly be overridden by their own properties.
   * </p>
   * 
   * @param value
   *          <tt>true</tt> if a JVM should be forked, otherwise <tt>false</tt>
   * 
   * @since Ant 1.2
   */
  public void setFork(boolean value) {
    this.fork = value;
  }

  //    /**
  //     * Property to set to "true" if there is a error in a test.
  //     *
  //     * <p>This property is applied on all BatchTest (batchtest) and
  //     * JUnitTest (test), however, it can possibly be overriden by
  //     * their own properties.</p>
  //     * @param propertyName the name of the property to set in the
  //     * event of an error.
  //     *
  //     * @since Ant 1.4
  //     */
  //    public void setErrorProperty(String propertyName) {
  //      Enumeration enum = allTests();
  //      while (enum.hasMoreElements()) {
  //        BaseTest test = (BaseTest) enum.nextElement();
  //        test.setErrorProperty(propertyName);
  //      }
  //    }

  //    /**
  //     * Property to set to "true" if there is a failure in a test.
  //     *
  //     * <p>This property is applied on all BatchTest (batchtest) and
  //     * JUnitTest (test), however, it can possibly be overriden by
  //     * their own properties.</p>
  //     * @param propertyName the name of the property to set in the
  //     * event of an failure.
  //     *
  //     * @since Ant 1.4
  //     */
  //    public void setFailureProperty(String propertyName) {
  //      Enumeration enum = allTests();
  //      while (enum.hasMoreElements()) {
  //        BaseTest test = (BaseTest) enum.nextElement();
  //        test.setFailureProperty(propertyName);
  //      }
  //    }

  //    /**
  //     * If true, JVM should be forked for each test.
  //     *
  //     * <p>It avoids interference between testcases and possibly avoids
  //     * hanging the build. this property is applied on all BatchTest
  //     * (batchtest) and JUnitTest (test) however it can possibly be
  //     * overridden by their own properties.</p>
  //     * @param value <tt>true</tt> if a JVM should be forked, otherwise
  //     * <tt>false</tt>
  //     * @see #setTimeout
  //     *
  //     * @since Ant 1.2
  //     */
  //    public void setFork(boolean value) {
  //      Enumeration enum = allTests();
  //      while (enum.hasMoreElements()) {
  //        BaseTest test = (BaseTest) enum.nextElement();
  //        test.setFork(value);
  //      }
  //    }

  //    /**
  //     * If true, print one-line statistics for each test, or "withOutAndErr"
  //     * to also show standard output and error.
  //     *
  //     * Can take the values on, off, and withOutAndErr.
  //     * @param value <tt>true</tt> to print a summary,
  //     * <tt>withOutAndErr</tt> to include the test&apos;s output as
  //     * well, <tt>false</tt> otherwise.
  //     * @see SummaryJUnitResultFormatter
  //     *
  //     * @since Ant 1.2
  //     */
  //    public void setPrintsummary(SummaryAttribute value) {
  //      summaryValue = value.getValue();
  //      summary = value.asBoolean();
  //    }

  //    /**
  //     * Print summary enumeration values.
  //     */
  //    public static class SummaryAttribute extends EnumeratedAttribute {
  //      public String[] getValues() {
  //        return new String[] {"true", "yes", "false", "no",
  //            "on", "off", "withOutAndErr"};
  //      }
  //
  //      public boolean asBoolean() {
  //        String value = getValue();
  //        return "true".equals(value)
  //        || "on".equals(value)
  //        || "yes".equals(value)
  //        || "withOutAndErr".equals(value);
  //      }
  //    }

  //    /**
  //     * Set the timeout value (in milliseconds).
  //     *
  //     * <p>If the test is running for more than this value, the test
  //     * will be canceled. (works only when in 'fork' mode).</p>
  //     * @param value the maximum time (in milliseconds) allowed before
  //     * declaring the test as 'timed-out'
  //     * @see #setFork(boolean)
  //     *
  //     * @since Ant 1.2
  //     */
  //    public void setTimeout(Integer value) {
  //      timeout = value;
  //    }

  /**
   * Set the maximum memory to be used by all forked JVMs.
   * 
   * @param max
   *          the value as defined by <tt>-mx</tt> or <tt>-Xmx</tt> in the
   *          java command line options.
   * 
   * @since Ant 1.2
   */
  public void setMaxmemory(String max) {
    commandline.setMaxmemory(max);
  }

  /**
   * The command used to invoke the Java Virtual Machine, default is 'java'.
   * The command is resolved by java.lang.Runtime.exec(). Ignored if fork is
   * disabled.
   * 
   * @param value
   *          the new VM to use instead of <tt>java</tt>
   * @see #setFork(boolean)
   * 
   * @since Ant 1.2
   */
  public void setJvm(String value) {
    commandline.setVm(value);
  }

  /**
   * Adds a JVM argument; ignored if not forking.
   * 
   * @return create a new JVM argument so that any argument can be passed to
   *         the JVM.
   * @see #setFork(boolean)
   * 
   * @since Ant 1.2
   */
  public Commandline.Argument createJvmarg() {
    return commandline.createVmArgument();
  }

  /**
   * The directory to invoke the VM in. Ignored if no JVM is forked.
   * 
   * @param dir
   *          the directory to invoke the JVM from.
   * @see #setFork(boolean)
   * 
   * @since Ant 1.2
   */
  public void setDir(File dir) {
    this.dir = dir;
  }

  /**
   * Adds a system property that tests can access. This might be useful to
   * tranfer Ant properties to the testcases when JVM forking is not enabled.
   * 
   * @since Ant 1.3
   */
  public void addSysproperty(Environment.Variable sysp) {
    commandline.addSysproperty(sysp);
  }

  /**
   * Adds path to classpath used for tests.
   * 
   * @since Ant 1.2
   */
  public Path createClasspath() {
    return commandline.createClasspath(getProject()).createPath();
  }

  /**
   * Adds an environment variable; used when forking.
   * 
   * <p>
   * Will be ignored if we are not forking a new VM.
   * </p>
   * 
   * @since Ant 1.5
   */
  public void addEnv(Environment.Variable var) {
    env.addVariable(var);
  }

  /**
   * If true, use a new environment when forked.
   * 
   * <p>
   * Will be ignored if we are not forking a new VM.
   * </p>
   * 
   * @since Ant 1.5
   */
  public void setNewenvironment(boolean newenv) {
    newEnvironment = newenv;
  }

  //    /**
  //     * Add a new single testcase.
  //     * @param test a new single testcase
  //     * @see JUnitTest
  //     *
  //     * @since Ant 1.2
  //     */
  //    public void addTest(JUnitTest test) {
  //      tests.addElement(test);
  //    }

  //    /**
  //     * Adds a set of tests based on pattern matching.
  //     *
  //     * @return a new instance of a batch test.
  //     * @see BatchTest
  //     *
  //     * @since Ant 1.2
  //     */
  //    public BatchTest createBatchTest() {
  //      BatchTest test = new BatchTest(getProject());
  //      batchTests.addElement(test);
  //      return test;
  //    }

  //    /**
  //     * Add a new formatter to all tests of this task.
  //     *
  //     * @since Ant 1.2
  //     */
  //    public void addFormatter(FormatterElement fe) {
  //      formatters.addElement(fe);
  //    }

  /**
   * If true, include ant.jar, optional.jar and junit.jar in the forked VM.
   * 
   * @since Ant 1.5
   */
  public void setIncludeantruntime(boolean b) {
    includeAntRuntime = b;
  }

  /**
   * Adds the jars or directories containing Ant, this task and JUnit to the
   * classpath - this should make the forked JVM work without having to specify
   * them directly.
   * 
   * @since Ant 1.4
   */
  public void init() {
    antRuntimeClasses = new Path(getProject());
    addClasspathEntry("/de/jdemo/framework/DemoCase.class");
    addClasspathEntry("/org/apache/tools/ant/Task.class");
    addClasspathEntry("/de/jdemo/capture/anttasks/JDemoRunner.class");
  }

  /**
   * Runs the testcase.
   * 
   * @since Ant 1.2
   */
  public final void execute() throws BuildException {
    checkParameters();
    execute(new DemoIdentifier(getDemoId()));
  }

  /**
   * Run the tests.
   */
  protected void execute(DemoIdentifier demoId) throws BuildException {
    // set the default values if not specified
    //@todo should be moved to the test class instead.

    //    if (antDemo.getTodir() == null) {
    //      antDemo.setTodir(getProject().resolveFile("."));
    //    }
    //
    //    if (antDemo.getOutfile() == null) {
    //      antDemo.setOutfile("TEST-" + antDemo.getName());
    //    }

    log("Capturing demo '" + demoId + "' to '" + getOutputFile() + "'", Project.MSG_INFO);

    // execute the test and get the return code
    boolean wasKilled = false;
    if (!fork) {
      executeInVM(demoId);
    }
    else {
      ExecuteWatchdog watchdog = createWatchdog();
      executeAsForked(demoId, watchdog);
      // null watchdog means no timeout, you'd better not check with null
      if (watchdog != null) {
        wasKilled = watchdog.killedProcess();
      }
    }

    if (wasKilled) {
      throw new BuildException("Demo " + demoId.getIdentifierName() + " failed (timeout)", getLocation());
    }

    //    // if there is an error/failure and that it should halt, stop
    //    // everything otherwise just log a statement
    //    boolean errorOccurredHere = exitValue == JUnitTestRunner.ERRORS;
    //    boolean failureOccurredHere = exitValue != JUnitTestRunner.SUCCESS;
    //    if (errorOccurredHere || failureOccurredHere) {
    //      if ((errorOccurredHere && test.getHaltonerror()) || (failureOccurredHere
    // && test.getHaltonfailure())) {
    //        throw new BuildException(
    //          "Test " + test.getName() + " failed" + (wasKilled ? " (timeout)" : ""),
    //          getLocation());
    //      }
    //      else {
    //        log("TEST " + test.getName() + " FAILED" + (wasKilled ? " (timeout)" :
    // ""), Project.MSG_ERR);
    //        if (errorOccurredHere && test.getErrorProperty() != null) {
    //          getProject().setNewProperty(test.getErrorProperty(), "true");
    //        }
    //        if (failureOccurredHere && test.getFailureProperty() != null) {
    //          getProject().setNewProperty(test.getFailureProperty(), "true");
    //        }
    //      }
    //    }
  }

  /**
   * Execute a testcase by forking a new JVM. The command will block until it
   * finishes. To know if the process was destroyed or not, use the <tt>killedProcess()</tt>
   * method of the watchdog class.
   * 
   * @param demoId
   *          the if of the demo to execute.
   * @param watchdog
   *          the watchdog in charge of cancelling the test if it exceeds a
   *          certain amount of time. Can be <tt>null</tt>, in this case the
   *          test could probably hang forever.
   */
  private void executeAsForked(DemoIdentifier demoId, ExecuteWatchdog watchdog) throws BuildException {
    CommandlineJava cmd;
    try {
      cmd = (CommandlineJava) commandline.clone();
    }
    //Strange: Ant build reported a CloneNotSupportedException to be thrown here? We catch it as Exception...
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    cmd.setClassname(getRunnerClassName());
    cmd.createArgument().setValue(demoId.getIdentifierName());

    addCommandLineArguments(cmd);

    //    // demo.getHaltonfailure());
    if (includeAntRuntime) {
      log("Implicitly adding " + antRuntimeClasses + " to CLASSPATH", Project.MSG_VERBOSE);
      cmd.createClasspath(getProject()).createPath().append(antRuntimeClasses);
    }
    //
    //    if (summary) {
    //      log("Running " + demoId, Project.MSG_INFO);
    //      //      cmd.createArgument().setValue(
    //      //        "formatter=org.apache.tools.ant.taskdefs.optional.junit.SummaryJUnitResultFormatter");
    //    }
    //
    //    cmd.createArgument().setValue("showoutput=" + String.valueOf(showOutput));

    //    StringBuffer formatterArg = new StringBuffer(128);
    //    final FormatterElement[] feArray = mergeFormatters(demo);
    //    for (int i = 0; i < feArray.length; i++) {
    //      FormatterElement fe = feArray[i];
    //      formatterArg.append("formatter=");
    //      formatterArg.append(fe.getClassname());
    //      File outFile = getOutput(fe, demo);
    //      if (outFile != null) {
    //        formatterArg.append(",");
    //        formatterArg.append(outFile);
    //      }
    //      cmd.createArgument().setValue(formatterArg.toString());
    //      formatterArg.setLength(0);
    //    }

    // Create a temporary file to pass the Ant properties to the
    // forked test
    //    File propsFile = FileUtils.newFileUtils().createTempFile("junit", ".properties", getProject().getBaseDir());
    //    cmd.createArgument().setValue("propsfile=" + propsFile.getAbsolutePath());
    //    Hashtable p = getProject().getProperties();
    //    Properties props = new Properties();
    //    for (Enumeration enum = p.keys(); enum.hasMoreElements();) {
    //      Object key = enum.nextElement();
    //      props.put(key, p.get(key));
    //    }
    //    try {
    //      FileOutputStream outstream = new FileOutputStream(propsFile);
    //      props.store(outstream, "Ant JUnitTask generated properties file");
    //      outstream.close();
    //    }
    //    catch (java.io.IOException e) {
    //      propsFile.delete();
    //      throw new BuildException("Error creating temporary properties " + "file.", e, getLocation());
    //    }

    Execute execute = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN), watchdog);
    execute.setCommandline(cmd.getCommandline());
    execute.setAntRun(getProject());
    if (dir != null) {
      execute.setWorkingDirectory(dir);
    }

    String[] environment = env.getVariables();
    if (environment != null) {
      for (int i = 0; i < environment.length; i++) {
        log("Setting environment variable: " + environment[i], Project.MSG_VERBOSE);
      }
    }
    execute.setNewenvironment(newEnvironment);
    execute.setEnvironment(environment);

    log(cmd.describeCommand(), Project.MSG_VERBOSE);
    try {
      int exitCode = execute.execute();
      if (exitCode != 0) {
        throw new BuildException("Error executing demo (error code " + exitCode + ")");
      }
    }
    catch (IOException e) {
      throw new BuildException("Process fork failed.", e, getLocation());
    }
    finally {
      if (watchdog != null && watchdog.killedProcess()) {
        throw new BuildException("Demo timeout " + demoId);
      }
      //      if (!propsFile.delete()) {
      //        throw new BuildException("Could not delete temporary " + "properties file.");
      //      }
    }
  }

  //  /**
  //   * Pass output sent to System.out to the TestRunner so it can collect ot
  // for
  //   * the formatters.
  //   *
  //   * @since Ant 1.5
  //   */
  //  protected void handleOutput(String line) {
  //    if (runner != null) {
  //      runner.handleOutput(line);
  //      if (showOutput) {
  //        super.handleOutput(line);
  //      }
  //    }
  //    else {
  //      super.handleOutput(line);
  //    }
  //  }

  //  /**
  //   * Pass output sent to System.out to the TestRunner so it can collect ot
  // for
  //   * the formatters.
  //   *
  //   * @since Ant 1.5.2
  //   */
  //  protected void handleFlush(String line) {
  //    if (runner != null) {
  //      runner.handleFlush(line);
  //      if (showOutput) {
  //        super.handleFlush(line);
  //      }
  //    }
  //    else {
  //      super.handleFlush(line);
  //    }
  //  }
  //
  //  /**
  //   * Pass output sent to System.err to the TestRunner so it can collect ot
  // for
  //   * the formatters.
  //   *
  //   * @since Ant 1.5
  //   */
  //  public void handleErrorOutput(String line) {
  //    if (runner != null) {
  //      runner.handleErrorOutput(line);
  //      if (showOutput) {
  //        super.handleErrorOutput(line);
  //      }
  //    }
  //    else {
  //      super.handleErrorOutput(line);
  //    }
  //  }
  //
  //  /**
  //   * Pass output sent to System.err to the TestRunner so it can collect ot
  // for
  //   * the formatters.
  //   *
  //   * @since Ant 1.5.2
  //   */
  //  public void handleErrorFlush(String line) {
  //    if (runner != null) {
  //      runner.handleErrorFlush(line);
  //      if (showOutput) {
  //        super.handleErrorFlush(line);
  //      }
  //    }
  //    else {
  //      super.handleErrorFlush(line);
  //    }
  //  }

  // in VM is not very nice since it could probably hang the
  // whole build. IMHO this method should be avoided and it would be best
  // to remove it in future versions. TBD. (SBa)

  protected void addCommandLineArguments(CommandlineJava cmd) {
    cmd.createArgument().setValue(IAntConstants.ATTRIB_OUTPUT_FILE + "=" + getOutputFile());
  }

  protected abstract String getRunnerClassName();

  /**
   * Execute inside VM.
   */
  private void executeInVM(DemoIdentifier demoId) throws BuildException {
    if (newEnvironment || null != env.getVariables()) {
      log("Changes to environment variables are ignored if running in " + "the same VM.", Project.MSG_WARN);
    }

    CommandlineJava.SysProperties sysProperties = commandline.getSystemProperties();
    if (sysProperties != null) {
      sysProperties.setSystem();
    }
    AntClassLoader cl = null;
    try {
      log("Using System properties " + System.getProperties(), Project.MSG_VERBOSE);
      Path userClasspath = commandline.getClasspath();
      Path classpath = userClasspath == null ? null : (Path) userClasspath.clone();
      if (classpath != null) {
        if (includeAntRuntime) {
          log("Implicitly adding " + antRuntimeClasses + " to CLASSPATH", Project.MSG_VERBOSE);
          classpath.append(antRuntimeClasses);
        }

        cl = new AntClassLoader(null, getProject(), classpath, false);
        log("Using CLASSPATH " + cl.getClasspath(), Project.MSG_VERBOSE);

        // make sure the test will be accepted as a TestCase
        cl.addSystemPackageRoot("de.jdemo");
        // will cause trouble in JDK 1.1 if omitted
        cl.addSystemPackageRoot("org.apache.tools.ant");
        cl.setThreadContextLoader();
      }
      runner = createDemoAntRunner(demoId, cl);
      //      if (summary) {
      //        log("Running " + test.getName(), Project.MSG_INFO);
      //
      //        SummaryJUnitResultFormatter f = new SummaryJUnitResultFormatter();
      //        f.setWithOutAndErr("withoutanderr".equalsIgnoreCase(summaryValue));
      //        f.setOutput(getDefaultOutput());
      //        runner.addFormatter(f);
      //      }

      //      final FormatterElement[] feArray = mergeFormatters(test);
      //      for (int i = 0; i < feArray.length; i++) {
      //        FormatterElement fe = feArray[i];
      //        File outFile = getOutput(fe, test);
      //        if (outFile != null) {
      //          fe.setOutfile(outFile);
      //        }
      //        else {
      //          fe.setOutput(getDefaultOutput());
      //        }
      //        runner.addFormatter(fe.createFormatter());
      //      }

      runner.run();
    }
    finally {
      if (sysProperties != null) {
        sysProperties.restoreSystem();
      }
      if (cl != null) {
        cl.resetThreadContextLoader();
      }
    }
  }

  protected abstract AbstractDemoAntRunner createDemoAntRunner(DemoIdentifier demo, AntClassLoader cl);

  /**
   * @return <tt>null</tt> if there is a timeout value, otherwise the
   *         watchdog instance.
   * 
   * @since Ant 1.2
   */
  protected ExecuteWatchdog createWatchdog() throws BuildException {
    if (timeout == null) {
      return null;
    }
    return new ExecuteWatchdog((long) timeout.intValue());
  }

  //  /**
  //   * Merge all individual tests from the batchtest with all individual tests
  //   * and return an enumeration over all <tt>JUnitTest</tt>.
  //   *
  //   * @since Ant 1.3
  //   */
  //  protected Enumeration getIndividualTests() {
  //    final int count = batchTests.size();
  //    final Enumeration[] enums = new Enumeration[count + 1];
  //    for (int i = 0; i < count; i++) {
  //      BatchTest batchtest = (BatchTest) batchTests.elementAt(i);
  //      enums[i] = batchtest.elements();
  //    }
  //    enums[enums.length - 1] = tests.elements();
  //    return Enumerations.fromCompound(enums);
  //  }

  /**
   * Search for the given resource and add the directory or archive that
   * contains it to the classpath.
   * 
   * <p>
   * Doesn't work for archives in JDK 1.1 as the URL returned by getResource
   * doesn't contain the name of the archive.
   * </p>
   * 
   * @since Ant 1.4
   */
  protected void addClasspathEntry(String resource) {
    URL url = getClass().getResource(resource);
    if (url != null) {
      String u = url.toString();
      if (u.startsWith("jar:file:")) {
        int pling = u.indexOf("!");
        String jarName = u.substring(9, pling);
        log("Found " + jarName, Project.MSG_DEBUG);
        antRuntimeClasses.createPath().setLocation(new File((new File(jarName)).getAbsolutePath()));
      }
      else if (u.startsWith("file:")) {
        int tail = u.indexOf(resource);
        String dirName = u.substring(5, tail);
        log("Found " + dirName, Project.MSG_DEBUG);
        antRuntimeClasses.createPath().setLocation(new File((new File(dirName)).getAbsolutePath()));
      }
      else {
        log("Don\'t know how to handle resource URL " + u, Project.MSG_DEBUG);
      }
    }
    else {
      log("Couldn\'t find " + resource, Project.MSG_DEBUG);
    }
  }

  //    /**
  //     * Take care that some output is produced in report files if the
  //     * watchdog kills the test.
  //     *
  //     * @since Ant 1.5.2
  //     */
  //
  //    private void logTimeout(FormatterElement[] feArray, JUnitTest test) {
  //      for (int i = 0; i < feArray.length; i++) {
  //        FormatterElement fe = feArray[i];
  //        File outFile = getOutput(fe, test);
  //        JUnitResultFormatter formatter = fe.createFormatter();
  //        if (outFile != null && formatter != null) {
  //          try {
  //            OutputStream out = new FileOutputStream(outFile);
  //            formatter.setOutput(out);
  //            formatter.startTestSuite(test);
  //            test.setCounts(0,0,1);
  //            Test t = new Test() {
  //              public int countTestCases() { return 0; }
  //              public void run(TestResult r) {
  //                throw new AssertionFailedError("Timeout occurred");
  //              }
  //            };
  //            formatter.startTest(t);
  //            formatter
  //            .addError(t,
  //                new AssertionFailedError("Timeout occurred"));
  //
  //            formatter.endTestSuite(test);
  //          } catch (IOException e) {}
  //        }
  //      }
  //    }
}