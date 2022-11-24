package lightbiny.search.rest.dto;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class SearchDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Request {
		private String query;
		private Integer page = 1;
		private Integer size = 5;
		
		@Builder
		public Request(String query) {
			this.query = query;
		}
	}
	
	@ToString
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {
		private Request request;
		private List<Document> documents = new ArrayList<>();
		private boolean error = false;
		
		public void addDocument(Document document) {
			documents.add(document);
		}
		
		@Builder
		public Response(Request request, List<Document> documents, boolean error) {
			this.request = request;
			this.documents = documents;
			this.error = error;
		}
		
	}
	
	@ToString
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Document {
		private String name;
		private String url;
		private String category;
		private String phone;
		private String address;
		private String roadAddress;
		
		@Builder
		public Document(String name, String url, String category, String phone, String address, String roadAddress) {
			this.name = Jsoup.parse(name).text();
			this.url = url;
			this.category = category;
			this.phone = phone;
			this.address = address;
			this.roadAddress = roadAddress;
		}
		
		@Override
		public int hashCode() {
			return (this.name + "_" + this.address).hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null) { 
				return false;
			}
			
			if (o instanceof Document d) {
				return this.getName().equals(d.getName()) && this.getAddress().equals(d.getAddress());
			}
			
			return false;
		}
	}
}
