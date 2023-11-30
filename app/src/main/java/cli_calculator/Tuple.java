package cli_calculator;

import java.util.ArrayList;

public class Tuple {
  private String x;
  private String y;
  private ArrayList<String> z = null;

  Tuple(String x, String y) {
    this.x = x;
    this.y = y;
  }

  Tuple(String x, String y, ArrayList<String> z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public String first() {
    return x;
  }

  public String second() {
    return y;
  }

  public ArrayList<String> third() {
    return z;
  }
}