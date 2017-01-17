package com.ly.bolg.builder.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ly.bolg.builder.model.TableColumns;

/**
 * @Description:  连接数据库
 * @User: mtwu
 * @Date: 2012-12-9 16:44:35
 * @Version: 1.0
 */
public class DBUtil {
	private static Connection conn;
	private static String dbuser;
	private static final String sqlserver_remark_sql = "select p.value as comment from sys.tables t,sys.columns c,sys.types v,sys.extended_properties p where t.object_id=c.object_id  and c.system_type_id=v.system_type_id and c.column_id=p.minor_id and t.name = ? and c.name = ?";

	private static synchronized Connection getConnection() {

		try {
			if (null == conn || conn.isClosed()) {
				/**
				 * Oracle 获取注释
				 */
				// Properties props =new Properties();
				// props.put("remarksReporting","true");
				// props.put("username",
				// varp.getProperty("dataSource.username"));
				// props.put("password",
				// varp.getProperty("dataSource.password"));
				/**
				 * SQLSERVER 获取注释 --查询某张表中所有列名、数据类型、长度、是否为空和注释 select c.name as
				 * columnName, v.name as typeName, c.max_length as length,
				 * c.is_nullable as isNull, p.value as comment from sys.tables
				 * t, sys.columns c, sys.types v, sys.extended_properties p
				 * where t.object_id=c.object_id and
				 * c.system_type_id=v.system_type_id and c.column_id=p.minor_id
				 * and t.object_id=p.major_id and t.name='SO_Master' order by
				 * c.column_id
				 */
				Properties varp = PropertiesUtil.getDatebaseProperties();
				dbuser = varp.getProperty("dataSource.username");
				Class.forName(varp.getProperty("dataSource.driverClassName"));
				conn = DriverManager.getConnection(
						varp.getProperty("dataSource.url"),
						varp.getProperty("dataSource.username"),
						varp.getProperty("dataSource.password"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 得到数据库表信息
	 * 
	 * @param tablename
	 *            表名
	 * @param needSchema
	 *            是否需要匹配模式 注：这个参数主要是因为在使用oracle时，不同用户下有相同表时，会查出所有表的属性，
	 *            为true时，将使用表名的用户进行匹配，过滤掉其他表，一般置为false就行了。
	 * @return 返回表的每个字段的属性 列表
	 */
	public static List<TableColumns> getDbTableInfo(String tablename,
			boolean needSchema) {
		List<TableColumns> tcList = new ArrayList<TableColumns>();
		try {
			DatabaseMetaData dmd = DBUtil.getConnection().getMetaData();
			// 要获得表所在的编目。串“""”意味着没有任何编目，Null表示所有编目。
			String catalog = null;
			// 要获得表所在的模式。串“""”意味着没有任何模式，Null表示所有模式。该参数可以包含单字符的通配符（“_”）,也可以包含多字符的通配符（“%”）。
			String schema = null;
			if (needSchema) {
				schema = dbuser.toUpperCase();
			}
			// 指出要返回表名与该参数匹配的那些表，该参数可以包含单字符的通配符（“_”）,也可以包含多字符的通配符（“%”）。
			String tableName = tablename.toLowerCase();

			String prikeyCol = null;
			ResultSet prikey = dmd.getPrimaryKeys(catalog, schema, tableName);
			if (prikey.next()) {
				prikeyCol = prikey.getString("COLUMN_NAME");
			}
			ResultSet columns = dmd.getColumns(catalog, schema, tableName, null);

			TableColumns tc = null;
			while (columns.next()) {
				if(needSchema){
					String schem = columns.getString("TABLE_SCHEM");
					if(!schem.equals(schema)){
						break;
					}
				}
				/**
				 * System.out.println("TABLE_CAT  " +
				 * columns.getString("TABLE_CAT") );
				 * System.out.println("TABLE_SCHEM  " +
				 * columns.getString("TABLE_SCHEM") );
				 * System.out.println("TABLE_NAME  " +
				 * columns.getString("TABLE_NAME") );
				 * System.out.println("DATA_TYPE  " +
				 * columns.getString("DATA_TYPE") );
				 * System.out.println("TYPE_NAME  " +
				 * columns.getString("TYPE_NAME") );
				 * System.out.println("COLUMN_SIZE  " +
				 * columns.getString("COLUMN_SIZE") );
				 * System.out.println("BUFFER_LENGTH  " +
				 * columns.getString("BUFFER_LENGTH") );
				 * System.out.println("DECIMAL_DIGITS  " +
				 * columns.getString("DECIMAL_DIGITS") );
				 * System.out.println("NUM_PREC_RADIX  " +
				 * columns.getString("NUM_PREC_RADIX") );
				 * System.out.println("NULLABLE  " +
				 * columns.getString("NULLABLE") );
				 * System.out.println("REMARKS  " + columns.getString("REMARKS")
				 * ); System.out.println("COLUMN_DEF  " +
				 * columns.getString("COLUMN_DEF") );
				 * System.out.println("SQL_DATA_TYPE  " +
				 * columns.getString("SQL_DATA_TYPE") );
				 * System.out.println("SQL_DATETIME_SUB  " +
				 * columns.getString("SQL_DATETIME_SUB") );
				 * System.out.println("CHAR_OCTET_LENGTH  " +
				 * columns.getString("CHAR_OCTET_LENGTH") );
				 * System.out.println("ORDINAL_POSITION  " +
				 * columns.getString("ORDINAL_POSITION") );
				 * System.out.println("IS_NULLABLE  " +
				 * columns.getString("IS_NULLABLE") );
				 * System.out.println("SCOPE_CATALOG  " +
				 * columns.getString("SCOPE_CATALOG") );
				 * System.out.println("SCOPE_SCHEMA  " +
				 * columns.getString("SCOPE_SCHEMA") );
				 * System.out.println("SCOPE_TABLE  " +
				 * columns.getString("SCOPE_TABLE") );
				 * System.out.println("SOURCE_DATA_TYPE  " +
				 * columns.getString("SOURCE_DATA_TYPE") );
				 * System.out.println("IS_AUTOINCREMENT  " +
				 * columns.getString("IS_AUTOINCREMENT") );
				 * System.out.println("COLUMN_NAME  " +
				 * columns.getString("COLUMN_NAME") );
				 * System.out.println("COLUMN_NAME  " +
				 * columns.getString("COLUMN_NAME") );
				 * System.out.println("COLUMN_NAME  " +
				 * columns.getString("COLUMN_NAME") ); System.out.println(
				 * "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				 **/
				String columnName = columns.getString("COLUMN_NAME");
				String columnTypeName = columns.getString("TYPE_NAME");
				// 某列类型的精确度(类型的长度)
				int precision = columns.getInt("COLUMN_SIZE");
				// 小数点后的位数
				int scale = columns.getInt("DECIMAL_DIGITS");
				tc = new TableColumns();
				tc.setTableName(tableName);
				if (prikeyCol != null) {

				}
				if (prikeyCol != null && prikeyCol.equals(columnName)) {
					tc.setPrikey(true);
				}
				tc.setDbColumnName(columnName);
				tc.setColumnName(columnName.toLowerCase());
				tc.setColumnType(columnTypeName);
				if (columnTypeName.equalsIgnoreCase("number")
						|| columnTypeName.equalsIgnoreCase("integer")
						|| columnTypeName.equalsIgnoreCase("bigint")
						|| columnTypeName.equalsIgnoreCase("int")
						|| columnTypeName.equalsIgnoreCase("bigint identity")
						|| columnTypeName.equalsIgnoreCase("smallint")) {
					if (precision > 10) {
						tc.setJavaType("Long");
					} else {
						tc.setJavaType("Integer");
					}
				} 
				else if (tc.getColumnType().equalsIgnoreCase("float")){
          tc.setJavaType("Float");
        }
        else if (tc.getColumnType().equalsIgnoreCase("double")){
          tc.setJavaType("Double");
        }
				else if (tc.getColumnType().equalsIgnoreCase("decimal")
						|| columnTypeName.equalsIgnoreCase("numeric")) {
					if (scale == 0) {
						if (precision > 10) {
							tc.setJavaType("Long");
						} else {
							tc.setJavaType("Integer");
						}
					} else if (scale > 0) {
						if (precision > 10) {
							tc.setJavaType("Double");
						} else {
							tc.setJavaType("Float");
						}
					}
				} else if (tc.getColumnType().equalsIgnoreCase("VARCHAR2")
						|| columnTypeName.equalsIgnoreCase("VARCHAR")
						|| columnTypeName.equalsIgnoreCase("text")) {
					tc.setJavaType("String");
				} else if (tc.getColumnType().equalsIgnoreCase("DATE")
						|| columnTypeName.equalsIgnoreCase("DATETIME")
						|| columnTypeName.equalsIgnoreCase("TIMESTAMP")) {
					tc.setJavaType("Date");
				} else if (tc.getColumnType().equalsIgnoreCase("bit")) {
					tc.setJavaType("Boolean");
				} else {
					tc.setJavaType("String");
				}

				tc.setColumnSize(columns.getInt("COLUMN_SIZE"));
				tc.setColumnDecimalDigits(columns.getInt("DECIMAL_DIGITS"));
				String remark = columns.getString("REMARKS");
				// 判断如果是SQLServer数据库 则使用查询语句获取注释，否则无法获取注释
				if (null == remark
						&& PropertiesUtil.getDatebaseProperties()
								.getProperty("dataSource.url").toLowerCase()
								.indexOf("sqlserver") != -1) {
					PreparedStatement pstmt = conn
							.prepareStatement(sqlserver_remark_sql);
					pstmt.setString(1, tableName);
					pstmt.setString(2, columnName);
					ResultSet resultSet = pstmt.executeQuery();
					if (resultSet.next()) {
						remark = resultSet.getString(1);
					}
					pstmt.close();
					resultSet.close();
				}
				tc.setRemark(null == remark ? "" : remark);
				if ("YES".equalsIgnoreCase(columns.getString("IS_NULLABLE"))) {
					tc.setNullable(true);
				} else {
					tc.setNullable(false);
				}
				tcList.add(tc);
			}

		} catch (SQLException e) {
			System.out.println("读取表结构出现异常");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
				// TODO: handle exception
			}
		}
		return tcList;
	}

	/**
	 * 得到数据库表信息
	 * 
	 * @param tablename
	 *            表名
	 * @param needSchema
	 *            是否需要匹配模式 注：这个参数主要是因为在使用oracle时，不同用户下有相同表时，会查出所有表的属性，
	 *            为true时，将使用表名的用户进行匹配，过滤掉其他表，一般置为false就行了。
	 * @return 返回表的每个字段的属性 列表
	 */
	public static List<TableColumns> getDbTableInfoBySelect(String tablename,
			boolean needSchema) {
		List<TableColumns> tcList = new ArrayList<TableColumns>();
		String sql = "select * from " + tablename;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			DatabaseMetaData dmd = DBUtil.getConnection().getMetaData();
			// 要获得表所在的编目。串“""”意味着没有任何编目，Null表示所有编目。
			String catalog = null;
			// 要获得表所在的模式。串“""”意味着没有任何模式，Null表示所有模式。该参数可以包含单字符的通配符（“_”）,也可以包含多字符的通配符（“%”）。
			String schema = null;
			if (needSchema) {
				schema = dbuser;
			}
			// 指出要返回表名与该参数匹配的那些表，该参数可以包含单字符的通配符（“_”）,也可以包含多字符的通配符（“%”）。
			String tableName = tablename.toUpperCase();

			String prikeyCol = null;
			ResultSet prikey = dmd.getPrimaryKeys(catalog, schema, tableName);
			if (prikey.next()) {
				prikeyCol = prikey.getString("COLUMN_NAME");
			}

			Connection conn = DBUtil.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				// 获得指定列的列名
				String columnName = rsmd.getColumnName(i);
				// 获得指定列的数据类型
				int columnType = rsmd.getColumnType(i);
				// 获得指定列的数据类型名
				String columnTypeName = rsmd.getColumnTypeName(i);
				// 所在的Catalog名字
				String catalogName = rsmd.getCatalogName(i);
				// 对应数据类型的类
				String columnClassName = rsmd.getColumnClassName(i);
				// 在数据库中类型的最大字符个数
				int columnDisplaySize = rsmd.getColumnDisplaySize(i);
				// 默认的列的标题
				String columnLabel = rsmd.getColumnLabel(i);
				// 获得列的模式
				String schemaName = rsmd.getSchemaName(i);
				// 某列类型的精确度(类型的长度)
				int precision = rsmd.getPrecision(i);
				// 小数点后的位数
				int scale = rsmd.getScale(i);
				// 是否自动递增
				boolean isAutoInctement = rsmd.isAutoIncrement(i);
				// 获取某列对应的表名
				String tName = rsmd.getTableName(i);
				// 在数据库中是否为货币型
				boolean isCurrency = rsmd.isCurrency(i);
				// 是否为空
				int isNullable = rsmd.isNullable(i);
				// 是否为只读
				boolean isReadOnly = rsmd.isReadOnly(i);
				// 能否出现在where中
				boolean isSearchable = rsmd.isSearchable(i);
				System.out.println("获得列" + i + "的字段名称:" + columnName);
				System.out.println("获得列" + i + "的类型,返回SqlType中的编号:"
						+ columnType);
				System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
				System.out.println("获得列" + i + "所在的Catalog名字:" + catalogName);
				System.out.println("获得列" + i + "对应数据类型的类:" + columnClassName);
				System.out.println("获得列" + i + "在数据库中类型的最大字符个数:"
						+ columnDisplaySize);
				System.out.println("获得列" + i + "的默认的列的标题:" + columnLabel);
				System.out.println("获得列" + i + "的模式:" + schemaName);
				System.out.println("获得列" + i + "类型的精确度(类型的长度):" + precision);
				System.out.println("获得列" + i + "小数点后的位数:" + scale);
				System.out.println("获得列" + i + "对应的表名:" + tableName);
				System.out.println("获得列" + i + "是否自动递增:" + isAutoInctement);
				System.out.println("获得列" + i + "在数据库中是否为货币型:" + isCurrency);
				System.out.println("获得列" + i + "是否为空:" + isNullable);
				System.out.println("获得列" + i + "是否为只读:" + isReadOnly);
				System.out.println("获得列" + i + "能否出现在where中:" + isSearchable);
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				TableColumns tc = new TableColumns();
				tc.setTableName(tableName);
				if (prikeyCol.equals(columnName)) {
					tc.setPrikey(true);
				}
				tc.setDbColumnName(columnName);
				tc.setColumnName(columnName.toLowerCase());
				tc.setColumnType(columnTypeName);
				if (columnTypeName.equalsIgnoreCase("number")
						|| columnTypeName.equalsIgnoreCase("integer")
						|| columnTypeName.equalsIgnoreCase("bigint")
						|| columnTypeName.equalsIgnoreCase("int")
						|| columnTypeName.equalsIgnoreCase("bigint identity")
						|| columnTypeName.equalsIgnoreCase("smallint")) {
					if (precision > 10) {
						tc.setJavaType("Long");
					} else {
						tc.setJavaType("Integer");
					}
				} else if (tc.getColumnType().equalsIgnoreCase("decimal")
						|| columnTypeName.equalsIgnoreCase("numeric")) {
					if (scale == 0) {
						if (precision > 10) {
							tc.setJavaType("Long");
						} else {
							tc.setJavaType("Integer");
						}
					} else if (scale > 0) {
						if (precision > 10) {
							tc.setJavaType("Double");
						} else {
							tc.setJavaType("Float");
						}
					}
				} else if (tc.getColumnType().equalsIgnoreCase("VARCHAR2")
						|| columnTypeName.equalsIgnoreCase("VARCHAR")
						|| columnTypeName.equalsIgnoreCase("text")) {
					tc.setJavaType("String");
				} else if (tc.getColumnType().equalsIgnoreCase("DATE")
						|| columnTypeName.equalsIgnoreCase("DATETIME")
						|| columnTypeName.equalsIgnoreCase("TIMESTAMP")) {
					tc.setJavaType("Date");
				} else if (tc.getColumnType().equalsIgnoreCase("bit")) {
					tc.setJavaType("boolean");
				} else {
					tc.setJavaType("String");
				}
				tc.setColumnSize(precision);
				if (isNullable == 0) {
					tc.setNullable(false);
				} else {
					tc.setNullable(true);
				}
				tcList.add(tc);
			}
			// 设置注释

		} catch (SQLException e) {
			System.out.println("读取表结构出现异常");
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				rs.close();
				conn.close();
			} catch (Exception ex) {

			}
		}
		return tcList;
	}
}