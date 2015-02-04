package edu.wayne.cs.severe.ir4se.processor.controllers.impl.bm25;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

import edu.wayne.cs.severe.ir4se.processor.controllers.RetrievalSearcher;
import edu.wayne.cs.severe.ir4se.processor.entity.Query;
import edu.wayne.cs.severe.ir4se.processor.entity.RetrievalDoc;
import edu.wayne.cs.severe.ir4se.processor.exception.SearchException;
import edu.wayne.cs.severe.ir4se.processor.utils.ExceptionUtils;
import edu.wayne.cs.severe.ir4se.processor.utils.ParameterUtils;

public class BM25RetrievalSearcher extends RetrievalSearcher {

	protected IndexSearcher searcher;
	protected IndexReader reader;
	protected QueryParser parser;
	protected Integer resultsNumber;

	public BM25RetrievalSearcher(String indexPath, Map<String, String> params,
			Analyzer analyzer) throws IOException, SearchException {
		super(indexPath, params);
		String field = "text";

		parser = new QueryParser(field, analyzer);

		File fileIndex = new File(indexPath);

		if (!fileIndex.exists() || !fileIndex.isDirectory()) {
			throw new SearchException("Invalid index path!");
		}

		reader = DirectoryReader.open(FSDirectory.open(fileIndex));

		// number of results
		String paramNum = params.get(ParameterUtils.NUM_BUGS_PARAM);
		if (paramNum == null) {
			this.resultsNumber = reader.numDocs();
		} else {
			this.resultsNumber = Integer.valueOf(paramNum);
		}

		searcher = new IndexSearcher(reader);
		if (params.get("k1") == "" || params.get("b") == "") {
			throw new SearchException("There are no values for k1 and b");
		}
		try {
			float k1 = Float.parseFloat(params.get("k1"));
			float b = Float.parseFloat(params.get("b"));
			BM25Similarity similarity = new BM25Similarity(k1, b);
			searcher.setSimilarity(similarity);
		} catch (NullPointerException | NumberFormatException e) {
			throw new SearchException(e.getMessage());
		}

	}

	@Override
	public List<RetrievalDoc> searchQuery(Query query) throws SearchException {

		List<RetrievalDoc> retrievedDocs = new ArrayList<RetrievalDoc>();
		try {

			String txtQuery = query.getTxt();
			org.apache.lucene.search.Query lucceneQuery = parser
					.parse(txtQuery);

			ScoreDoc[] hits = searcher
					.search(lucceneQuery, null, resultsNumber).scoreDocs;
			for (int i = 0; i < hits.length; i++) {

				RetrievalDoc doc = new RetrievalDoc();
				doc.setDocRank(i + 1);
				doc.setDocId(hits[i].doc);

				retrievedDocs.add(doc);
			}
		} catch (IOException | ParseException | NullPointerException e) {
			SearchException e2 = new SearchException(e.getMessage());
			ExceptionUtils.addStackTrace(e, e2);
			throw e2;
		}

		return retrievedDocs;
	}

	@Override
	public void close() throws SearchException {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				throw new SearchException(e.getMessage());
			}
		}
	}
}
