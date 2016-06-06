package com.iaskdata.service;

import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.BinomialModelPrediction;
import hex.genmodel.easy.prediction.RegressionModelPrediction;

import java.util.ArrayList;
import java.util.List;
import com.iaskdata.model.BadLoanModel;
import com.iaskdata.model.InterestRateModel;

public class PredictService {

	static final boolean VERBOSE = true;

	static EasyPredictModelWrapper badLoanModel;
	static EasyPredictModelWrapper interestRateModel;

	static {
		BadLoanModel rawBadLoanModel = new BadLoanModel();
		badLoanModel = new EasyPredictModelWrapper(rawBadLoanModel);

		InterestRateModel rawInterestRateModel = new InterestRateModel();
		interestRateModel = new EasyPredictModelWrapper(rawInterestRateModel);
	}

	private BinomialModelPrediction predictBadLoan(RowData row)
			throws Exception {
		return badLoanModel.predictBinomial(row);
	}

	private RegressionModelPrediction predictInterestRate(RowData row)
			throws Exception {
		return interestRateModel.predictRegression(row);
	}

	private String createJsonResponse(BinomialModelPrediction p,
			RegressionModelPrediction p2) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("  \"labelIndex\" : ").append(p.labelIndex).append(",\n");
		sb.append("  \"label\" : \"").append(p.label).append("\",\n");
		sb.append("  \"classProbabilities\" : ").append("[\n");
		for (int i = 0; i < p.classProbabilities.length; i++) {
			double d = p.classProbabilities[i];
			if (Double.isNaN(d)) {
				throw new RuntimeException("Probability is NaN");
			} else if (Double.isInfinite(d)) {
				throw new RuntimeException("Probability is infinite");
			}

			sb.append("    ").append(d);
			if (i != (p.classProbabilities.length - 1)) {
				sb.append(",");
			}
			sb.append("\n");
		}
		sb.append("  ],\n");
		sb.append("\n");
		sb.append("  \"interestRate\" : ").append(p2.value).append("\n");
		sb.append("}\n");

		return sb.toString();
	}

	public List predictHandle(String querystr) throws PredictException {
		RowData row = new RowData();
		// String querystr =
		// "loan_amnt=10000&term=36+months&emp_length=5&home_ownership=RENT&annual_inc=60000&verification_status=verified&purpose=debt_consolidation&addr_state=FL&dti=3&delinq_2yrs=0&revol_util=35&total_acc=4&longest_credit_length=10";
		String strs[] = querystr.split("&");
		for (String str : strs) {
			String argv[] = str.split("=");
			row.put(argv[0], argv[1]);
		}
		BinomialModelPrediction p = badLoanModel.predictBinomial(row);
		RegressionModelPrediction p2 = interestRateModel.predictRegression(row);

		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("  \"labelIndex\" : ").append(p.labelIndex).append(",\n");
		sb.append("  \"label\" : \"").append(p.label).append("\",\n");
		sb.append("  \"classProbabilities\" : ").append("[\n");
		for (int i = 0; i < p.classProbabilities.length; i++) {
			double d = p.classProbabilities[i];
			if (Double.isNaN(d)) {
				throw new RuntimeException("Probability is NaN");
			} else if (Double.isInfinite(d)) {
				throw new RuntimeException("Probability is infinite");
			}

			sb.append("    ").append(d);
			if (i != (p.classProbabilities.length - 1)) {
				sb.append(",");
			}
			sb.append("\n");
		}
		sb.append("  ],\n");
		sb.append("\n");
		sb.append("  \"interestRate\" : ").append(p2.value).append("\n");
		sb.append("}\n");

		String s = sb.toString();
		List result = new ArrayList<>();
		result.add(s);
		return result;
	}

}
