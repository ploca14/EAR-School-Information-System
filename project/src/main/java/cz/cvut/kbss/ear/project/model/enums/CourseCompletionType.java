package cz.cvut.kbss.ear.project.model.enums;

public enum CourseCompletionType {
  KZ("Klasifikovaný zápočet"),
  Z("Zápočet"),
  ZK("Zkouška");

  private final String description;

  CourseCompletionType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
