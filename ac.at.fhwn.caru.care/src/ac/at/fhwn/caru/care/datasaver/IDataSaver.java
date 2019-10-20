package ac.at.fhwn.caru.care.datasaver;

import ac.at.fhwn.caru.care.evaluation.EvaluationResult;

public interface IDataSaver {
	public void storeResult(EvaluationResult result) throws Exception;
}
