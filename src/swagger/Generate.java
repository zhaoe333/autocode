package swagger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.template.JDBCUtils;
import com.template.JavaType;
import com.template.TemplateColumn;
import com.template.TemplateTable;
import com.template.util.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class Generate {
	
	private static final Logger log = LoggerFactory.getLogger(Generate.class);
	
	private static final String jdbc_url = "jdbc:mysql://59.110.12.207:3306/na_iot?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8";

	private static final String user_name = "root"; //jdbc连接用户名

	private static final String user_password = "FM3.1415926"; //jdbc连接密码
	
	private static final String tableNamePrefix="cm_";
	
	private static final String basePackagePath = "com.cm";
	//生成代码文件夹
	private static final String basePath = "/home/futuremove/myswagger/";
	//数据库名称
	private static final String dbName = "na_iot";
	//生成文件编码
	private static String fileEncoding = "UTF-8";
	//模板文件路径
	private static String baseTemplatePath = "swagger";
	//作者（生成Class文件注解的作者信息）
	private static String author = "zyl";
	
	public static void main(String[] args) throws Exception{
		
		Connection conn = null;
		Statement stmt =null;
		ResultSet rs = null;
		List<TemplateTable> tables = new ArrayList<TemplateTable>();
		
		try{
			conn = JDBCUtils.getInstance(jdbc_url,user_name,user_password).getConnection();
			String sql = "select table_name,table_comment from information_schema.tables where table_schema='"+dbName+"'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				TemplateTable table = new TemplateTable();
				table.setTableName(rs.getString("table_name").toLowerCase());
				String comment = rs.getString("table_comment").toString().split(";")[0].trim();
				if(comment != null && !comment.equals("")){
					//去除最后一个“表”字
					if("表".equals(comment.substring(comment.length()-1))){
						comment = comment.substring(0,comment.length()-1);
					}
				}else{
					comment = "";
				}
				table.setComment(comment);
				tables.add(table);
			}
			rs.close();
			stmt.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null&&!rs.isClosed()){
				rs.close();
			}
			if(stmt!=null&&!stmt.isClosed()){
				stmt.close();
			}
			if(conn!=null&&!conn.isClosed()){
				conn.close();
			}
		}
		/**
		 * 生成文件
		 */
		bulid(tables);
	}
	
	/**
	 * 生成所有Java类文件
	 * @param tableNames
	 * 		表名数组
	 * @throws Exception
	 */
	public static void bulid(List<TemplateTable> tables) throws Exception{
		List<Map<String,String>> entitys = new ArrayList<Map<String,String>>();
		for(TemplateTable table : tables){
			entitys.add(build(table));
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("entitys", entitys);
		generateFile(map,"sqlconfig.ftl","sqlconfig.txt");
	}
	
	/**
	 * 生成所有Java类文件
	 * @param tableName
	 * 		表名
	 * @throws Exception
	 */
	private static Map<String,String> build(TemplateTable table) throws Exception{
		log.info("生成文件...");
		//实体Name
		String entityName = getEntityClassName(table.getTableName());
		//包动态路径
		String prex=table.getTableName().split("_")[1];
		//包路径
		String packagePath = getPackageName(table.getTableName());
		//ftl访问路径
		String ftlPath = getFtlPath(table.getTableName());
		//生成类文件实际路径
		String filePath = getFilePath(packagePath)+"/"+prex;
		//获取所有属性
		List<TemplateColumn> columns = getColumns(table);
		boolean hasTime = false;
		for(TemplateColumn column:columns){
			if("create_time".equals(column.getColumnName())||
					"update_time".equals(column.getColumnName())){
				hasTime=true;
			}
		}
		//生成Entity和Vo
		buildEntity(entityName, packagePath, table, filePath,columns,prex);
		//生成Service
		buildService(entityName, packagePath, table.getComment(), filePath,prex,hasTime);
		//生成Controller
		buildController(entityName, packagePath, ftlPath, table.getComment(), filePath,prex,columns);
		//生成mapper
		buildMapper(entityName, packagePath, table, filePath,columns,prex,hasTime);
		Map<String,String> map = new HashMap<String,String>();
		map.put("packagePath", packagePath);
		map.put("prex", prex);
		map.put("entityName", entityName);
		return map;
	}
	
	/**
	 * 生成Service
	 * @param tableName
	 * 				表名
	 * @throws Exception
	 */
	public static void buildService(String entityName, String packagePath, String comment, String filePath,String prex,boolean hasTime)throws Exception{
		
		log.info("生成"+entityName+"Service..");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("interfaceName", "I"+ entityName+"Service");
		map.put("className", entityName+"ServiceImpl");
		map.put("entityName", entityName);
		map.put("commentInfo", comment);
		map.put("author", author);
		map.put("packagePath", packagePath);
		map.put("prex", prex);
		map.put("hasTime", hasTime);
		//生成接口
		generateFile(map, "iservice.ftl", filePath + "/service/I"+entityName+"Service.java");
		//生成实现类
		generateFile(map, "serviceImpl.ftl", filePath + "/service/impl/"+entityName+"ServiceImpl.java");
		
	}
	
	/**
	 * 生成Controller
	 * @param tableName
	 * 				表名
	 * @throws Exception
	 */
	public static void buildController(String entityName, String packagePath, String ftlPath, String comment, String filePath,String prex,List<TemplateColumn> columns )throws Exception{
		
		log.info("生成"+entityName+"Controller..");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("className", entityName+"Controller");
		map.put("entityName", entityName);
		map.put("ftlPath", ftlPath);
		map.put("prex", prex);
		map.put("author", author);
		map.put("commentInfo", comment);
		map.put("prex", prex);
		map.put("packagePath", packagePath);
		
		List<String> required=new ArrayList<String>();
		List<String> norequired=new ArrayList<String>();
		for(TemplateColumn column:columns){
			if(Arrays.asList("id","createTime","updateTime","deleteFlag").contains(column.getName())){
				continue;
			}
			if(column.getIsNullAble()==0){
				norequired.add(column.getName());
			}else{
				required.add(column.getName());
			}
		}
		map.put("norequired","$"+ StringUtil.join(norequired));
		map.put("required", "$"+ StringUtil.join(required));
		//生成接口
		generateFile(map, "controller.ftl", filePath + "/web/"+entityName+"Controller.java");
	}
	
	/**
	 * 生成实体
	 * @param entityName
	 * @param packagePath
	 * @param table
	 * @param filePath
	 * @param columns
	 * @throws Exception
	 */
	public static void buildEntity(String entityName, String packagePath, TemplateTable table, String filePath,List<TemplateColumn> columns,String prex)throws Exception{
		log.info("生成"+entityName+"Entity...");
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("columns", columns);
		map.put("tableName", table.getTableName());
		map.put("commentInfo", table.getComment());
		map.put("author", author);
		map.put("entityName", entityName);
		map.put("prex", prex);
		map.put("packagePath", packagePath);
		//生成实体
		generateFile(map, "entity.ftl", filePath + "/entity/" + entityName + ".java");
		//生成Vo
		generateFile(map, "vo.ftl", filePath + "/query/vo/" + entityName + "Vo.java");
		//生成QueryVo
		generateFile(map, "queryVo.ftl", filePath + "/query/" + entityName + "Query.java");
	}
	
	/**
	 * 生成mapper
	 * @throws Exception
	 */
	public static void buildMapper(String entityName, String packagePath, TemplateTable table, String filePath,List<TemplateColumn> columns,String prex,boolean hasTime)throws Exception{
		log.info("生成"+entityName+"Mapper...");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("interfaceName",  entityName+"Mapper");
		map.put("columns", columns);
		map.put("tableName", table.getTableName());
		map.put("commentInfo", table.getComment());
		map.put("author", author);
		map.put("entityName", entityName);
		map.put("prex", prex);
		map.put("packagePath", packagePath);
		map.put("hasDeleteFlag", table.isHasDeleteFlag());
		map.put("hasTime", hasTime);
		//生成mapper
		generateFile(map, "mapper.ftl", filePath + "/mapper/" + entityName + "Mapper.java");
		//生成mapper.xml
		generateFile(map, "mapperxml.ftl", filePath + "/mapper/" + entityName + "Mapper.xml");
	}
	/**
	 * 生成文件
	 * @throws Exception
	 */
	private static void generateFile(Map<String,Object> map, String templateFile, String fileName) throws Exception{
		
		Configuration config = new Configuration();
		config.setDirectoryForTemplateLoading(new File(baseTemplatePath));
		config.setObjectWrapper(new DefaultObjectWrapper());
		//生成文件
		Template template = config.getTemplate(templateFile, fileEncoding);
		File file = new File(basePath + fileName);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		file.createNewFile();
		Writer out = new BufferedWriter(new FileWriter(file));
		template.process(map, out);
		out.flush();
		out.close();
	}
	
	/**
	 * 根据table名称获取所有Column列
	 * @param tableName
	 * 		表名
	 * @return
	 * 		字段名List
	 * @throws Exception
	 */
	private static List<TemplateColumn> getColumns(TemplateTable table) throws Exception{
		Connection conn = null;
		PreparedStatement stmt =null;
		ResultSet rs = null;
		try{
			List<TemplateColumn> columns = new ArrayList<TemplateColumn>();
			conn = JDBCUtils.getInstance().getConnection();
			String sql = "select * from information_schema.columns where table_name=? and table_schema='"+dbName+"'";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, table.getTableName());
			rs = stmt.executeQuery();
			while(rs.next()){
				TemplateColumn column = new TemplateColumn();
				column.setTableName(table.getTableName());
				String columnName = rs.getString("column_name").toLowerCase();
				column.setColumnName(columnName);
				//java字段名
				column.setName(getFiledName(columnName));
				//java类型
				column.setType(JavaType.preferredJavaTypeForSqlType.get(rs.getString("data_type")));
				if(rs.getString("is_nullable").equals("YES")){
					column.setIsNullAble((byte)0);
				}else{
					column.setIsNullAble((byte)1);
				}
				
				String byteLength=rs.getString("character_octet_length");
				column.setByteLength(byteLength==null?0:Long.parseLong(byteLength));
				String charLength=rs.getString("character_maximum_length");
				column.setCharLength(charLength==null?0:Long.parseLong(charLength));
				String numLength=rs.getString("numeric_precision");
				column.setNumLength(numLength==null?0:Long.parseLong(numLength));
				String pointLength=rs.getString("numeric_scale");
				column.setPointLength(pointLength==null?0:Long.parseLong(pointLength));
				column.setCharSetCode(rs.getString("character_set_name"));
				column.setComment(rs.getString("column_comment"));
				String columnKey = rs.getString("column_key");
				if(columnKey!=null&&columnKey.equals("PRI")){
					column.setIsKey((byte)0);
				}else{
					if(columnKey.equals("UNI")){
						column.setUnique((byte)0);
					}else{
						column.setUnique((byte)1);
					}
					column.setIsKey((byte)1);
				}
				column.setJdbcName(JavaType.preferredJavaTypeToSqlType.get(column.getType()));
				columns.add(column);
				if("delete_flag".equals(column.getColumnName())){
					table.setHasDeleteFlag(true);
				}
			}
			rs.close();
			stmt.close();
			conn.close();
			return columns;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(rs!=null&&!rs.isClosed()){
				rs.close();
			}
			if(stmt!=null&&!stmt.isClosed()){
				stmt.close();
			}
			if(conn!=null&&!conn.isClosed()){
				conn.close();
			}
		}
	}
	
	/**
	 * 根据表名生成Entity类名
	 * @param tableName
	 * @return
	 */
	private static String getEntityClassName(String tableName){
		log.info("tableName:"+tableName);
		String entityName = getHumpString(tableName.replaceFirst(tableNamePrefix, ""));
		if(null!=entityName&&!"".equals(entityName.trim())){
			
		}else{
			entityName=tableNamePrefix.split("_")[1];
		}
		return entityName.substring(0,1).toUpperCase() + entityName.substring(1);
	}
	/**
	 * 根据表名获取包名称
	 * @param tableName
	 * 		表名称
	 * @return
	 * 		包名称
	 */
	private static String getPackageName(String tableName){
		tableName = tableName.replaceFirst(tableNamePrefix, "");
//		return Constant.basePackagePath+ "." + tableName.replace("_", ".");
		return basePackagePath;
	}
	
	/**
	 * 根据表名获取ftl文件访问路径
	 * @param tableName
	 * 		表名称
	 * @return
	 * 		包名称
	 */
	private static String getFtlPath(String tableName){
		tableName = tableName.replaceFirst(tableNamePrefix, "");
		return tableName.replace("_", "/");
	}
	
	/**
	 * 根据字段名获取Java Field名称
	 * @param tableName
	 * @return
	 */
	private static String getFiledName(String columnName){
		return getHumpString(columnName);
	}
	
	/**
	 * 获取驼峰法字符串
	 * @param inStr
	 * @return
	 */
	private static String getHumpString(String inStr){
		String str = "";
		String[] strs = inStr.split("_");
		for(int i =0 ;i<strs.length;i++){
			if(i==0){
				str = strs[i];
			}else{
				str += strs[i].substring(0,1).toUpperCase()+strs[i].substring(1);
			}
		}
		return str;
	}
	
	/**
	 * 根据packagePath获取生成文件路径
	 * @param packagePath
	 * @return
	 */
	private static String getFilePath(String packagePath){
		return packagePath.replace(".", "/");
	}
	
	
}
