package me.yummykang.utils;

import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.yummykang.bean.FetcherUrl;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * mongodb工具类.
 *
 * @author demon
 * @Date 2016/11/30 13:54
 */
public class MongodbUtils {

    private static Logger logger = LoggerFactory.getLogger(MongodbUtils.class);

    private static final String HOST = "mongo.host";

    private static final String PORT = "mongo.port";

    private static final String DB_NAME = "mongo.db_name";

    private static final String PROPERTIES_FILE_NAME = "mongodb.properties";

    private static PropertiesUtils propertiesUtils = null;

    private static MongoClient client = null;

    private static String DB_NAME_RUNTIME = "";

    private static MongoDatabase DB = null;

    static {
        propertiesUtils = new PropertiesUtils(PROPERTIES_FILE_NAME);
        client = new MongoClient(propertiesUtils.getStringValue(HOST), propertiesUtils.getIntValue(PORT));
        DB_NAME_RUNTIME = propertiesUtils.getStringValue(DB_NAME);
        DB = client.getDatabase(DB_NAME_RUNTIME);
    }

    /**
     * 新增一条数据
     *
     * @param param
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void insertOne(Object param) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        logger.info("添加一条数据至{}：{}", DB_NAME_RUNTIME, param);
        DB.getCollection(DB_NAME_RUNTIME).insertOne(new Document(BeanUtil.beanToMap(param, param.getClass())));
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    public static List<Document> findAll() {
        MongoCollection<Document> result = DB.getCollection(DB_NAME_RUNTIME);
        return result.find(Filters.eq("status", 1)).into(new ArrayList<>());
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        FetcherUrl fetcherUrl = new FetcherUrl();
//        fetcherUrl.setUrl("www.google.com");
//        fetcherUrl.setStatus(1);
//        MongodbUtils.insertOne(fetcherUrl);
        System.out.println(findAll().size());
    }
}
