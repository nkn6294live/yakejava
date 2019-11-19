package home.tut.yake;

import java.util.List;

import home.tut.yake.Yake.KeywordExtractorOutput;

public class Program {
	
	public static void main(String[] args) throws Exception {
//		testYake(args);
		testYakeWithVN(args);
//		testVNCoreNLP(args);
	}
	
	public static void testYake(String[] args) {
		String text = "Sources tell us that Google is acquiring Kaggle, a platform that hosts data science and machine learning " +
				"competitions. Details about the transaction remain somewhat vague, but given that Google is hosting its Cloud " +
				"Next conference in San Francisco this week, the official announcement could come as early as tomorrow. " +
				"Reached by phone, Kaggle co-founder CEO Anthony Goldbloom declined to deny that the acquisition is happening. " +
				"Google itself declined 'to comment on rumors'. Kaggle, which has about half a million data scientists on its platform, " +
				"was founded by Goldbloom  and Ben Hamner in 2010. " +
				"The service got an early start and even though it has a few competitors like DrivenData, TopCoder and HackerRank, " +
				"it has managed to stay well ahead of them by focusing on its specific niche. " +
				"The service is basically the de facto home for running data science and machine learning competitions. " +
				"With Kaggle, Google is buying one of the largest and most active communities for data scientists - and with that, " +
				"it will get increased mindshare in this community, too (though it already has plenty of that thanks to Tensorflow " +
				"and other projects). Kaggle has a bit of a history with Google, too, but that's pretty recent. Earlier this month, " +
				"Google and Kaggle teamed up to host a $100,000 machine learning competition around classifying YouTube videos. " +
				"That competition had some deep integrations with the Google Cloud Platform, too. Our understanding is that Google " +
				"will keep the service running - likely under its current name. While the acquisition is probably more about " +
				"Kaggle's community than technology, Kaggle did build some interesting tools for hosting its competition " +
				"and 'kernels', too. On Kaggle, kernels are basically the source code for analyzing data sets and developers can " +
				"share this code on the platform (the company previously called them 'scripts'). " +
				"Like similar competition-centric sites, Kaggle also runs a job board, too. It's unclear what Google will do with " +
				"that part of the service. According to Crunchbase, Kaggle raised $12.5 million (though PitchBook says it's $12.75) " +
				"since its   launch in 2010. Investors in Kaggle include Index Ventures, SV Angel, Max Levchin, Naval Ravikant, " +
				"Google chief economist Hal Varian, Khosla Ventures and Yuri Milner ";
		
		// ------ Simple Run --------
		Yake.KeywordExtractor kw_extractor1 = new Yake.KeywordExtractor();
		List<KeywordExtractorOutput>keywords1 = kw_extractor1.extract_keywords(text);
		for (KeywordExtractorOutput kw : keywords1) {			
			System.out.println(kw);
		}
		// ------ Run with config --------
//		String language = "vi";
//		int max_ngram_size = 3;
//		double deduplication_thresold = 0.9;
//		DedupAlg deduplication_algo = DedupAlg.jaro;
//		int windowSize = 1;
//		int numOfKeywords = 20;
//		Yake.KeywordExtractor kw_extractor2 = new Yake.KeywordExtractor(language, max_ngram_size, deduplication_thresold, deduplication_algo, windowSize, numOfKeywords, null);
//		List<KeywordExtractorOutput>keywords2 = kw_extractor2.extract_keywords(text);
//		for (KeywordExtractorOutput kw : keywords2) {			
//			System.out.println(kw);
//		}
	}
	public static void testYakeWithVN(String[] args) {
		String text = "Bóng đá Đông Nam Á suốt hai thập niên qua, dù có những cuộc đấu nội bộ tại AFF Cup và SEA Games, nhưng loanh quanh cũng chỉ có vài cái tên Thái Lan, Việt Nam, Malaysia và Singapore. Hữu ích thì cũng có hữu ích, danh vị thì cũng quyến rũ, nhưng giá trị của các sân chơi mang tính kèn cựa nhau ấy không lớn. Kỳ thực, Thái Lan đã chán phải bơi trong cái ao làng ấy bởi sự thống trị của họ với bảy HC vàng SEA Games liên tiếp và ba chức vô địch AFF Cup cũng chẳng giúp cho đội bóng của Kiatisuk có nổi một chiến thắng tại vòng loại cuối cùng World Cup 2018, kết thúc cuộc phiêu lưu với vỏn vẹn hai điểm kiếm được khi cuộc chơi gần tàn. Thậm chí, họ còn thua Iraq cả lượt đi lẫn lượt về dù đây là đối thủ mà họ từng cầm hòa hai trận khi nằm chung bảng F với Việt Nam.";
		
		// ------ Simple Run --------
		Yake.KeywordExtractor kw_extractor1 = new Yake.KeywordExtractor("vi");
		List<KeywordExtractorOutput>keywords1 = kw_extractor1.extract_keywords(text);
		for (KeywordExtractorOutput kw : keywords1) {			
			System.out.println(kw);
		}
		// ------ Run with config --------
//		String language = "vi";
//		int max_ngram_size = 3;
//		double deduplication_thresold = 0.9;
//		DedupAlg deduplication_algo = DedupAlg.jaro;
//		int windowSize = 1;
//		int numOfKeywords = 20;
//		Yake.KeywordExtractor kw_extractor2 = new Yake.KeywordExtractor(language, max_ngram_size, deduplication_thresold, deduplication_algo, windowSize, numOfKeywords, null);
//		List<KeywordExtractorOutput>keywords2 = kw_extractor2.extract_keywords(text);
//		for (KeywordExtractorOutput kw : keywords2) {			
//			System.out.println(kw);
//		}
	}
}
