import java.util.List;

import home.tut.yake.Yake;
import home.tut.yake.Yake.DedupAlg;
import home.tut.yake.Yake.KeywordExtractorOutput;

public class Demo {

	public static void main(String[] args) throws Exception {
		String text = "Sources tell us that Google is acquiring Kaggle, a platform that hosts data science and machine learning "
				+ "competitions. Details about the transaction remain somewhat vague, but given that Google is hosting its Cloud "
				+ "Next conference in San Francisco this week, the official announcement could come as early as tomorrow. "
				+ "Reached by phone, Kaggle co-founder CEO Anthony Goldbloom declined to deny that the acquisition is happening. "
				+ "Google itself declined 'to comment on rumors'. Kaggle, which has about half a million data scientists on its platform, "
				+ "was founded by Goldbloom  and Ben Hamner in 2010. "
				+ "The service got an early start and even though it has a few competitors like DrivenData, TopCoder and HackerRank, "
				+ "it has managed to stay well ahead of them by focusing on its specific niche. "
				+ "The service is basically the de facto home for running data science and machine learning competitions. "
				+ "With Kaggle, Google is buying one of the largest and most active communities for data scientists - and with that, "
				+ "it will get increased mindshare in this community, too (though it already has plenty of that thanks to Tensorflow "
				+ "and other projects). Kaggle has a bit of a history with Google, too, but that's pretty recent. Earlier this month, "
				+ "Google and Kaggle teamed up to host a $100,000 machine learning competition around classifying YouTube videos. "
				+ "That competition had some deep integrations with the Google Cloud Platform, too. Our understanding is that Google "
				+ "will keep the service running - likely under its current name. While the acquisition is probably more about "
				+ "Kaggle's community than technology, Kaggle did build some interesting tools for hosting its competition "
				+ "and 'kernels', too. On Kaggle, kernels are basically the source code for analyzing data sets and developers can "
				+ "share this code on the platform (the company previously called them 'scripts'). "
				+ "Like similar competition-centric sites, Kaggle also runs a job board, too. It's unclear what Google will do with "
				+ "that part of the service. According to Crunchbase, Kaggle raised $12.5 million (though PitchBook says it's $12.75) "
				+ "since its   launch in 2010. Investors in Kaggle include Index Ventures, SV Angel, Max Levchin, Naval Ravikant, "
				+ "Google chief economist Hal Varian, Khosla Ventures and Yuri Milner ";

		// Example 1
		Yake.KeywordExtractor kw_extractor1 = new Yake.KeywordExtractor();
		List<KeywordExtractorOutput> keywords1 = kw_extractor1.extract_keywords(text);
		for (KeywordExtractorOutput kw : keywords1) {
			System.out.println(kw);
		}
		// Example 2
		String language = "en";
		int max_ngram_size = 3;
		double deduplication_thresold = 0.9;
		DedupAlg deduplication_algo = DedupAlg.jaro;
		int windowSize = 1;
		int numOfKeywords = 20;
		Yake.KeywordExtractor kw_extractor2 = new Yake.KeywordExtractor(language, max_ngram_size,
				deduplication_thresold, deduplication_algo, windowSize, numOfKeywords, null);
		List<KeywordExtractorOutput> keywords2 = kw_extractor2.extract_keywords(text);
		for (KeywordExtractorOutput kw : keywords2) {
			System.out.println(kw);
		}

	}
}