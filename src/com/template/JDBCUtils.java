package com.template;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


/**
 * 
 * 类JDBCUtils.java的实现描述：TODO JDBC操作简单工具类
 * 
 * @author yinhongbiao 2011-4-19 下午05:37:40
 */
@Component
public class JDBCUtils {
	
	private static final Logger logger = Logger.getLogger(JDBCUtils.class);

	private static JDBCUtils jdbcUtils = null;

	private String jdbc_driver = "com.mysql.jdbc.Driver"; // jdbc驱动

//	private String jdbc_url = "jdbc:mysql://59.110.12.207:3306/monitor?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8";
//	private String jdbc_url = "jdbc:mysql://123.57.214.169:3306/test";
	private String jdbc_url = "jdbc:mysql://40.73.16.153:3308/honda_com?useUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true";
	// //jdbc连接Urls

	private String user_name = "benz"; //jdbc连接用户名

	private String user_password = "19GEg!Dai*923r12."; //jdbc连接密码

	private String batch_size = "200000"; //批量提交数

	private JDBCUtils() {
		
		
	}
	
	public JDBCUtils(String url,String name,String passwd) {
		jdbc_url = url;
		user_name = name;
		user_password = passwd;
	}
	
	private void initProperty() throws Exception {
		Config c =Config.getInstance();
		jdbc_url = c.getValue("jdbc.url");
		user_name = c.getValue("jdbc.username");
		user_password = c.getValue("jdbc.password");
		batch_size = c.getValue("jdbc.batch_size");
	}


	/**
	 * 创建JDBC工具类实例
	 * 
	 * @return
	 */
	public static synchronized JDBCUtils getInstance() {

		if (jdbcUtils == null) {
			jdbcUtils = new JDBCUtils();
		}
		return jdbcUtils;
	}
	
	public static synchronized JDBCUtils getInstance(String url,String name,String passwd) {
		if (jdbcUtils == null) {
			jdbcUtils = new JDBCUtils(url,name,passwd);
		}
		return jdbcUtils;
	}

	/**
	 * 获取 数据库连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		try {
//			initProperty();
			Class.forName(jdbc_driver);
			Connection conn = DriverManager.getConnection(jdbc_url, user_name, user_password);
			//Connection conn = DriverManager.getConnection("jdbc:mysql://172.20.1.93:8066/yly_b2c_db?useUnicode=true&characterEncoding=utf-8&autoReconnect=true","yougou","ems");
			//Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yly_b2c_db?useUnicode=true&characterEncoding=utf-8&autoReconnect=true","root","123456");
			return conn;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	/**
	 * 关闭数据库相关连接
	 * 
	 * @param connection
	 */
	public void close(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
				st = null;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null)
						conn.close();
					conn = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 关闭数据库相关连接
	 * 
	 * @param connection
	 */
	private void close(PreparedStatement pstmt, Connection conn) {
		try {
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 增加单条数据
	 * 
	 * @param sql
	 *            sql语句
	 * @param values
	 *            参数值
	 * @return 是否增加成功
	 * @throws Exception
	 */
	public boolean saveOrUpdate(String sql, Object... values) throws Exception {
		Connection conn = getConnection(); // 获取数据库连接
		PreparedStatement pstmt = null;
		try {
			conn.setAutoCommit(false); // 设置手动提交事务
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			// 赋值
			if(null!=values){
			  for (int i = 0; i < values.length; i++) {
				pstmt.setObject(i + 1, values[i]) ;		
			  }
			}
			pstmt.execute(); // 执行操作
			conn.commit(); // 提交事务
			close(pstmt, conn); // 关闭相关连接
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception();
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return true;
	}

	/**
	 * 删除
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean batchDelete(String sql) {
		Connection conn = null; // 获取数据库连接
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false); // 设置手动提交事务
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象

			pstmt.execute(); // 执行操作
			conn.commit(); // 提交事务
			close(pstmt, conn); // 关闭相关连接
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return true;

	}

	/**
	 * 批量增加与修改
	 * 
	 * @param sql
	 *            insert or update 语句
	 * @param params
	 *            参数集合
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * @throws SQLException
	 */
	public boolean batchSaveOrUpdate(String sql, List<Object[]> paramList) {
		PreparedStatement pstmt = null;
		int count = 0;
		Connection conn = null;
		try {
			conn = getConnection(); // 获取数据库连接
			count = Integer.parseInt(batch_size) - 1;
			conn.setAutoCommit(false); // 设置手动提交事务
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			// 赋值
			for (int i = 0; i < paramList.size(); i++) {

				Object[] values = paramList.get(i);
				for (int j = 0; j < values.length; j++) {
					pstmt.setObject(j + 1, values[j]);
				}
				pstmt.addBatch();

				// 批量数等于 batch_size 时 提交数据
				if (i != 0 && (i % count == 0)) {
					int ids[] = pstmt.executeBatch(); // 执行操作
					if (ids.length == count + 1) {
						conn.commit(); // 提交事务
					} else {
						conn.rollback(); // 事务回滚
					}
					pstmt.clearBatch();
				}
			}

			int ids[] = pstmt.executeBatch(); // 执行操作
			if (ids.length == paramList.size() % (count + 1)) {
				conn.commit(); // 提交事务
			} else {
				conn.rollback(); // 事务回滚
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false; // 如果异常就返回false
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return true;
	}

	/**
	 * 批量更新 or 保存
	 * 
	 * @author huangbin
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean batchAllSaveOrUpdate(Map<String, List<Object[]>> map) throws Exception {
		if (map == null || map.size() == 0)
			return false;
		// int count = Integer.parseInt(batch_size)-1;
		Connection conn = getConnection(); // 获取数据库连接
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			for (Iterator<Entry<String, List<Object[]>>> iter = map.entrySet().iterator(); iter.hasNext();) {
				Entry<String, List<Object[]>> entry = iter.next();
				// 动态获取preparedstatement对象
				ps = conn.prepareStatement(entry.getKey());
				for (int i = 0; i < entry.getValue().size(); i++) {
					Object[] obj = entry.getValue().get(i);
					for (int j = 0; j < obj.length; j++) {
						ps.setObject(j + 1, obj[j]);
					}

					ps.addBatch();

					if (i == entry.getValue().size() - 1) {

						int[] num = ps.executeBatch();

						if (num.length != entry.getValue().size()) {
							throw new Exception("执行的数据记录不相等");
						}

						ps.clearBatch();

					}
				}
			}

			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return false;
			}
			e.printStackTrace();
			return false;
		} finally {
			close(ps, conn);
		}
		return true;
	}
	
	/**
	 * 批量更新库存预占数量
	 * 
	 * @author huangbin
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean batchUpdateInventoryPreemptedQuantity(Map<String, List<Object[]>> map) throws Exception {
		if (map == null || map.size() == 0)
			return false;
		// int count = Integer.parseInt(batch_size)-1;
		Connection conn = getConnection(); // 获取数据库连接
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			for (Iterator<Entry<String, List<Object[]>>> iter = map.entrySet().iterator(); iter.hasNext();) {
				Entry<String, List<Object[]>> entry = iter.next();
				// 动态获取preparedstatement对象
				ps = conn.prepareStatement(entry.getKey());
				for (int i = 0; i < entry.getValue().size(); i++) {
					Object[] obj = entry.getValue().get(i);
					for (int j = 0; j < obj.length; j++) {
						ps.setObject(j + 1, obj[j]);
					}

					ps.addBatch();

					if (i == entry.getValue().size() - 1) {

						int[] num = ps.executeBatch();

						if (num.length != entry.getValue().size()) {
							throw new Exception("执行的数据记录不相等");
						}

						ps.clearBatch();

					}
				}
			}

			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return false;
			}
			e.printStackTrace();
			return false;
		} finally {
			close(ps, conn);
		}
		return true;
	}
	
	
	/**
	 * 
	 * 方法描述：在调用处提交事务
	 * @author xiongjingang
	 * @date 2011-7-8 下午01:37:50
	 * @param conn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean batchAllSaveOrUpdate(Connection conn, Map<String, List<Object[]>> map) throws SQLException {
		logger.info("批量JDBC开始---------------------------------------");
		boolean success = false;
		if (map == null || map.size() == 0) {
			return false;
		}
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			for (Iterator<Entry<String, List<Object[]>>> iter = map.entrySet().iterator(); iter.hasNext();) {
				Entry<String, List<Object[]>> entry = iter.next();
				// 动态获取preparedstatement对象
				ps = conn.prepareStatement(entry.getKey());
				for (int i = 0; i < entry.getValue().size(); i++) {
					Object[] obj = entry.getValue().get(i);
					for (int j = 0; j < obj.length; j++) {
						ps.setObject(j + 1, obj[j]);
					}
					ps.addBatch();
					if (i == entry.getValue().size() - 1) {

						int[] num = ps.executeBatch();

						if (num.length != entry.getValue().size()) {
							throw new SQLException("执行的数据记录不相等");
						}
						ps.clearBatch();
					}
				}
			}
			//conn.commit();
			logger.info("批量JDBC"+success+"--");
			success = true;
			logger.info("批量JDBC"+success+"--");
		} catch (SQLException e) {
			logger.error("batchAllSaveOrUpdate批量JDBC"+e.getMessage());
			e.printStackTrace();
		}finally{
			if(null!=ps){
				ps.close();
			}
			logger.info("状态为--"+success);
		}
		logger.info("批量JDBC结束---------------------------------------");
		return success;
	}

	/**
	 * 批量增加与修改
	 * 
	 * @param sql
	 *            insert or update 语句
	 * @param params
	 *            参数集合
	 * @return
	 * @author huangbin
	 * @throws Exception
	 * @throws Exception
	 * @throws Exception
	 * @throws SQLException
	 */
	public boolean batchOffPayUpdate(String sql, List<Object[]> paramList) throws Exception {
		PreparedStatement pstmt = null;
		int count = 0;
		Connection conn = null;
		try {
			conn = getConnection(); // 获取数据库连接
			count = Integer.parseInt(batch_size) - 1;
			conn.setAutoCommit(false); // 设置手动提交事务
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			// 赋值
			for (int i = 0; i < paramList.size(); i++) {

				Object[] values = paramList.get(i);
				for (int j = 0; j < values.length; j++) {
					pstmt.setObject(j + 1, values[j]);
				}
				pstmt.addBatch();

				// 批量数等于 batch_size 时 提交数据
				if (i != 0 && (i % count == 0)) {
					int ids[] = pstmt.executeBatch(); // 执行操作
					if (ids.length == count + 1) {
						conn.commit(); // 提交事务
					} else {
						conn.rollback(); // 事务回滚
					}
					pstmt.clearBatch();
				}
			}

			int ids[] = pstmt.executeBatch(); // 执行操作
			if (ids.length == paramList.size() % (count + 1)) {
				conn.commit(); // 提交事务
			} else {
				conn.rollback(); // 事务回滚
			}

		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			throw new Exception();
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return true;
	}

	/**
	 * CSV批量导入 方法描述：TODO 方法实现描述
	 * 
	 * @date 2011-6-14 下午04:39:46
	 * @param sql
	 * @param list
	 * @return
	 * @throws Exception
	 */

	/**
	 * 执行存储过程 方法描述：TODO 方法实现描述
	 * 
	 * @date 2011-6-14 下午04:21:27
	 * 
	 * @throws Exception
	 */
	public boolean executecallprocedure(String supplyid) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			// conn=WmsConnectionInitproperty(BaseDataManagerConstant.AREA_JDBC_DRIVER,BaseDataManagerConstant.AREA_JDBC_URL,BaseDataManagerConstant.AREA_JDBC_USERNAME,BaseDataManagerConstant.AREA_JDBC_PASSWORD);
			// //获取数据库连接
			conn = getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareCall("{call sp_update_area_warehouse_inventory (?)}");// 执行存储过程
			pstmt.setString(1, supplyid);
			pstmt.executeUpdate();
//			String sql = "delete from area_warehouse_inventory";
//			deletewarehouseinventory(sql, pstmt, conn);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			close(pstmt, conn);
		}
		return true;
	}

	public void deletewarehouseinventory(String sql, PreparedStatement pstmt, Connection conn) throws Exception {
		try {
			pstmt = conn.prepareStatement(sql);// 将库存记录表数据清空
			pstmt.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}



    /**
     * 一淘增量查询索引
     * 
     * @Title: IncrementQueryFeed
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param @param timestmap 时间戳
     * @param @return
     * @param @throws Exception 设定文件
     * @return List<IEtaoParamGoodsComeVo> 返回类型
     * @author zhu.b
     * @date 2011-6-25 上午10:39:35
     * @throws
     */

	/**
	 * jdbc查询
	 * 
	 * @param sql
	 *            sql语句
	 * @return
	 * @throws Exception
	 */
	public ResultSet queryList(String sql) {
		Connection conn = null; // 获取数据库连接
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			resultSet = pstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return resultSet;
	}
	
	/**
	 * 
	 * 方法描述：统计查询结果集行数
	 * @author xiongjingang
	 * @date 2011-7-12 上午09:21:27
	 * @param sql
	 * @return int
	 */
	public int count(String sql) {
		int result = 0;
		Connection conn = null; // 获取数据库连接
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return result;
	}

	/**
	 * 
	 * 方法描述：统计查询结果集行数
	 * @author xiongjingang
	 * @date 2011-7-12 上午09:21:27
	 * @param sql
	 * @return int
	 */
	public int count(String sql, Object[] objects) {
		int result = 0;
		Connection conn = null; // 获取数据库连接
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			for (int j = 0; j < objects.length; j++) {
				pstmt.setObject(j + 1, objects[j]);
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return result;
	}
	
	/**
	 * 批量增加与修改
	 * 
	 * @param sql
	 *            insert or update 语句
	 * @param params
	 *            参数集合
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public boolean batchSaveOrUpdate(String sql, List<Object[]> paramList, long timestamp) throws Exception {
		boolean flag = false;
		int count = Integer.parseInt(batch_size) - 1;
		Connection conn = getConnection(); // 获取数据库连接
		PreparedStatement pstmt = null;
		try {
			conn.setAutoCommit(false); // 设置手动提交事务
			pstmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			// 赋值
			for (int i = 0; i < paramList.size(); i++) {

				Object[] values = paramList.get(i);
				for (int j = 0; j < values.length; j++) {
					pstmt.setObject(j + 1, values[j]);
				}
				pstmt.setObject(values.length + 1, timestamp++);
				pstmt.addBatch();

				// 批量数等于 batch_size 时 提交数据
				if (i != 0 && (i % count == 0)) {
					int ids[] = pstmt.executeBatch(); // 执行操作
					if (ids.length == count + 1) {
						conn.commit(); // 提交事务
					} else {
						conn.rollback(); // 事务回滚
					}
					pstmt.clearBatch();
				}
			}

			int ids[] = pstmt.executeBatch(); // 执行操作
			if (ids.length == paramList.size() % (count + 1)) {
				conn.commit(); // 提交事务
				flag = true;
			} else {
				conn.rollback(); // 事务回滚
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt, conn); // 关闭相关连接
		}
		return flag;
	}

	/**
	 * 查询终止原因：
	 * 
	 * @return
	 * @throws Exception
	 */
	/*public Map<String,List<DictionaryTypeData>> getDictionary() throws Exception{
		Map<String,List<DictionaryTypeData>> m=new HashMap<String,List<DictionaryTypeData>>();
		 //联通发起终止
		 List<DictionaryTypeData> cData=new ArrayList<DictionaryTypeData>();
		 //用户发起终止
		 List<DictionaryTypeData> tData=new ArrayList<DictionaryTypeData>();
		 //商品税率
		 List<DictionaryTypeData> offerTaxData = new ArrayList<DictionaryTypeData>();
		 
		 Connection conn = getConnection(); // 获取数据库连接
		 PreparedStatement pstmt = null;
		 ResultSet resultSet = null;
		 String sql="select t1.id id,t1.type_code typeCode,t2.id dataId,t2.data_key dataKey,t2.data_reason dataReason," +
		 		"t2.interface_code interfaceCode,t2.data_order dataOrder,t2.data_other dataOther,t2.data_other2 dataOther2,t2.data_remark dataRemark " +
		 		" from tbl_dictionary_type t1 ,tbl_dictionary_type_data t2 where t1.id=t2.type_id ORDER BY t1.type_name ,t2.data_order";
		 pstmt=conn.prepareStatement(sql);
		 resultSet=pstmt.executeQuery();
		 if(resultSet!=null){
			 while(resultSet.next()){
				 DictionaryTypeData data= new DictionaryTypeData();
				 data.setId(resultSet.getString("dataId"));
				 data.setTypeId(resultSet.getString("id"));
				 data.setData_other(resultSet.getString("dataOther"));
				 data.setData_other2(resultSet.getString("dataOther2"));
				 data.setDataKey(resultSet.getString("dataKey"));
				 data.setDataOrder(resultSet.getString("dataOrder"));
				 data.setDataRemark(resultSet.getString("dataRemark"));
				 data.setDataReason(resultSet.getString("dataReason"));
				 data.setInterfaceCode(resultSet.getString("interfaceCode"));
				 //属于联通发起的终止
				 if(Constant.CANCELLATION.equals(resultSet.getString("typeCode"))){
					 cData.add(data);
				 }
			    //用户发起的终止	 
				 if(Constant.TERMINATION.equals(resultSet.getString("typeCode"))){
					 tData.add(data);
				 }
				 //商品税率
				 if(Constant.OFFER_TAX.equals(resultSet.getString("typeCode"))){
					 offerTaxData.add(data);
				 }
			 }
		 }
		 m.put(Constant.CANCELLATION, cData);
		 m.put(Constant.TERMINATION, tData);
		 m.put(Constant.OFFER_TAX, offerTaxData);
		return m;
	}*/
	/**
	 * 查询返回单一结果
	 * @param sql
	 * @return
	 */
	public Object uniqueResult(String sql) {
		
		return uniqueResult(sql, null);
	}
	
	public Object uniqueResult(String sql, Object[] values) {
		
		Object result = null;
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(sql);
			
			if (ArrayUtils.isNotEmpty(values)) {
				
				for (int j = 0; j < values.length; j++) {
					
					pstmt.setObject(j + 1, values[j]);
				}
			}
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				
				result = rs.getObject(1);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			close(pstmt, conn);
		}
		
		return result;
	}
	
	/**
	 * 批理执行更新
	 * @param sqlBatchs
	 * @return boolean
	 */
	public boolean executeBatch(Map<String, List<Object[]>> sqlBatchs) {
		
		boolean result = false;
		
		if (MapUtils.isEmpty(sqlBatchs)) {
			
			return result;
		}
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			conn.setAutoCommit(false);
			
			int rowCount = 0;
			
			int batchCount = Integer.parseInt(batch_size);
			
			for (Map.Entry<String, List<Object[]>> entry : sqlBatchs.entrySet()) {
				
				pstmt = conn.prepareStatement(entry.getKey());
				
				rowCount = 0;
				
				for (Object[] objects : entry.getValue()) {
					
					for (int i = 0; i < objects.length; i++) {
						
						pstmt.setObject(i + 1, objects[i]);
					}
					
					pstmt.addBatch();
					
					if (++rowCount % batchCount == 0) {
						
						pstmt.executeBatch();
					}
				}
				
				if (rowCount % batchCount != 0) {
					
					pstmt.executeBatch();
				}
			}
			
			conn.commit();
			
			result = true;
			
		} catch (Exception e) {
			
			logger.error(e);
			
			try {
				conn.rollback();
				
			} catch (SQLException ex) {
				
				logger.error(e);
			}
		} finally {
			
			logger.info(sqlBatchs);
			
			close(conn, pstmt);
		}
		
		return result;
	}
	
	/**
	 * 批理执行更新
	 * @param sqlBatchs
	 * @return boolean
	 */
	public boolean executeBatch(List<SQLBatch> sqlBatchs) {
		
		boolean result = false;
		
		if (CollectionUtils.isEmpty(sqlBatchs)) {
			
			return result;
		}
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			conn.setAutoCommit(false);
			
			int rowCount = 0;
			
			int batchCount = Integer.parseInt(batch_size);
			
			Collections.sort(sqlBatchs);
			
			for (SQLBatch sqlBatch : sqlBatchs) {
				
				pstmt = conn.prepareStatement(sqlBatch.getSql());
				
				rowCount = 0;
				
				for (Object[] objects : sqlBatch.getSqlParams()) {
					
					for (int i = 0; i < objects.length; i++) {
						
						pstmt.setObject(i + 1, objects[i]);
					}
					
					pstmt.addBatch();
					
					if (++rowCount % batchCount == 0) {
						
						pstmt.executeBatch();
					}
				}
				
				if (rowCount % batchCount != 0) {
					
					pstmt.executeBatch();
				}
			}
			
			conn.commit();
			
			result = true;
			
		} catch (Exception e) {
			
			logger.error(e);
			
			try {
				conn.rollback();
				
			} catch (SQLException ex) {
				
				logger.error(e);
			}
		} finally {
			
			logger.info(sqlBatchs);
			
			close(conn, pstmt);
		}
		
		return result;
	}
	
	
	/**
	 * 商品价格段批量更新
	 * @author JASON
	 * @param sqlBatchs
	 * @return
	 */
	public boolean executeBatchOfCommodity(List<SQLBatch> sqlBatchs) {

		boolean result = false;

		if (sqlBatchs == null || sqlBatchs.isEmpty()) {

			return result;
		}

		Connection conn = null;

		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			conn.setAutoCommit(false);

			Collections.sort(sqlBatchs);

			for (SQLBatch sqlBatch : sqlBatchs) {
				pstmt = conn.prepareStatement(sqlBatch.getSql());
				pstmt.addBatch();
				pstmt.executeBatch();
			}

			conn.commit();

			result = true;

		} catch (Exception e) {

			logger.error(e);

			try {
				conn.rollback();

			} catch (SQLException ex) {

				logger.error(e);
			}
		} finally {

			logger.info(sqlBatchs);

			try {
				if (pstmt != null) {

					pstmt.close();

					pstmt = null;
				}
			} catch (SQLException e) {

				logger.error(e);
			}

			try {
				if (conn != null) {

					if (!conn.isClosed()) {

						conn.close();
					}

					conn = null;
				}
			} catch (SQLException e) {

				logger.error(e);
			}
		}

		return result;
	}
	
	
	
	private void close(Connection conn, Statement pstmt) {
		
		close(conn, pstmt, null);
	}
	
	private void close(Connection conn, Statement pstmt, ResultSet rs) {
		
		try {
			if (rs != null) {
				
				rs.close();
				
				rs = null;
			}
		} catch (SQLException e) {
			
			logger.error(e);
		}
		
		try {
			if (pstmt != null) {
				
				pstmt.close();
				
				pstmt = null;
			}
		} catch (SQLException e) {
			
			logger.error(e);
		}
		
		try {
			if (conn != null) {
				
				if (!conn.isClosed()) {
					
					conn.close();
				}
				
				conn = null;
			}
		} catch (SQLException e) {
			
			logger.error(e);
		}
	}
	
	public static final class SQLBatch implements Comparable<SQLBatch> {
		
		private Integer priority;//优先级
		private String sql;//SQL语句
		private List<Object[]> sqlParams;//SQL语句参数

		public SQLBatch(Integer priority, String sql, List<Object[]> sqlParams) {
			this.priority = priority;
			this.sql = sql;
			this.sqlParams = sqlParams;
		}

		public Integer getPriority() {
			return priority;
		}

		public String getSql() {
			return sql;
		}

		public List<Object[]> getSqlParams() {
			return sqlParams;
		}

		@Override
		public int compareTo(SQLBatch o) {
			return this.priority.compareTo(o.priority);
		}

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(" SQLBatch { priority = ").append(priority).append(", sql = ").append(sql);
			
			for (int i = sqlParams.size() - 1; i >= 0; i--) {
				
				sb.append(", sqlParams[").append(i).append("] = ").append(Arrays.toString(sqlParams.get(i)));
			}
			
			return sb.append(" } ").toString();
		}
	}	

	public static void main(String[] args) throws SQLException {
		 JDBCUtils utils = JDBCUtils.getInstance();
		
		 String sql =
		 "insert into tbl_mnemonic_code (id,code,content,create_user_id,type,create_time) values(?,?,?,?,?,?);";
		 List paramList = new ArrayList();
		 for (int i = 0; i < 3; i++) {
			 Object [] param = new Object[]{i+"",i+"c",i+"co",i+"user",i+"",new Date()};
			 paramList.add(param);
		 }
		 boolean t = utils.batchSaveOrUpdate(sql, paramList);
		 System.out.println(t);
	}
}
class Config {

	private static Config instance = null;
	
	private Properties properties = null;
	
	private Config() {
		init();
	}
	
	public static Config getInstance() {
		
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	/**
	 * 初始化配置文件
	 */
	public void init(){
		
		try{
			InputStream is = Config.class.getResourceAsStream("/application.properties");
			properties = new Properties();
			properties.load(is);
			
		}catch (Exception e){
			throw new RuntimeException("Failed to get properties!");
		}
	}
	
	/**
	 * 根据key值取得对应的value值
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		return properties.getProperty(key);
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}
}