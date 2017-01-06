package com.ly.common.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.X;

public class ElasticsearchUtil {
  private final static Logger    log         = LoggerFactory.getLogger(ElasticsearchUtil.class);
  private static TransportClient client      = null;
  private static String[]        hostArray   = null;
  private static String          clusterName = null;

  private static void initClient() {
    try {
      hostArray = X.getConfig("com.go2plus.globle.search.host").split(",");
      clusterName = X.getConfig("com.go2plus.globle.search.clusterName");

      if (hostArray.length == 0) {
        throw new NullPointerException("es.host.ip cannot be null");
      }
      if (StringUtils.isEmpty(clusterName)) {
        throw new NullPointerException("es.cluster.name cannot be null");
      }

      Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();
      client = TransportClient.builder().settings(settings).build();
      for (String host : hostArray) {
        String[] ipAndPort = host.split(":");
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAndPort[0]), X.string2int(ipAndPort[1])));
      }

    } catch (UnknownHostException e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (NullPointerException e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
  }

  private static Client getClient() {
    if (client == null) {
      initClient();
    }
    return client;
  }

  public static SearchRequestBuilder buildSearchRequestBuilder(String index, String type) {
    SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(index).setTypes(type);
    return searchRequestBuilder;
  }

  /**
   * 创建一个索引
   * 
   * @param index
   * @return
   */
  public static CreateIndexResponse createIndex(String index) {
    return getClient().admin().indices().prepareCreate(index).execute().actionGet();
  }
  
  /**
   * 删除一个索引
   * 
   * @param index
   * @return
   */
  public static DeleteIndexResponse deleteIndex(String index) {
    return getClient().admin().indices().prepareDelete(index).execute().actionGet();
  }

  /**
   * 创建一个Mapping映射
   * 
   * @param index
   * @param type
   * @param mapping
   * @return
   */
  public static PutMappingResponse putMapping(String index, String type, XContentBuilder mapping) {
    PutMappingRequest mappingRequest = Requests.putMappingRequest(index).type(type).source(mapping);
    return getClient().admin().indices().putMapping(mappingRequest).actionGet();
  }

  /**
   * 将指定的index 加上别名, 同时从其他所有index上移除该别名 (原子操作)
   * 
   * @param index
   * @param alias
   */
  public static void setUniqueAliases(String index, String alias) {
    GetAliasesResponse response = getClient().admin().indices().prepareGetAliases(alias).execute().actionGet();
    String[] existingIndex = response.getAliases().keys().toArray(String.class);
    IndicesAliasesRequestBuilder iarb = getClient().admin().indices().prepareAliases();
    iarb.addAlias(index, alias);
    for (String i : existingIndex) {
      if(!i.equals(index)){
        iarb.removeAlias(i, alias);
      }
    }
    iarb.execute().actionGet();
  }
  
  public static boolean existIndex(String index){
    IndicesExistsRequest inicesExists = new IndicesExistsRequest(index);
    ActionFuture<IndicesExistsResponse> response = getClient().admin().indices().exists(inicesExists);
    return response.actionGet().isExists();
  }
  /**
   *  创建索引mapping
   * @param date  格式yyyyMMdd
   * @param changeAlias
   * @throws Exception
   */
  public static void initGo2ProductSearchIndex(String date, boolean changeAlias) throws Exception{
    String indexName = X.getConfig("com.go2plus.globle.search.indexName");
    if (StringUtils.isEmpty(indexName)) {
      throw new NullPointerException("com.go2plus.globle.search.indexName cannot be null");
    }
    String index = indexName + date;
    String prodcut_type = "product";// shoes
    String supplier_type = "supplier";
      IndicesExistsRequest inicesExists = new IndicesExistsRequest(index);
      ActionFuture<IndicesExistsResponse> response = getClient().admin().indices().exists(inicesExists);
      if(!response.actionGet().isExists()){
        ElasticsearchUtil.createIndex(index);
      }
      else{
        if(changeAlias){
          String lastIndex = indexName + DateTime.now().plusDays(-1).toString("yyyyMMdd");
          ElasticsearchUtil.setUniqueAliases(lastIndex, "go2_production");
        }
        //先删除原来的索引
        ElasticsearchUtil.deleteIndex(index);
        
        //在创建一个索引
        ElasticsearchUtil.createIndex(index);
      }
      XContentBuilder productMapping, supplierMapping;
      productMapping = jsonBuilder().
          startObject()
          .startObject(prodcut_type)
          .startObject("_all").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("term_vector","no").field("store","false").endObject()
          .startObject("properties")
            .startObject("userId").field("type","long").endObject()
            .startObject("categoryId").field("type","integer").endObject()
            .startObject("articleNumber").field("type","string").endObject()
            .startObject("title").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("characters").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("sumary").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("color").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("size").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("price").field("type","float").endObject()
            .startObject("keyword").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("modifyTime").field("type","date").endObject()
            .startObject("state").field("type","integer").endObject()
            .startObject("createTime").field("type","date").endObject()
            .startObject("updateTime").field("type","date").endObject()
            .startObject("userCreateTime").field("type","date").endObject()
            .startObject("utype").field("type","integer").endObject()
            .startObject("ustate").field("type","integer").endObject()
            .startObject("sid").field("type","integer").endObject()
            .startObject("sstate").field("type","integer").endObject()
            .startObject("shopName").field("type","string").endObject()
            .startObject("marketArea").field("type","string").endObject()
            .startObject("marketId").field("type", "integer").endObject()
            .startObject("ppath").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("ptags").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
             .startObject("stags").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("fulltext").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            .startObject("province").field("type","string").endObject()
            .startObject("town").field("type","string").endObject()
            .startObject("isOuter").field("type","integer").endObject()
            .startObject("isVip").field("type","integer").endObject()
            .startObject("isUnique").field("type","integer").endObject()
            .startObject("isFk").field("type","integer").endObject()
            .startObject("uniqueState").field("type","integer").endObject()
            .startObject("isShowcase").field("type","integer").endObject()
            .startObject("isBlock").field("type","integer").endObject()
            .startObject("isHide").field("type","integer").endObject()
            .startObject("certifiedType").field("type","integer").endObject()
            .startObject("isTreasure").field("type","integer").endObject()
            .startObject("labelId").field("type","string").endObject()
            .startObject("parentIds").field("type","string").field("index", "not_analyzed").endObject()
            .startObject("platform").field("type","string").endObject()
            .startObject("categoryNid").field("type","integer").endObject()
            .startObject("promotionId").field("type","integer").endObject()
            .startObject("day7TotalNum").field("type","integer").endObject()
            .startObject("day15TotalNum").field("type","integer").endObject()
            .startObject("day90TotalNum").field("type","integer").endObject()
            .startObject("isFactory").field("type","integer").endObject()
            .startObject("isPromotion").field("type","integer").endObject()
            .startObject("changeDays").field("type","integer").endObject()
            .startObject("returnDays").field("type","integer").endObject()
            .startObject("isManual").field("type","integer").endObject()
            .startObject("weightSort").field("type","integer").endObject()
            .startObject("defaultSort").field("type","double").endObject()
            .startObject("hotSort").field("type","double").endObject()
            .startObject("compSort").field("type","double").endObject()
            .startObject("profitSort").field("type","double").endObject()
            .startObject("popularitySort").field("type","double").endObject()
            .startObject("isMogujie").field("type","integer").endObject()
            .startObject("isBeautiful").field("type","integer").endObject()
            .startObject("isWeidian").field("type","integer").endObject()
            .startObject("isDitan").field("type","integer").endObject()
            .startObject("isAlibaba").field("type","integer").endObject()
            .startObject("thirdId").field("type","integer").endObject()
            .startObject("supplierAddress").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
            // .startObject("userId").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
          .endObject().endObject().endObject();
      ElasticsearchUtil.putMapping(index, prodcut_type, productMapping);

    // 商家mapping
      supplierMapping = jsonBuilder().startObject().startObject(supplier_type).startObject("_all").field("analyzer", "ik_max_word")
          .field("search_analyzer", "ik_max_word").field("term_vector", "no").field("store", "false").endObject().startObject("properties")
          .startObject("userId").field("type", "long").endObject().startObject("marketId").field("type", "integer").endObject()
          .startObject("siteId").field("type", "integer").endObject().startObject("subdomain").field("type", "string").endObject()
          .startObject("title").field("type", "string").field("store", "no").field("term_vector", "with_positions_offsets")
          .field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8)
          .endObject().startObject("contact").field("type", "string").field("store", "no").field("term_vector", "with_positions_offsets")
          .field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8)
          .endObject().startObject("capital").field("type", "string").endObject().startObject("address").field("type", "string")
          .field("store", "no").field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
          .field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8).endObject().startObject("qqun")
          .field("type", "string").field("store", "no").field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
          .field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8).endObject().startObject("qq")
          .field("type", "string").endObject().startObject("phone").field("type", "string").endObject().startObject("cardNumber")
          .field("type", "string").endObject().startObject("state").field("type", "integer").endObject().startObject("certifiedType")
          .field("type", "integer").endObject().startObject("createTime").field("type", "date").endObject().startObject("updateTime")
          .field("type", "date").endObject().startObject("tags").field("type", "string").field("store", "no")
          .field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word")
          .field("include_in_all", "true").field("boost", 8).endObject().startObject("fulltext").field("type", "string")
          .field("store", "no").field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
          .field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8).endObject()
          .startObject("sMarketArea").field("type","integer").endObject()
          .startObject("productCountSort").field("type", "integer").endObject()
          .startObject("distributeSort").field("type", "integer").endObject()
          .startObject("defaultSort").field("type","double").endObject()
          .startObject("hotSort").field("type","double").endObject()
          .startObject("compSort").field("type","double").endObject()
          .startObject("profitSort").field("type","double").endObject()
          .startObject("popularitySort").field("type","double").endObject()
          .startObject("platform").field("type","string").endObject()
          // .startObject("userId").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8).endObject()
          .endObject().endObject().endObject();
      ElasticsearchUtil.putMapping(index, supplier_type, supplierMapping);
  }
  /**
   * 初始化Go2产品索引以及Mapping
   */
  public static void initGo2ProductSearchIndex() throws Exception{
    String date = DateTime.now().toString("yyyyMMdd");
    ElasticsearchUtil.initGo2ProductSearchIndex(date, true);
  }

  /**
   * 创建一个添加索引请求
   * 
   * @param index
   * @param type
   * @param id
   * @param source
   * @return
   */
  public static IndexRequestBuilder buildIndexRequestBuilder(String index, String type, String id, String source) {
    IndexRequestBuilder request = getClient().prepareIndex(index, type, id).setSource(source);
    return request;
  }

  /**
   * 创建一个添加索引请求
   * 
   * @param index
   * @param type
   * @param id
   * @param source
   * @return
   */
  public static IndexRequestBuilder buildIndexRequestBuilder(String index, String type, String id, Map<String, Object> source) {
    IndexRequestBuilder request = getClient().prepareIndex(index, type, id).setSource(source);
    return request;
  }

  /**
   * 创建一个删除索引请求
   * 
   * @param index
   * @param type
   * @param id
   * @param source
   * @return
   */
  public static DeleteRequestBuilder buildDeleteRequestBuilder(String index, String type, String id) {
    DeleteRequestBuilder request = getClient().prepareDelete(index, type, id);
    return request;
  }

  /**
   * 创建一个更新索引请求
   * 
   * @param index
   * @param type
   * @param id
   * @param source
   * @return
   */
  public static UpdateRequestBuilder buildUpdateRequestBuilder(String index, String type, String id, String source) {
    UpdateRequestBuilder request = null;
    try {
      request = getClient().prepareUpdate(index, type, id).setDoc(source);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
    return request;
  }

  /**
   * 创建一个更新索引请求
   * 
   * @param index
   * @param type
   * @param id
   * @param source
   * @return
   */
  public static UpdateRequestBuilder buildUpdateRequestBuilder(String index, String type, String id, Map<String, Object> source) {
    UpdateRequestBuilder request = null;
    try {
      request = getClient().prepareUpdate(index, type, id).setDoc(source);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
    return request;
  }

  /**
   * 批量增加多个索引
   * 
   * @param requests
   * @return
   */
  public static BulkResponse bulkIndexGet(List<IndexRequestBuilder> requests) {
    BulkRequestBuilder bulkRequest = getClient().prepareBulk();
    for (int i = 0; i < requests.size(); i++) {
      bulkRequest.add(requests.get(i));
    }
    return bulkRequest.get();
  }

  /**
   * 批量处理多个删除操作
   * 
   * @param requests
   * @return
   */
  public static BulkResponse bulkDeleteGet(List<DeleteRequestBuilder> requests) {
    BulkRequestBuilder bulkRequest = getClient().prepareBulk();
    for (int i = 0; i < requests.size(); i++) {
      bulkRequest.add(requests.get(i));
    }
    return bulkRequest.get();
  }

  /**
   * 批量处理多个更新操作
   * 
   * @param requests
   * @return
   */
  public static BulkResponse bulkUpdateGet(List<UpdateRequestBuilder> requests) {
    BulkRequestBuilder bulkRequest = getClient().prepareBulk();
    for (int i = 0; i < requests.size(); i++) {
      bulkRequest.add(requests.get(i));
    }
    return bulkRequest.get();
  }

  /**
   * 获取单个索引
   * 
   * @param index
   * @param type
   * @param id
   * @return
   */
  public static GetResponse getIndex(String index, String type, String id) {
    GetResponse response = getClient().prepareGet(index, type, id)
    // .setOperationThreaded(false)
        .get();
    return response;
  }

  /**
   * 获取多个索引
   * 
   * @param index
   * @param type
   * @param id
   * @return
   */
  public static MultiGetResponse getMultiIndex(String index, String type, List<String> ids) {
    MultiGetResponse multiGetItemResponses = getClient().prepareMultiGet().add(index, type, ids).get();
    return multiGetItemResponses;
  }

  public static void main(String[] args) {
    // ElasticsearchUtil.initGo2ProductSearchIndex();
    System.out.println(DateTime.now().toString("yyyyMMdd"));
  }
  
  /**
   * 初始化模糊词词条索引及Mapping
   */
  public static void initRuleSearchIndex() throws Exception{
    String index = X.getConfig("com.go2plus.rule.search.indexName");
    if(StringUtils.isEmpty(index)){
       throw new NullPointerException("com.go2plus.rule.search.indexName cannot be null");    
    }
    String rule_type = "fuzzyMapping";//索引类型
      IndicesExistsRequest existsRequest = new IndicesExistsRequest(index);
      ActionFuture<IndicesExistsResponse> response = getClient().admin().indices().exists(existsRequest);
      if(!response.actionGet().isExists()){
         ElasticsearchUtil.createIndex(index);
      }
      XContentBuilder mapping;
      mapping = jsonBuilder()
          .startObject()
          .startObject(rule_type)
          .startObject("_all").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("term_vector","no").field("store","false").endObject()
          .startObject("properties")
            .startObject("fuzzyword").field("type","string").field("store","no").field("term_vector","with_positions_offsets").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("include_in_all","true").endObject()
            .endObject().endObject().endObject();
      ElasticsearchUtil.putMapping(index, rule_type, mapping);
  }
  
  /**
   * 检查索引库中是否存在这个索引
   * @param index
   * @return
   */
  public static void deleteIndicesExistsIndex(String index){
    IndicesExistsRequest inicesExists = new IndicesExistsRequest(index);
    ActionFuture<IndicesExistsResponse> response = getClient().admin().indices().exists(inicesExists);
    if(response.actionGet().isExists()){
      deleteIndex(index);
    }
  };
}
