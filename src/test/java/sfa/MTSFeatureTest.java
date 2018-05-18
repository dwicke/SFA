// Copyright (c) 2017 - Patrick Schäfer (patrick.schaefer@hu-berlin.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package sfa;

//import sfa.classification.EVM;
import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Problem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sfa.classification.Classifier;
import sfa.classification.MUSEClassifier;
import sfa.timeseries.MultiVariateTimeSeries;
import sfa.timeseries.TimeSeries;
import sfa.timeseries.TimeSeriesLoader;
import sfa.transformation.SFA;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class MTSFeatureTest {

  // The multivariate datasets to use
  public static String[] datasets = new String[]{
      "ECG"
          /*"LP1",
      "LP2",
      "LP3",
      "LP4",
      "LP5",
      "PenDigits",
      "ShapesRandom",
      "DigitShapeRandom",
      "CMUsubject16",
      "ECG",
      "JapaneseVowels",
      "KickvsPunch",
      "Libras",
      "UWave",
      "Wafer",
      "WalkvsRun",
      "CharacterTrajectories",
      "ArabicDigits",
      "AUSLAN",
      "NetFlow",*/
  };


  @Test
  public void testMultiVariatelassification() throws IOException {
    try {
      // the relative path to the datasets
      ClassLoader classLoader = SFAWordsTest.class.getClassLoader();

      File dir = new File(classLoader.getResource("datasets/multivariate/").getFile());

      for (String s : datasets) {
        File d = new File(dir.getAbsolutePath() + "/" + s);
        if (d.exists() && d.isDirectory()) {
          for (File train : d.listFiles()) {
            if (train.getName().toUpperCase().endsWith("TRAIN3")) {
              File test = new File(train.getAbsolutePath().replaceFirst("TRAIN3", "TEST3"));

              if (!test.exists()) {
                System.err.println("File " + test.getName() + " does not exist");
                test = null;
              }

              Classifier.DEBUG = false;

              boolean useDerivatives = true;
              MultiVariateTimeSeries[] trainSamples = TimeSeriesLoader.loadMultivariateDatset(train, useDerivatives);
              MultiVariateTimeSeries[] testSamples = TimeSeriesLoader.loadMultivariateDatset(test, useDerivatives);

              MUSEClassifier muse = new MUSEClassifier();
              MUSEClassifier.BIGRAMS = true;
              Problem p = muse.getProblem(trainSamples,4, SFA.HistogramType.INFORMATION_GAIN,false);

              System.err.println("Number of x in problem = " + p.x.length + " num train samples = " + trainSamples.length);

              int numSamPerClass[] = new int[p.y.length];
              Set<Integer> labels = new HashSet<>();
              for (int i = 0; i < p.x.length; i++) {
                numSamPerClass[(int)p.y[i]]++;
                labels.add((int)p.y[i]);
              }


              Map<Integer, double[][]> data = new HashMap<>();
              int currentIndexForClass[] = new int[p.y.length];
              for (int i = 0; i < p.x.length; i++) {
                double xs[] = new double[p.n];
                // the features are sparse features!!!
                for (Feature f : p.x[i]) {
                  xs[f.getIndex()] = f.getValue();
                }
                double[][] classData = data.getOrDefault((int)p.y[i], new double[numSamPerClass[(int)p.y[i]]][p.n]);
                classData[currentIndexForClass[(int)p.y[i]]] = xs;
                currentIndexForClass[(int)p.y[i]]++;
                data.putIfAbsent((int)p.y[i], classData);
              }
//
//              for (Map.Entry<Integer, double[][]> en : data.entrySet()) {
//                System.err.println("Label = " + en.getKey() + " has " + en.getValue().length + " examples");
//                System.err.println("One examples is = " + Arrays.toString(en.getValue()[0]));
//              }


//              EVM evm = new EVM(true);
//              int tau = 20;
//              double sigma = .5;
//              int maxEVs = 3000;
//              double tolerance = .1;
//
//              int alabels[] = new int[labels.size()];
//              int i = 0;
//              for (Integer a : labels) {
//                alabels[i] = a;
//                i++;
//              }
//
//
//              List<EVM.Model> themodel = evm.train(data,tau,alabels,p.l,sigma, maxEVs, tolerance);

//              System.err.println("I got the EVs! = " + themodel.size());

//              for (int i = 0; i < p.x.length; i++) {
//                System.err.println("label = " + p.y[i] + " feature vector length = " + p.x[i].length + " vals = ");
////                for(int j = 0; j < 10; j++) {
////                  System.err.print(p.x[i][j].getIndex() + ":" + p.x[i][j].getValue() + " ");
////                }
//                System.err.println("");
//              }

//              MUSEClassifier.Score museScore = muse.eval(trainSamples, testSamples);
//
//              System.out.println(s + ";" + museScore.toString());
            }
          }
        } else{
          // not really an error. just a hint:
          System.out.println("Dataset could not be found: " + d.getAbsolutePath() + ".");
        }
      }
    } finally {
      TimeSeries.APPLY_Z_NORM = true; // FIXME static variable breaks some test cases!
    }
  }
}
