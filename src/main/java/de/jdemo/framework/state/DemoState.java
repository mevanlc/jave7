package de.jdemo.framework.state;

/**
 * Typesafe enumeration for demo execution states.
 * @author Markus Gebhard
 */
public enum DemoState {

  INITIAL("initial") { //$NON-NLS-1$
    @Override
    public boolean isTerminated() {
      return false;
    }

    @Override
    public void accept(final IDemoStateVisitor visitor) {
      visitor.visitInitial(this);
    }
  },
  CRASHED("crashed") { //$NON-NLS-1$
    @Override
    public boolean isTerminated() {
      return true;
    }

    @Override
    public void accept(final IDemoStateVisitor visitor) {
      visitor.visitCrashed(this);
    }
  },
  STARTING("starting") { //$NON-NLS-1$
    @Override
    public boolean isTerminated() {
      return false;
    }

    @Override
    public void accept(final IDemoStateVisitor visitor) {
      visitor.visitStarting(this);
    }
  },
  RUNNING("running") { //$NON-NLS-1$
    @Override
    public boolean isTerminated() {
      return false;
    }

    @Override
    public void accept(final IDemoStateVisitor visitor) {
      visitor.visitRunning(this);
    }
  },
  FINISHED("finished") { //$NON-NLS-1$
    @Override
    public boolean isTerminated() {
      return true;
    }

    @Override
    public void accept(final IDemoStateVisitor visitor) {
      visitor.visitFinished(this);
    }
  };

  private String name;

  private DemoState(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "DemoCase{" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
  }

  public abstract boolean isTerminated();

  public abstract void accept(IDemoStateVisitor visitor);
}