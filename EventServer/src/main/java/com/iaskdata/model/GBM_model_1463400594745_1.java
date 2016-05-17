package com.iaskdata.model;
/*
  Licensed under the Apache License, Version 2.0
    http://www.apache.org/licenses/LICENSE-2.0.html

  AUTOGENERATED BY H2O at 2016-05-16T20:10:11.835+08:00
  3.6.0.11
  
  Standalone prediction code with sample test data for GBMModel named GBM_model_1463400594745_1

  How to download, compile and execute:
      mkdir tmpdir
      cd tmpdir
      curl http:/localhost/127.0.0.1:54321/3/h2o-genmodel.jar > h2o-genmodel.jar
      curl http:/localhost/127.0.0.1:54321/3/Models.java/GBM_model_1463400594745_1 > GBM_model_1463400594745_1.java
      javac -cp h2o-genmodel.jar -J-Xmx2g -J-XX:MaxPermSize=128m GBM_model_1463400594745_1.java

     (Note:  Try java argument -XX:+PrintCompilation to show runtime JIT compiler behavior.)
*/
import java.util.Map;
import hex.genmodel.GenModel;
import hex.genmodel.annotations.ModelPojo;

@ModelPojo(name="GBM_model_1463400594745_1", algorithm="gbm")
public class GBM_model_1463400594745_1 extends GenModel {
  public hex.ModelCategory getModelCategory() { return hex.ModelCategory.Multinomial; }

  public boolean isSupervised() { return true; }
  public int nfeatures() { return 4; }
  public int nclasses() { return 3; }

  // Names of columns used by model.
  public static final String[] NAMES = NamesHolder_GBM_model_1463400594745_1.VALUES;
  // Number of output classes included in training data response column.
  public static final int NCLASSES = 3;

  // Column domains. The last array contains domain of response column.
  public static final String[][] DOMAINS = new String[][] {
    /* sepal_len */ null,
    /* sepal_wid */ null,
    /* petal_len */ null,
    /* petal_wid */ null,
    /* class */ GBM_model_1463400594745_1_ColInfo_4.VALUES
  };
  // Prior class distribution
  public static final double[] PRIOR_CLASS_DISTRIB = {0.3333333333333333,0.3333333333333333,0.3333333333333333};
  // Class distribution used for model building
  public static final double[] MODEL_CLASS_DISTRIB = {0.3333333333333333,0.3333333333333333,0.3333333333333333};

  public GBM_model_1463400594745_1() { super(NAMES,DOMAINS); }
  public String getUUID() { return Long.toString(8286890330376485258L); }

  // Pass in data in a double[], pre-aligned to the Model's requirements.
  // Jam predictions into the preds[] array; preds[0] is reserved for the
  // main prediction (class for classifiers or value for regression),
  // and remaining columns hold a probability distribution for classifiers.
  public final double[] score0( double[] data, double[] preds ) {
    java.util.Arrays.fill(preds,0);
    double[] fdata = hex.genmodel.GenModel.SharedTree_clean(data);
    GBM_model_1463400594745_1_Forest_0.score0(fdata,preds);
    GBM_model_1463400594745_1_Forest_1.score0(fdata,preds);
    GBM_model_1463400594745_1_Forest_2.score0(fdata,preds);
    GBM_model_1463400594745_1_Forest_3.score0(fdata,preds);
    GBM_model_1463400594745_1_Forest_4.score0(fdata,preds);
    hex.genmodel.GenModel.GBM_rescale(preds);
    preds[0] = hex.genmodel.GenModel.getPrediction(preds, PRIOR_CLASS_DISTRIB, data, 0.5);
    return preds;
  }
}
// The class representing training column names
class NamesHolder_GBM_model_1463400594745_1 implements java.io.Serializable {
  public static final String[] VALUES = new String[4];
  static {
    NamesHolder_GBM_model_1463400594745_1_0.fill(VALUES);
  }
  static final class NamesHolder_GBM_model_1463400594745_1_0 implements java.io.Serializable {
    static final void fill(String[] sa) {
      sa[0] = "sepal_len";
      sa[1] = "sepal_wid";
      sa[2] = "petal_len";
      sa[3] = "petal_wid";
    }
  }
}
// The class representing column class
class GBM_model_1463400594745_1_ColInfo_4 implements java.io.Serializable {
  public static final String[] VALUES = new String[3];
  static {
    GBM_model_1463400594745_1_ColInfo_4_0.fill(VALUES);
  }
  static final class GBM_model_1463400594745_1_ColInfo_4_0 implements java.io.Serializable {
    static final void fill(String[] sa) {
      sa[0] = "Iris-setosa";
      sa[1] = "Iris-versicolor";
      sa[2] = "Iris-virginica";
    }
  }
}

class GBM_model_1463400594745_1_Forest_0 {
  public static void score0(double[] fdata, double[] preds) {
    preds[1] += GBM_model_1463400594745_1_Tree_0_class_0.score0(fdata);
    preds[2] += GBM_model_1463400594745_1_Tree_0_class_1.score0(fdata);
    preds[3] += GBM_model_1463400594745_1_Tree_0_class_2.score0(fdata);
  }
}
class GBM_model_1463400594745_1_Tree_0_class_0 {
  static final double score0(double[] data) {
    double pred =  (data[3 /* petal_wid */] <0.79960954f ? 0.19999997f : -0.1f);
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_0_class_1 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? -0.1f
      : (data[3 /* petal_wid */] <1.7500002f
        ? (data[2 /* petal_len */] <4.6529303f
          ? (data[2 /* petal_len */] <4.452771f ? 0.19999997f : 0.17272726f)
          : 0.1142857f)
        : (data[0 /* sepal_len */] <6.247657f ? -0.07272728f : -0.1f)));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_0_class_2 {
  static final double score0(double[] data) {
    double pred =  (data[3 /* petal_wid */] <1.7500002f
      ? (data[2 /* petal_len */] <4.6529303f
        ? (data[2 /* petal_len */] <4.4531603f ? -0.1f : -0.07272728f)
        : -0.01428572f)
      : (data[0 /* sepal_len */] <6.251172f ? 0.17272726f : 0.19999997f));
    return pred;
  }
}


class GBM_model_1463400594745_1_Forest_1 {
  public static void score0(double[] fdata, double[] preds) {
    preds[1] += GBM_model_1463400594745_1_Tree_1_class_0.score0(fdata);
    preds[2] += GBM_model_1463400594745_1_Tree_1_class_1.score0(fdata);
    preds[3] += GBM_model_1463400594745_1_Tree_1_class_2.score0(fdata);
  }
}
class GBM_model_1463400594745_1_Tree_1_class_0 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? 0.16544242f
      : (data[2 /* petal_len */] <4.4501f
        ? -0.09503717f
        : (data[3 /* petal_wid */] <1.7500001f
          ? (data[2 /* petal_len */] <4.65f ? -0.095142215f : -0.095297754f)
          : (data[0 /* sepal_len */] <6.247657f ? -0.095142215f : -0.09503717f))));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_1_class_1 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? -0.09503717f
      : (data[3 /* petal_wid */] <1.649219f
        ? (data[2 /* petal_len */] <4.6529303f
          ? (data[2 /* petal_len */] <4.452771f ? 0.16544242f : 0.16957662f)
          : 0.09131485f)
        : (data[2 /* petal_len */] <5.0490236f
          ? -0.037125852f
          : (data[2 /* petal_len */] <5.453925f ? -0.09555979f : -0.09503717f))));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_1_class_2 {
  static final double score0(double[] data) {
    double pred =  (data[3 /* petal_wid */] <1.649219f
      ? (data[2 /* petal_len */] <4.6529303f
        ? (data[2 /* petal_len */] <4.4531603f ? -0.09503717f : -0.09627922f)
        : -0.0059446166f)
      : (data[2 /* petal_len */] <5.0504885f
        ? 0.119372696f
        : (data[2 /* petal_len */] <5.451457f ? 0.1671566f : 0.16544242f)));
    return pred;
  }
}


class GBM_model_1463400594745_1_Forest_2 {
  public static void score0(double[] fdata, double[] preds) {
    preds[1] += GBM_model_1463400594745_1_Tree_2_class_0.score0(fdata);
    preds[2] += GBM_model_1463400594745_1_Tree_2_class_1.score0(fdata);
    preds[3] += GBM_model_1463400594745_1_Tree_2_class_2.score0(fdata);
  }
}
class GBM_model_1463400594745_1_Tree_2_class_0 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? 0.14279138f
      : (data[2 /* petal_len */] <4.4501f
        ? -0.09089577f
        : (data[3 /* petal_wid */] <1.7500001f
          ? (data[2 /* petal_len */] <4.65f ? -0.091075316f : -0.09151465f)
          : (data[0 /* sepal_len */] <6.247657f
            ? -0.09113215f
            : (data[3 /* petal_wid */] <1.953125f ? -0.0909278f : -0.09089053f)))));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_2_class_1 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? -0.09089577f
      : (data[3 /* petal_wid */] <1.7500002f
        ? (data[2 /* petal_len */] <4.6529303f
          ? (data[0 /* sepal_len */] <5.546875f
            ? 0.123195715f
            : (data[2 /* petal_len */] <4.3375f ? 0.14279138f : 0.14489378f))
          : 0.086685784f)
        : (data[1 /* sepal_wid */] <3.1496093f
          ? (data[0 /* sepal_len */] <6.345703f
            ? -0.09305751f
            : (data[0 /* sepal_len */] <6.74776f ? -0.09088405f : -0.09088796f))
          : -0.06700113f)));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_2_class_2 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <4.8488283f
      ? (data[3 /* petal_wid */] <1.4500003f
        ? (data[2 /* petal_len */] <4.354688f ? -0.09089577f : -0.09412297f)
        : -0.033342563f)
      : (data[2 /* petal_len */] <5.149293f
        ? 0.09943313f
        : (data[3 /* petal_wid */] <1.9500002f
          ? 0.15419684f
          : (data[1 /* sepal_wid */] <3.15f ? 0.1427567f : 0.14305198f))));
    return pred;
  }
}


class GBM_model_1463400594745_1_Forest_3 {
  public static void score0(double[] fdata, double[] preds) {
    preds[1] += GBM_model_1463400594745_1_Tree_3_class_0.score0(fdata);
    preds[2] += GBM_model_1463400594745_1_Tree_3_class_1.score0(fdata);
    preds[3] += GBM_model_1463400594745_1_Tree_3_class_2.score0(fdata);
  }
}
class GBM_model_1463400594745_1_Tree_3_class_0 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? 0.12692769f
      : (data[2 /* petal_len */] <5.1494513f
        ? (data[2 /* petal_len */] <4.649872f
          ? (data[0 /* sepal_len */] <5.548047f
            ? -0.08773579f
            : (data[3 /* petal_wid */] <1.3500001f ? -0.087433815f : -0.087233044f))
          : (data[2 /* petal_len */] <4.9505434f ? -0.08889633f : -0.08792603f))
        : (data[3 /* petal_wid */] <1.9492189f
          ? -0.08715321f
          : (data[1 /* sepal_wid */] <3.15f ? -0.08741398f : -0.08727362f))));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_3_class_1 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? -0.08741831f
      : (data[2 /* petal_len */] <4.8497286f
        ? (data[3 /* petal_wid */] <1.4511719f
          ? (data[2 /* petal_len */] <4.3500004f
            ? (data[1 /* sepal_wid */] <2.5437503f ? 0.12788187f : 0.12701294f)
            : 0.13528204f)
          : 0.09464298f)
        : (data[2 /* petal_len */] <5.1500616f
          ? -0.024400676f
          : (data[3 /* petal_wid */] <1.95f
            ? -0.093975686f
            : (data[1 /* sepal_wid */] <3.15f ? -0.08741083f : -0.088002525f)))));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_3_class_2 {
  static final double score0(double[] data) {
    double pred =  (data[3 /* petal_wid */] <1.649219f
      ? (data[2 /* petal_len */] <4.6529303f
        ? (data[2 /* petal_len */] <4.4531603f
          ? (data[2 /* petal_len */] <2.4568021f
            ? -0.08741831f
            : (data[0 /* sepal_len */] <5.5375004f ? -0.08769264f : -0.08746965f))
          : -0.08919612f)
        : -0.005464209f)
      : (data[2 /* petal_len */] <5.0504885f
        ? 0.094578f
        : (data[2 /* petal_len */] <5.451457f
          ? 0.1293304f
          : (data[1 /* sepal_wid */] <3.15f ? 0.12666883f : 0.12757294f))));
    return pred;
  }
}


class GBM_model_1463400594745_1_Forest_4 {
  public static void score0(double[] fdata, double[] preds) {
    preds[1] += GBM_model_1463400594745_1_Tree_4_class_0.score0(fdata);
    preds[2] += GBM_model_1463400594745_1_Tree_4_class_1.score0(fdata);
    preds[3] += GBM_model_1463400594745_1_Tree_4_class_2.score0(fdata);
  }
}
class GBM_model_1463400594745_1_Tree_4_class_0 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? 0.11530148f
      : (data[2 /* petal_len */] <5.1494513f
        ? (data[2 /* petal_len */] <4.649872f
          ? (data[0 /* sepal_len */] <5.548047f
            ? -0.0847457f
            : (data[3 /* petal_wid */] <1.3500001f ? -0.0844823f : -0.08464653f))
          : (data[2 /* petal_len */] <4.9505434f ? -0.08578074f : -0.08521188f))
        : (data[3 /* petal_wid */] <1.9492189f
          ? -0.08472449f
          : (data[1 /* sepal_wid */] <3.15f ? -0.084476076f : -0.08437565f))));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_4_class_1 {
  static final double score0(double[] data) {
    double pred =  (data[2 /* petal_len */] <2.4519534f
      ? -0.08448473f
      : (data[3 /* petal_wid */] <1.7500002f
        ? (data[2 /* petal_len */] <4.6529303f
          ? (data[0 /* sepal_len */] <5.546875f
            ? 0.096840255f
            : (data[3 /* petal_wid */] <1.3500001f ? 0.11538624f : 0.11852978f))
          : 0.0735551f)
        : (data[1 /* sepal_wid */] <3.1496093f
          ? (data[2 /* petal_len */] <5.1527348f ? -0.08937519f : -0.08438126f)
          : -0.058402594f)));
    return pred;
  }
}

class GBM_model_1463400594745_1_Tree_4_class_2 {
  static final double score0(double[] data) {
    double pred =  (data[3 /* petal_wid */] <1.649219f
      ? (data[2 /* petal_len */] <4.6529303f
        ? (data[2 /* petal_len */] <4.4531603f
          ? (data[3 /* petal_wid */] <0.7972657f
            ? -0.08448473f
            : (data[0 /* sepal_len */] <5.5375004f ? -0.08471147f : -0.08453423f))
          : -0.0862996f)
        : -0.0058451756f)
      : (data[2 /* petal_len */] <5.0504885f
        ? 0.08604707f
        : (data[2 /* petal_len */] <5.3503118f
          ? 0.11828123f
          : (data[1 /* sepal_wid */] <3.15f ? 0.11505154f : 0.11594205f))));
    return pred;
  }
}


