package controllers;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.util.ObjectBuilder;
import com.google.inject.Inject;
import models.BookIndex;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;
import requests.BookRequest;
import services.ElasticService;
import utils.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class IndexController extends Controller {
	private ElasticService elasticService;

	@Inject
	public IndexController(ElasticService elasticService) {
		this.elasticService = elasticService;
	}

	public Result get(Http.Request request, String id) throws IOException {
		return ok(Json.toJson(elasticService.get(System.getenv("ELASTIC_INDEX_BOOKS"), id, BookIndex.class)));
	}

	public Result search(Http.Request request, String author)
	{
		 BoolQuery.Builder query = QueryBuilders.bool();

		 query.must(b -> b.terms(TermsQuery.of(t -> t
			 .field("author")
			 .terms(TermsQueryField.of(m -> m
					 .value(
						 Collections.singletonList(FieldValue.of(author))
					 )
				 )
			 )
		 )));

		 SearchRequest searchRequest = SearchRequest.of(b -> b
				 .index(System.getenv("ELASTIC_INDEX_BOOKS"))
				 .query(query.build()._toQuery()
			 )
		 );

		List<BookIndex> result = elasticService.search(searchRequest, BookIndex.class);

		return ok(Json.toJson(result));
	}

	public Result getAll(Http.Request request)
	{
		BoolQuery.Builder query = QueryBuilders.bool();

		SearchRequest searchRequest = SearchRequest.of(b -> b
				.index(System.getenv("ELASTIC_INDEX_BOOKS"))
				.size(1000)
				.query(query.build()._toQuery()
			)
		);

		List<BookIndex> result = elasticService.search(searchRequest, BookIndex.class);

		return ok(Json.toJson(result));
	}

	public Result post(Http.Request request)
	{
		BookRequest bookRequest = null;
		try {
			bookRequest = Utils.convertObject(request.body().asJson(), BookRequest.class);
		} catch (Exception ex) {
			String sh = "sh";
			ex.printStackTrace();
		}

		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		BookIndex bookIndex = new BookIndex(id, bookRequest.getName(), bookRequest.getAuthor());

		try {
			elasticService.index(id, bookIndex, System.getenv("ELASTIC_INDEX_BOOKS"));
		} catch (Exception ex) {
			String sh = "sh";
			ex.printStackTrace();
		}

		return ok("POST REQUEST with payload: " + request.body().asJson().toString());
	}

	public Result put(Http.Request request, String id)
	{
		BookRequest bookRequest = null;
		try {
			bookRequest = Utils.convertObject(request.body().asJson(), BookRequest.class);
		} catch (Exception ex) {
			String sh = "sh";
			ex.printStackTrace();
			throw ex;
		}

		BookIndex bookIndex = elasticService.get(System.getenv("ELASTIC_INDEX_BOOKS"), id, BookIndex.class);

		try {
			bookIndex.setName(bookRequest.getName());
			bookIndex.setAuthor(bookRequest.getAuthor());
			elasticService.index(id, bookIndex, System.getenv("ELASTIC_INDEX_BOOKS"));
		} catch (Exception ex) {
			String sh = "sh";
			ex.printStackTrace();
		}

		return ok("POST REQUEST with payload: " + request.body().asJson().toString());
	}

	public Result delete(Http.Request request, String id) throws IOException
	{
		elasticService.delete(System.getenv("ELASTIC_INDEX_BOOKS"), id);
		return ok("DELETE REQUEST");
	}
}