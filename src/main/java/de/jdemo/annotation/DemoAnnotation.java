package de.jdemo.annotation;

/**
 * JDK &lt;= 1.4 compatible representation of an annotation for a JDemo demo. 
 * @author Markus Gebhard
 */
public class DemoAnnotation {
 
  private final String description;
  private final String[] categories;

  public DemoAnnotation(String description, String[] categories) {
    this.description = description;
    this.categories = categories;
  }
  
  public String getDescription() {
    return description;
  }
  
  public String[] getCategories() {
    return categories;
  }
}