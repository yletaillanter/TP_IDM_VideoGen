package app;

import app.videoGenerator;

@SuppressWarnings("all")
public class Main {
  public static void main(final String[] args) {
    videoGenerator videoGen = new videoGenerator();
    videoGen.generateVignette();
  }
}
