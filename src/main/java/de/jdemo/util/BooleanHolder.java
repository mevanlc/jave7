package de.jdemo.util;

public final class BooleanHolder {
  private boolean value;

  public BooleanHolder(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }
}